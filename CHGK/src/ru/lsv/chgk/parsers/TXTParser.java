/**
 * Парсер текстового файла базы Что/Где/Когда
 */
package ru.lsv.chgk.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ru.lsv.chgk.db.Question;

/**
 * Парсер текстового файла базы Что/Где/Когда
 * 
 * @author slezhnev
 */
public class TXTParser {

	/**
	 * Файловый ридер
	 */
	private BufferedReader reader = null;

	/**
	 * Создание парсера
	 * 
	 * @param path
	 *            Путь, где лежит вся база
	 * @param fileName
	 *            Имя файла для обработки
	 */
	public TXTParser(String path, String fileName) {
		File file = new File(path + fileName);
		if (file.exists()) {
			try {
				reader = Files.newBufferedReader(file.toPath(),
						Charset.forName("KOI8_R"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Парсинг файла и возврат списка вопросов
	 * 
	 * @return Список вопросов
	 * @throws ParseException
	 *             В случае ошибок разбора файла
	 */
	public List<Question> parse() throws ParseException {
		if (reader == null) {
			throw new ParseException("Ошибка создания ридера", 0);
		}
		String championship = null;
		String round = null;
		String date = null;
		String author = null;
		String info = null;
		List<Question> questions = new ArrayList<>();
		try {
			while (reader.ready()) {
				// Проверяем тут на глобальные части
				String line = reader.readLine();
				if ((line != null) && (line.trim().length() > 0)) {
					if ((line.startsWith("Чемпионат:"))
							|| (line.toUpperCase().startsWith("Пакет:"))) {
						championship = readPart(reader);
					} else if (line.startsWith("Тур:")) {
						round = readPart(reader);
					} else if (line.startsWith("Инфо:")) {
						info = readPart(reader);
					} else if (line.startsWith("Дата:")) {
						date = readPart(reader);
					} else if (line.startsWith("Автор:")) {
						author = readPart(reader);
					} else if (line.startsWith("Вопрос")) {
						questions.add(parseQuestion(reader, championship, info,
								round, date, author, line));
					}
				}
			}
		} catch (IOException e) {
			throw new ParseException("Ошибка ввода/вывода при чтении из файла",
					0);
		}
		return questions;

	}

	/**
	 * Парсинг вопроса
	 * 
	 * @param reader
	 *            Откуда читать
	 * @param championship
	 *            Чемпионат
	 * @param info
	 *            Дополнительная информация о вопросе или чемпионате
	 * @param round
	 *            Тур
	 * @param date
	 *            Дата
	 * @param author
	 *            Автор (если он указан в начале файла)
	 * @param questionNum
	 *            "Вопрос №"
	 * @return Распарсенный вопрос
	 */
	private Question parseQuestion(BufferedReader reader, String championship,
			String info, String round, String date, String author,
			String questionNum) {
		Question question = new Question();
		question.setChampionship(championship);
		question.setInfo(info);
		question.setRound(round);
		question.setAuthor(author);
		question.setDate(date);
		// Уберем ":"
		question.setQuestionNum(questionNum.replaceAll(":", ""));
		String line = null;
		do {
			if (line == null) {
				// Если line == null - то мы только сюда попали. Будем читать
				// контент вопроса
				question.setQuestion(readPart(reader));
			} else if ((line != null) && (line.trim().length() > 0)) {
				if (line.startsWith("Обработан:")
						|| line.startsWith("Редактор:")
						|| line.startsWith("Копирайт:")
						|| line.startsWith("Ссылка:")
						|| line.startsWith("URL:") || line.startsWith("Тема:")
						|| line.startsWith("Мета:")) {
					// Дропаем
					readPart(reader);
				} else if (line.startsWith("Автор:")) {
					question.setAuthor(readPart(reader));
				} else if (line.startsWith("Инфо:")) {
					question.setInfo(readPart(reader));
				} else if (line.startsWith("Рейтинг:")) {
					question.setRate(readPart(reader));
				} else if (line.startsWith("Источник:")
						|| line.startsWith("Источники:")) {
					question.setSource(readPart(reader));
				} else if (line.startsWith("Вид:") || line.startsWith("Тип:")) {
					question.setKind(readPart(reader));
				} else if (line.startsWith("Ответ:")) {
					question.setAnswer(readPart(reader));
				} else if (line.startsWith("Зачет:")
						|| line.startsWith("Зачёт:")) {
					if (question.getAnswer() != null) {
						question.setAnswer(question.getAnswer() + "\nЗачет:\n"
								+ readPart(reader));
					}
				} else if (line.startsWith("Комментарий:")) {
					question.setComment(readPart(reader));
				} else {
					// Чо-то тут странное. Выдадим пока это для отладки - и
					// закончим. Выдавать будем только если это не новый вопрос
					// или новый тур
					if ((!line.startsWith("Тур") && !line.startsWith("Вопрос"))) {
						System.out.println("Fail in question parse. Line - "
								+ line);
						System.exit(0);
					}
					// Перед выходом - попробуем еще сдвинуть обратно указатель
					// в потоке
					try {
						reader.reset();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
			try {
				if (reader.ready()) {
					reader.mark(1000);
					line = reader.readLine();
				} else {
					break;
				}
			} catch (IOException e) {
				// Заказчиваем
				break;
			}
		} while (true);
		return question;
	}

	/**
	 * Чтение куска до пустой строки
	 * 
	 * @param reader
	 *            Откуда читать
	 * @return Прочитанный кусок
	 */
	private String readPart(BufferedReader reader) {
		StringBuffer res = new StringBuffer();
		String line = "";
		do {
			try {
				if (reader.ready()) {
					line = reader.readLine();
				} else {
					break;
				}
			} catch (IOException e) {
				// Если что-то упало - то завершаем цикл
				line = "";
			}
			res.append(line).append("\n");
		} while (line.trim().length() != 0);
		return res.toString();
	}

}

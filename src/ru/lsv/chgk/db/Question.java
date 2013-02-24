/**
 * Один вопрос
 */
package ru.lsv.chgk.db;

import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.hibernate.annotations.Index;

/**
 * Один вопрос
 * 
 * @author slezhnev
 */
@Entity
@Table(name = "QUESTIONS")
public class Question {

	/**
	 * Идентификатор
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	/**
	 * Чемпионат
	 */
	private String championship;
	/**
	 * Тур
	 */
	private String round;
	/**
	 * Дата
	 */
	private String date;
	/**
	 * Автор
	 */
	private String author;
	/**
	 * Рейтинг вопроса
	 */
	private String rate;
	/**
	 * Источник вопроса
	 */
	@Column(length = 10000)
	private String source;
	/**
	 * Тип вопроса: <br/>
	 * Ч -- ЧГК (на минуту обсуждения) <br/>
	 * Б -- Брэйн-ринг (до первой ответившей команды) <br/>
	 * Д -- Детский (для школьников) <br/>
	 * И (и, похоже, Я) -- Вопрос "Своей игры" (блок из 5 вопросов нарастающей
	 * сложности) <br/>
	 * Л -- "Бескрылка".
	 */
	private String kind;
	/**
	 * Дополнительная информация
	 */
	@Column(length = 10000)
	private String info;
	/**
	 * Номер вопроса
	 */
	private String questionNum;
	/**
	 * Вопрос
	 */
	@Column(length = 10000)
	private String question;
	/**
	 * Ответ
	 */
	@Column(length = 10000)
	private String answer;
	/**
	 * Комментарий к вопросу
	 */
	@Column(length = 10000)
	private String comment;

	/**
	 * Признак того, что вопрос уже сыгран
	 */
	private Boolean played;
	/**
	 * Рандомный номер вопроса - для рандомизации получения вопросов
	 */
	@Index(name = "randIndex")
	private Double rand;

	/**
	 * Default constructor
	 */
	public Question() {
		played = false;
		rand = Math.random();
	}

	/**
	 * @return the championship
	 */
	public String getChampionship() {
		return championship;
	}

	/**
	 * @return the round
	 */
	public String getRound() {
		return round;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @return the rate
	 */
	public String getRate() {
		return rate;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @return the kind
	 */
	public String getKind() {
		return kind;
	}

	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param championship
	 *            the championship to set
	 */
	public void setChampionship(String championship) {
		this.championship = championship;
	}

	/**
	 * @param round
	 *            the round to set
	 */
	public void setRound(String round) {
		this.round = round;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(String rate) {
		this.rate = rate;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @param kind
	 *            the kind to set
	 */
	public void setKind(String kind) {
		this.kind = kind;
	}

	/**
	 * @param question
	 *            the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * @param answer
	 *            the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the questionNum
	 */
	public String getQuestionNum() {
		return questionNum;
	}

	/**
	 * @param questionNum
	 *            the questionNum to set
	 */
	public void setQuestionNum(String questionNum) {
		this.questionNum = questionNum;
	}

	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info
	 *            the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the played
	 */
	public Boolean getPlayed() {
		return played;
	}

	/**
	 * @param played
	 *            the played to set
	 */
	public void setPlayed(Boolean played) {
		this.played = played;
	}

	/**
	 * @return the rand
	 */
	public Double getRand() {
		return rand;
	}

	/**
	 * @param rand
	 *            the rand to set
	 */
	public void setRand(Double rand) {
		this.rand = rand;
	}

	/**
	 * Формирование RichText-описания вопроса для того, чтобы иметь возможность
	 * засунуть его в JTextPane
	 * 
	 * @param listener
	 *            action listener для обработки проигрывания звуков. В имени
	 *            кнопки - название звука
	 * @return Сформированное описание вопроса
	 */
	public StyledDocument formQuestion(ActionListener listener) {
		StyledDocument doc = new DefaultStyledDocument();
		// Добавляем два стиля по умолчанию
		Style def = StyleContext.getDefaultStyleContext().getStyle(
				StyleContext.DEFAULT_STYLE);

		Style regular = doc.addStyle("regular", def);
		StyleConstants.setFontFamily(regular, "Tahoma");
		StyleConstants.setFontSize(regular, 11);

		Style bold = doc.addStyle("bold", regular);
		StyleConstants.setBold(bold, true);
		//
		try {
			// Автор
			doc.insertString(doc.getLength(), "Автор:\n", bold);
			if (author != null) {
				doc.insertString(doc.getLength(), author + "\n", regular);
			} else {
				doc.insertString(doc.getLength(), "\n", regular);
			}
			// Вопрос
			doc.insertString(doc.getLength(), "\n", regular);
			doc.insertString(doc.getLength(), "Вопрос:\n", bold);
			if (question != null) {
				doc = parseSoundAndImages(question, doc, listener);
			} else {
				doc.insertString(doc.getLength(), "\n", regular);
			}
			// Чемпионат
			doc.insertString(doc.getLength(), "\n", regular);
			doc.insertString(doc.getLength(), "\n", regular);
			doc.insertString(doc.getLength(), "\n", regular);
			doc.insertString(doc.getLength(), "Чемпионат:\n", bold);
			if (championship != null) {
				doc.insertString(doc.getLength(), championship + "\n", regular);
			} else {
				doc.insertString(doc.getLength(), "\n", regular);
			}
			// Инфо
			doc.insertString(doc.getLength(), "Инфо:\n", bold);
			if (info != null) {
				doc.insertString(doc.getLength(), info + "\n", regular);
			} else {
				doc.insertString(doc.getLength(), "\n", regular);
			}
			// Тур
			doc.insertString(doc.getLength(), "Тур:\n", bold);
			if (round != null) {
				doc.insertString(doc.getLength(), round + "\n", regular);
			} else {
				doc.insertString(doc.getLength(), "\n", regular);
			}
			// Номер вопроса
			doc.insertString(doc.getLength(), "№ вопроса:\n", bold);
			if (questionNum != null) {
				doc.insertString(doc.getLength(), questionNum + "\n", regular);
			} else {
				doc.insertString(doc.getLength(), "\n", regular);
			}
			// Дата
			doc.insertString(doc.getLength(), "Дата:\n", bold);
			if (date != null) {
				doc.insertString(doc.getLength(), date + "\n", regular);
			} else {
				doc.insertString(doc.getLength(), "\n", regular);
			}
			// Рейтинг
			doc.insertString(doc.getLength(), "Рейтинг:\n", bold);
			if (rate != null) {
				doc.insertString(doc.getLength(), rate + "\n", regular);
			} else {
				doc.insertString(doc.getLength(), "\n", regular);
			}
		} catch (BadLocationException e) {
			// Do nothing
		}
		//
		return doc;
	}

	/**
	 * Формирование RichText-описания ответа для того, чтобы иметь возможность
	 * засунуть его в JTextPane
	 * 
	 * @param listener
	 *            action listener для обработки проигрывания звуков. В имени
	 *            кнопки - название звука
	 * @return Сформированное описание ответа
	 */
	public StyledDocument formAnswer(ActionListener listener) {
		StyledDocument doc = new DefaultStyledDocument();
		// Добавляем два стиля по умолчанию
		Style def = StyleContext.getDefaultStyleContext().getStyle(
				StyleContext.DEFAULT_STYLE);

		Style regular = doc.addStyle("regular", def);
		StyleConstants.setFontFamily(regular, "Tahoma");
		StyleConstants.setFontSize(regular, 11);

		Style bold = doc.addStyle("bold", regular);
		StyleConstants.setBold(bold, true);
		//
		try {
			// Ответ
			doc.insertString(doc.getLength(), "Ответ:\n", bold);
			if (answer != null) {
				doc = parseSoundAndImages(answer, doc, listener);
			} else {
				doc.insertString(doc.getLength(), "\n", regular);
			}
			// Комментарий
			doc.insertString(doc.getLength(), "\n", regular);
			doc.insertString(doc.getLength(), "Комментарий:\n", bold);
			if (comment != null) {
				doc.insertString(doc.getLength(), comment + "\n", regular);
			} else {
				doc.insertString(doc.getLength(), "\n", regular);
			}
			// Источник
			doc.insertString(doc.getLength(), "\n", regular);
			doc.insertString(doc.getLength(), "Источник:\n", bold);
			if (source != null) {
				doc.insertString(doc.getLength(), source + "\n", regular);
			} else {
				doc.insertString(doc.getLength(), "\n", regular);
			}
		} catch (BadLocationException e) {
			// Do nothing
		}
		//
		return doc;
	}

	/**
	 * Парсит представление вопроса и ответа с загрузкой картинок и звуков в
	 * StyledDocument
	 * 
	 * @param text
	 *            Текст для распарсивания
	 * @param doc
	 *            Документ, куда надо совать результат
	 * @param listener
	 *            Обработчик запуска проигрывания
	 * @return Обработанный документ
	 */
	private StyledDocument parseSoundAndImages(String text, StyledDocument doc,
			ActionListener listener) {
		Style def = StyleContext.getDefaultStyleContext().getStyle(
				StyleContext.DEFAULT_STYLE);
		// Картинки в тексте хранятся в виде (pic: [imageName])
		// Звуки - (aud: [название звука])
		final String img = "(pic: ";
		final String audio = "(aud: ";
		try {
			// Вначале - обработаем картинки
			while (text.indexOf(img) > -1) {
				int pos = text.indexOf(img);
				doc.insertString(doc.getLength(), text.substring(0, pos),
						doc.getStyle("regular"));
				text = text.substring(pos + img.length());
				int last = text.indexOf(")");
				if (last != -1) {
					String imgName = text.substring(0, last);
					if (new File("images/" + imgName).exists()) {
						Style imgStyle = doc.addStyle(imgName, def);
						StyleConstants.setAlignment(imgStyle,
								StyleConstants.ALIGN_CENTER);
						StyleConstants.setIcon(imgStyle, new ImageIcon(
								"images/" + imgName, imgName));
						doc.insertString(doc.getLength(), " ", imgStyle);
					}
					text = text.substring(last + 1);
				}
			}
			// Теперь - звуки
			while (text.indexOf(audio) > -1) {
				int pos = text.indexOf(audio);
				doc.insertString(doc.getLength(), text.substring(0, pos),
						doc.getStyle("regular"));
				text = text.substring(pos + audio.length());
				int last = text.indexOf(")");
				if (last != -1) {
					String audioName = text.substring(0, last);
					if (new File("sounds/" + audioName).exists()) {
						Style audioStyle = doc.addStyle(audioName, def);
						StyleConstants.setAlignment(audioStyle,
								StyleConstants.ALIGN_CENTER);
						JButton button = new JButton();
						button.setIcon(new ImageIcon("res/sound.gif",
								"Проиграть " + audioName));
						button.setCursor(Cursor
								.getPredefinedCursor(Cursor.HAND_CURSOR));
						button.setMargin(new Insets(0, 0, 0, 0));
						button.setActionCommand(audioName);
						button.addActionListener(listener);
						button.setToolTipText("Проиграть " + audioName);
						StyleConstants.setComponent(audioStyle, button);
						doc.insertString(doc.getLength(), " ", audioStyle);
					}
					text = text.substring(last + 1);
				}
			}
			doc.insertString(doc.getLength(), text, doc.getStyle("regular"));
		} catch (BadLocationException e) {
			// Do nothing
		}
		return doc;
	}

}

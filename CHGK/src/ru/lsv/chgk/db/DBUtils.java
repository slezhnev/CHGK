/**
 * Utility-класс для работы с базой
 */
package ru.lsv.chgk.db;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Utility-класс для работы с базой
 * 
 * @author s.lezhnev
 * 
 */
public class DBUtils {

	/**
	 * Маска для questionKind <br/>
	 * Ч -- ЧГК (на минуту обсуждения)
	 */
	public static final int CHGK = 0b00001;
	/**
	 * Маска для questionKind <br/>
	 * Б -- Брэйн-ринг (до первой ответившей команды)
	 */
	public static final int BRAIN_RING = 0b00010;
	/**
	 * Маска для questionKind <br/>
	 * Д -- Детский (для школьников)
	 */
	public static final int SCHOOLAR = 0b00100;
	/**
	 * Маска для questionKind <br/>
	 * И (и, похоже, Я) -- Вопрос "Своей игры" (блок из 5 вопросов нарастающей
	 * сложности)
	 */
	public static final int OWN_GAME = 0b01000;
	/**
	 * Маска для questionKind <br/>
	 * Л -- "Бескрылка"
	 */
	public static final int POEM = 0b10000;

	/**
	 * Получение следующего вопроса
	 * 
	 * @param questionKind
	 *            Маска поиска вопроса. Константы - в определении класса <br/>
	 *            ВНИМАНИЕ! Сейчас - не работает!
	 * @return Следующий вопрос или null в случае проблем
	 */
	public static Question getNextQuestion(int questionKind) {
		Question res = null;
		//
		Session sess = HibernateUtils.getSession();
		// Делаем хитрую сортировку. Пытаемся искать вопросы через random
		// Вопросы пытаемся искать до 3-х раз...
		int tries = 0;
		Criteria criteria;
		while (tries < 3) {
			double rand = Math.random();
			criteria = sess.createCriteria(Question.class)
					.addOrder(Order.asc("rand"))
					.add(Restrictions.eq("played", false))
					.add(Restrictions.ge("rand", rand));
			criteria.setMaxResults(1);
			res = (Question) criteria.uniqueResult();
			if (res == null) {
				tries++;
				rand /= 2;
			} else {
				break;
			}
		}
		if (res == null) {
			// Значит за 3 попытки не смогли найти вопрос - значит просто
			// выберем первый
			criteria = sess.createCriteria(Question.class)
					.addOrder(Order.asc("rand"))
					.add(Restrictions.eq("played", false));
			criteria = doFormKindsCriteria(criteria, questionKind);
			criteria.setMaxResults(1);
			res = (Question) criteria.uniqueResult();
		}
		// Отладочное
		// Question res = new Question();
		// res.setChampionship("ЧГК-микст \"Кубок Аленки - 2011\" (Санкт-Петербург)");
		// res.setDate("07-Jun-2011");
		// res.setInfo("Посвящено двухлетию Елены Львовны Орловой. Авторы благодарят за\n"
		// +
		// "тестирование и ценные советы Евгения Рубашкина, Асю Самойлову, Дмитрия\n"
		// +
		// "Карякина, Диану Ильягуеву, Анну Парфёнову, Сергея Коновалова, Ирину\n"
		// +
		// "Медноногову, Игоря Бахарева, Владимира Севриновского, Екатерину и\n"
		// + "Станислава Мереминских, Константина Изъюрова.");
		// res.setRound("1 тур");
		// res.setQuestionNum("Вопрос 1:");
		// res.setQuestion("(pic: 20110432.jpg) (pic: 20000001.gif)\n"
		// + "(aud: 20110001.mp3) (aud: 20050001.mp3)\n"
		// + "Тезка этих девочек, появившаяся на свет в Петербурге, по мнению\n"
		// +
		// "некоторых остряков, является однофамилицей советского политического\n"
		// + "деятеля. Назовите имя и отчество этого деятеля.");
		// res.setAnswer("Надежда Константиновна.");
		// res.setComment("Раздатка изображает обертки шоколадки \"Аленка\" разных лет. Петербургская\n"
		// +
		// "\"Фабрика им. Крупской\" выпускает шоколадки \"Крупская Аленка\".");
		// res.setSource("1. http://www.alenka.ru/museum/\n"
		// + "2. http://www.uralweb.ru/pages/article.4455.html");
		// res.setAuthor("Лев Орлов и Светлана Орлова");
		return res;
	}

	/**
	 * Добавляет к Criteria параметры по фильтрации вопросов по нужным типам
	 * вопросам
	 * 
	 * @param crit
	 *            Criteria, к которой добавлять
	 * @param questionKind
	 *            Типы вопросов
	 * @return Сформированная Criteria
	 */
	private static Criteria doFormKindsCriteria(Criteria crit, int questionKind) {
		// TODO Доделать формирование критерия!
		return crit;
	}

}

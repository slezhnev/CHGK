/**
 * Helper работы с Hibernate
 */
package ru.lsv.chgk.db;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Helper работы с Hibernate
 * 
 * @author slezhnev
 */
@SuppressWarnings("deprecation")
public class HibernateUtils {

	private static final SessionFactory ourSessionFactory;

	static {
		try {
			Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
			cfg.setProperty("hibernate.connection.hsqldb.default_table_type",
					"cached");
			ourSessionFactory = cfg.buildSessionFactory();
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * Получение текущей сессии работы с Hibernate
	 * 
	 * @return Текущую сессию
	 */
	public static Session getSession() {
		Session currSession;
		currSession = ourSessionFactory.openSession();
		currSession.setFlushMode(FlushMode.COMMIT);
		currSession.setCacheMode(CacheMode.NORMAL);
		return currSession;
	}

}

/**
 * Формирователь базы вопросов из базы вопросов "Что?Где?Когда?" (http://db.chgk.info/) <br/>
 * База представляет собой набор txt-файлов. Описание формата - http://db.chgk.info/format_voprosov <br/>
 * Саму базу можно взять с http://bilbo.dynip.com/home/cvsroot (раздел ""Секрет" третьий" в описании формата) 
 */
package ru.lsv.chgk;

import java.io.File;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ru.lsv.chgk.db.HibernateUtils;
import ru.lsv.chgk.db.Question;
import ru.lsv.chgk.parsers.TXTParser;

/**
 * Формирователь базы вопросов из базы вопросов "Что?Где?Когда?"
 * (http://db.chgk.info/) <br/>
 * База представляет собой набор txt-файлов. Описание формата -
 * http://db.chgk.info/format_voprosov <br/>
 * Саму базу можно взять с CVS (http://bilbo.dynip.com/home/cvsroot) (раздел
 * ""Секрет" третьий" в описании формата)
 * 
 * @author slezhnev
 */
public class Loader {

	/**
	 * Запуск лоадера
	 * 
	 * @param args
	 *            В args[0] должен находиться путь до каталога, где лежит база
	 *            (набор txt-файлов)
	 * @throws ParseException
	 *             В случае проблем парсинга
	 */
	public static void main(String[] args) throws ParseException {
		if (args.length < 1) {
			System.out
					.println("Usage: java -cp ./chgk.jar Loader [path_to_chgk_txt_files]");
			return;
		}
		File inFile = new File(args[0]);
		if (!inFile.exists()) {
			System.out.println("Path " + args[0] + " does not exists");
		}
		String[] files = inFile.list(new SuffixFileFilter(".txt"));
		Session sess;
		for (String file : files) {
			System.out.println();
			System.out.println("Processing " + file);
			TXTParser parser = new TXTParser(args[0], file);
			List<Question> res = parser.parse();
			sess = HibernateUtils.getSession();
			Transaction trx = sess.beginTransaction();
			for (Question quest : res) {
				sess.save(quest);
			}
			sess.flush();
			trx.commit();
			System.out.println("Total questions in file - " + res.size());
		}
		sess = HibernateUtils.getSession();
		System.out.println();
		System.out.println("Total questions after load - "
				+ sess.createSQLQuery("select count(*) from Questions")
						.uniqueResult());
	}

}

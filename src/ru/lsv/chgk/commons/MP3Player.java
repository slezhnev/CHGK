/**
 * MP3 плейер для проигрывания звуковых файлов вопросов <br/>
 * Первый клик - запуск на воспроизведение <br/>
 * Второй - завершение воспроизведения 
 */
package ru.lsv.chgk.commons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * MP3 плейер для проигрывания звуковых файлов вопросов <br/>
 * Первый клик - запуск на воспроизведение <br/>
 * Второй - завершение воспроизведения
 * 
 * @author s.lezhnev
 */
public class MP3Player implements ActionListener {

	/**
	 * Хранилище потоков воспроизведения
	 */
	private Map<String, PlayerThread> threads = Collections
			.synchronizedMap(new HashMap<String, PlayerThread>());

	@Override
	public void actionPerformed(final ActionEvent e) {
		// Первый клик - запуск на воспроизведение
		// Второй - завершение воспроизведения
		// Первое - попробуем получить поток для такого имени файла
		String audioName = e.getActionCommand();
		PlayerThread thread = threads.get(audioName);
		if (thread == null) {
			// Создаем поток
			new PlayerThread(audioName, threads).start();
		} else {
			thread.stopPlaying();
		}
	}

	/**
	 * Поток проигрывания конкретного файла
	 * 
	 * @author s.lezhnev
	 * 
	 */
	private static class PlayerThread extends Thread {

		/**
		 * Непосредственно плейер
		 */
		private Player player = null;
		/**
		 * Имя файла, которое играется
		 */
		private String audioName = null;
		/**
		 * Хранилище потоков воспроизведения
		 */
		private Map<String, PlayerThread> threads = null;

		/**
		 * Default constructor
		 * 
		 * @param name
		 *            Имя файла для воспроизведения
		 * @param threadMan
		 *            Список поток, куда добавлять текущий
		 */
		public PlayerThread(String name, Map<String, PlayerThread> threadMan) {
			audioName = name;
			threads = threadMan;
		}

		@Override
		public void run() {
			File mp3 = new File("sounds/" + audioName);
			threads.put(audioName, this);
			if (mp3.exists()) {
				try {
					player = new Player(new FileInputStream(mp3));
					player.play();
				} catch (FileNotFoundException | JavaLayerException e1) {
					// Do nothing
				}
			}
			threads.remove(audioName);
		}

		/**
		 * Останавливает воспроизведение
		 */
		public void stopPlaying() {
			if (player != null) {
				player.close();
			}
		}

	}

}

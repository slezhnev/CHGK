package ru.lsv.chgk.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.hibernate.Session;
import org.hibernate.Transaction;

import ru.lsv.chgk.commons.MP3Player;
import ru.lsv.chgk.db.DBUtils;
import ru.lsv.chgk.db.HibernateUtils;
import ru.lsv.chgk.db.Question;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.looks.LookUtils;
import com.jgoodies.looks.Options;
import javax.swing.JSeparator;

public class MainForm {

	/**
	 * Card с отображением ответа
	 */
	private final String answerCard = "answerCard";
	/**
	 * Card с отображением кнопки "Показать ответ"
	 */
	private final String answerBtnCard = "answerBtnCard";
	/**
	 * Основной frame приложения
	 */
	private JFrame frame;
	/**
	 * Edit с ответом
	 */
	private JTextField answerEdit;
	/**
	 * Вопрос
	 */
	private JTextPane questionPane;
	/**
	 * Ответ
	 */
	private JTextPane answerPane;
	/**
	 * Отображение
	 */
	private JPanel showAnswerCardPanel;
	/**
	 * Отображение счета - знатоки
	 */
	private JLabel expertsCount;
	/**
	 * Отображение счета - зрители
	 */
	private JLabel audienceCount;
	/**
	 * При поиске - искать любые вопросы
	 */
	private JCheckBox questionAll;
	/**
	 * При поиске - искать только ЧКГ вопросы
	 */
	private JCheckBox questionCHGK;
	/**
	 * При поиске - искать только Брейн-ринг вопросы
	 */
	private JCheckBox questionBR;
	/**
	 * При поиске - искать только детские вопросы
	 */
	private JCheckBox questionChild;
	/**
	 * При поиске - искать только вопросы "своей игры"
	 */
	private JCheckBox questionSI;
	/**
	 * При поиске - искать только вопросы "бескрылки"
	 */
	private JCheckBox questionR;
	/**
	 * Label типа времени
	 */
	private JLabel timeTypeLabel;
	/**
	 * Результат игры - знатоки
	 */
	private int experts = 0;
	/**
	 * Результат игры - зрители
	 */
	private int audience = 0;
	/**
	 * Плейер для того, чтобы воспроизвести звуковые файлы
	 */
	private MP3Player player = new MP3Player();
	/**
	 * Текущий вопрос
	 */
	Question currentQuestion = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		configureUI();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm window = new MainForm();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static void configureUI() {
		UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, Boolean.TRUE);
		Options.setDefaultIconSize(new Dimension(18, 18));

		String lafName = LookUtils.IS_OS_WINDOWS_MODERN ? Options
				.getCrossPlatformLookAndFeelClassName() : Options
				.getSystemLookAndFeelClassName();

		try {
			UIManager.setLookAndFeel(lafName);
		} catch (Exception e) {
			System.err.println("Can't set look & feel:" + e);
		}
	}

	/**
	 * Create the application.
	 */
	public MainForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(0, 0, 1280, 720);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setTitle("Что? Где? Когда? - клиент для игры");

		JPanel mainPanel = new JPanel();
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		GridBagLayout gbl_mainPanel = new GridBagLayout();
		gbl_mainPanel.columnWidths = new int[] { 966, 0 };
		gbl_mainPanel.rowHeights = new int[] { 422, 238, 0 };
		gbl_mainPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_mainPanel.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		mainPanel.setLayout(gbl_mainPanel);

		JPanel questionPanel = new JPanel();
		questionPanel.setBorder(new TitledBorder(new EtchedBorder(
				EtchedBorder.LOWERED, null, null),
				"\u0412\u043E\u043F\u0440\u043E\u0441", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		GridBagConstraints gbc_questionPanel = new GridBagConstraints();
		gbc_questionPanel.anchor = GridBagConstraints.NORTH;
		gbc_questionPanel.fill = GridBagConstraints.BOTH;
		gbc_questionPanel.insets = new Insets(0, 0, 5, 0);
		gbc_questionPanel.gridx = 0;
		gbc_questionPanel.gridy = 0;
		mainPanel.add(questionPanel, gbc_questionPanel);
		questionPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		questionPanel.add(scrollPane, BorderLayout.CENTER);

		questionPane = new JTextPane();
		questionPane.setEditable(false);
		scrollPane.setViewportView(questionPane);
		// questionPane.setDocument(new Question().formQuestion(null));

		JPanel answerPanel = new JPanel();
		answerPanel.setBorder(new TitledBorder(new EtchedBorder(
				EtchedBorder.LOWERED, null, null),
				"\u041E\u0442\u0432\u0435\u0442", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		GridBagConstraints gbc_answerPanel = new GridBagConstraints();
		gbc_answerPanel.fill = GridBagConstraints.BOTH;
		gbc_answerPanel.gridx = 0;
		gbc_answerPanel.gridy = 1;
		mainPanel.add(answerPanel, gbc_answerPanel);
		GridBagLayout gbl_answerPanel = new GridBagLayout();
		gbl_answerPanel.columnWidths = new int[] { 0 };
		gbl_answerPanel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_answerPanel.columnWeights = new double[] { 1.0 };
		gbl_answerPanel.rowWeights = new double[] { 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		answerPanel.setLayout(gbl_answerPanel);

		answerEdit = new JTextField();
		GridBagConstraints gbc_answerEdit = new GridBagConstraints();
		gbc_answerEdit.insets = new Insets(0, 0, 5, 0);
		gbc_answerEdit.fill = GridBagConstraints.HORIZONTAL;
		gbc_answerEdit.gridx = 0;
		gbc_answerEdit.gridy = 0;
		answerPanel.add(answerEdit, gbc_answerEdit);
		answerEdit.setColumns(10);

		showAnswerCardPanel = new JPanel();
		GridBagConstraints gbc_showAnswerPanel = new GridBagConstraints();
		gbc_showAnswerPanel.insets = new Insets(0, 0, 5, 0);
		gbc_showAnswerPanel.fill = GridBagConstraints.BOTH;
		gbc_showAnswerPanel.gridx = 0;
		gbc_showAnswerPanel.gridy = 1;
		answerPanel.add(showAnswerCardPanel, gbc_showAnswerPanel);
		CardLayout cl = new CardLayout(0, 0);
		showAnswerCardPanel.setLayout(cl);

		scrollPane = new JScrollPane();
		showAnswerCardPanel.add(scrollPane, answerCard);

		answerPane = new JTextPane();
		answerPane.setEditable(false);
		// answerPane.setDocument(new Question().formAnswer(null));
		scrollPane.setViewportView(answerPane);

		JPanel showAnswerPanel = new JPanel();
		showAnswerCardPanel.add(showAnswerPanel, answerBtnCard);
		GridBagLayout gbl_showAnswerPanel = new GridBagLayout();
		gbl_showAnswerPanel.columnWidths = new int[] { 0, 0 };
		gbl_showAnswerPanel.rowHeights = new int[] { 0, 0, 0 };
		gbl_showAnswerPanel.columnWeights = new double[] { 1.0, 0.5 };
		gbl_showAnswerPanel.rowWeights = new double[] { 0.5, 0.5, 1.0 };
		showAnswerPanel.setLayout(gbl_showAnswerPanel);

		JButton showAnswerBtn = new JButton("Показать ответ");
		showAnswerBtn.setFont(new Font("Tahoma", Font.PLAIN, 35));
		// showAnswerCardPanel.add(showAnswerBtn, answerBtnCard);
		GridBagConstraints gbc_showAnswerBtn = new GridBagConstraints();
		gbc_showAnswerBtn.gridheight = 3;
		gbc_showAnswerBtn.insets = new Insets(0, 0, 0, 5);
		gbc_showAnswerBtn.fill = GridBagConstraints.BOTH;
		gbc_showAnswerBtn.gridx = 0;
		gbc_showAnswerBtn.gridy = 0;
		showAnswerPanel.add(showAnswerBtn, gbc_showAnswerBtn);

		JLabel timeCaption = new JLabel("Время");
		timeCaption.setEnabled(false);
		timeCaption.setVerticalAlignment(SwingConstants.TOP);
		timeCaption.setHorizontalAlignment(SwingConstants.CENTER);
		timeCaption.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_timeCaption = new GridBagConstraints();
		gbc_timeCaption.insets = new Insets(0, 0, 5, 0);
		gbc_timeCaption.fill = GridBagConstraints.BOTH;
		gbc_timeCaption.gridx = 1;
		gbc_timeCaption.gridy = 0;
		showAnswerPanel.add(timeCaption, gbc_timeCaption);

		timeTypeLabel = new JLabel("чтение вопроса");
		timeTypeLabel.setEnabled(false);
		timeTypeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_timeTypeLabel = new GridBagConstraints();
		gbc_timeTypeLabel.insets = new Insets(0, 0, 5, 0);
		gbc_timeTypeLabel.gridx = 1;
		gbc_timeTypeLabel.gridy = 1;
		showAnswerPanel.add(timeTypeLabel, gbc_timeTypeLabel);

		JLabel timeLabel = new JLabel("00");
		timeLabel.setEnabled(false);
		timeLabel.setFont(new Font("Tahoma", Font.BOLD, 42));
		GridBagConstraints gbc_timeLabel = new GridBagConstraints();
		gbc_timeLabel.gridx = 1;
		gbc_timeLabel.gridy = 2;
		showAnswerPanel.add(timeLabel, gbc_timeLabel);
		showAnswerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				doShowAnswer();
			}
		});
		cl.show(showAnswerCardPanel, answerBtnCard);

		JPanel answerBtnPanel = new JPanel();
		answerBtnPanel
				.setBorder(new TitledBorder(
						new EtchedBorder(EtchedBorder.LOWERED, null, null),
						"\u041F\u0440\u0430\u0432\u0438\u043B\u044C\u043D\u043E\u0441\u0442\u044C \u043E\u0442\u0432\u0435\u0442\u0430",
						TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_answerBtnPanel = new GridBagConstraints();
		gbc_answerBtnPanel.fill = GridBagConstraints.BOTH;
		gbc_answerBtnPanel.gridx = 0;
		gbc_answerBtnPanel.gridy = 2;
		answerPanel.add(answerBtnPanel, gbc_answerBtnPanel);
		answerBtnPanel.setLayout(new GridLayout(1, 0, 0, 0));

		JButton expertsPlusBtn = new JButton("ЗНАТОКИ +1");
		expertsPlusBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Делаем +1 знатокам
				experts++;
				doCountRefresh();
				doFormNextQuestion();
			}
		});
		expertsPlusBtn.setForeground(Color.ORANGE);
		expertsPlusBtn.setFont(new Font("Tahoma", Font.BOLD, 28));
		answerBtnPanel.add(expertsPlusBtn);

		JButton audiencePlusBtn = new JButton("ЗРИТЕЛИ +1");
		audiencePlusBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Делаем +1 зрителям
				audience++;
				doCountRefresh();
				doFormNextQuestion();
			}
		});
		audiencePlusBtn.setForeground(new Color(0, 128, 0));
		audiencePlusBtn.setFont(new Font("Tahoma", Font.BOLD, 28));
		answerBtnPanel.add(audiencePlusBtn);

		JPanel rightPanel = new JPanel();
		frame.getContentPane().add(rightPanel, BorderLayout.EAST);
		rightPanel.setLayout(new BorderLayout(0, 0));

		JPanel countPanel = new JPanel();
		countPanel
				.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		rightPanel.add(countPanel, BorderLayout.NORTH);
		GridBagLayout gbl_countPanel = new GridBagLayout();
		gbl_countPanel.columnWidths = new int[] { 280 };
		gbl_countPanel.rowHeights = new int[] { 0, 20, 0, 0, 50, 0, 0, 50, 0,
				50 };
		gbl_countPanel.columnWeights = new double[] { 0.0 };
		gbl_countPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0 };
		countPanel.setLayout(gbl_countPanel);

		JLabel countLabel = new JLabel("ТЕКУЩИЙ СЧЕТ");
		countLabel.setHorizontalAlignment(SwingConstants.CENTER);
		countLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		GridBagConstraints gbc_countLabel = new GridBagConstraints();
		gbc_countLabel.insets = new Insets(0, 0, 5, 0);
		gbc_countLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_countLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_countLabel.gridx = 0;
		gbc_countLabel.gridy = 0;
		countPanel.add(countLabel, gbc_countLabel);

		JLabel expertsCountHeader = new JLabel("ЗНАТОКИ");
		expertsCountHeader.setFont(new Font("Tahoma", Font.BOLD, 19));
		GridBagConstraints gbc_expertsCountHeader = new GridBagConstraints();
		gbc_expertsCountHeader.insets = new Insets(0, 0, 5, 0);
		gbc_expertsCountHeader.gridx = 0;
		gbc_expertsCountHeader.gridy = 2;
		countPanel.add(expertsCountHeader, gbc_expertsCountHeader);

		expertsCount = new JLabel("0");
		expertsCount.setForeground(Color.ORANGE);
		expertsCount.setFont(new Font("Tahoma", Font.BOLD, 44));
		GridBagConstraints gbc_expertsCount = new GridBagConstraints();
		gbc_expertsCount.insets = new Insets(0, 0, 5, 0);
		gbc_expertsCount.gridx = 0;
		gbc_expertsCount.gridy = 3;
		countPanel.add(expertsCount, gbc_expertsCount);

		JLabel audienceLabelHeader = new JLabel("ЗРИТЕЛИ");
		audienceLabelHeader.setFont(new Font("Tahoma", Font.BOLD, 19));
		GridBagConstraints gbc_audienceLabelHeader = new GridBagConstraints();
		gbc_audienceLabelHeader.insets = new Insets(0, 0, 5, 0);
		gbc_audienceLabelHeader.gridx = 0;
		gbc_audienceLabelHeader.gridy = 5;
		countPanel.add(audienceLabelHeader, gbc_audienceLabelHeader);

		audienceCount = new JLabel("0");
		audienceCount.setForeground(new Color(0, 128, 0));
		audienceCount.setFont(new Font("Tahoma", Font.BOLD, 44));
		GridBagConstraints gbc_audienceCount = new GridBagConstraints();
		gbc_audienceCount.insets = new Insets(0, 0, 5, 0);
		gbc_audienceCount.gridx = 0;
		gbc_audienceCount.gridy = 6;
		countPanel.add(audienceCount, gbc_audienceCount);

		JButton button = new JButton("Начать новую игру");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doStartNextGame();
			}
		});
		button.setToolTipText("Начать новую игру со сбросом счета");
		button.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.gridx = 0;
		gbc_button.gridy = 8;
		countPanel.add(button, gbc_button);

		JPanel nextQuestionPanel = new JPanel();
		nextQuestionPanel
				.setBorder(new TitledBorder(
						new EtchedBorder(EtchedBorder.LOWERED, null, null),
						"\u0421\u043B\u0435\u0434\u0443\u044E\u0449\u0438\u0439 \u0432\u043E\u043F\u0440\u043E\u0441",
						TitledBorder.LEADING, TitledBorder.TOP, null, null));
		rightPanel.add(nextQuestionPanel, BorderLayout.CENTER);
		nextQuestionPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("280px"), },
				new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("23px"),
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		questionAll = new JCheckBox("Любой вопрос");
		questionAll.setEnabled(false);
		questionAll.setSelected(true);
		nextQuestionPanel.add(questionAll, "2, 2, left, top");

		questionCHGK = new JCheckBox("ЧГК (на минуту обсуждения)");
		questionCHGK.setEnabled(false);
		nextQuestionPanel.add(questionCHGK, "2, 4");

		questionBR = new JCheckBox("Брэйн-ринг (до первой ответившей команды)");
		questionBR.setEnabled(false);
		nextQuestionPanel.add(questionBR, "2, 6");

		questionChild = new JCheckBox("Детский (для школьников)");
		questionChild.setEnabled(false);
		nextQuestionPanel.add(questionChild, "2, 8");

		questionSI = new JCheckBox("Вопрос \"Своей игры\" (блок из 5 вопросов)");
		questionSI.setEnabled(false);
		nextQuestionPanel.add(questionSI, "2, 10");

		questionR = new JCheckBox("\"Бескрылка\"");
		questionR.setEnabled(false);
		nextQuestionPanel.add(questionR, "2, 12");

		JButton nextQuestionBtn = new JButton("Следующий вопрос");
		nextQuestionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doFormNextQuestion();
			}
		});
		nextQuestionBtn.setToolTipText("Выбрать из базы следующий вопрос");
		nextQuestionBtn.setFont(new Font("Tahoma", Font.BOLD, 23));
		nextQuestionPanel.add(nextQuestionBtn, "2, 15, fill, top");

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu menu = new JMenu("Действия");
		menuBar.add(menu);

		JMenuItem resortMI = new JMenuItem("Пересортировать вопросы");
		resortMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doMakeQuestionResort();
			}
		});
		menu.add(resortMI);

		JSeparator separator = new JSeparator();
		menu.add(separator);

		JMenuItem exitMI = new JMenuItem("Выход");
		menu.add(exitMI);
		exitMI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doExit();
			}
		});

		frame.addWindowListener(new WindowAdapter() {

			/**
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent arg0) {
				doExit();
			}

		});

		// Формируем первый вопрос
		doFormNextQuestion();
	}

	/**
	 * Выполнение пересотировки вопросов
	 */
	protected void doMakeQuestionResort() {
		if (JOptionPane
				.showConfirmDialog(
						frame,
						"Выполнить пересортировку вопросов? Пересортировка будет выполняться достаточно длительное время.",
						"Пересортировка вопросов", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			// Выполняем пересортировку...
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			Session sess = HibernateUtils.getSession();
			Transaction trx = sess.beginTransaction();
			sess.createSQLQuery(
					"update Questions set rand=rand("
							+ (int) new Date().getTime() + ")").executeUpdate();
			sess.flush();
			trx.commit();
			frame.setCursor(Cursor.getDefaultCursor());
			JOptionPane.showMessageDialog(frame,
					"Пересортировка вопросов выполнена",
					"Пересортировка вопросов", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Отображает ответ
	 */
	private void doShowAnswer() {
		CardLayout cl = (CardLayout) showAnswerCardPanel.getLayout();
		cl.show(showAnswerCardPanel, answerCard);
		Session sess = HibernateUtils.getSession();
		if (currentQuestion != null) {
			currentQuestion.setPlayed(true);
			Transaction trx = sess.beginTransaction();
			sess.update(currentQuestion);
			sess.flush();
			trx.commit();
		}
	}

	/**
	 * Начать новую игру со сбросом счета
	 */
	private void doStartNextGame() {
		if (JOptionPane.showConfirmDialog(frame,
				"Начать новую игру? Текущий счет будет сброшен!", "Новая игра",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			// Сбрасываем счет
			experts = 0;
			audience = 0;
			doCountRefresh();
			// Генерируем вопрос
			doFormNextQuestion();
		}

	}

	/**
	 * Выбрать из базы и задать следующий вопрос
	 */
	private void doFormNextQuestion() {
		// Вырубаем отображение
		CardLayout cl = (CardLayout) showAnswerCardPanel.getLayout();
		cl.show(showAnswerCardPanel, answerBtnCard);
		// Генерируем вопрос
		// Формируем тип искомого вопроса
		int questionKind = 0;
		if (questionCHGK.isSelected()) {
			questionKind = questionKind ^ DBUtils.CHGK;
		}
		if (questionBR.isSelected()) {
			questionKind = questionKind ^ DBUtils.BRAIN_RING;
		}
		if (questionChild.isSelected()) {
			questionKind = questionKind ^ DBUtils.SCHOOLAR;
		}
		if (questionSI.isSelected()) {
			questionKind = questionKind ^ DBUtils.OWN_GAME;
		}
		if (questionR.isSelected()) {
			questionKind = questionKind ^ DBUtils.POEM;
		}
		currentQuestion = DBUtils.getNextQuestion(questionKind);
		if (currentQuestion != null) {
			questionPane.setDocument(currentQuestion.formQuestion(player));
			answerPane.setDocument(currentQuestion.formAnswer(player));
		} else {
			// Формируем err document
			StyledDocument doc = new DefaultStyledDocument();
			// Добавляем два стиля по умолчанию
			Style def = StyleContext.getDefaultStyleContext().getStyle(
					StyleContext.DEFAULT_STYLE);
			try {
				doc.insertString(doc.getLength(), "Ошибка получения вопроса!",
						def);
				questionPane.setDocument(doc);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Завершение работы с системой
	 */
	private void doExit() {
		if (JOptionPane.showConfirmDialog(MainForm.this.frame,
				"Завершить работу с системой?", "Завершение работы",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	/**
	 * Выполняет обновление счета
	 */
	private void doCountRefresh() {
		audienceCount.setText("" + audience);
		expertsCount.setText("" + experts);
	}

}

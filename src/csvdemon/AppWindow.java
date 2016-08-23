package csvdemon;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import panel.PanelSettings;
import panel.WebBrowserPanel;

import java.awt.Cursor;
import java.awt.DisplayMode;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JTabbedPane;
import java.awt.SystemColor;

import javax.swing.ImageIcon;
import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JCheckBox;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class AppWindow {
	
	public static String defaultDirectory = "." + File.pathSeparator + "Source" + File.pathSeparator;
	
	public static String examplesPath = "." + File.separator + "config" + File.separator + "EXAMPLES";
	
	public static AppWindow instance;
	
	public JFrame frame;
	
	private static boolean needBrowser;
	
	private PanelSettings panelSettings;
	private WebBrowserPanel browser_1;
	private JPanel panel;
	private JTextField textField_metrica;
	private JTextField textField_csv;
	private JTextField textField_optNum;
	private JTextField textField_site;
	private JTextField textField_appeal;
	private JTextField textField_date;
	private JTextField textField_region;
	private JTextField textField_num1;
	private JTextField textField_num2;
	private JTextField textField_optMail;
	private JTextField textField_csvPrice;
	private static JTextArea localeConsole;

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					File file = new File("." + File.separator + "config" + File.separator + "config.txt");
					File defPath = null;
					if(file.exists()){
						FileInputStream fis = new FileInputStream(file);
						InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
						BufferedReader bReader = new BufferedReader(isr);
						String temp = bReader.readLine();
						defPath = new File(temp);
						
						needBrowser = bReader.readLine().equals("1");
						
						bReader.close();
					}
					
					AppWindow window = new AppWindow();
					instance = window;
					
					if(defPath.isDirectory() & defPath.exists()){
						defaultDirectory = defPath.getAbsolutePath();
						AppWindow.printToLocalConsole("ПУТЬ ПО УМОЛЧАНИЮ(config.txt):\n" + defaultDirectory);
					}
					
					instance.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	public AppWindow() {
		initialize();
	}


	private void initialize() {

		frame = new JFrame();
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(AppWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/computer.gif")));
		frame.setTitle("CSV-Daemon");
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice monitor = ge.getDefaultScreenDevice();
		DisplayMode dm = monitor.getDisplayMode();
		int height = dm.getHeight();
		int width = dm.getWidth();
		frame.setBounds((width / 2 - 350), (height / 2 - 350), 705, 745);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(SystemColor.activeCaption);
		tabbedPane.setBounds(0, 0, 700, 700);
		tabbedPane.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				int tabN = tabbedPane.getSelectedIndex();
				if(tabN == 3 & needBrowser){
					frame.setBounds((frame.getX() + frame.getWidth() / 2 - 750), frame.getY(), 1500, frame.getHeight());
					tabbedPane.setSize(1495, frame.getHeight()-20);
				}else{
					frame.setBounds((frame.getX() + frame.getWidth() / 2 - 352), frame.getY(), 705, frame.getHeight());
					tabbedPane.setSize(700, frame.getHeight()-10);
				}
				
			}
		});
		
		frame.getContentPane().add(tabbedPane);
		
		panel = new JPanel();
		tabbedPane.addTab("\u041E\u0442\u0447\u0451\u0442", null, panel, null);
		panel.setLayout(null);
		
		JLabel label = new JLabel("\u0411\u043B\u0430\u043D\u043A:");
		label.setBounds(10, 11, 105, 14);
		panel.add(label);
		
		JComboBox<String> comboBox_opt = new JComboBox<String>();
		comboBox_opt.setBounds(125, 33, 560, 20);
		panel.add(comboBox_opt);
		
		comboBox_opt.addItemListener(new ItemListener() {
			
			Pattern p = Pattern.compile("(.*?)!(.*?)!(.*?)!(.*?)$");
			Matcher m;
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(comboBox_opt.getSelectedIndex() == -1){
					return;
				}
				String str = (String)comboBox_opt.getSelectedItem();
				m = p.matcher(str);
				m.matches();
				textField_optNum.setText(m.group(3));
				textField_optMail.setText(m.group(4));			
			}
		});
		
		JComboBox<File> comboBox_Blank = new JComboBox<File>();
		comboBox_Blank.setBounds(125, 8, 560, 20);

		
		File examples = new File(examplesPath);
		
		Pattern p = Pattern.compile("(.*?).docx$");
		Matcher m;
		for(File ex : examples.listFiles()){
			m = p.matcher(ex.getName());
			if(m.matches()){
				comboBox_Blank.addItem(ex);
			}
		}
		comboBox_Blank.setSelectedIndex(-1);
		comboBox_Blank.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				File file = (File) comboBox_Blank.getSelectedItem(); //берем файл шаблона
				file = new File(file.getAbsolutePath().replace(".docx", ".txt")); // берем одноименный файл формата txt
				
				comboBox_opt.setSelectedIndex(-1);
				comboBox_opt.removeAllItems();
				if(file.exists()){
					
					FileInputStream fis = null;
					InputStreamReader isr = null;
					try {
						fis = new FileInputStream(file);
						isr = new InputStreamReader(fis, "UTF-8");
					} catch (FileNotFoundException | UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
					BufferedReader bReader = new BufferedReader(isr);
					
					String opt = null;
					try {
						textField_region.setText(bReader.readLine());
						opt = bReader.readLine();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					while(opt != null){
						comboBox_opt.addItem(opt);
						
						try {
							opt = bReader.readLine();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					
					try {
						bReader.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		panel.add(comboBox_Blank);
		
		JLabel label_1 = new JLabel("\u041E\u043F\u0442\u0438\u043C\u0438\u0437\u0430\u0442\u043E\u0440:");
		label_1.setBounds(10, 36, 105, 14);
		panel.add(label_1);
		
		JCheckBox checkBox_reach = new JCheckBox("\u041F\u043E \u0432\u044B\u0445\u043E\u0434\u0443");
		checkBox_reach.setBounds(10, 57, 97, 23);
		panel.add(checkBox_reach);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 87, 675, 6);
		panel.add(separator);
		
		JLabel label_2 = new JLabel("\u0424\u0430\u0439\u043B \u0441 \u043C\u0435\u0442\u0440\u0438\u043A\u043E\u0439:");
		label_2.setBounds(10, 104, 105, 14);
		panel.add(label_2);
		
		textField_metrica = new JTextField();
		textField_metrica.setEditable(false);
		textField_metrica.setBounds(125, 101, 498, 20);
		panel.add(textField_metrica);
		textField_metrica.setColumns(10);
		
		JButton button_metrica = new JButton("...");
		button_metrica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileopen = new JFileChooser();  
				fileopen.setCurrentDirectory(new File(AppWindow.defaultDirectory));
				
				fileopen.setFileFilter(new FileNameExtensionFilter("Документы Microsoft Word", "docx"));
				fileopen.setFileSelectionMode(JFileChooser.FILES_ONLY);
				
				int ret = fileopen.showDialog(null, "Открыть файлс docx с Метрикой");
				if(ret == JFileChooser.APPROVE_OPTION){
					File file = fileopen.getSelectedFile();
					textField_metrica.setText(file.getAbsolutePath());
				}
			}
		});
		button_metrica.setBounds(633, 100, 52, 23);
		panel.add(button_metrica);
		
		JLabel lblCsv = new JLabel("CSV:");
		lblCsv.setBounds(10, 129, 97, 14);
		panel.add(lblCsv);
		
		textField_csv = new JTextField();
		textField_csv.setEditable(false);
		textField_csv.setBounds(125, 126, 498, 20);
		panel.add(textField_csv);
		textField_csv.setColumns(10);
		
		JButton button_csv = new JButton("...");
		button_csv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileopen = new JFileChooser();  
				if(textField_csv.getText().isEmpty()){
					fileopen.setCurrentDirectory(new File(AppWindow.defaultDirectory));
				}else{
					fileopen.setCurrentDirectory(new File(textField_csv.getText()).getParentFile());
				}
						
				fileopen.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileopen.setFileFilter(new FileNameExtensionFilter(null, "csv"));
				int ret = fileopen.showDialog(null, "Открыть файл");
				if(ret == JFileChooser.APPROVE_OPTION){
					File file = fileopen.getSelectedFile();
					textField_csv.setText(file.getAbsolutePath());
				}
			}
		});
		button_csv.setBounds(633, 125, 52, 23);
		panel.add(button_csv);
		
		JLabel lblCsv_1 = new JLabel("CSV \u0446\u0435\u043D\u044B:");
		lblCsv_1.setBounds(10, 154, 97, 14);
		lblCsv_1.setVisible(false);
		panel.add(lblCsv_1);
		
		textField_csvPrice = new JTextField();
		textField_csvPrice.setEditable(false);
		textField_csvPrice.setBounds(125, 151, 498, 20);
		textField_csvPrice.setVisible(false);
		panel.add(textField_csvPrice);
		textField_csvPrice.setColumns(10);
		
		JButton button_csvPrice = new JButton("...");
		button_csvPrice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileopen = new JFileChooser();  
				if(textField_csv.getText().isEmpty()){
					fileopen.setCurrentDirectory(new File(AppWindow.defaultDirectory));
				}else{
					fileopen.setCurrentDirectory(new File(textField_csv.getText()).getParentFile());
				}
						
				fileopen.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileopen.setFileFilter(new FileNameExtensionFilter(null, "csv"));
				int ret = fileopen.showDialog(null, "Открыть файл");
				if(ret == JFileChooser.APPROVE_OPTION){
					File file = fileopen.getSelectedFile();
					textField_csvPrice.setText(file.getAbsolutePath());
				}
			}
		});
		button_csvPrice.setBounds(633, 150, 52, 23);
		button_csvPrice.setVisible(false);
		panel.add(button_csvPrice);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 190, 675, 2);
		panel.add(separator_1);
		
		JLabel label_4 = new JLabel("\u041D\u043E\u043C. \u043E\u043F\u0442\u0438\u043C\u0438\u0437\u0430\u0442\u043E\u0440\u0430:");
		label_4.setBounds(10, 203, 130, 14);
		panel.add(label_4);
		
		textField_optNum = new JTextField();
		textField_optNum.setBounds(150, 203, 190, 20);
		panel.add(textField_optNum);
		textField_optNum.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("\u041F\u043E\u0447\u0442\u0430 \u043E\u043F\u0442\u0438\u043C\u0438\u0437\u0430\u0442\u043E\u0440\u0430:");
		lblNewLabel_4.setBounds(355, 206, 130, 14);
		panel.add(lblNewLabel_4);
		
		textField_optMail = new JTextField();
		textField_optMail.setBounds(495, 203, 190, 20);
		panel.add(textField_optMail);
		textField_optMail.setColumns(10);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(10, 234, 675, 4);
		panel.add(separator_2);
		
		JLabel lblNewLabel = new JLabel("\u0421\u0430\u0439\u0442:");
		lblNewLabel.setBounds(10, 249, 97, 14);
		panel.add(lblNewLabel);
		
		textField_site = new JTextField();
		textField_site.setBounds(150, 246, 305, 20);
		panel.add(textField_site);
		textField_site.setColumns(10);
		
		JLabel label_5 = new JLabel("\u041E\u0431\u0440\u0430\u0449\u0435\u043D\u0438\u0435:");
		label_5.setBounds(10, 274, 130, 14);
		panel.add(label_5);
		
		textField_appeal = new JTextField();
		textField_appeal.setText("\u0423\u0432\u0430\u0436\u0430\u0435\u043C\u044B\u0439 ");
		textField_appeal.setBounds(150, 271, 305, 20);
		panel.add(textField_appeal);
		textField_appeal.setColumns(10);
		
		JLabel label_6 = new JLabel("\u0414\u0430\u0442\u0430:");
		label_6.setBounds(10, 299, 130, 14);
		panel.add(label_6);
		
		textField_date = new JTextField();
		textField_date.setBounds(150, 296, 305, 20);
		panel.add(textField_date);
		textField_date.setColumns(10);
		Calendar c = Calendar.getInstance();
		textField_date.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG_STANDALONE, new Locale.Builder().setLanguage("ru").setScript("Cyrl").build()) + ", " + c.get(Calendar.YEAR));
		
		JLabel label_7 = new JLabel("\u0420\u0435\u0433\u0438\u043E\u043D \u043F\u0440\u043E\u0434\u0432\u0438\u0436\u0435\u043D\u0438\u044F:");
		label_7.setBounds(10, 324, 130, 14);
		label_7.setVisible(false);
		panel.add(label_7);
		
		textField_region = new JTextField();
		textField_region.setBounds(150, 321, 305, 20);
		textField_region.setVisible(false);
		panel.add(textField_region);
		textField_region.setColumns(10);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(10, 349, 675, 2);
		panel.add(separator_3);
		
		JCheckBox checkBox_leaveEmpty = new JCheckBox("\u041E\u0441\u0442\u0430\u0432\u043B\u044F\u0442\u044C \"\u043D\u0435\u043D\u0443\u0436\u043D\u044B\u0435\" \u0441\u0442\u0440\u043E\u0447\u043A\u0438");
		checkBox_leaveEmpty.setBounds(355, 402, 224, 23);
		panel.add(checkBox_leaveEmpty);
		
		JLabel label_10 = new JLabel("\u0420\u0430\u0437\u0434\u0435\u043B\u044C\u043D\u043E?");
		label_10.setBounds(198, 356, 75, 14);
		panel.add(label_10);
		
		JLabel label_8 = new JLabel("\u0424\u0440\u0430\u0437\u044B \u0431\u0435\u0437 \u0434\u043E\u0431\u0430\u0432\u043B\u0435\u043D\u0438\u0439:");
		label_8.setBounds(10, 381, 150, 14);
		panel.add(label_8);
		
		JLabel lblNewLabel_1 = new JLabel("\u0420\u043E\u0441\u0442\u043E\u0432-\u043D\u0430-\u0414\u043E\u043D\u0443:");
		lblNewLabel_1.setBounds(10, 406, 150, 14);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("\u0414\u0440\u0443\u0433\u0438\u0435 \u0433\u043E\u0440\u043E\u0434\u0430:");
		lblNewLabel_2.setBounds(10, 431, 150, 14);
		panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("\u0414\u043E\u043F. \u0441\u043B\u043E\u0432\u0430:");
		lblNewLabel_3.setBounds(10, 456, 150, 14);
		panel.add(lblNewLabel_3);
		
		JCheckBox checkBox_isSingles = new JCheckBox("");
		checkBox_isSingles.setSelected(true);
		checkBox_isSingles.setBounds(166, 377, 30, 23);
		panel.add(checkBox_isSingles);
		
		JCheckBox checkBox_isRostov = new JCheckBox("");
		checkBox_isRostov.setSelected(true);
		checkBox_isRostov.setBounds(166, 402, 30, 23);
		panel.add(checkBox_isRostov);
		
		JCheckBox checkBox_isCities = new JCheckBox("");
		checkBox_isCities.setBounds(166, 427, 30, 23);
		panel.add(checkBox_isCities);
		
		JCheckBox checkBox_isAddWords = new JCheckBox("");
		checkBox_isAddWords.setBounds(166, 452, 30, 23);
		panel.add(checkBox_isAddWords);
		
		JLabel label_9 = new JLabel("\u0411\u043E\u043D\u0443\u0441?");
		label_9.setBounds(283, 356, 46, 14);
		panel.add(label_9);
		
		JCheckBox checkBox_isSinglesBonus = new JCheckBox("");
		checkBox_isSinglesBonus.setSelected(true);
		checkBox_isSinglesBonus.setBounds(290, 377, 30, 23);
		panel.add(checkBox_isSinglesBonus);
		
		JCheckBox checkBox_isRostovBonus = new JCheckBox("");
		checkBox_isRostovBonus.setBounds(290, 402, 30, 23);
		panel.add(checkBox_isRostovBonus);
		
		JCheckBox checkBox_isCitiesBonus = new JCheckBox("");
		checkBox_isCitiesBonus.setBounds(290, 427, 30, 23);
		panel.add(checkBox_isCitiesBonus);
		
		JCheckBox checkBox_isAddWordsBonus = new JCheckBox("");
		checkBox_isAddWordsBonus.setBounds(290, 452, 30, 23);
		panel.add(checkBox_isAddWordsBonus);
		

		
		JCheckBox checkBox_1 = new JCheckBox("");
		checkBox_1.setSelected(true);
		checkBox_1.setBounds(224, 377, 30, 23);
		panel.add(checkBox_1);
		
		JCheckBox checkBox_2 = new JCheckBox("");
		checkBox_2.setBounds(224, 402, 30, 23);
		panel.add(checkBox_2);
		
		JCheckBox checkBox_3 = new JCheckBox("");
		checkBox_3.setBounds(224, 427, 30, 23);
		panel.add(checkBox_3);
		
		JCheckBox checkBox_4 = new JCheckBox("");
		checkBox_4.setBounds(224, 452, 30, 23);
		panel.add(checkBox_4);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setOrientation(SwingConstants.VERTICAL);
		separator_4.setBounds(350, 352, 2, 118);
		panel.add(separator_4);
		
		JCheckBox checkBox_isTableCheck = new JCheckBox("\u041F\u0440\u043E\u0432\u0435\u0440\u044F\u0442\u044C \u0446\u0435\u043B\u043E\u0441\u0442\u043D\u043E\u0441\u0442\u044C \u0442\u0430\u0431\u043B\u0438\u0446");
		checkBox_isTableCheck.setSelected(true);
		checkBox_isTableCheck.setBounds(355, 358, 224, 23);
		panel.add(checkBox_isTableCheck);
		
		JCheckBox checkBox_bestLineToTop = new JCheckBox("\u0412\u044B\u0442\u044F\u0433\u0438\u0432\u0430\u0442\u044C \"\u043B\u0443\u0447\u0448\u0438\u0435\" \u0441\u0442\u0440\u043E\u0447\u043A\u0438");
		checkBox_bestLineToTop.setSelected(true);
		checkBox_bestLineToTop.setBounds(355, 381, 224, 23);
		panel.add(checkBox_bestLineToTop);
		
		
		JLabel label_11 = new JLabel("\u0411\u043E\u043D\u0443\u0441:");
		label_11.setBounds(355, 431, 100, 14);
		panel.add(label_11);
		
		JLabel label_12 = new JLabel("\u041E\u0441\u0442\u0430\u043B\u044C\u043D\u044B\u0435:");
		label_12.setBounds(355, 456, 100, 14);
		panel.add(label_12);
		
		textField_num1 = new JTextField();
		textField_num1.setText("16");
		textField_num1.setBounds(461, 428, 65, 20);
		panel.add(textField_num1);
		textField_num1.setColumns(10);
		
		textField_num2 = new JTextField();
		textField_num2.setText("28");
		textField_num2.setBounds(461, 453, 65, 20);
		panel.add(textField_num2);
		textField_num2.setColumns(10);
		
		JButton btnNewButton = new JButton("DO");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBox_Blank.getSelectedItem() == null){
					showMessage("Бланк...");
					return;
				}
				if(textField_metrica.getText().equals("")){
					showMessage("Метрика...");
					return;
				}
				if(textField_csv.getText().equals("")){
					showMessage("CSV...");
					return;
				}
				
				String docExamplePath;
				boolean isReach = checkBox_reach.isSelected();
				
				HashMap<String, String> values = new HashMap<String, String>();
				
				String example = ((File)comboBox_Blank.getSelectedItem()).getAbsolutePath();
				values.put("examplePath", example);
				String metrica = textField_metrica.getText();
				values.put("metricaPath", metrica);
				String csv = textField_csv.getText();
				values.put("csvPath", csv);
				String resultFileName = csv.replace(".csv", ".docx");
				
				values.put("phphonenumber", textField_optNum.getText());
				values.put("phmailto", textField_optMail.getText());
				
				values.put("phsitename", textField_site.getText());
				values.put("phdirectorname", textField_appeal.getText());
				values.put("phmonth", textField_date.getText());
				
				putBool(values, "isSingles", checkBox_isSingles);
				putBool(values, "isRostov", checkBox_isRostov);
				putBool(values, "isCities", checkBox_isCities);
				putBool(values, "isAddWords", checkBox_isAddWords);
				
				putBool(values, "isNeedTableChecking", checkBox_isTableCheck);
				putBool(values, "leaveEmpty", checkBox_leaveEmpty);
				putBool(values, "isBestLineToTop", checkBox_bestLineToTop);
				
				if(!isReach){
					docExamplePath = "." + File.separator + "config" + File.separator + "EXAMPLE.docx";
					
					putBool(values, "isSepSingles", checkBox_1);
					putBool(values, "isSepRostov", checkBox_2);
					putBool(values, "isSepOther", checkBox_3);
					putBool(values, "isSepAddw", checkBox_4);
					
					putBool(values, "isSinglesBonus", checkBox_isSinglesBonus);
					putBool(values, "isRostovBonus", checkBox_isRostovBonus);
					putBool(values, "isCitiesBonus", checkBox_isCitiesBonus);
					putBool(values, "isAddWordsBonus", checkBox_isAddWordsBonus);
					
					putBool(values, "leaveEmpty", checkBox_leaveEmpty);
					
					values.put("lastBonusPos", textField_num1.getText());
					values.put("lastCitiesPos", textField_num2.getText());
					
					normalModule.ReportBuilder tableBuilder = new normalModule.ReportBuilder(csv, values);
					WordprocessingMLPackage tableWMLP = tableBuilder.buildReport(docExamplePath).getWMLP();
					try {
						normalModule.ReportAssembler.assebmle(example, tableWMLP, metrica, resultFileName, values, ".");
					} catch (Docx4JException | JAXBException | IOException e1) {
						e1.printStackTrace();
					}

					
				}else{
					if(textField_csvPrice.getText().equals("")){
						showMessage("CSV с ценами...");
						return;
					}
					docExamplePath = "." + File.separator + "config" + File.separator + "priceEXAMPLE.docx";
					String csvPrice = textField_csvPrice.getText();
					
					values.put("promoRegion", textField_region.getText());
					
					putBool(values, "isSinglesReach", checkBox_1);
					putBool(values, "isRostovReach", checkBox_2);
					putBool(values, "isCitiesReach", checkBox_3);
					putBool(values, "isAddWordsReach", checkBox_4);
					
					values.put("topNum", textField_num1.getText());
					values.put("leaveNum", textField_num2.getText());
					
					summCounterModule.ReportBuilder tableBuilder = new summCounterModule.ReportBuilder(csv, csvPrice, values);
					summCounterModule.DOCXworker dw = tableBuilder.buildReport(docExamplePath);
					
					WordprocessingMLPackage tableWMLP = dw.getWMLP();
					try {
						normalModule.ReportAssembler.assebmle(example, tableWMLP, metrica, resultFileName, values, ".");
					} catch (Docx4JException | JAXBException | IOException e1) {
						e1.printStackTrace();
					}
					
				}
			}
		});
		btnNewButton.setBounds(585, 352, 97, 118);
		panel.add(btnNewButton);
		
		localeConsole = new JTextArea();
		localeConsole.setEditable(false);
		localeConsole.setBounds(10, 481, 675, 191);
		
		panel.add(localeConsole);
		
		checkBox_reach.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				checkBox_leaveEmpty.setEnabled(!checkBox_reach.isSelected());
				checkBox_isSinglesBonus.setVisible(!checkBox_reach.isSelected());
				checkBox_isRostovBonus.setVisible(!checkBox_reach.isSelected());
				checkBox_isCitiesBonus.setVisible(!checkBox_reach.isSelected());
				checkBox_isAddWordsBonus.setVisible(!checkBox_reach.isSelected());
				label_9.setVisible(!checkBox_reach.isSelected());
				label_7.setVisible(checkBox_reach.isSelected());
				textField_region.setVisible(checkBox_reach.isSelected());
				
				lblCsv_1.setVisible(checkBox_reach.isSelected());
				textField_csvPrice.setVisible(checkBox_reach.isSelected());
				button_csvPrice.setVisible(checkBox_reach.isSelected());
				
				if(checkBox_reach.isSelected()){
					label_10.setText("По выходу?");
					label_11.setText("Топ:");
					label_12.setText("Оставлять до:");
					textField_num1.setText("10");
					textField_num2.setText("15");
				}else{
					label_10.setText("Раздельно?");
					label_11.setText("Бонус:");
					label_12.setText("Остальные:");
					textField_num1.setText("16");
					textField_num2.setText("28");
				}
			}
		});
				
		panelSettings = new PanelSettings();
		tabbedPane.addTab("\u041D\u0430\u0441\u0442\u0440\u043E\u0439\u043A\u0438", null, panelSettings, null);
		
		if(needBrowser){
			browser_1 = new WebBrowserPanel();
			tabbedPane.addTab("Яндекс Метрика", new ImageIcon(AppWindow.class.getResource("/chrriis/dj/nativeswing/swtimpl/components/resource/internal_browser.gif")), browser_1, BorderLayout.CENTER);
		}
							
	}
	
	public static void printToLocalConsole(String text){
		if(text != null){
			localeConsole.append(text + "\n");
		}
	}
	
	public void throwError(String str, Exception e){
		JOptionPane.showMessageDialog(frame, "Произошла ошибка:\n" + str);
		if(e != null){
			e.printStackTrace();
			for(int i = 0; i < e.getStackTrace().length; i++){
				e.getStackTrace()[i].toString();
			}
		}
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		printToLocalConsole("Отчет НЕ БЫЛ СОЗДАН");
	}
	
	public void showMessage(String message){
		JOptionPane.showMessageDialog(frame, message);
	}
	
	private void putBool(HashMap<String, String> values, String key, JCheckBox param){
		if(param.isSelected()){
			values.put(key, "checked");
		}
		return;
	}
}

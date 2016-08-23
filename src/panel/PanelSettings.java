package panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;

public class PanelSettings extends JPanel{
	
	private String optsPath = "." + File.separator + "config" + File.separator + "EXAMPLES";
	
	private static final long serialVersionUID = 1L;
	private JTextField textField_defpath;
	private JTextField textField_datesettngs;
	private JCheckBox checkBox_browser;
	
	private JTable table;
	private File curentOptFile;
	private DefaultTableModel model;
	private JLabel label_defPath;
	private JButton button_defpath;
	private JLabel label_datesettings;
	private JButton btnNewButton_1;
	private JSeparator separator_4;
	private JLabel label_15;
	private JComboBox<File> comboBox_3;
	private JButton btnNewButton;
	private JButton btnNewButton2;
	
	public PanelSettings() {
		super();
		init();
		fill();
	}

	private void init(){
		
		setBackground(UIManager.getColor("Button.background"));
		setLayout(null);
		
		label_defPath = new JLabel("\u041F\u0430\u043F\u043A\u0430 \u043F\u043E-\u0443\u043C\u043E\u043B\u0447\u0430\u043D\u0438\u044E");
		label_defPath.setBounds(10, 11, 675, 14);
		add(label_defPath);
		
		textField_defpath = new JTextField();
		textField_defpath.setBounds(10, 36, 595, 20);
		add(textField_defpath);
		textField_defpath.setColumns(10);
		textField_defpath.setEditable(false);
		
		button_defpath = new JButton("...");
		button_defpath.setBounds(615, 35, 70, 23);
		add(button_defpath);
		button_defpath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(textField_defpath.getText());
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int ret = jfc.showDialog(null, "Выбрать папку");
				if(ret == JFileChooser.APPROVE_OPTION){
					textField_defpath.setText(jfc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		label_datesettings = new JLabel("\u041C\u0435\u0441\u044F\u0446, \u0433\u043E\u0434");
		label_datesettings.setBounds(10, 67, 675, 14);
		add(label_datesettings);
		
		textField_datesettngs = new JTextField();
		textField_datesettngs.setBounds(10, 92, 250, 20);
		add(textField_datesettngs);
		textField_datesettngs.setColumns(10);
		
		checkBox_browser = new JCheckBox("\u0418\u0441\u043F\u043E\u043B\u044C\u0437\u043E\u0432\u0430\u0442\u044C \u0431\u0440\u0430\u0443\u0437\u0435\u0440? [добавится 4я \u0432\u043A\u043B\u0430\u0434\u043A\u0430]");
		checkBox_browser.setBounds(10, 117, 595, 23);
		add(checkBox_browser);
		
		btnNewButton_1 = new JButton("\u041F\u0440\u0438\u043C\u0435\u043D\u0438\u0442\u044C(требуется перезапуск)");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		btnNewButton_1.setBounds(10, 578, 675, 45);
		add(btnNewButton_1);
		
		separator_4 = new JSeparator();
		separator_4.setBounds(10, 142, 675, 2);
		add(separator_4);
		
		label_15 = new JLabel("\u0424\u0430\u0439\u043B \u0441\u043E \u0441\u043F\u0438\u0441\u043E\u043C \u043E\u043F\u0442\u0438\u043C\u0438\u0437\u0430\u0442\u043E\u0440\u043E\u0432:");
		label_15.setBounds(10, 150, 675, 14);
		add(label_15);
		table = new JTable();
		
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		model = new DefaultTableModel(){  

			private static final long serialVersionUID = 1L;

			@Override  
			public boolean isCellEditable(int row, int column){  
				return true;  
			};     
		}; 
		
		model.addColumn("Имя");  
		model.addColumn("Телефон");  
		model.addColumn("Почта");  
		table.setEnabled(true);
		                  
		table.setModel(model);
		table.setBounds(10, 205, 675, 200);
		JScrollPane jsp = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setBounds(10, 205, 675, 200);
		add(jsp);
		
		comboBox_3 = new JComboBox<File>();
		comboBox_3.setBounds(10, 175, 675, 20);
		add(comboBox_3);
		
		File opts = new File (optsPath);
		Pattern p = Pattern.compile("(.*?).txt$");
		Matcher m;
		for(File f : opts.listFiles()){
			m = p.matcher(f.getName());
			if(m.matches()){
				comboBox_3.addItem(f);
			}
		}
		comboBox_3.setSelectedIndex(-1);
		
		comboBox_3.addActionListener(new ActionListener() {
			Pattern p = Pattern.compile("(.*?)!(.*?)!(.*?)$");
			Matcher m = null;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(comboBox_3.getSelectedIndex() == -1) return;
				
				File file = (File) comboBox_3.getSelectedItem();
				if(file.exists()){
					for(int i = model.getRowCount() - 1; i >= 0 ; i--){
						model.removeRow(i);
					}
					curentOptFile = file;
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
						opt = bReader.readLine();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					while(opt != null){
						m = p.matcher(opt);
						m.matches();
						String[] data = { m.group(1), m.group(2), m.group(3) };
						model.addRow(data);
						
						try {
							opt = bReader.readLine();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					model.fireTableDataChanged();
					try {
						bReader.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				
			}
		});
		
		btnNewButton = new JButton("+");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				model.addRow(new String[] {null, null, null});
			}
		});
		btnNewButton.setBounds(630, 420, 50, 23);
		add(btnNewButton);
		
		btnNewButton2 = new JButton("-");
		btnNewButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				model.removeRow(model.getRowCount() - 1);
			}
		});
		btnNewButton2.setBounds(580, 420, 50, 23);
		add(btnNewButton2);

	}
	
	public void fill(){
		try {
			File file = new File("." + File.separator + "config" + File.separator + "config.txt");
			if(file.exists()){
				FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
				BufferedReader bReader = new BufferedReader(isr);
				String string = bReader.readLine();
				textField_defpath.setText(string);
				string = bReader.readLine();
				checkBox_browser.setSelected(string.equals("1"));
				
				bReader.close();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	public void save(){
		try {
			File file = new File("." + File.separator + "config" + File.separator + "config.txt");

			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter bWriter = new BufferedWriter(osw);
			bWriter.write(textField_defpath.getText() + "\n");
			bWriter.write(textField_datesettngs.getText() + "\n");
			bWriter.write(checkBox_browser.isSelected() ? "1" : "0");
			bWriter.close();
			
			if(curentOptFile != null){
				fos = new FileOutputStream(curentOptFile);
				osw = new OutputStreamWriter(fos, "UTF-8");
				bWriter = new BufferedWriter(osw);
				for(int i = 0; i < model.getRowCount(); i++){
					if(((String)model.getValueAt(i, 0)).equals("")) continue;
					bWriter.write((String)model.getValueAt(i, 0) + "!" + (String)model.getValueAt(i, 1) + "!" + (String)model.getValueAt(i, 2) + "\n");
				}
				
				bWriter.close();
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
		
}
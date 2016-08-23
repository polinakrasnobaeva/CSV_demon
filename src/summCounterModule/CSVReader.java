package summCounterModule;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import csvdemon.AppWindow;

public class CSVReader {
private BufferedReader bReader;
private BufferedReader bReader2;
private Pattern dataPattern;
private Pattern dataPattern2;
private Matcher m;
private Matcher m2;
private Pattern separatorPattern;


	
	public CSVReader(String phaseFileName, String priceFileName) {
		try {
			FileInputStream fis = new FileInputStream(phaseFileName);
			InputStreamReader isr = new InputStreamReader(fis, "windows-1251");
			bReader = new BufferedReader(isr);
			FileInputStream fis2 = new FileInputStream(priceFileName);
			InputStreamReader isr2 = new InputStreamReader(fis2, "windows-1251");
			bReader2 = new BufferedReader(isr2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		separatorPattern = Pattern.compile("^[0-9]{1,}$"); // ��������� �������� ���� ��� ���� ���� ���� "20000000214124"
		
		dataPattern = skipLine(); //������� ������������ ������� � ������ �������� �������
		dataPattern2 = skipLineForPrice();
		
	}
	
		

	//��������� ������ CSV � ���������� ��� ������ ������ Line
	public Line readLine() {
		String result = "";
		String priceResult = "";
		try {
			result = bReader.readLine();
			priceResult = bReader2.readLine();

			if(result == null || priceResult == null){
				return null;
			}
			
			String string;
			int ya;
			int price;
			
			
			m = dataPattern.matcher(result);
			m2 = dataPattern2.matcher(priceResult);
			
			Line line = null;
			if(m.find() & m2.find()){
				string = m.group(1).replace('\\', '0').replace('/', '0'); //�������� ��� ����� ����������� ��� Tanyusha-style
				try{// ���� ����� �� ������, ���� �� ������� ���������� ����� �� ������ � ��������. 
					ya = Integer.valueOf(m.group(2));
				}catch(NumberFormatException e){
					ya = 60;
				}
				
				try{
					price = Integer.valueOf(m2.group(2));
				}catch(NumberFormatException e){
					price = 0;
				}
				
				line = new Line(string, ya, price);
				
				return line;
			}else{
				AppWindow.printToLocalConsole("��|" + result);
				AppWindow.printToLocalConsole(m.pattern().toString());
				AppWindow.printToLocalConsole("������ ������ ������\n������ �� �������� �������");
				return null;
			}
			
			
		} catch (IOException e) {
			AppWindow.printToLocalConsole("������� ������||" + result);
			try {
				bReader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			AppWindow.printToLocalConsole("��������� ������ ��� ����������� �� �����.");
			return null;
		}		
	}
	
	//���������� ������ ������� ��� ������ ����������� �����, ������������ �� ������ ������ CSV, ����������� ���������� �������
	private Pattern skipLine(){
		int stringBySeparatorType = 0;
		String line = null;
		try {
			line = bReader.readLine();
		} catch (IOException e) {
			AppWindow.printToLocalConsole("�� ������� �������� ������");
			try {
				bReader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			AppWindow.printToLocalConsole("��������� ������ ��� ����������� �� �����.");
		}
		Pattern p = Pattern.compile("^\".*?\";\".*?\".*$"); //����� �������
		Matcher m = p.matcher(line);
		if(m.matches()){
			stringBySeparatorType = 1;
		}else{
			p = Pattern.compile("^.*?,.*?,.*$"); //����� ����������� - �������
			m = p.matcher(line);
			if(m.matches()){
				stringBySeparatorType = 2;
			}else{
				p = Pattern.compile("^.*?\t.*?\t.*$"); //����� ����������� - ���������
				m = p.matcher(line);
				if(m.matches()){
					stringBySeparatorType = 3;
				}else{
					stringBySeparatorType = 0;
				}
			}
		}
		
		boolean isThereISCheckedColumn = line.substring(0, 15).contains("IsChecked");
		if(isThereISCheckedColumn){
			AppWindow.printToLocalConsole("������ ������� IsChecked ����");
		}else{
			AppWindow.printToLocalConsole("������� �������� IsChecked ���");
		}
		switch(stringBySeparatorType){
			// ��� �������� �� �� ����������� - ;
			case 1 : {
				AppWindow.printToLocalConsole("������� - � ������������ ';'");
				if(isThereISCheckedColumn){
					return Pattern.compile("^(?:\"[Ff]alse\"|\"[Tt]rue\");\"(.*?)\";\"(.*?)\";\"(.*?)\"(:?.*$|$)");
				}else{
					return Pattern.compile("^\"(.*?)\";\"(.*?)\";\"(.*?)\"(:?.*$|$)");
				}
			}
			//����� ������� CSV �������, �� ���������� ������ ������������� �����. ������ ';' - ',' � ������� ���������
			//(�� �� ������. ���� � ������ ���� �������, �� ������� ��������, ����� �� ������� � ������������ ������)
			case 2 : {
				AppWindow.printToLocalConsole("������� - � ������������ ',', �������� ������� ������");
				if(isThereISCheckedColumn){
					return Pattern.compile("^(?:[Ff]alse|[Tt]rue),(\".*?,.*?\"|.*?),(\".+?,.+?\"|.*?),(\".+?,.+?\"|.*?)(:?,.*$|$)");
				}else{
					return Pattern.compile("^(\".*?,.*?\"|.*?),(\".+?,.+?\"|.*?),(\".+?,.+?\"|.*?)(:?,.*$|$)");
				}
			}
			//� ����� ��%$# ������� ����� ������, �� ���� ����������� ������ ����� ���
			case 3 : {
				AppWindow.printToLocalConsole("������� - � ������������ '���������'");
				if(isThereISCheckedColumn){
					return Pattern.compile("^(?:[Ff]alse|[Tt]rue)\t(.*?)\t(.*?)\t(.*?)(:?\t.*$|$)");
				}else{
					return Pattern.compile("^(.*?)\t(.*?)\t(.*?)(:?\t.*$|$)");
				}
			}	
		}
		//�� ��� ��� �������� ����� �������������������
		return null;
	}
	
	private Pattern skipLineForPrice(){
		String line = null;
		try {
			line = bReader2.readLine();
		} catch (IOException e) {
			AppWindow.printToLocalConsole("�� ������� �������� ������");
			try {
				bReader2.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			AppWindow.printToLocalConsole("��������� ������ ��� ����������� �� �����.");
		}
		Pattern p = Pattern.compile("^\"(.*?)\";\"(.*)\"$"); //����� �������
		Matcher m = p.matcher(line);
		if(m.matches()){
			return p;
		}
		p = Pattern.compile("^(.*?),(.*)$"); //����� ����������� - �������
		m = p.matcher(line);
		if(m.matches()){
			return p;
		}
		
		p = Pattern.compile("^(.*?)\t(.*)$"); //����� ����������� - ���������
		m = p.matcher(line);
		if(m.matches()){
			return p;
		}
		
		//�� ��� ��� �������� ����� �������������������
		return null;
	}
	
	
	public void close()
	{
		try {
			bReader.close();
			bReader2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isTableSeparator(String string){
		Matcher m = separatorPattern.matcher(string); 
		return m.matches();
	}
	
}

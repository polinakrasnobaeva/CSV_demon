package normalModule;
import java.util.ArrayList;

import csvdemon.AppWindow;
import normalModule.Line;
import normalModule.Table;

public class TableFixer {
	//������������ | ������������ � getAdditionalPhrase, ���������� ���������� �����, ������� ������� ��� ���������
	//��, � �����, ��� 7 �������, � ������������������ ����� �� ������
	private static final int redundancy = 7;
	
	
	//������� ����, ��� ������ �������� �������� � ��� ��� ����� ������� �����
	//(������ 2 ����) ��, � ����� ����, �� ��� �� � �����
	
	//���������� "�����" ������� � �������� ������, � ��� ���������� �����-���� ������ ��������� ������ ��� ������ ������.
	//����� ��� ����, ����� ��� ��������� ������, � �������, ���� �����-���� ����� ������, ������ "�� ��������"
	//"�����" ������� - ����� ������, ������� �� ��������� � ������������� �����, ��������, 2 ������� ��� ����� � ������� ("��������, "� ���������") ��� 4 ��� �������
	public static void checkTables(ArrayList<Table> tables, Table etalonTable){
		for(Table checkingTable : tables){
			 //���� ������ �������� ��������� � ������� � �������, ����� �������, ��� �������� ������
			String additionalPhrase = getAdditionalPhrase(checkingTable);
			
			if(checkingTable.getSize() == etalonTable.getSize()){
				AppWindow.printToLocalConsole("������|" + additionalPhrase + "' ��������� � �������� �� ������");
				continue;
			}else{
				AppWindow.printToLocalConsole("������|" + additionalPhrase + "' �������, ������ ����� ���������!");
			}
			
			int fixNumber = 0;
			
			ArrayList<Line> etalonLines = etalonTable.getLines();
			
			
			String etalonString;
			
			for(int i = 0; i < etalonLines.size(); i++){
				etalonString = etalonLines.get(i).getString();
				
				if(i < checkingTable.getSize()){
					if(etalonString.equals(getBaseOfPhrase(checkingTable.getLine(i).getString(), additionalPhrase))){ //���� ������ �� �����, ������� ���������
						//U.println("������ ������ �� �����|" + checkingTable.getLine(i).getString());
						continue;
					}else{
						AppWindow.printToLocalConsole("� '" + additionalPhrase + "' ����������� ������ '" + etalonString + "'");
						checkingTable.getLines().add(i, new Line(" ", 60, 60)); //���� ���, ��������� ������, ������� ����������� � ������������� ����� ��������
						fixNumber++;
					}
				}else{
					checkingTable.getLines().add(new Line(" ", 60, 60));
				}
				
			}
			
			if(checkingTable.getSize() != etalonTable.getSize()){
				AppWindow.printToLocalConsole(
						"������ ������ �� ���������, � ���������� � ������������ ������� ���������� ����� ������� ���������� �� ������.\n"
						+ "������ �����, � ������� '" + additionalPhrase + "' ������� ������� �����"
						);
				break;
			}else{
				AppWindow.printToLocalConsole("||||������ ������ ������� " + fixNumber + " ����� � '" + additionalPhrase + "', ����� ��?");
			}
		}
	}
	
	
	//�������� ���. ����� ��� ����� �� ������ ������� (����� ���� �� �� ������ ���� ����, �� ����� �������� ������
	//�.�. ����� ���������� ����������� ��� ������ ������ ������, ���� ��� ���������,
	//�� "����" ��������� ���� ��� �� ����� ������������ ���������
	//� � ���� ������� "����" ���� ������ �� ����� ���������(���� ��)
	//*******
	//��������: "���������� � �������", "����� � �������" - ������� "� �������"
	public static String getAdditionalPhrase(Table table){	
		ArrayList<Line> lines = table.getLines();
		String firstLine = lines.get(0).getString();
		
		int magicNumber = -1; // ����� �����, � ������� ���������� ���. �����/�����
		boolean match = true;
		
		for(int cIndex = 0; cIndex < firstLine.length()-1; cIndex++){  //������ �� ���� ������ ������ ������ � �����; cIndex - ����� ����� � ����� ������
			char currentChar = firstLine.charAt(firstLine.length() -1 - cIndex);
			
			//������ �� ������ ������ �� ��. ������ ������, ��� ����� redundancy
			//����, ���� ����� � ������� ������, ��� redundancy, �� ��������� ������
			for(int i = 1; i < (table.getSize() - 1 < redundancy ? table.getSize() - 1 : redundancy); i++){ 
				
				String iString = lines.get(i).getString();
				
				char iStringChar = iString.charAt(iString.length() - 1 - cIndex);
				if(iStringChar != currentChar){
					match = false;
					break;
				}
				
			}
			if(!match){
				magicNumber = firstLine.length() - 1 - cIndex + 1 + 1; //����� ����� ������ ������� �������������� �������(� �����) ���� 1(��������� �����) ���� 1(�.�. ��� �� ���� ������)
				break;
			}
		}
		
		if(match){
			AppWindow.printToLocalConsole("������ ������� ��� ������ ����� �������, ����� ��������, �.�. ��� ����������");
		}
		
		String result = firstLine.substring(magicNumber);
		
		return result;
	}
	
	//�������� "����" ����� � ������� ����� ������� � ���������� ���. ��������� (�����, ���), ������� ����� ������ ������� getAdditionalPhrase()
	//��������, � "����� ������� ������" ���� "����� �������"
	public static String getBaseOfPhrase(String fullPhrase , String additionalPhrase){
		if(!fullPhrase.endsWith(additionalPhrase)){
			AppWindow.printToLocalConsole("������ getBaseOfPhrase �� ����� � ������ '" + fullPhrase + "' ����������� '" + additionalPhrase + "'");
		}
		
		int addPhStart = fullPhrase.indexOf(additionalPhrase);
		
		return fullPhrase.substring(0, addPhStart - 1);
	}
}

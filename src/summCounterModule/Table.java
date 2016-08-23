package summCounterModule;
import java.util.ArrayList;

public class Table {
	
	private ArrayList<Line> lines;
	
	public Table(){
		this.lines = new ArrayList<Line>();
	}
	
	public Table(ArrayList<Table> tables){
		this.lines = new ArrayList<Line>();
		for(Table table : tables){
			this.lines.addAll(table.getLines());
		}
	}
	
	public void addLine(Line line){
		this.lines.add(line);
	}
	
	public Line getLine(int num){
		return this.lines.get(num);
	}
	
	public int getSize(){
		return this.lines.size();
	}
	
	public ArrayList<Line> getLines(){
		return this.lines;
	}
	
	
	public static Table buildBestTable(ArrayList<Table> tables, int leaveNum, boolean isBestLineToTop){ //�� ���� ��������� ����� �������� �� CSV, ������� ������� � boolean �������� �� ������� ��������� google
		
		Table result = new Table();
		int tabSize = tables.get(0).getSize();
		
		for(int i = 0; i < tabSize; i++){ // � ���� ����� �� ������� ���������� �� �������� ����������� �� ������, ������� ������
			Line bestLine = null;
			for(Table tab : tables){
				Line tempLine = tab.getLine(i);
				
				if(tempLine.getYa() == -1){ // -1 ������ �� 60 ��� ��������� ��������� �������
					tempLine.setYa(60);
				}
				
				if(bestLine == null){ //������ �� ������������ ������� ��-��������� ������
					bestLine = tempLine;
				}else{
					if(tempLine.getYa() > bestLine.getYa()){ //���� ������ ���. ���� ��� � ������ �������, �� ����� ���������� ���������� ���������
						continue;
					}else if(tempLine.getYa() < bestLine.getYa()){ //���� ���. ���. �����, �� ������� �������������� ������
						bestLine = tempLine;
					}
				}
			}

			if(bestLine.getYa() <= leaveNum)
				result.addLine(bestLine);
		}
		
		if(isBestLineToTop){
			result.bestLinetoTop();
		}
		
		return result;
	}
	
	
	public void plusTable(Table table){
		lines.addAll(table.getLines());
	}
	
	public void bestLinetoTop(){
		Line bestLine = null;
		if(lines.isEmpty()) return;
		for(Line line : lines){
			if(bestLine == null){
				bestLine = line;
				continue;
			}
			if(line.getYa() < bestLine.getYa()){
				bestLine = line;
			}
			
		}
		lines.remove(bestLine);
		lines.add(0, bestLine);
	}
	
}

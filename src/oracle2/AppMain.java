package oracle2;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class AppMain extends JFrame implements ItemListener{
	ConnectionManager manager;	
	Connection con; //��簴ü�� �����ϱ� ����
	JTable table;
	JScrollPane scroll;
	JPanel p_west,p_center;
	Choice choice;
	String[][] item={
			{"���̺��� �����ϼ��� �� ",""},
			{"������̺�","emp"},
			{"�μ����̺�","dept"}
	};
	
	TableModel[] model = new TableModel[item.length];
	
	public AppMain() {
		
		//�����ΰ� ������ �и���Ű�� ���� �߰���(Controller)�� ���簡 �ʿ��ϴ�
		//JTable������ �� ��Ʈ�ѷ��� ������ TableModel�� ���ش�
		//Table�� ����� ���, JTable�� �ڽ��� ������� �� �����͸� Table�𵨷� ���� ������ ��������Ѵ�.
		manager = ConnectionManager.getInstance();
		con = manager.getConnection();
				
		
		p_west = new JPanel();
		p_center = new JPanel();
		table = new JTable();
		scroll = new JScrollPane(table);
		choice = new Choice();
		
		//���̺�𵨵��� �÷�����
		model[0] = new DefaultTableModel();
		model[1] = new EmpModel(con);
		model[2] = new DeptModel(con);
		
		
		//���̽� ����
		for(int i =0; i<item.length;i++){
			choice.add(item[i][0]);
		}

		p_west.add(choice);		//west������ ���̱�
		p_center.add(scroll);

		add(p_west, BorderLayout.WEST);
		add(p_center,BorderLayout.CENTER);
		
		pack();
		//���̽��� ������ ����
		choice.addItemListener(this);
		//������â ������ ����Ŭ ���� ����.
		this.addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent e) {
					//Ŀ�ؼ� �ݱ�
					try {
						//manager.disConnect(con);						
						//�ý�������
						System.exit(0);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		});
		
		
		
		setVisible(true);
		
		
		
	}
	
	//�ش�Ǵ� ���̺� �����ֱ�
	public void showData(int index){
		System.out.println("����� ���Ե� ���̺��� "+ item[index][1]);
		table.setModel(model[index]);
		//�ش�Ǵ� ���̺� ���� ����ϸ� �ȴ�!
		//emp-->E��, dept->DeptModel
		//�ƹ��͵� �ƴϸ�..? =>DefaultTableModel
	
	
	}
	
	public void itemStateChanged(ItemEvent e) {	
		Choice ch = (Choice)e.getSource();
		int index =ch.getSelectedIndex();
		showData(index);
		
	}
	public static void main(String[] args) {
		new AppMain();
	}
	
}

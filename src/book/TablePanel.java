/*
 * Jpanel�� ������ �г�
 * */
package book;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class TablePanel extends JPanel{
	Connection con;
	JTable table;
	JScrollPane scroll;
	TableModel model;
	//Vector ArrayList�� ����
	//������? ����ȭ ���� ����
	Vector list = new Vector(); //����ȭ�۾��� ����.. ����ȭ�� �߿��ϴٸ� ���͸� ���...�ӵ��� �߿��ϸ� Array(���⼭ �����ϴ°�...���͹ۿ��ȵ�)
	
	Vector<String> columnName=new Vector<String>();
	int cols;
	String[] column;

	
	public TablePanel() {
	
		
		table = new JTable();
		scroll = new JScrollPane(table);
		this.setLayout(new BorderLayout());
		this.add(scroll);
		
		this.setBackground(Color.PINK);
		setPreferredSize(new Dimension(650, 550));
	}

	
	//db��������, Book���̺��� ���ڵ� ��������
	public void init(){
		String sql="select * from book order by book_id asc";
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		
		try {
			
			pstmt = con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			cols = rs.getMetaData().getColumnCount();
			column= new String[cols];
			
			list.removeAll(list);
			
			for(int i=0; i<column.length;i++){
				column[i] = rs.getMetaData().getColumnName(i+1);
			}
			while(rs.next()){
				//rs�� ������ �÷����� DTO�� �Űܴ���!!
				//Book dto = new Book();
				
				Vector<String> data = new Vector<String>();
				
				data.add(Integer.toString(rs.getInt("book_id")));
				data.add(rs.getString("book_img"));
				data.add(rs.getString("book_name"));
				data.add(Integer.toString(rs.getInt("book_price")));
				data.add(Integer.toString(rs.getInt("subcategory_id")));
				list.add(data);
			
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(rs!=null){
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(pstmt!=null){
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}

	}
	
	public void setConnection(Connection con){
		this.con=con;
		init();
		//���̺� ���� jtable�� ����
		model = new AbstractTableModel() {
			
			public String getColumnName(int index){
				return column[index];
				
			}
			
			public int getRowCount() {
			
				return list.size();
			}

			public int getColumnCount() {

				return cols;
			}
			public Object getValueAt(int row, int col) {
				Vector vec = (Vector)list.get(row);
				return vec.elementAt(col);
				
			}
		};
		
		
		table.setModel(model);
		
		
		
	}
	
}

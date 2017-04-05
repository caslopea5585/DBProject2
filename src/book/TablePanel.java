/*
 * Jpanel이 얹혀질 패널
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
	//Vector ArrayList는 같다
	//차이점? 동기화 지원 여부
	Vector list = new Vector(); //동기화작업을 지원.. 동기화가 중요하다면 벡터를 사용...속도가 중요하면 Array(여기서 지원하는건...벡터밖에안됨)
	
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

	
	//db가져오기, Book테이블의 레코드 가져오기
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
				//rs의 정보를 컬렉션의 DTO에 옮겨담자!!
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
		//테이블 모델을 jtable에 적용
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

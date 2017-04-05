package book;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GridPanel extends JPanel {
	private Connection con;
	String path="C:/java_workspace2/DBProject2/data/";
	ArrayList<Book> list = new ArrayList<Book>();
	Book dto;
	BookItem item=null;
	
	public GridPanel() {
		
		
		this.setBackground(Color.CYAN);
		setPreferredSize(new Dimension(650, 550));
		this.setVisible(false);
	}
	
	public void setConnection(Connection con){
		this.con=con;		
		loadData();
	}
	public void loadData(){
		String sql ="select * from book order by book_id asc";
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			pstmt= con.prepareStatement(sql);
			rs=pstmt.executeQuery();//���� ����
			
			for(int i=0;i<list.size();i++){
			
					//list.remove(i);
					list.removeAll(list);
					System.out.println(list.size());
			
			
			}

			
			System.out.println(list + " ������?");
			
			while(rs.next()){
				dto  = new Book(); //���ڵ� 1�� ��� ���� �ν��Ͻ�
				dto.setBook_id(rs.getInt("book_id"));
				dto.setBook_img(rs.getString("book_img"));
				dto.setBook_name(rs.getString("book_name"));
				dto.setBook_price(rs.getInt("book_price"));
				dto.setSubcategory_id(rs.getInt("subcategory_id"));
				list.add(dto);
				System.out.println(dto+"dto������");
			}
			//�����ͺ��̽��� ��� ���������Ƿ� �����ο� �ݿ�����!
			init();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
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
	
	public void init(){
		
		//list�� ����ִ� Book ��ü��ŭ BookItem�� �����ؼ� ȭ�鿡 ��������!!
		for(int i=0; i<list.size();i++){
			
			Book book= list.get(i);
			System.out.println(list.size()+"������");
			try {
				Image img = ImageIO.read(new File(path+book.getBook_img()));
				String name = book.getBook_name();
				String price = Integer.toString(book.getBook_price());
				
				item = new BookItem(img, name, price);
				add(item);
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		

	}
	
	
}

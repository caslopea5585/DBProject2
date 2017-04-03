package book;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BookMain extends JFrame implements ItemListener,ActionListener{
	JPanel p_west; //좌측 등록폼
	JPanel p_content; //우측 영역 전체
	JPanel p_north; //우측 선택모드(p_content의 상단영역)
	JPanel p_table; //우측 하단모드(JTable이 붙여질 패널 ,보더방식)
	JPanel p_grid; //그리드 방식으로 보여질 패널
	Choice ch_top;
	Choice ch_sub;
	JTextField t_name;
	JTextField t_price;
	Canvas can;
	JButton bt_regist;
	CheckboxGroup group;
	Checkbox ch_table;
	Checkbox ch_grid;


	DBManager manager=DBManager.getInstance();
	Connection con;
	
	Toolkit kit =Toolkit.getDefaultToolkit() ;
	Image img;
	
	JFileChooser chooser;
	

	public BookMain() {
		p_west = new JPanel();
		p_content = new JPanel();
		p_north = new JPanel();
		p_table = new JPanel();
		p_grid = new JPanel();
		ch_top = new Choice();
		ch_sub = new Choice();
		t_name = new JTextField(20);
		t_price = new JTextField(20);
		URL url = this.getClass().getResource("/duck1.png");
		try {
			img = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		can = new Canvas(){
			public void paint(Graphics g) {
				g.drawImage(img,0, 0, 140, 140,this);
			
			}
		};
		can.setPreferredSize(new Dimension(150, 150));
		bt_regist = new JButton("등록");
		group = new CheckboxGroup();
		ch_table= new Checkbox("테이블", true, group);
		ch_grid = new Checkbox("그리드",false,group);
		chooser = new JFileChooser("C:/html_workspace/images"); //파일츄저 올리기
		
		
		p_west.add(ch_top);
		p_west.add(ch_sub);
		p_west.add(t_name);
		p_west.add(t_price);
		p_west.add(can);
		p_west.add(bt_regist);
		
	
		ch_top.setPreferredSize(new Dimension(130, 45));
		ch_sub.setPreferredSize(new Dimension(130, 45));
		p_west.setPreferredSize(new Dimension(150, 600));
		add(p_west,BorderLayout.WEST);
		
		
		p_north.add(ch_table);
		p_north.add(ch_grid);
		
		p_content.add(p_north,BorderLayout.NORTH);
		p_content.add(p_table,BorderLayout.CENTER);
		
		add(p_content);
		
		init();
		ch_top.addItemListener(this);
		can.addMouseListener(new MouseAdapter() {
		
			public void mouseClicked(MouseEvent e) {
				openFile();
			
			}
		});
		
		setVisible(true);
		setSize(800,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	public void init(){
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//초이스 컴포넌트에 최상위 목록 보이기!!
		try {
		
			con = manager.getConnection();
			String sql = "select * from topcategory";
			pstmt = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			 rs = pstmt.executeQuery();
			
			while(rs.next()){
				ch_top.add(rs.getString("category_name"));				
			}

		} catch (Exception e) {
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
	
	//하위 카테고리 가져오기
	public void getSub(String v){

		ch_sub.removeAll();		//기존에 이미 채워진 아이템이 있다면... 먼저 싹지운다. 
		StringBuffer sb = new StringBuffer();
		sb.append("select * from subcategory");
		sb.append(" where topcategory_id=(");
		sb.append(" select topcategory_id from topcategory where category_name ='"+v+"' ) ");
		
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sb.toString());
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				ch_sub.add(rs.getString("category_name"));
				
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

	//그림 파일 불러오기
	public void openFile(){
		int result = chooser.showOpenDialog(this);
		if(result==JFileChooser.APPROVE_OPTION){
			//선택한 이미지를 캔버스에 그릴꺼얌~
			File file = chooser.getSelectedFile();
			img = kit.getImage(file.getAbsolutePath());
			can.repaint();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		System.out.println("나눌렀어?");
		
	}
	
	
	public void itemStateChanged(ItemEvent e) {
		Choice ch =(Choice)e.getSource();
		getSub(ch.getSelectedItem());
	}
	
	public static void main(String[] args) {
		new BookMain();		
	}
}

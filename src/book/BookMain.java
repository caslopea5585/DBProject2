package book;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BookMain extends JFrame implements ItemListener,ActionListener{
	JPanel p_west; //좌측 등록폼
	JPanel p_content; //우측 영역 전체
	JPanel p_north; //우측 선택모드(p_content의 상단영역)
	JPanel p_center;//테이블과그리드를 묶을 패널 (FlowLayout적용될 예정 // p_table, p_grid를 존재시킬 컨테이너)
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
	File file;
	
	TablePanel tablePanel;
	

	//chooser의 값은 html과 자바가 다르므로 Choice컴포넌트의 값을 미리 받아놓자, rs객체를 대신할것이다.
	//장점은??? 더이상 rs.last(), rs.getRow..이런게 필요없어짐...
	ArrayList<SubCategory> subcategory = new ArrayList<SubCategory>();
	

	public BookMain() {
		p_west = new JPanel();
		p_content = new JPanel();
		p_north = new JPanel();
		p_center= new JPanel();
		p_table = new TablePanel();
		p_grid = new GridPanel();
		ch_top = new Choice();
		ch_sub = new Choice();
		t_name = new JTextField(10);
		t_price = new JTextField(10);
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
		
		p_content.setLayout(new BorderLayout());

		p_center.setBackground(Color.yellow);
		p_center.add(p_table);
		p_center.add(p_grid);
		
		p_content.add(p_north,BorderLayout.NORTH);
		p_content.add(p_center);
		
		add(p_content);
		
		init();
		ch_top.addItemListener(this);
		can.addMouseListener(new MouseAdapter() {
		
			public void mouseClicked(MouseEvent e) {
				openFile();
			
			}
		});
		
		bt_regist.addActionListener(this);
		
		//초이스 컴포넌트와 리스너 연결
		ch_table.addItemListener(this);
		ch_grid.addItemListener(this);
		
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
			String sql = "select * from topcategory order by topcategory_id asc";
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
		//테이블 패널과 그리드 패널에게 Connection전달(con이 생성된 이후에 전달되어야 하므로)
		//자식의 메서드를 볼수 없으므로 자식으로 형변환...후 전달
		((TablePanel)p_table).setConnection(con);
		((GridPanel)p_grid).setConnection(con);

	}
	
	//하위 카테고리 가져오기
	public void getSub(String v){

		ch_sub.removeAll();		//기존에 이미 채워진 아이템이 있다면... 먼저 싹지운다. 
		StringBuffer sb = new StringBuffer();
		sb.append("select * from subcategory");
		sb.append(" where topcategory_id=(");
		sb.append(" select topcategory_id from topcategory where category_name ='"+v+"' ) order by subcategory_id asc ");
		
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sb.toString());
			rs=pstmt.executeQuery();
			
			//rs에 담겨진 레코드 1개는 SubCategory의 클래스 인스턴스 1개로 받자!!!!
			
			while(rs.next()){
				SubCategory dto = new SubCategory();
				dto.setSubcategory_id(rs.getInt("subcategory_id"));
				dto.setCategory_name(rs.getString("category_name"));
				dto.setTopcategory_id(rs.getInt("topcategory_id"));
				
				subcategory.add(dto); //컬렉션에 담아두기...
				ch_sub.add(dto.getCategory_name());
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
			file = chooser.getSelectedFile();
			img = kit.getImage(file.getAbsolutePath());
			can.repaint();
		}
	}
	
	//상품등록 메서드
	public void regist(){
		//기존에 등록된 추가된 컴포넌트가 있다면 컴포넌트 지우기
		Component[] comp =  p_grid.getComponents();
		System.out.println(comp.length+"포함된 자식의 수는");
		
		
		//내가 지금 선택한 서브 카테고리 초이스의 index를 구해서 그 index로 ArrayList를 접근하여 객체를 반환받으면
		//정보를 유용하게 쓸 수 있다..
		int index = ch_sub.getSelectedIndex();
		SubCategory dto= subcategory.get(index);
		
		//책이름 구해오기
		String book_name = t_name.getText();
		int price = Integer.parseInt(t_price.getText());
		String img = file.getName();
		
		StringBuffer sb = new StringBuffer();
		sb.append("insert into book(book_id,subcategory_id,book_name,book_price,book_img)");
		sb.append(" values(seq_book.nextval,"+dto.getSubcategory_id()+",'"+book_name+"',"+price+",'"+img+"')");
		System.out.println(sb.toString());
		
		PreparedStatement pstmt = null;
		try {
			pstmt=con.prepareStatement(sb.toString());
			int result = pstmt.executeUpdate();//SQL문이 DML일때 executeUpdate사용...(insert, delete,update)
			//위의 메서드는 숫자값을 반환하며 , 이 숫자값은 이 쿼리에 의해 영향을 받은 레코드의 수를 반환한다.
			//insert의 경우 언제나??? 몇이 반환될까? 1!!! 인서트는 무조건 하나의 레코드만 영향 받으므로
			if(result!=0){
				copy();
				((TablePanel)p_table).init(); //조회일으킴
				((TablePanel)p_table).table.updateUI(); //UI갱신
				
				
				for(int i=0;i<comp.length;i++){
					p_grid.remove(0);
				}
				((GridPanel)p_grid).loadData();
		
				

			}else{
				System.out.println(book_name+"실패");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(pstmt!=null){
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	/*이미지 복사하기
	 * 
	 * 유저가 선택한 이미지를 개발자가 지정한 위치로 복사를 해놓자
	 * */
	public void copy(){

		FileInputStream fis=null;
		FileOutputStream fos=null;
		String filename=file.getName();
		String dest = "C:/java_workspace2/DBProject2/data/"+filename;
		
		try {
			fis=new FileInputStream(file);
			fos=new FileOutputStream(dest);
			
			int data;  //읽어들인 데이터X , 읽어들인 갯수가 존재..
			byte[] b = new byte[1024];
			while(true){
				data = fis.read(b);
				if(data==-1)break;
				fos.write(b);
			}
			JOptionPane.showMessageDialog(this, "파일등록성공");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(fos!=null){
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(fis!=null){
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		
	}
	
	public void actionPerformed(ActionEvent e) {
		regist();
		System.out.println("나눌렀어?");
		
	}
	
	public void itemStateChanged(ItemEvent e) {
		Object obj = e.getSource();
		if(obj==ch_top){
			Choice ch =(Choice)e.getSource();
			getSub(ch.getSelectedItem());
		}else if(obj==ch_table){
			p_table.setVisible(true);
			p_grid.setVisible(false);
		}else if(obj==ch_grid){
			p_table.setVisible(false);
			p_grid.setVisible(true);
		}
	}
	
	public static void main(String[] args) {
		new BookMain();		
	}
}

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
	JPanel p_west; //���� �����
	JPanel p_content; //���� ���� ��ü
	JPanel p_north; //���� ���ø��(p_content�� ��ܿ���)
	JPanel p_table; //���� �ϴܸ��(JTable�� �ٿ��� �г� ,�������)
	JPanel p_grid; //�׸��� ������� ������ �г�
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
		bt_regist = new JButton("���");
		group = new CheckboxGroup();
		ch_table= new Checkbox("���̺�", true, group);
		ch_grid = new Checkbox("�׸���",false,group);
		chooser = new JFileChooser("C:/html_workspace/images"); //�������� �ø���
		
		
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
		//���̽� ������Ʈ�� �ֻ��� ��� ���̱�!!
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
	
	//���� ī�װ� ��������
	public void getSub(String v){

		ch_sub.removeAll();		//������ �̹� ä���� �������� �ִٸ�... ���� �������. 
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

	//�׸� ���� �ҷ�����
	public void openFile(){
		int result = chooser.showOpenDialog(this);
		if(result==JFileChooser.APPROVE_OPTION){
			//������ �̹����� ĵ������ �׸�����~
			File file = chooser.getSelectedFile();
			img = kit.getImage(file.getAbsolutePath());
			can.repaint();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		System.out.println("��������?");
		
	}
	
	
	public void itemStateChanged(ItemEvent e) {
		Choice ch =(Choice)e.getSource();
		getSub(ch.getSelectedItem());
	}
	
	public static void main(String[] args) {
		new BookMain();		
	}
}

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
	JPanel p_west; //���� �����
	JPanel p_content; //���� ���� ��ü
	JPanel p_north; //���� ���ø��(p_content�� ��ܿ���)
	JPanel p_center;//���̺���׸��带 ���� �г� (FlowLayout����� ���� // p_table, p_grid�� �����ų �����̳�)
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
	File file;
	
	TablePanel tablePanel;
	

	//chooser�� ���� html�� �ڹٰ� �ٸ��Ƿ� Choice������Ʈ�� ���� �̸� �޾Ƴ���, rs��ü�� ����Ұ��̴�.
	//������??? ���̻� rs.last(), rs.getRow..�̷��� �ʿ������...
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
		
		//���̽� ������Ʈ�� ������ ����
		ch_table.addItemListener(this);
		ch_grid.addItemListener(this);
		
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
		//���̺� �гΰ� �׸��� �гο��� Connection����(con�� ������ ���Ŀ� ���޵Ǿ�� �ϹǷ�)
		//�ڽ��� �޼��带 ���� �����Ƿ� �ڽ����� ����ȯ...�� ����
		((TablePanel)p_table).setConnection(con);
		((GridPanel)p_grid).setConnection(con);

	}
	
	//���� ī�װ� ��������
	public void getSub(String v){

		ch_sub.removeAll();		//������ �̹� ä���� �������� �ִٸ�... ���� �������. 
		StringBuffer sb = new StringBuffer();
		sb.append("select * from subcategory");
		sb.append(" where topcategory_id=(");
		sb.append(" select topcategory_id from topcategory where category_name ='"+v+"' ) order by subcategory_id asc ");
		
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sb.toString());
			rs=pstmt.executeQuery();
			
			//rs�� ����� ���ڵ� 1���� SubCategory�� Ŭ���� �ν��Ͻ� 1���� ����!!!!
			
			while(rs.next()){
				SubCategory dto = new SubCategory();
				dto.setSubcategory_id(rs.getInt("subcategory_id"));
				dto.setCategory_name(rs.getString("category_name"));
				dto.setTopcategory_id(rs.getInt("topcategory_id"));
				
				subcategory.add(dto); //�÷��ǿ� ��Ƶα�...
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

	//�׸� ���� �ҷ�����
	public void openFile(){
		int result = chooser.showOpenDialog(this);
		if(result==JFileChooser.APPROVE_OPTION){
			//������ �̹����� ĵ������ �׸�����~
			file = chooser.getSelectedFile();
			img = kit.getImage(file.getAbsolutePath());
			can.repaint();
		}
	}
	
	//��ǰ��� �޼���
	public void regist(){
		//������ ��ϵ� �߰��� ������Ʈ�� �ִٸ� ������Ʈ �����
		Component[] comp =  p_grid.getComponents();
		System.out.println(comp.length+"���Ե� �ڽ��� ����");
		
		
		//���� ���� ������ ���� ī�װ� ���̽��� index�� ���ؼ� �� index�� ArrayList�� �����Ͽ� ��ü�� ��ȯ������
		//������ �����ϰ� �� �� �ִ�..
		int index = ch_sub.getSelectedIndex();
		SubCategory dto= subcategory.get(index);
		
		//å�̸� ���ؿ���
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
			int result = pstmt.executeUpdate();//SQL���� DML�϶� executeUpdate���...(insert, delete,update)
			//���� �޼���� ���ڰ��� ��ȯ�ϸ� , �� ���ڰ��� �� ������ ���� ������ ���� ���ڵ��� ���� ��ȯ�Ѵ�.
			//insert�� ��� ������??? ���� ��ȯ�ɱ�? 1!!! �μ�Ʈ�� ������ �ϳ��� ���ڵ常 ���� �����Ƿ�
			if(result!=0){
				copy();
				((TablePanel)p_table).init(); //��ȸ����Ŵ
				((TablePanel)p_table).table.updateUI(); //UI����
				
				
				for(int i=0;i<comp.length;i++){
					p_grid.remove(0);
				}
				((GridPanel)p_grid).loadData();
		
				

			}else{
				System.out.println(book_name+"����");
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
	
	/*�̹��� �����ϱ�
	 * 
	 * ������ ������ �̹����� �����ڰ� ������ ��ġ�� ���縦 �س���
	 * */
	public void copy(){

		FileInputStream fis=null;
		FileOutputStream fos=null;
		String filename=file.getName();
		String dest = "C:/java_workspace2/DBProject2/data/"+filename;
		
		try {
			fis=new FileInputStream(file);
			fos=new FileOutputStream(dest);
			
			int data;  //�о���� ������X , �о���� ������ ����..
			byte[] b = new byte[1024];
			while(true){
				data = fis.read(b);
				if(data==-1)break;
				fos.write(b);
			}
			JOptionPane.showMessageDialog(this, "���ϵ�ϼ���");
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
		System.out.println("��������?");
		
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

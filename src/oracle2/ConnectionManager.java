/*
 * 각 TableModel 마다 접속정보와 접속객체를 두게되면
 * 접속정보가 바뀔때 모든 클래스의 코드도 수정해야 하는 유지보수상의 문제 뿐만 아니라,
 * 각 TableModel마다 Connection을 생성하기 떄문에 접속이 여러개 발생한다.
 * 하나의 어플리케이션이 오라클과 맺는 접속은 1개만으로도 충분하다.
 * 그리고 접속이 여러개이면 하나의 세션에서 발생시키는 각종 DML 작업이 통일되지 못한다.
 * 즉... 다른사람으로 인식됨
 *
 * 객체의 인스턴스를 메모리 힙에 1개만 만드는 법. 
 * 
 * */
package oracle2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	static private ConnectionManager instance;
	
	String driver="oracle.jdbc.OracleDriver";
	String url="jdbc:oracle:thin:@localhost:1521:XE";
	String user="batman";
	String password="1234";
	
	
	Connection con;
	
	
	//개발자가 제공하는 방법 이외의 접근은 아예차단하자!! 
	//사용자에 의한 임의 생성을 막자!! 즉,new막자!!
	private ConnectionManager(){
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url,user,password);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	//인스턴스의 생성 없이도, 외부에서 메서드를 호출하여 이 객체의 인스턴스를 가져갈 수 있도록 
	//getter를 제공해주자
	static public ConnectionManager getInstance(){
		if(instance==null){
			instance = new ConnectionManager();
		}
		return instance;
	}
	
	//이 메서드를 호출자는 Connection 객체를 반환받게 된다.
	public Connection getConnection(){
		return con;		
	}
	
	//	커넥션을 다 사용후 닫기..
	public void disConnect(){
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} 
	
	
	
}

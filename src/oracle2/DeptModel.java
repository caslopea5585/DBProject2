//EMP테이블의 데이터를 처리하는 컨트롤러
package oracle2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

public class DeptModel extends AbstractTableModel{

	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	String[] column; //컬럼을 넣을 배열
	String[][] data; //레코드를 넣을 배열
	
	
	public DeptModel(Connection con) {
		/*1단계 드라이버로드
		 *2단계 접속
		 *3단계 쿼리문수행
		 *4단계 접속닫기
		 * */
		this.con=con;
		
		try {
			if(con!=null){
				String sql="select * from emp";
				//pstmt에 의해 생성되는 rs는 커서가 자유로울 수 있다.
				pstmt = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				//결과집합 반환
				rs= pstmt.executeQuery();
				
				//컬럼을 구해보자
				ResultSetMetaData meta = rs.getMetaData();
				int count = meta.getColumnCount();
				column = new String[count];
				//컬럼명을 채우자
				for(int i=0;i<column.length;i++){
					column[i]=meta.getColumnName(i+1); //첫번쨰컬럼을 1이라 생각한다.
				}
				
				rs.last();
				int total = rs.getRow(); //레코드번호
				rs.beforeFirst();
				
				//총 레코드 수를 알았으니 2차원 배열 생성
				data = new String[total][column.length];
				
				//레코드를 2차원 배열인 data에 채워넣기
				for(int i=0; i<data.length;i++){
					rs.next();
					for(int j=0; j<data[i].length;j++){
						data[i][j] = rs.getString(column[j]);
					}
				}
				
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
	
	public int getColumnCount() {
		return column.length;
	}
	
	public String getColumnName(int index) {
		return column[index];
	}
	
	public int getRowCount() {
		return data.length;
	}
	
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}
	
}

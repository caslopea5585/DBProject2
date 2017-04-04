/*
 * 객체 지향 언어인 자바에서는 현실의 사물을 클래스로 정의하지만
 * DataBase에서는 현실의 사물을 Entity라는 객체개념으로 표현한다.
 * 결국! 객체를 표현하는 방법만 다를뿐 개념은 같다
 * 현실반영이라는 개념이라는 틀에서는 둘다 같다.
 * 
 * 따라서 객체지향 언어에서 클래스가 인스턴스를 생성해내는 거푸집이라면....
 * 데이터베이스 분야에서 테이블은 레코드를 저장할 수 있는 틀로 봐도 무방..
 * 이때 하나의 레코드는 결국! 하나의 객체로봐야한다.
 * (클래스에서 new해서 인스턴스를 찍어내듯 데이터베이스에서는 레코드를 테이블에서 찍어내는것과 같은 존재)
 * 
 * 
 * 결론!!) 테이블에 존재하는 레코드 수가 총 5개라면 개발자는 이 각각의 레코드를 5개의 인스턴스로
 * 각각 받으면 된다.
 * 
 * 	아래의 클래스는 로직 작성용이 아니다!!
 * 	즉 한건의 레코드를 담기 위한 저장 공간 용도로만 사용할 클래스이다.
 *	어플리케이션 설계분야에서 이러한 목적의 클래스를 가리켜 VO, DTO라 한다.
 *	Value Object = 값만 담긴 객체
 *	Data Transfer Object = 값을 전달하기 위한 객체
 *
 *	 
 * 	클래스는 로직과 변수로만 존재해야한다는 생각에서 벗어나자~~!!!
 * 단순히 한 자료형을 묶어놓는 용도로도 사용될 수 있따!
 * 그럼? 배열과의 차이점은?
 * 1. 서로 다른 데이터를 담을 수 있고
 * 2. 객체로 처리가 가능해짐.. 작업방식이 훨씩 좋음
 * 
 * */
package book;
//로직이 없는 클래스! Dummy 클래스
//오직 데이터 담는 그릇용! 배열과차이점은?
public class SubCategory {
	private int subcategory_id;
	private int topcategory_id;
	private String category_name;
	
	
	public int getSubcategory_id() {
		return subcategory_id;
	}
	public void setSubcategory_id(int subcategory_id) {
		this.subcategory_id = subcategory_id;
	}
	public int getTopcategory_id() {
		return topcategory_id;
	}
	public void setTopcategory_id(int topcategory_id) {
		this.topcategory_id = topcategory_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
		
}

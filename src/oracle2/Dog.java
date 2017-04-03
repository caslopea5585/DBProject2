/*
 * 강아지 클래스의 인스턴스를 오직1개만 만들기
	SingleTon pattern 개발 패턴 중 하나임..
	(객체의 인스턴스를 오직 1개만 만드는 패턴)
	
 * JavaSE 
 * JavaEE 고급기술 (javaSE를 포함하여 기업용 어플리케이션 제작에사용됨)
 * 
 * 
 * 
//둘다 스태틱으로 만들어버려서 힙영역에 안올라가게하고
//올라가게하는건 내부에 하나만 만들어버리면 된다.
//그럼 널일때만 힙영역에 내부변수로공유해서올릴수있도로갛면된다.
//
*/package oracle2;
public class Dog {
	static private Dog instance;
	
	//new에 의한 생성을 막자!!
	private Dog(){
		
	}
	static public Dog getInstance() {
		if(instance==null){
			instance = new Dog();
		}
		return instance;
	}
}

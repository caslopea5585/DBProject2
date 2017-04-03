/*
 * ������ Ŭ������ �ν��Ͻ��� ����1���� �����
	SingleTon pattern ���� ���� �� �ϳ���..
	(��ü�� �ν��Ͻ��� ���� 1���� ����� ����)
	
 * JavaSE 
 * JavaEE ��ޱ�� (javaSE�� �����Ͽ� ����� ���ø����̼� ���ۿ�����)
 * 
 * 
 * 
//�Ѵ� ����ƽ���� ���������� �������� �ȿö󰡰��ϰ�
//�ö󰡰��ϴ°� ���ο� �ϳ��� ���������� �ȴ�.
//�׷� ���϶��� �������� ���κ����ΰ����ؼ��ø����ֵ��ΰ���ȴ�.
//
*/package oracle2;
public class Dog {
	static private Dog instance;
	
	//new�� ���� ������ ����!!
	private Dog(){
		
	}
	static public Dog getInstance() {
		if(instance==null){
			instance = new Dog();
		}
		return instance;
	}
}

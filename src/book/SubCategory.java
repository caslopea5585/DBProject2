/*
 * ��ü ���� ����� �ڹٿ����� ������ �繰�� Ŭ������ ����������
 * DataBase������ ������ �繰�� Entity��� ��ü�������� ǥ���Ѵ�.
 * �ᱹ! ��ü�� ǥ���ϴ� ����� �ٸ��� ������ ����
 * ���ǹݿ��̶�� �����̶�� Ʋ������ �Ѵ� ����.
 * 
 * ���� ��ü���� ���� Ŭ������ �ν��Ͻ��� �����س��� ��Ǫ���̶��....
 * �����ͺ��̽� �о߿��� ���̺��� ���ڵ带 ������ �� �ִ� Ʋ�� ���� ����..
 * �̶� �ϳ��� ���ڵ�� �ᱹ! �ϳ��� ��ü�κ����Ѵ�.
 * (Ŭ�������� new�ؼ� �ν��Ͻ��� ���� �����ͺ��̽������� ���ڵ带 ���̺��� ���°Ͱ� ���� ����)
 * 
 * 
 * ���!!) ���̺� �����ϴ� ���ڵ� ���� �� 5����� �����ڴ� �� ������ ���ڵ带 5���� �ν��Ͻ���
 * ���� ������ �ȴ�.
 * 
 * 	�Ʒ��� Ŭ������ ���� �ۼ����� �ƴϴ�!!
 * 	�� �Ѱ��� ���ڵ带 ��� ���� ���� ���� �뵵�θ� ����� Ŭ�����̴�.
 *	���ø����̼� ����о߿��� �̷��� ������ Ŭ������ ������ VO, DTO�� �Ѵ�.
 *	Value Object = ���� ��� ��ü
 *	Data Transfer Object = ���� �����ϱ� ���� ��ü
 *
 *	 
 * 	Ŭ������ ������ �����θ� �����ؾ��Ѵٴ� �������� �����~~!!!
 * �ܼ��� �� �ڷ����� ������� �뵵�ε� ���� �� �ֵ�!
 * �׷�? �迭���� ��������?
 * 1. ���� �ٸ� �����͸� ���� �� �ְ�
 * 2. ��ü�� ó���� ��������.. �۾������ �ξ� ����
 * 
 * */
package book;
//������ ���� Ŭ����! Dummy Ŭ����
//���� ������ ��� �׸���! �迭����������?
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

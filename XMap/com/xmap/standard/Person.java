package com.xmap.standard;
//XMap_ʵ����淶:
public class Person {
	
	//�ֶ���������XML�ڵ������/��Ԫ������ͬ
	private int id;
	private String name;
	private String job;
	
	//�����޲ι�����������
	public Person(){}
	//�вι��������п���
	public Person(int id,String name,String job){
		this.id = id;
		this.name = name;
		this.job = job;
	}
	/**************************get������set����������****************************/
	
	public int getId() {
		return id;
	}
	
	//ע��:���ֶ����в���String����������,��set��������������String����,�����������ʹ���
	public void setId(String id) {
		//������set�����ڲ���String���͵Ĳ���ת������������,�ٸ��ֶθ�ֵ
		this.id = Integer.parseInt(id);
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	
	
	
	
	
}

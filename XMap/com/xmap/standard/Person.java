package com.xmap.standard;
//XMap_实体类规范:
public class Person {
	
	//字段名必须与XML节点的属性/子元素名相同
	private int id;
	private String name;
	private String job;
	
	//公共无参构造器必须有
	public Person(){}
	//有参构造器可有可无
	public Person(int id,String name,String job){
		this.id = id;
		this.name = name;
		this.job = job;
	}
	/**************************get方法与set方法必须有****************************/
	
	public int getId() {
		return id;
	}
	
	//注意:若字段中有不是String的类型属性,其set方法参数必须是String类型,否则会造成类型错误
	public void setId(String id) {
		//可以在set方法内部把String类型的参数转换成其他类型,再给字段赋值
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

package com.xmap.standard;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.DocumentException;
import com.xmap.MapperFactory;
import com.xmap.SimpleMapper;


//XMap_使用规范
public class Test {
	//Mapper工厂
	MapperFactory factory = null;
	//也可以用接口Mapper作为类型
	SimpleMapper mapper = null;
	//使用规范:获取Xml文件映射对象
	public void getObject(){
		List<Person> personList = mapper.getAll();//获取所有对象
		Person person = (Person) mapper.get(0);//通过顺序(从0开始)获取一个对象
		System.out.println(person.getName());
	}
	
	public void addObject(){
		Person pa = new Person(4,"Flake","Bus Driver");
		Person pb = new Person(5,"Tom","Actor");
		Person pc = new Person(6,"Taylor","Singer");
		List<Person> addList = new ArrayList();
		addList.add(pa);
		addList.add(pb);
		addList.add(pc);
		//一次性添加多个对象
		mapper.addAll(addList);
		//或者一次只添加一个对象
		mapper.add(new Person(7,"Frank","no job"));
	}
	
	
	public static void main(String[] args) throws FileNotFoundException, DocumentException {
		Test test = new Test();
		//传入映射类与Xml文件路径
		test.factory = new MapperFactory(Person.class,"XMap/com/xmap/standard/Persons.xml");
		//传入要映射的节点名称
		test.mapper = (SimpleMapper) test.factory.getSimpleMapper("person");
		//节点名称可以随时修改
		//test.mapper.setElementName("XXX");
		test.getObject();
		test.addObject();
	}
}

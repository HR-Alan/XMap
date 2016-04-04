package com.xmap.standard;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.DocumentException;
import com.xmap.MapperFactory;
import com.xmap.SimpleMapper;


//XMap_ʹ�ù淶
public class Test {
	//Mapper����
	MapperFactory factory = null;
	//Ҳ�����ýӿ�Mapper��Ϊ����
	SimpleMapper mapper = null;
	//ʹ�ù淶:��ȡXml�ļ�ӳ�����
	public void getObject(){
		List<Person> personList = mapper.getAll();//��ȡ���ж���
		Person person = (Person) mapper.get(0);//ͨ��˳��(��0��ʼ)��ȡһ������
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
		//һ������Ӷ������
		mapper.addAll(addList);
		//����һ��ֻ���һ������
		mapper.add(new Person(7,"Frank","no job"));
	}
	
	
	public static void main(String[] args) throws FileNotFoundException, DocumentException {
		Test test = new Test();
		//����ӳ������Xml�ļ�·��
		test.factory = new MapperFactory(Person.class,"XMap/com/xmap/standard/Persons.xml");
		//����Ҫӳ��Ľڵ�����
		test.mapper = (SimpleMapper) test.factory.getSimpleMapper("person");
		//�ڵ����ƿ�����ʱ�޸�
		//test.mapper.setElementName("XXX");
		test.getObject();
		test.addObject();
	}
}

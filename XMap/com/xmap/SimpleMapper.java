package com.xmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xmap.inter.Mapper;
import com.xmap.parser.MapperClassParser;
import com.xmap.parser.XmlParser;

public class SimpleMapper implements Mapper{

	private MapperClassParser classParser;
	private XmlParser xmlParser;
	//���ڴ������л�ȡ��ӳ�����
	private List mapObjects = null;
	//������HashMap�����������нڵ���������ӽڵ�ֵ
	private HashMap attrMap;
	private HashMap eleMap;
	//�������ƵĽڵ�����
	private int elementNumber;
	protected SimpleMapper(MapperClassParser classParser,XmlParser xmlParser) {
		this.classParser = classParser;
		this.xmlParser = xmlParser;
		this.attrMap = null;
		this.eleMap = null;
	}

	//��ȡ����ӳ��ڵ�
	public List getAll() {
		if (mapObjects == null) {
			assemble();
		}
		return mapObjects;
	}
	
	//ͨ���ڵ�˳���ȡ�ڵ�(��0��ʼ)
	public Object get(int index){
		this.attrMap = xmlParser.getAttributes(index);
		this.eleMap = xmlParser.getChildValues(index);
		//�϶�Ϊһ
		eleMap.putAll(attrMap);
		//ִ��setter����,����һ������
		return classParser.excuteSetter(eleMap);
	}
	//���һ���ڵ�
	public void add(Object o){
		//��ȡvalueMap(�洢��������������ֵ,�Ա���xmlParserд���ļ�)
		Map valueMap = classParser.excuteGetter(o);
		xmlParser.addElement(valueMap);
	}
	//��Ӷ���ڵ�,Ҫ��ӵĽڵ�洢��List
	public void addAll(List addList){
		for(Object o : addList){
			Map valueMap = classParser.excuteGetter(o);
			xmlParser.addElement(valueMap);
		}
	}
	
	//ƥ��,������������з���ֵ,���Ὣ�õ��Ķ���ȫ���Ž���Ա����"mapObjects"��
	//�Ա���getAll������ȡ(�ڵ�һ�ε���getAll������ʱ��Ż����assemble������)
	//����ʱֻ��Ҫ����getAll�Ϳ��Ի�ȡ������,������assemble�ٵ���getAll�������һ��,Ӱ��Ч��
	public void assemble() {
		//���³�ʼ��һ��List
		this.mapObjects = new ArrayList();
		//���»�ȡӳ��ڵ�����
		this.elementNumber = xmlParser.mapElmentNumber();
		this.mapObjects = new ArrayList(elementNumber);
		//��ȡÿһ��ӳ��ڵ�,����map
		for(int f=0;f<elementNumber;f++){
			this.attrMap = xmlParser.getAttributes(f);
			this.eleMap = xmlParser.getChildValues(f);
			eleMap.putAll(attrMap);
			this.mapObjects.add(classParser.excuteSetter(eleMap));
		}
	}
	
	public void close(){
		classParser.close();
		classParser = null ;
		xmlParser.close();
		xmlParser = null;
		mapObjects = null;
		attrMap = null;
		eleMap = null;
	}

	public String getElementName() {
		return xmlParser.getElementName();
	}
	public void setElementName(String elementName) {
		this.xmlParser.setElementName(elementName);;
	}
}

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
	//用于储存所有获取的映射对象
	private List mapObjects = null;
	//这两个HashMap用来储存所有节点的属性与子节点值
	private HashMap attrMap;
	private HashMap eleMap;
	//符合名称的节点数量
	private int elementNumber;
	protected SimpleMapper(MapperClassParser classParser,XmlParser xmlParser) {
		this.classParser = classParser;
		this.xmlParser = xmlParser;
		this.attrMap = null;
		this.eleMap = null;
	}

	//获取所有映射节点
	public List getAll() {
		if (mapObjects == null) {
			assemble();
		}
		return mapObjects;
	}
	
	//通过节点顺序获取节点(从0开始)
	public Object get(int index){
		this.attrMap = xmlParser.getAttributes(index);
		this.eleMap = xmlParser.getChildValues(index);
		//合二为一
		eleMap.putAll(attrMap);
		//执行setter方法,返回一个对象
		return classParser.excuteSetter(eleMap);
	}
	//添加一个节点
	public void add(Object o){
		//获取valueMap(存储了属性名与属性值,以便于xmlParser写入文件)
		Map valueMap = classParser.excuteGetter(o);
		xmlParser.addElement(valueMap);
	}
	//添加多个节点,要添加的节点存储在List
	public void addAll(List addList){
		for(Object o : addList){
			Map valueMap = classParser.excuteGetter(o);
			xmlParser.addElement(valueMap);
		}
	}
	
	//匹配,这个方法不会有返回值,它会将得到的对象全部放进成员属性"mapObjects"中
	//以便于getAll方法获取(在第一次调用getAll方法的时候才会调用assemble来解析)
	//调用时只需要调用getAll就可以获取对象了,若调用assemble再调用getAll会解析多一次,影响效率
	public void assemble() {
		//重新初始化一个List
		this.mapObjects = new ArrayList();
		//重新获取映射节点数量
		this.elementNumber = xmlParser.mapElmentNumber();
		this.mapObjects = new ArrayList(elementNumber);
		//获取每一个映射节点,放入map
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

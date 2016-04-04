package com.xmap.parser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
public class XmlParser {
	private String path;//XML文件路径
	private SAXReader saxReader;
	private Document document;
	//存储根节点
	private Element root;
	//智能识别实体类的那些字段要映射为属性,那么其他就映射为节点
	private Map attrbuteMap = null;
	//被映射 的节点名
	private String elementName;
	public XmlParser(String elementName,String xmlPath) throws FileNotFoundException, DocumentException{
		this(new File(xmlPath));
		this.path = xmlPath;
		this.elementName = elementName;
//		//获取属性(这里的目的是缓存属性列表,参见getAttributes方法倒数第三行)
		this.getAttributes(0);
	}
	
	private XmlParser(File xmlFile) throws FileNotFoundException, DocumentException{
		this(new FileInputStream(xmlFile));
	}
	
	private XmlParser(InputStream in) throws DocumentException{
		saxReader = new SAXReader();
		document = saxReader.read(in);	
		root = document.getRootElement();

	}
	
	
	
	
	
	//获取符合名称的节点数量
	public int mapElmentNumber (){
		int number = 0;
		Iterator<Element> allElements = root.elementIterator(this.elementName);
		while(allElements.hasNext()){
			allElements.next();
			number++;
		}
		return number;
	}
	
	
	//通过节点索引(从0开始),获取某个节点所有属性
	public HashMap getAttributes(int number){
		HashMap map = new HashMap();
		Iterator<Element> allElements = root.elementIterator(this.elementName);
		int j = 0;
		while(allElements.hasNext()){
			Element mapElement = allElements.next();//获取节点
			if(j==number){
				List<Attribute> attrList = mapElement.attributes();
				for(Attribute attr : attrList){
					//属性名作为键,属性值作为值
					map.put(attr.getName(),attr.getValue());
					}
				}
			j++;
			}
		//缓存属性列表
		if(this.attrbuteMap == null){
			this.attrbuteMap = map;
		}
		return map;
		}
	
	
	
	//通过节点索引(从0开始),获取某个节点所有子节点的值
	public HashMap getChildValues(int number){
		HashMap map = new HashMap();
		Iterator<Element> allElements = root.elementIterator(this.elementName);
		int j = 0;
		while(allElements.hasNext()){
			Element mapElement = allElements.next();//获取节点
			if(j==number){
				Iterator<Element> childIt =mapElement.elementIterator();//子节点
				while(childIt.hasNext()){
					Element child = childIt.next();
					//System.out.println(child.getName());
					map.put(child.getName(),child.getStringValue());
					}
				}
			j++;
			}
		return map;
		}
	
	
	//通过一个valueMap,先获取Map里面的值,再把这些值映射到XML文件
	public void addElement(Map valueMap){
		Iterator valueIter = valueMap.entrySet().iterator();
		//新建一个节点
		Element addElement = root.addElement(this.elementName);
		while(valueIter.hasNext()){
			Map.Entry entry = (Map.Entry) valueIter.next(); 
			String key = (String) entry.getKey(); 
			String value = entry.getValue()+"";
			//如果属性列表中能找到这个key对应的值,那么代表这个值要被添加为属性
			if(this.attrbuteMap.get(key) != null){
				addElement.addAttribute(key,value);
			}else{//否则以节点的形式添加到XML
				Element childElement = addElement.addElement(key);
				childElement.setText(value);
			}
		}
		
		//格式化输出,这里设置了换行与缩进
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		XMLWriter writer = null;
		try {
			writer = new XMLWriter(new FileOutputStream(this.path),format);
			writer.setEscapeText(false);//遇到特殊字符时不转义
			writer.write(document);//写入DOM
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	//清空成员属性
	public void close(){
		saxReader = null;
		document = null;
		root = null;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
}

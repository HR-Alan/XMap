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
	private String path;//XML�ļ�·��
	private SAXReader saxReader;
	private Document document;
	//�洢���ڵ�
	private Element root;
	//����ʶ��ʵ�������Щ�ֶ�Ҫӳ��Ϊ����,��ô������ӳ��Ϊ�ڵ�
	private Map attrbuteMap = null;
	//��ӳ�� �Ľڵ���
	private String elementName;
	public XmlParser(String elementName,String xmlPath) throws FileNotFoundException, DocumentException{
		this(new File(xmlPath));
		this.path = xmlPath;
		this.elementName = elementName;
//		//��ȡ����(�����Ŀ���ǻ��������б�,�μ�getAttributes��������������)
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
	
	
	
	
	
	//��ȡ�������ƵĽڵ�����
	public int mapElmentNumber (){
		int number = 0;
		Iterator<Element> allElements = root.elementIterator(this.elementName);
		while(allElements.hasNext()){
			allElements.next();
			number++;
		}
		return number;
	}
	
	
	//ͨ���ڵ�����(��0��ʼ),��ȡĳ���ڵ���������
	public HashMap getAttributes(int number){
		HashMap map = new HashMap();
		Iterator<Element> allElements = root.elementIterator(this.elementName);
		int j = 0;
		while(allElements.hasNext()){
			Element mapElement = allElements.next();//��ȡ�ڵ�
			if(j==number){
				List<Attribute> attrList = mapElement.attributes();
				for(Attribute attr : attrList){
					//��������Ϊ��,����ֵ��Ϊֵ
					map.put(attr.getName(),attr.getValue());
					}
				}
			j++;
			}
		//���������б�
		if(this.attrbuteMap == null){
			this.attrbuteMap = map;
		}
		return map;
		}
	
	
	
	//ͨ���ڵ�����(��0��ʼ),��ȡĳ���ڵ������ӽڵ��ֵ
	public HashMap getChildValues(int number){
		HashMap map = new HashMap();
		Iterator<Element> allElements = root.elementIterator(this.elementName);
		int j = 0;
		while(allElements.hasNext()){
			Element mapElement = allElements.next();//��ȡ�ڵ�
			if(j==number){
				Iterator<Element> childIt =mapElement.elementIterator();//�ӽڵ�
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
	
	
	//ͨ��һ��valueMap,�Ȼ�ȡMap�����ֵ,�ٰ���Щֵӳ�䵽XML�ļ�
	public void addElement(Map valueMap){
		Iterator valueIter = valueMap.entrySet().iterator();
		//�½�һ���ڵ�
		Element addElement = root.addElement(this.elementName);
		while(valueIter.hasNext()){
			Map.Entry entry = (Map.Entry) valueIter.next(); 
			String key = (String) entry.getKey(); 
			String value = entry.getValue()+"";
			//��������б������ҵ����key��Ӧ��ֵ,��ô�������ֵҪ�����Ϊ����
			if(this.attrbuteMap.get(key) != null){
				addElement.addAttribute(key,value);
			}else{//�����Խڵ����ʽ��ӵ�XML
				Element childElement = addElement.addElement(key);
				childElement.setText(value);
			}
		}
		
		//��ʽ�����,���������˻���������
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		XMLWriter writer = null;
		try {
			writer = new XMLWriter(new FileOutputStream(this.path),format);
			writer.setEscapeText(false);//���������ַ�ʱ��ת��
			writer.write(document);//д��DOM
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
	
	
	//��ճ�Ա����
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

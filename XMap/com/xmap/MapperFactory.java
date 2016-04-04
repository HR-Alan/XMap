package com.xmap;

import java.io.FileNotFoundException;
import org.dom4j.DocumentException;
import com.xmap.inter.Mapper;
import com.xmap.parser.MapperClassParser;
import com.xmap.parser.XmlParser;

public class MapperFactory {
	private Class mapClass;
	private String source;
	
	
	public MapperFactory(Class mapClass,String source){
		this.mapClass = mapClass;
		this.source = source;
	}
	
	public Mapper getSimpleMapper(String elementName) throws FileNotFoundException, DocumentException{
		XmlParser xmlParser = new XmlParser(elementName,source);
		MapperClassParser classParser = new MapperClassParser(mapClass);
		return new SimpleMapper(classParser,xmlParser);
	}
}

package com.xmap.parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MapperClassParser {
	//存储映射实体类的Class对象
	private Class mapClass;
	//存储实体类所有方法和字段
	private List<Method> methodList;
	private List<Field> fieldList;

	public MapperClassParser(Class mapClass) {
		this.mapClass = mapClass;
		doParse();
		newMapInstance();
	}
	
	//获取映射类的信息
	private void doParse(){
		methodList = Arrays.asList(mapClass.getDeclaredMethods());
		fieldList = Arrays.asList(mapClass.getDeclaredFields());
		//方法名称转换
		for(int i=0;i<fieldList.size();i++){
			String current = fieldList.get(i).getName();
			String setter ="set"+ current.substring(0,1).toUpperCase()+current.substring(1);
			String getter ="get"+ current.substring(0,1).toUpperCase()+current.substring(1);
		}
	}
	
	
	
	//新建一个对象
	private Object newMapInstance() {
		Constructor construct;
		Object mapInstance = null;
		try {
			construct = mapClass.getConstructor();
			mapInstance = construct.newInstance();
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}finally{
			//mapInstance = null;
		}
		return mapInstance;
	}
	
	//根据字段名,得到一个setter方法
	public Method getSetter(String fieldName){
		Field f = fieldExist(fieldName);
		if(f !=null){
			String name = f.getName();
			String setterName ="set"+ name.substring(0,1).toUpperCase()+name.substring(1);
			Method setter = methodExist(setterName);
			return setter;
		}
		return null;
	}
	
	//根据字段名,得到这个字段的getter方法
	public Method getGetter(String fieldName){
		Field f = fieldExist(fieldName);
		if(f !=null){
			String name = f.getName();
			String getterName ="get"+ name.substring(0,1).toUpperCase()+name.substring(1);
			Method getter = methodExist(getterName);
			return getter;
		}
		return null;
	}
	
	
	//在方法列表查找是否存在某个字段,存在则返回这个field对象
	public Field fieldExist(String fieldName){
		//使用字段名列表来查找某个字段,可以避免因找不到字段而抛出异常
		for(Field field : fieldList){
			if(field.getName().equals(fieldName)){
				return field;
			}
		}
		return null;
	}
	
	//在方法列表查找是否存在某个方法,存在则返回这个method对象
	public Method methodExist(String methodName){
		for(Method method : methodList){
			if(method.getName().equals(methodName)){
				return method;
			}
		}
		return null;
	}

	
	
	
	//执行Setter方法
	public Object excuteSetter(HashMap argsMap) {
		//新建一个对象
		Object mapInstance = this.newMapInstance();
		Method setter;
		try {//遍历map
			Iterator iter = argsMap.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry entry = (Map.Entry) iter.next(); 
				String key = entry.getKey()+""; 
				Object value = entry.getValue();
				for(Field field : fieldList){
					if(this.fieldExist(key) != null){
						setter = getSetter(key);
						setter.invoke(mapInstance,value);	
					}
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}catch (IllegalAccessException e) {
			e.printStackTrace();
		}catch (InvocationTargetException e) {
			e.printStackTrace();
		}finally{
			setter = null;
		}
			return mapInstance;
	}
	
	//执行Getter,返回一个保存了对象属性的Map
	public HashMap excuteGetter(Object o){
		HashMap valueMap = new HashMap();
		Method getter = null;
		try {
			for(Field f : fieldList){
				//找到getter方法
				getter = this.getGetter(f.getName());
				Object arg = getter.invoke(o,null);
				valueMap.put(f.getName(),arg);
			}
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}finally{
			getter = null;
		}
		return valueMap;
	}
	
	public void close(){
		mapClass = null;
		methodList = null;
		fieldList = null;
	}
	
}

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
	//�洢ӳ��ʵ�����Class����
	private Class mapClass;
	//�洢ʵ�������з������ֶ�
	private List<Method> methodList;
	private List<Field> fieldList;

	public MapperClassParser(Class mapClass) {
		this.mapClass = mapClass;
		doParse();
		newMapInstance();
	}
	
	//��ȡӳ�������Ϣ
	private void doParse(){
		methodList = Arrays.asList(mapClass.getDeclaredMethods());
		fieldList = Arrays.asList(mapClass.getDeclaredFields());
		//��������ת��
		for(int i=0;i<fieldList.size();i++){
			String current = fieldList.get(i).getName();
			String setter ="set"+ current.substring(0,1).toUpperCase()+current.substring(1);
			String getter ="get"+ current.substring(0,1).toUpperCase()+current.substring(1);
		}
	}
	
	
	
	//�½�һ������
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
	
	//�����ֶ���,�õ�һ��setter����
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
	
	//�����ֶ���,�õ�����ֶε�getter����
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
	
	
	//�ڷ����б�����Ƿ����ĳ���ֶ�,�����򷵻����field����
	public Field fieldExist(String fieldName){
		//ʹ���ֶ����б�������ĳ���ֶ�,���Ա������Ҳ����ֶζ��׳��쳣
		for(Field field : fieldList){
			if(field.getName().equals(fieldName)){
				return field;
			}
		}
		return null;
	}
	
	//�ڷ����б�����Ƿ����ĳ������,�����򷵻����method����
	public Method methodExist(String methodName){
		for(Method method : methodList){
			if(method.getName().equals(methodName)){
				return method;
			}
		}
		return null;
	}

	
	
	
	//ִ��Setter����
	public Object excuteSetter(HashMap argsMap) {
		//�½�һ������
		Object mapInstance = this.newMapInstance();
		Method setter;
		try {//����map
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
	
	//ִ��Getter,����һ�������˶������Ե�Map
	public HashMap excuteGetter(Object o){
		HashMap valueMap = new HashMap();
		Method getter = null;
		try {
			for(Field f : fieldList){
				//�ҵ�getter����
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

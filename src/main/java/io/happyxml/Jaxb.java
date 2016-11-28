package io.happyxml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import io.happyxml.annotation.XmlInit;

/**
 * XML与Java对象绑定
 */
public class Jaxb {
	
	public static <T extends Object> T parseFromString(Class<T> clazz, String xmlStr){
		return parse(clazz, new StringReader(xmlStr));
	}
	
	public static <T extends Object> T parse(Class<T> clazz, final String xmlFile){
		File f = new File(xmlFile);
		if(!f.exists()){
			return null;
		}
		
		T obj = null;
		try {
			obj = parse(clazz, new FileInputStream(f));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return obj;
	}
	
	public static <T extends Object> T parse(Class<T> clazz, InputStream is){
		return parse(clazz, is, "UTF-8");
	}
	public static <T extends Object> T parse(Class<T> clazz, InputStream is, final String encoding){
		T obj = null;
		try {
			obj = parse(clazz, new InputStreamReader(is, encoding));
			is.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return obj;
	}
	private static void execInit(Class<?> clazz, Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method[] methods = clazz.getMethods();
		if(methods != null){
			for(Method method : methods){
				if(method.getAnnotation(XmlInit.class) != null){
					method.invoke(obj);
					return ;
				}
			}
		}
	}
	public static <T extends Object> T parse(Class<T> clazz, Reader reader){
		T obj = null;
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			Unmarshaller um = context.createUnmarshaller();
			obj = clazz.cast(um.unmarshal(reader));
			reader.close();
			execInit(clazz, obj);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return obj;
	}
	
	public static void write(Object obj , final String xmlFile){
		File f = new File(xmlFile);
		try {
			if(!f.exists()){
				f.createNewFile();
			}
			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8"); //指定已编组 XML 数据中的输出编码,默认为UTF-8
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); //使用换行和缩排对已编组 XML 数据进行格式化,默认为false
			FileWriter fw = new FileWriter(f);
			m.marshal(obj, fw);
			fw.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public static void main(String args[]){
		write(new String("111"), "e:/test/jaxb.xml");
	}

}



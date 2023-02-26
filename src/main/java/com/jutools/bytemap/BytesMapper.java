package com.jutools.bytemap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.PriorityQueue;

import lombok.Data;

public class BytesMapper<T> {
	
	private Class<T> mappingClass;
	private ArrayList<MapInfo> maps = new ArrayList<MapInfo>();
	private long totalSize = 0;
	
	/**
	 * 
	 * 
	 * @param mappingClass
	 */
	public BytesMapper(Class<T> mappingClass) throws Exception {
		
		//
		if(mappingClass == null) {
			throw new NullPointerException("");
		}
		
		this.mappingClass = mappingClass;
		
		//
		PriorityQueue<MapInfo> sortedMapQueue = new PriorityQueue<MapInfo>();
		
		Field[] fields = mappingClass.getFields();
		for(Field field: fields) {
			
			MapInfo mapInfo = new MapInfo(mappingClass);
			
			//
			Map map = field.getAnnotation(Map.class);
			if(map == null) {
				continue;
			}
			
			//
			mapInfo.setFieldSetter(field);
			mapInfo.setOrder(map.order());
			mapInfo.setSize(map.size());
			mapInfo.setSkip(map.skip());
			mapInfo.setMethod(map.method());
			
			//
			this.totalSize += mapInfo.getSkip();
			this.totalSize += mapInfo.getSize();
			
			//
			sortedMapQueue.add(mapInfo);
			
		}
		
		//
		sortedMapQueue.forEach(map -> {
			this.maps.add(map);
		});
	}
	
	/**
	 * 
	 * 
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public T mapping(byte[] bytes) throws Exception {
		
		//
		if(bytes == null) {
			throw new NullPointerException("");
		}
		
		//
		T obj = this.mappingClass.getConstructor().newInstance();
		
		int loc = 0;
		for(MapInfo mapInfo: this.maps) {
			
			loc += mapInfo.getSkip();
			
			byte[] mappedBytes = null;
			if(mapInfo.getSize() > 0) {
				
				mappedBytes = new byte[mapInfo.getSize()];
				System.arraycopy(bytes, loc, mappedBytes, 0, mappedBytes.length);
				
				if(mapInfo.getMethod() != null) {
					Object result = mapInfo.getMethod().invoke(obj, mappedBytes);
					mapInfo.getFieldSetter().invoke(obj, result);
				}
				
			} else {
				
				Class<?> fieldType = mapInfo.getField().getType();
				
				if(fieldType == int.class || fieldType == Integer.class) {
					mappedBytes = new byte[4];
					System.arraycopy(bytes, loc, mappedBytes, 0, mappedBytes.length);
					Integer value = ByteBuffer.wrap(bytes).getInt();
					mapInfo.getFieldSetter().invoke(obj, value);
				}
			}
			
			loc += mappedBytes.length;
		}
		
		//
		return obj;
	}
	
	/**
	 * 
	 * 
	 * @param bytes
	 * @param mappingClass
	 * @return
	 */
	public static <T> T mapping(byte[] bytes, Class<T> mappingClass) throws Exception {
		return new BytesMapper<T>(mappingClass).mapping(bytes);
	}

}

@Data
class MapInfo implements Comparable<MapInfo>{

	private Class<?> mappingClass;
	private Field field;
	private Method fieldSetter;
	private int order;
	private int size;
	private int skip;
	private Method method;
	
	/**
	 * 
	 * 
	 * @param mappingClass
	 */
	MapInfo(Class<?> mappingClass) throws Exception {
		
		if(mappingClass == null) {
			throw new NullPointerException("");
		}
		
		this.mappingClass = mappingClass;
	}
	
	/**
	 * 
	 */
	public int compareTo(MapInfo o) {
		return this.getOrder() - o.getOrder();
	}
	
	/**
	 * 
	 * @param fieldName
	 */
	public void setFieldSetter(Field field) throws Exception {
		
		if(field == null) {
			throw new NullPointerException("");
		}
		
		this.field = field;
		String fieldName = this.field.getName();
		
		StringBuffer setterBuffer = new StringBuffer("set");
		setterBuffer.append(Character.toUpperCase(fieldName.charAt(0)))
			.append(fieldName.substring(1));

		this.fieldSetter = this.mappingClass.getMethod(setterBuffer.toString(), this.field.getType());
	}

	/**
	 * 
	 * 
	 * @param methodName
	 */
	public void setMethod(String methodName) throws Exception {
		
		if(methodName == null || methodName.trim().equals("") == true) {
			
			this.method = null;
			
		} else {
			
		    this.method = mappingClass.getMethod(methodName, byte[].class);
		    if(this.method == null) {
		    	throw new NullPointerException("");
		    }
		}
	}
}

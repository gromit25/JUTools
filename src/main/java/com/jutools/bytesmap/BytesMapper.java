package com.jutools.bytesmap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.PriorityQueue;

import com.jutools.TypeUtil;

import lombok.Data;

/**
 * Under Construction
 * 
 * @author jmsohn
 * @param <T>
 */
public class BytesMapper<T> {
	
	/** 바이트 맵핑을 수행할 클래스 */
	private Class<T> mappingClass;
	/** 클래스에 선언된 맵의 정보 목록 */
	private ArrayList<MapInfo> maps = new ArrayList<MapInfo>();
	/** 클래스의 맵 정보에 선언된 바이트의 전체 크기 */
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
			
			MapInfo mapInfo = new MapInfo(field);
			
			//
			BytesMap map = field.getAnnotation(BytesMap.class);
			if(map == null) {
				continue;
			}
			
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
			loc += mapInfo.setValue(obj, bytes, loc);
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

	/** */
	private Field field;
	/** */
	private boolean primitive;
	/** */
	private int order;
	/** */
	private int size;
	/** */
	private int skip;
	/** */
	private Method method;
	
	/**
	 * 
	 * 
	 * @param mappingClass
	 */
	MapInfo(Field field) throws Exception {
		
		// 입력값 검증
		if(field == null) {
			throw new NullPointerException("field is null.");
		}
		
		// BytesMap 어노테이션 
		BytesMap map = field.getAnnotation(BytesMap.class);
		if(map == null) {
			throw new IllegalArgumentException("BytesMap is not found:" + field.getName());
		}
		
		//
		this.field = field;
		
		this.setOrder(map.order());
		this.setSkip(map.skip());
		
		//
		Class<?> fieldType = this.field.getDeclaringClass();
		
		if(TypeUtil.isPrimitive(fieldType) == true) {
			
			this.setSize(TypeUtil.getPrimitiveSize(fieldType));
			this.setPrimitive(true);
			
		} else {
			
			if(map.size() < 1) {
				throw new Exception("size in BytesMap must be greater than 0:" + map.size());
			}
			
			this.setSize(map.size());
			this.setMethod(map.method());
		}
	}

	
	/**
	 * 
	 * @param obj
	 * @param bytes
	 * @param startLoc
	 * @return
	 */
	int setValue(Object obj, byte[] bytes, int startLoc) throws Exception {
		
		byte[] mappedBytes = null;
		
		if(this.isPrimitive() == false) {
			
			mappedBytes = new byte[this.getSize()];
			System.arraycopy(bytes, startLoc, mappedBytes, 0, mappedBytes.length);
			
			if(this.getMethod() != null) {
				Object result = this.getMethod().invoke(obj, mappedBytes);
				this.setField(obj, result);
			} else {
				throw new Exception("set method is not found.");
			}
			
		} else {
			
			Class<?> fieldType = this.getField().getType();
			
			if(fieldType == byte.class || fieldType == Byte.class) {
				
				mappedBytes = new byte[1];
				System.arraycopy(bytes, startLoc, mappedBytes, 0, mappedBytes.length);
				Byte value = mappedBytes[0];
				this.setField(obj, value);
				
			} else if(fieldType == short.class || fieldType == Short.class) {
				
				mappedBytes = new byte[2];
				System.arraycopy(bytes, startLoc, mappedBytes, 0, mappedBytes.length);
				Short value = ByteBuffer.wrap(bytes).getShort();
				this.setField(obj, value);
				
			} else if(fieldType == int.class || fieldType == Integer.class) {
				
				mappedBytes = new byte[4];
				System.arraycopy(bytes, startLoc, mappedBytes, 0, mappedBytes.length);
				Integer value = ByteBuffer.wrap(bytes).getInt();
				this.setField(obj, value);
				
			} else if(fieldType == long.class || fieldType == Long.class) {
				
				mappedBytes = new byte[8];
				System.arraycopy(bytes, startLoc, mappedBytes, 0, mappedBytes.length);
				Long value = ByteBuffer.wrap(bytes).getLong();
				this.setField(obj, value);
				
			} else if(fieldType == float.class || fieldType == Float.class) {
				
				mappedBytes = new byte[4];
				System.arraycopy(bytes, startLoc, mappedBytes, 0, mappedBytes.length);
				Float value = ByteBuffer.wrap(bytes).getFloat();
				this.setField(obj, value);

			} else if(fieldType == double.class || fieldType == Double.class) {
				
				mappedBytes = new byte[8];
				System.arraycopy(bytes, startLoc, mappedBytes, 0, mappedBytes.length);
				Double value = ByteBuffer.wrap(bytes).getDouble();
				this.setField(obj, value);

			} else {
				
				throw new Exception("type is not primitive type:" + fieldType.toString());
			}
		}
		
		return mappedBytes.length; 
	}
	
	/**
	 * 
	 * @param value
	 */
	void setField(Object obj, Object value) throws Exception {
		
		if(this.method == null) {
			TypeUtil.setField(obj, this.field.getName(), value);
		} else {
			this.method.invoke(obj, value);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(MapInfo o) {
		return this.getOrder() - o.getOrder();
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
			
			String[] classAndMethod = methodName.split("\\.");
			if(classAndMethod.length != 2) {
				throw new IllegalArgumentException("");
			}
			
			Class<?> clazz = Class.forName(classAndMethod[0]);
		    this.method = clazz.getMethod(classAndMethod[1], byte[].class);
		}
	}
	
}

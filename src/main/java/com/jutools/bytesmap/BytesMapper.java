package com.jutools.bytesmap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.PriorityQueue;

import com.jutools.BytesUtil;
import com.jutools.TypeUtil;
import com.jutools.common.OrderType;

import lombok.Data;

/**
 * Under Construction
 * 
 * @author jmsohn
 */
public class BytesMapper {
	
	/** 바이트 맵핑을 수행할 클래스 */
	private Class<?> mappingClass;
	
	/** 클래스에 선언된 맵의 정보 목록 */
	private ArrayList<MapInfo> maps = new ArrayList<MapInfo>();
	
	/** 클래스의 맵 정보에 선언된 바이트의 전체 크기 */
	private long totalSize = 0;
	
	/**
	 * 
	 * 
	 * @param mappingClass
	 */
	private BytesMapper(Class<?> mappingClass) throws Exception {
		
		//
		if(mappingClass == null) {
			throw new NullPointerException("");
		}
		
		this.mappingClass = mappingClass;
		
		//
		PriorityQueue<MapInfo> sortedMapQueue = new PriorityQueue<MapInfo>();
		
		Field[] fields = mappingClass.getDeclaredFields();
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
        while (sortedMapQueue.isEmpty() == false) {
            this.maps.add(sortedMapQueue.poll());
        }
	}
	
	/**
	 * 
	 * 
	 * @param bytes
	 * @return
	 */
	private Object mapping(byte[] bytes) throws Exception {
		
		//
		if(bytes == null) {
			throw new NullPointerException("");
		}
		
		if(this.totalSize > bytes.length) {
			throw new Exception("");
		}
		
		//
		Object obj = this.mappingClass.getConstructor().newInstance();
		
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
		Object obj = new BytesMapper(mappingClass).mapping(bytes);
		return mappingClass.cast(obj);
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
	private boolean sizeSet;
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
		Class<?> fieldType = this.field.getType();
		this.setPrimitive(TypeUtil.isPrimitive(fieldType));
		
		if(map.size() < 0) {
			
			if(this.isPrimitive() == true) {
				
				this.setSize(TypeUtil.getPrimitiveSize(fieldType));
				this.setSizeSet(false);
				
			} else {
				throw new Exception("size must be set:" + fieldType.getName());
			}
			
		} else {
			
			if(map.size() < 1) {
				throw new Exception("size in BytesMap must be greater than 0:" + map.size());
			}
			
			this.setSize(map.size());
			this.setSizeSet(true);
			
			this.setMethod(map.method());
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
	 * @param obj
	 * @param bytes
	 * @param startLoc
	 * @return
	 */
	int setValue(Object obj, byte[] bytes, int startLoc) throws Exception {
		
		byte[] mappedBytes = new byte[this.getSize()];
		Class<?> fieldType = this.getField().getType();
		System.arraycopy(bytes, startLoc, mappedBytes, 0, mappedBytes.length);
		
		if(this.isPrimitive() == true) {
			
			if(this.isSizeSet() == true) {
				
				//
				String valueStr = new String(mappedBytes); 
				
				//
				if(fieldType == boolean.class || fieldType == Boolean.class) {
					
					this.setFieldValue(obj, Boolean.parseBoolean(valueStr));
					
				} else if(fieldType == byte.class || fieldType == Byte.class) {
					
					this.setFieldValue(obj, BytesUtil.strToBytes(valueStr, OrderType.ASCEND)[0]);
					
				} else if(fieldType == char.class || fieldType == Character.class) {
					
					this.setFieldValue(obj, valueStr.charAt(0));
					
				} else if(fieldType == short.class || fieldType == Short.class) {
					
					this.setFieldValue(obj, Short.parseShort(valueStr));
					
				} else if(fieldType == int.class || fieldType == Integer.class) {
					
					this.setFieldValue(obj, Integer.parseInt(valueStr));
					
				} else if(fieldType == long.class || fieldType == Long.class) {
					
					this.setFieldValue(obj, Long.parseLong(valueStr));
					
				} else if(fieldType == float.class || fieldType == Float.class) {
					
					this.setFieldValue(obj, Float.parseFloat(valueStr));

				} else if(fieldType == double.class || fieldType == Double.class) {
					
					this.setFieldValue(obj, Double.parseDouble(valueStr));

				} else {
					
					throw new Exception("type is not primitive type:" + fieldType.toString());
				}
				
			} else {
				
				//
				if(fieldType == boolean.class || fieldType == Boolean.class) {
					
					if(mappedBytes[0] == 0) {
						this.setFieldValue(obj, false);
					} else {
						this.setFieldValue(obj, true);
					}
					
				} else if(fieldType == byte.class || fieldType == Byte.class) {
					
					Byte value = mappedBytes[0];
					this.setFieldValue(obj, value);
					
				} else if(fieldType == char.class || fieldType == Character.class) {
					
					Character value = ByteBuffer.wrap(mappedBytes).getChar();
					this.setFieldValue(obj, value);
					
				} else if(fieldType == short.class || fieldType == Short.class) {
					
					Short value = ByteBuffer.wrap(mappedBytes).getShort();
					this.setFieldValue(obj, value);
					
				} else if(fieldType == int.class || fieldType == Integer.class) {
					
					Integer value = ByteBuffer.wrap(mappedBytes).getInt();
					this.setFieldValue(obj, value);
					
				} else if(fieldType == long.class || fieldType == Long.class) {
					
					Long value = ByteBuffer.wrap(mappedBytes).getLong();
					this.setFieldValue(obj, value);
					
				} else if(fieldType == float.class || fieldType == Float.class) {
					
					Float value = ByteBuffer.wrap(mappedBytes).getFloat();
					this.setFieldValue(obj, value);

				} else if(fieldType == double.class || fieldType == Double.class) {
					
					Double value = ByteBuffer.wrap(mappedBytes).getDouble();
					this.setFieldValue(obj, value);

				} else {
					
					throw new Exception("type is not primitive type:" + fieldType.toString());
				}
				
			}
			
		} else {
			
			System.arraycopy(bytes, startLoc, mappedBytes, 0, mappedBytes.length);
			
			if(this.getMethod() != null) {
				Object result = this.getMethod().invoke(obj, mappedBytes);
				this.setFieldValue(obj, result);
			} else {
				throw new Exception("set method is not found.");
			}
			
		}
		
		return mappedBytes.length; 
	}
	
	/**
	 * 
	 * @param value
	 */
	private void setFieldValue(Object obj, Object value) throws Exception {
		
		if(this.method == null) {
			TypeUtil.setField(obj, this.field.getName(), value);
		} else {
			this.method.invoke(obj, value);
		}
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

package com.jutools.env;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 
 * 
 * @author jmsohn
 */
public class EnvMapper {
	
	/**
	 * 
	 * 
	 * @param mapClass
	 */
	public static void set(Class<?> mapClass) throws Exception {
		
		if(mapClass == null) {
			throw new NullPointerException("map class is null");
		}
		
		for(Field field : mapClass.getFields()) {
			
			Env envInfo = field.getAnnotation(Env.class);
			if(envInfo == null) {
				continue;
			}
			
			if(Modifier.isStatic(field.getModifiers()) == false
				|| Modifier.isPublic(field.getModifiers()) == false) {
				throw new Exception(field.getName() + " is not static or public");
			}
			
			String value = System.getenv(envInfo.name());
			if(value == null) {
				if(envInfo.mandatory() == true) {
					throw new Exception(envInfo.name() + " is not set");
				} else {
					continue;
				}
			}
			
			//
			if(envInfo.separator().isEmpty() == true) {

			} else {
				String[] splitedValueList = value.split(envInfo.separator());
				for(String splitValue: splitedValueList) {
					
				}
			}
			
			
		} // End of for
	}

	/**
	 * 
	 * 
	 * @param mapClass
	 * @param field
	 * @param methodName
	 * @param value
	 */
	private static void setValue(Class<?> mapClass, Field field, String methodName, String value) throws Exception {
		
		Class<?> type = field.getType();
		
		if(methodName == null || methodName.isBlank() == true) {
		
			if(type.isPrimitive() == true || isPrimitiveClass(type) == true) {
				
				setPrimitiveValue(field, value);
				
			} else if(type == String.class) {
				
				field.set(null, value);
				
			} else {
				
				throw new Exception("can't set value");
	
			}
			
		} else {
			
			Method method = mapClass.getMethod(methodName, String.class);
			Object result = method.invoke(null, value);
			
			field.set(null, result);
		}

	}
	
	/**
	 * 
	 * 
	 * @param type
	 * @return
	 */
	private static boolean isPrimitiveClass(Class<?> type) {
		
		return type == Boolean.class || type == Character.class || type == Byte.class
				|| type == Short.class || type == Integer.class || type == Float.class
				|| type == Long.class || type == Double.class;
	}
	
	/**
	 * 
	 * 
	 * @param field
	 * @param value
	 */
	private static void setPrimitiveValue(Field field, String value) throws Exception {
		
		Class<?> type = field.getType();
		
		if(type == boolean.class || type == Boolean.class) {
			field.set(null, Boolean.parseBoolean(value));
		} else if(type == short.class || type == Short.class) {
			field.set(null, Short.parseShort(value));
		} else if(type == int.class || type == Integer.class) {
			field.set(null, Integer.parseInt(value));
		} else if(type == float.class || type == Float.class) {
			field.set(null, Float.parseFloat(value));
		} else if(type == long.class || type == Long.class) {
			field.set(null, Long.parseLong(value));
		} else if(type == double.class || type == Double.class) {
			field.set(null, Double.parseDouble(value));
		} else {
			// char과 byte는 지원하지 않음
			throw new Exception("Unsupported type: " + type);
		}
	}

}

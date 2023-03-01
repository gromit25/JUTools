package com.jutools.env;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
		
		if(Modifier.isPublic(mapClass.getModifiers()) == false) {
			throw new Exception("map class is not accessible");
		}
		
		//
		for(Field field : mapClass.getFields()) {
			
			//
			Env envInfo = field.getAnnotation(Env.class);
			if(envInfo == null) {
				continue;
			}
			
			//
			if(Modifier.isStatic(field.getModifiers()) == false
				|| Modifier.isPublic(field.getModifiers()) == false) {
				throw new Exception(field.getName() + " is not static or public");
			}
			
			//
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
				
				//
				Object valueObject = transferValueType(mapClass, field.getType(), envInfo.method(), value);
				field.set(null, valueObject);
				
			} else {
				
				//
				if(isArrayType(field) == false) {
					throw new Exception("field(" + field.getName() + ") is not array type(array, List, Map):" + field.getType());
				}
				
				//
				Object arrayObj = null;
				Class<?> memberType = null;
				
				if(field.getType().isArray() == true) {
					arrayObj = new ArrayList();
					memberType = field.getType().componentType();
				} else {
					arrayObj = field.getType().getConstructor().newInstance();
					memberType = Object.class;
				}
				
				//
				String[] splitedValueList = value.split(envInfo.separator());
				for(String splitValue: splitedValueList) {
					
					if(envInfo.trim() == true) {
						splitValue = splitValue.trim();
					}
					
					Object valueObject = transferValueType(mapClass, memberType, envInfo.method(), splitValue);
					if(List.class.isAssignableFrom(arrayObj.getClass()) == true) {
						((List)arrayObj).add(valueObject);
					} else if(Set.class.isAssignableFrom(arrayObj.getClass()) == true) {
						((Set)arrayObj).add(valueObject);
					} else {
						throw new Exception("Unexpected array type:" + arrayObj.getClass());
					}
				}
				
				//
				if(field.getType().isArray() == true) {
					
					Object array = Array.newInstance(memberType, ((List)arrayObj).size());
					
					for(int index = 0; index < ((List)arrayObj).size(); index++) {
						setArrayElement(array, memberType, index, ((List)arrayObj).get(index));
					}
					
					field.set(null, array);
					
				} else {
					field.set(null, arrayObj);
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
	private static Object transferValueType(Class<?> mapClass, Class<?> type, String methodName, String value) throws Exception {
		
		if(methodName == null || methodName.isBlank() == true) {
		
			if(isPrimitiveType(type) == true) {
				return getPrimitiveValue(type, value);
			} else if(type == String.class) {
				return value;
			} else {
				throw new Exception("can't transfer value:" + type);
			}
			
		} else {
			
			Method method = mapClass.getMethod(methodName, String.class);
			return method.invoke(null, value);
		}

	}
	
	/**
	 * 
	 * 
	 * @param type
	 * @return
	 */
	private static boolean isPrimitiveType(Class<?> type) {
		
		if(type.isPrimitive() == true) {
			return true;
		}
		
		return type == Boolean.class || type == Character.class || type == Byte.class
				|| type == Short.class || type == Integer.class || type == Float.class
				|| type == Long.class || type == Double.class;
	}
	
	/**
	 * 
	 * 
	 * @param type
	 * @param value
	 */
	private static Object getPrimitiveValue(Class<?> type, String value) throws Exception {
		
		if(type == boolean.class || type == Boolean.class) {
			return Boolean.parseBoolean(value);
		} else if(type == byte.class || type == Byte.class) {
			return Byte.parseByte(value);
		} else if(type == short.class || type == Short.class) {
			return Short.parseShort(value);
		} else if(type == int.class || type == Integer.class) {
			return Integer.parseInt(value);
		} else if(type == float.class || type == Float.class) {
			return Float.parseFloat(value);
		} else if(type == long.class || type == Long.class) {
			return Long.parseLong(value);
		} else if(type == double.class || type == Double.class) {
			return Double.parseDouble(value);
		} else {
			// char는 지원하지 않음
			throw new Exception("Unsupported type: " + type);
		}
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	private static boolean isArrayType(Field field) {
		
		Class<?> type = field.getType();
		
		return type.isArray() || List.class.isAssignableFrom(type)
				|| Set.class.isAssignableFrom(type);
	}

	/**
	 * 
	 * 
	 * @param array
	 * @param type
	 * @param index
	 * @param value
	 */
	private static void setArrayElement(Object array, Class<?> type, int index, Object value) throws Exception {
		
		if(type.isPrimitive() == true) {
			
			if(type == boolean.class) {
				Array.setBoolean(array, index, (Boolean)value);
			} else if(type == byte.class) {
				Array.setByte(array, index, (Byte)value);
			} else if(type == short.class) {
				Array.setShort(array, index, (Short)value);
			} else if(type == int.class) {
				Array.setInt(array, index, (Integer)value);
			} else if(type == float.class) {
				Array.setFloat(array, index, (Float)value);
			} else if(type == long.class) {
				Array.setFloat(array, index, (Long)value);
			} else if(type == double.class) {
				Array.setDouble(array, index, (Double)value);
			} else {
				// char와 byte는 지원하지 않음
				throw new Exception("Unsupported type: " + type);
			}
			
		} else {
			Array.set(array, index, value);
		}
	}

}

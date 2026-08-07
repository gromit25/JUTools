package com.jutools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 컬렉션 유틸리티 클래스
 * 
 * @author jmsohn
 */
public class CollectionUtil {
	
	/**
	 * 대상 문자열을 컴마(,) 단위로 잘라서 목록 객체에 넣어 반환
	 * 
	 * @param listStr 대상 문자열
	 * @param supplier 목록 객체 생성 객체
	 * @return 목록 객체
	 */
	public static List<String> toList(String listStr, Supplier<List<String>> supplier) {
		
		if(StringUtil.isBlank(listStr) == true) {
			return List.of();
		}
		
		if(supplier == null) {
			throw new IllegalArgumentException("'supplier' is null.");
		}
		
		List<String> list = supplier.get();
		
		String[] splitedAry = listStr.split(" *, *");
		for(String splited: splitedAry) {
			list.add(splited);
		}
		
		return list;
	}
	
	/**
	 * 대상 문자열을 컴마(,) 단위로 잘라서 목록 객체(ArrayList)에 넣어 반환
	 * 
	 * @param listStr 대상 문자열
	 * @return 목록 객체
	 */
	public static List<String> toList(String listStr) {
		return toList(listStr, () -> new ArrayList<String>());
	}
	
	/**
	 * p1 - p2 결과 반환
	 * 
	 * @param <T> 목록 요소의 타입
	 * @param p1 p1 목록
	 * @param p2 p2 목록
	 * @param supplier 결과 생성용 객체 ex) new ArrayList<String>();
	 * @return p1 - p2 결과
	 */
	public static <T> List<T> minus(List<T> p1, List<T> p2, Supplier<List<T>> supplier) {
	      
		// 입력값 검증
		if(supplier == null) {
			throw new IllegalArgumentException("'supplier' is null.");
		}

		// 결과 객체 생성
		List<T> result = supplier.get();
		
		// p1 이 비어 있으면 빈 객체 반환
		if(p1 == null || p1.isEmpty() == true) {
			return result;
		}
		
		// p1 목록을 result 에 추가
		result.addAll(p1);
		
		// p2 가 비어 있으면 뺼 것이 없으므로 p1 목록을 반환
		if (p2 == null || p2.isEmpty() == true) {
			return result;
		}
		
		// result(p1) - p2
		result.removeAll(p2);

		// 결과 반환
		return result;
	}
	
	/**
	 * p1 - p2 결과 반환<br>
	 * 결과 목록은 ArrayList 타입임
	 * 
	 * @param <T> 목록 요소의 타입
	 * @param p1 p1 목록
	 * @param p2 p2 목록
	 * @return p1 - p2 결과
	 */
	public static <T> List<T> minus(List<T> p1, List<T> p2) {
		return minus(p1, p2, ArrayList::new);
	}
	
	/**
	 * 주어진 키들 목록이 맵에 모두 존재하는지 여부 반환
	 * 
	 * @param map 맵
	 * @param keys 키 목록
	 * @return 키가 모두 맵에 존재하는지 연부
	 */
	public static boolean containsAll(Map<?, ?> map, Object... keys) {
		
		for(Object key: keys) {
			if(map.containsKey(key) == false) {
				return false;
			}
		}
		
		return true;
	}
}

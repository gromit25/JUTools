package com.jutools.thread;

import java.util.List;

/**
 * 각 묶음별 처리 정의 클래스
 * 
 * @author jmsohn
 */
@FunctionalInterface
public interface BundleTask {
	
	/**
	 * 각 묶음별 처리 정의 메소드
	 * -> 주의) 목록이 총 20개(0~19)이고 2개로 묶음으로 나누어질 경우,
	 *         start:0 ~ end:10, start:10 ~ end:20 으로 나누어짐 
	 * 
	 * @param list 전체 데이터 목록
	 * @param bundleId 현재 묶음의 번호 
	 * @param start 시작 번호 - 데이터 목록에서 시작할 번호
	 * @param end 끝 번호 - 데이터 목록에서 끝 번호
	 */
	public void consume(List<?> list, int bundleId, int start, int end);
}

package com.jutools;

import com.jutools.stat.nelsonrule.NelsonRule;

/**
 * 통계 유틸리티 클래스
 * 
 * @author jmsohn
 */
public class StatUtil {
	
	/**
	 * 표준 편차 계산 후 반환
	 * 만일) count 가 0 이하 이면 0을 반환
	 * 
	 * @param sum 합계
	 * @param squaredSum 제곱 합
	 * @param count 개수
	 * @return 표준 편차
	 */
	public static double std(double sum, double squaredSum, int count) {
		return Math.sqrt(variance(sum, squaredSum, count));
	}
	
	/**
	 * 분산 계산 후 반환
	 * 만일) count 가 0 이하 이면 0을 반환
	 * 
	 * @param sum 합계
	 * @param squaredSum 제곱 합
	 * @param count 개수
	 * @return 분산 계산
	 */
	public static double variance(double sum, double squaredSum, int count) {
		
		if(count <= 0) {
			return 0;
		}
		
		double mean = sum/count;
		return (squaredSum/count) - (mean*mean);
	}
	
	/**
	 * 넬슨룰 검사 객체 생성
	 * 
	 * @param mean 평균
	 * @param std 표준편차
	 * @param rules 검사할 넬슨룰 목록 비트맵
	 * @return
	 */
	public static NelsonRule createNelsonRuleChecker(double mean, double std, int rules) throws Exception {
		return NelsonRule.builder()
				.mean(10)
				.std(1)
				.rules(rules)
				.build();
	}
	
}

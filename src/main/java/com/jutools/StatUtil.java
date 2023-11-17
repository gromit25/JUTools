package com.jutools;

import java.util.Random;

import com.jutools.stat.RTParameter;
import com.jutools.stat.RTStatistic;
import com.jutools.stat.nelsonrule.NelsonRule;

/**
 * 통계 유틸리티 클래스
 * 
 * @author jmsohn
 */
public class StatUtil {
	
	/** 
	 * 평균과 표준편차에 따른 랜덤값 생성
	 * 
	 * @param mean 평균
	 * @param std 표준 편차
	 */
	public static double genNormalDistribution(double mean, double std) {

	    Random random = new Random();

	    double u1 = random.nextDouble();
	    double u2 = random.nextDouble();

	    double z = Math.sqrt(-2 * Math.log(u1)) * Math.cos(2 * Math.PI * u2);

	    return mean + (z * std);
	}
	
	/**
	 * 실시간 모수(parameter) 계산 객체 반환
	 * 
	 * @return 실시간 모수(parameter) 계산 객체
	 */
	public static RTParameter createRTParameter() {
		return new RTParameter();
	}

	/**
	 * 실시간 모수(parameter) 계산 객체 반환
	 * 
	 * @param sum 합계
	 * @param squaredSum 제곱합(표준편차 계산용)
	 * @param cubedSum 세제곱 합(왜도 계산용)
	 * @param fourthPoweredSum 네제곱 합(첨도 계산용)
	 * @param count 데이터의 개수
	 * @return 실시간 모수(parameter) 계산 객체
	 */
	public static RTParameter createRTParameter(double sum, double squaredSum, double cubedSum, double fourthPoweredSum, int count) throws Exception {
		return new RTParameter(sum, squaredSum, cubedSum, fourthPoweredSum, count);
	}
	
	/**
	 * 실시간 표본 통계량(statistic) 계산 객체 반환
	 * 
	 * @return 실시간 표본 통계량(statistic) 계산 객체
	 */
	public static RTStatistic createRTStatistic() {
		return new RTStatistic();
	}

	/**
	 * 실시간 표본 통계량(statistic) 계산 객체 반환
	 * 
	 * @param sum 합계
	 * @param squaredSum 제곱합(표준편차 계산용)
	 * @param cubedSum 세제곱 합(왜도 계산용)
	 * @param fourthPoweredSum 네제곱 합(첨도 계산용)
	 * @param count 데이터의 개수
	 * @return 실시간 표본 통계량(statistic) 계산 객체
	 */
	public static RTStatistic createRTStatistic(double sum, double squaredSum, double cubedSum, double fourthPoweredSum, int count) throws Exception {
		return new RTStatistic(sum, squaredSum, cubedSum, fourthPoweredSum, count);
	}

	/**
	 * 넬슨룰 검사 객체 생성<br>
	 * 사용법)
	 * <pre> 
	 * NelsonRule nrule = StatUtil.createNelsonRule(10, 1, NelsonRule.RULE1|NelsonRule.RULE2);
	 * 
	 * for(double value: dataList) {
	 *     ArrayList&lt;Integer&gt; violatedRules = nrule.check(value);
	 *     for(Integer ruleIndex: violatedRules) {
	 *         // 넬슨룰 위반 처리
	 *     }
	 * }
	 * </pre>
	 * 
	 * @param mean 평균
	 * @param std 표준편차
	 * @param rules 검사할 넬슨룰 목록 비트맵
	 * @return
	 */
	public static NelsonRule createNelsonRule(double mean, double std, int rules) throws Exception {
		return NelsonRule.builder()
				.mean(mean)
				.std(std)
				.rules(rules)
				.build();
	}
	
}

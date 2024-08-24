package com.jutools.stat;

import lombok.Getter;

/**
 * T-Test 검정<br>
 * 대조군과 비교군의 평균이 의미있는 차이가 있는지 검정
 * 
 * @author jmsohn
 */
public class PairedTTest {
	
	/** 대조군의 통계량 */
	@Getter
	private Statistic controlStat = new Statistic();
	
	/** 실험군의 통계량 */
	@Getter
	private Statistic experimentalStat = new Statistic();
	
	/**
	 * 대조군과 실험군의 데이터를 추가
	 * 
	 * @param controlData 대조군 데이터
	 * @param experimentalData 실험군 데이터
	 */
	public void add(double controlData, double experimentalData) throws Exception {
		
		// 대조군 데이터 추가
		this.controlStat.add(controlData);
		
		// 실험군 데이터 추가
		this.experimentalStat.add(experimentalData);
	}
	
	/**
	 * 현재까지 입력된 데이터의 T 검정값 반환
	 * 
	 * @return T 검정값
	 */
	public double getTValue() {
		
		// 대조군 통계량 획득(평균, 분산)
		double controlMean = this.controlStat.getMean();
		double controlVar = this.controlStat.getVariance();
		
		// 실험군 통계량 획득(평균, 분산)
		double exprimentalMean = this.experimentalStat.getMean();
		double exprimentalVar = this.experimentalStat.getVariance();
		
		// 데이터의 개수
		double count = (long)this.controlStat.getCount();
		
		// T 검정 값 계산 
		// (controlMean - exprimentalMean)/root(controlVar/count + exprimentalVar/count)
		double diffMean = controlMean - exprimentalMean;
		double sumVar = (controlVar + exprimentalVar)/count;
		
		return diffMean/Math.sqrt(sumVar);
	}
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder("");
		
		// JSON 시작
		builder
			.append("{");
		
		// T 검정 값 출력
		builder
			.append("\"t_value\":")
			.append(this.getTValue())
			.append(",");
			;
		
		// 대조군 통계량 추가
		builder
			.append("\"control_stat\":")
			.append(this.controlStat.toString())
			.append(",");
		
		// 실험군 통계량 추가
		builder
			.append("\"experimental_stat\":")
			.append(this.experimentalStat.toString());
		
		// JSON 종료
		builder
			.append("}");
		
		return builder.toString();
	}
}

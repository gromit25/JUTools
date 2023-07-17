package com.jutools.stat.nelsonrule;

import java.util.ArrayList;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 넬슨룰 검사 클래스<br>
 * 사용법) 넬슨룰 1, 2에 대한 검사
 * <pre> 
 * NelsonRule nrule = NelsonRule.builder().
 *                        .mean(10)
 *                        .std(1)
 *                        .rules(NelsonRule.RULE1|NelsonRule.RULE2)
 *                        .build();
 * 
 * for(double value: dataList) {
 *     ArrayList&lt;Integer&gt; violatedRules = nrule.check(value);
 *     for(Integer ruleIndex: violatedRules) {
 *         // 넬슨룰 위반 처리
 *     }
 * }
 * </pre>
 * 
 * @author jmsohn
 */
public class NelsonRule {
	
	/** 넬슨룰-1의 비트맵 */
	public static int RULE1 = 0b00000001;
	/** 넬슨룰-2의 비트맵 */
	public static int RULE2 = 0b00000010;
	/** 넬슨룰-3의 비트맵 */
	public static int RULE3 = 0b00000100;
	/** 넬슨룰-4의 비트맵 */
	public static int RULE4 = 0b00001000;
	/** 넬슨룰-5의 비트맵 */
	public static int RULE5 = 0b00010000;
	/** 넬슨룰-6의 비트맵 */
	public static int RULE6 = 0b00100000;
	/** 넬슨룰-7의 비트맵 */
	public static int RULE7 = 0b01000000;
	/** 넬슨룰-8의 비트맵 */
	public static int RULE8 = 0b10000000;
	/** 모든 넬슨룰의 비트맵 */
	public static int ALL   = 0b11111111;

	/** 평균 */
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private double mean;
	
	/** 표준 편차 */
	@Getter
	private double std;
	
	/** 검사 진행할 넬슨룰 목록 */
	@Getter
	private ArrayList<Rule> rules;
	
	/**
	 * 생성자
	 * 
	 * @param mean 평균
	 * @param std 표준 편차
	 * @param rules 넬슨룰 목록 비트맵
	 */
	@Builder
	private NelsonRule(double mean, double std, int rules) throws Exception {
		
		// 평균, 표준 편차, 넬슨룰 설정
		this.setMean(mean);
		this.setStd(std);
		this.setRules(rules);
		
	}
	
	/**
	 * 넬슨룰 검사 수행
	 * 
	 * @param value 현재 값
	 * @return 위반한 룰 목록(위반이 없을 경우, 크기 0)
	 */
	public ArrayList<Integer> check(double value) {
		
		// 검사할 룰이 없으면 빈 목록 반환
		if(this.getRules() == null) {
			return new ArrayList<>();
		}
		
		// 위반한 룰의 비트맵 변수
		int violatedRules = 0;
		
		// 현재 입력값에 대해 검사 룰 목록 별로 검사 수행
		for(Rule rule: this.getRules()) {
			
			// 룰 위반시 위반 룰 비트맵 추가
			if(rule.isViolated(value) == true) {
				violatedRules |= rule.bitmap();
			}
		}
		
		return RuleUtil.bitmapToIndices(violatedRules, 8);
	}
	
	/**
	 * 표준 편차 설정
	 * 
	 * @param std 표준 편차
	 */
	private void setStd(double std) throws Exception {
		
		if(std <= 0) {
			throw new Exception("std is not valid:" + std);
		}
		
		this.std = std;
	}
	
	/**
	 * 검사할 넬슨룰 목록 설정
	 * 
	 * @param rules 검사할 넬슨룰 목록(ex. NelsonRule.RULE1 | NelsonRule.RULE3)
	 */
	private void setRules(int rules) {
		
		this.rules = new ArrayList<>();
		
		if((rules & RULE1) != 0) {
			this.rules.add(new Rule1(this));
		}
		
		if((rules & RULE2) != 0) {
			this.rules.add(new Rule2(this));
		}
		
		if((rules & RULE3) != 0) {
			this.rules.add(new Rule3(this));
		}
		
		if((rules & RULE4) != 0) {
			this.rules.add(new Rule4(this));
		}
		
		if((rules & RULE5) != 0) {
			this.rules.add(new Rule5(this));
		}
		
		if((rules & RULE6) != 0) {
			this.rules.add(new Rule6(this));
		}
		
		if((rules & RULE7) != 0) {
			this.rules.add(new Rule7(this));
		}
		
		if((rules & RULE8) != 0) {
			this.rules.add(new Rule8(this));
		}
	}
}
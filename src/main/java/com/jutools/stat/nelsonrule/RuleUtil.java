package com.jutools.stat.nelsonrule;

import java.util.ArrayList;

import com.jutools.MathUtil;

/**
 * nelson rule 유틸리티 클래스
 * 
 * @author jmsohn
 */
class RuleUtil {
	
	/**
	 * bit로 표현된 map에 설정된 index 목록을 반환
	 * ex) bitMap: 0b1101, size:3 -> 1, 3
	 * 
	 * @param bitMap bit로 표현된 맵 
	 * @param size bit 표현된 맵의 크기
	 * @return index 목록
	 */
	static ArrayList<Integer> bitmapToIndices(int bitMap, int size) {
		
		// index 목록 변수
		ArrayList<Integer> indices = new ArrayList<>();
		
		// 사이즈 만큼 루프 수행
		for(int index = 0; index < size; index++) {
			
			// 가장 끝에 bit가 1인지 확인
			if((bitMap & 1) == 1) {
				indices.add(index + 1);
			}

			// 오른쪽으로 한칸시프트
			bitMap = bitMap >>> 1;
		}
		
		return indices;
	}
	
	/**
	 * 기준값에 대한 방향에 따라 개수를 셈<br>
	 * ex)<br>
	 * value: 10, base: 0, preCount: 3 -&gt; 4를 반환 <br>
	 * value: -2, base: 0, preCount: 3 -&gt; -1을 반환<br>
	 * value: -2, base: 0, preCount: -3 -&gt; -4를 반환<br>
	 * 
	 * @param value 현재 값
	 * @param base 기준 값
	 * @param preCount 이전 개수
	 * @return 방향에 따른 개수
	 */
	static int countWithDirection(double value, double base, int preCount) {
		
		//
		if(Double.isNaN(base) == true) {
			return 0;
		}
		
		// 기준 값과의 차이를 확인
		double diff = value - base;
		
		// 기준 값과의 차이에 대한 부호 확인
		int sign = MathUtil.sign(diff);
		
		// 분별자(discriminator) 계산 
		// 이전 개수와 현재 값의 부호가 같으면 +, 다르면 -
		int count = sign;	// 만일 부호가 다를 경우 sign 값을 할당
		int discriminator = preCount * sign;
		
		// 이전 개수와 현재 값의 부호가 같으면,
		// 기존 count에 추가
		if(discriminator > 0) {
			count = preCount + sign;
		}
		
		return count;
	}
	
	/**
	 * counter queue 내에 mask 범위에 1의 개수가<br>
	 * 모두 1 이거나 하나만 1이 아닌 경우를 반환<br>
	 * 만일, 0 이면 모두 1 이거나 하나만 1이 아님
	 * 
	 * @param counterQueue 검사할 queue
	 * @param range 검사할 범위(실제 숫자가 아니라 bitmap 형태여야 함, ex) 0b111)
	 * @return 모두 1이거나 하나만 1이 아닌지 여부
	 */
	static int hasOnlyOneZeroInRange(int counterQueue, int range) {
		
		// counterQueue : 0b100101,
		// mask : 0b111 이면,
		//
		// 100101 & 000111 -> 000101 (111로 마스킹)
		// 000101 ^ 000111 -> 000010 (비트 반전, 만일 반전된 값이 모두 0이면, 모든 비트가 설정된 것임)
		// 000010 & 000001 -> 0
		// 비트 반전된 값에 1을 뺀 값을 & 연산하면 비트 반전된 값이 2^n 인지 확인 가능함
		// 2^n이면 1개의 bit만 있는 것이므로 원본에서는 0이 하나만 있는 경우임
		//
		
		int discriminator = (counterQueue & range) ^ range;
		
		if(discriminator == 0) {
			return discriminator;
		}
		
		discriminator &= (discriminator - 1);
		
		return discriminator;
	}

}

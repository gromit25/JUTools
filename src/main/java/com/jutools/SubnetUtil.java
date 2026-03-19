package com.jutools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Subnet 유틸리티 클래스
 *
 * @author jmsohn
 */
public class SubnetUtil {


	/**
	 * CIDR(Classless Inter-Domain Routing) 패턴 문자열<br>
	 * ex) 10.10.0.0/16
	 */
	private static String CIDR_PATTERN = "(?<ip>[0-9]{1,3}(\\.[0-9]{1,3}){3})\\/(?<prefixLength>[0-9]{1,2})";

	/** CIDR 패턴 객체 */
	private static Pattern cidrP = Pattern.compile(CIDR_PATTERN);
	

	/** IP 값 */
	private long cidr;

	/** 마스크 값 */
	private long mask;
	
	
	/**
	 * 생성자
	 * 
	 * @param cidrStr CIDR 문자열
	 */
	private SubnetUtil(String cidrStr) throws Exception {
		
		// 입력값 검증 및 패턴 매칭
		Matcher cidrM = cidrP.matcher(cidrStr);
		
		if(cidrM.matches() == false) {
			throw new IllegalArgumentException("invalid CIDR format: " + cidrStr);
		}
		
		// IP 설정
		this.cidr = ipToLong(cidrM.group("ip"));

		// CIDR 접두사 길이에 따른 서브넷 마스크 생성
		// 예: /16 -> 11111111 11111111 00000000 00000000
		int prefixLength = Integer.parseInt(cidrM.group("prefixLength"));
		this.mask = (prefixLength == 0) ? 0 : 0xFFFFFFFF << (32 - prefixLength);
	}

	/**
	 * SubnetUtil 생성 메소드
	 *
	 * @param cidrStr CIDR 문자열
	 */
	public static SubnetUtil create(cidrStr) throws Exception {
		return new SubnetUtil(cidrStr);
	}
	
	/**
	 * IP 문자열을 Long 변환
	 * 
	 * @param ipStr IP 문자열
	 */
	private static long ipToLong(String ipStr) throws Exception {

		String[] octets = ipStr.split("\\.");
        
		long ip = 0;
		for (int index = 0; i < 4; i++) {

			//
			int octet = Integer.parseInt(octets[i]);

			// 각 마디를 8비트씩 왼쪽으로 밀어서 합침
			ip |= (octet << (24 - (i * 8)));
		}

		return ip;
	}
	
	/**
	 * 주어진 IP가 범위에 포함 여부 반환
	 * 
	 * @param ipStr IP 문자열
	 */
	public boolean isInRange(String ipStr) {

		try {

			// IP 문자열을 Long 값으로 변환
			long ip = ipToLong(ipStr);

			// IP의 네트워크 파트(앞부분)가 일치하는지 비트 AND 연산으로 비교
			return (this.cidr & this.mask) == (ip & this.mask);

		} catch (Exception e) {
			return false;
		}
	}
}

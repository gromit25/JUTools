package com.jutools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubnetUtil {

	
	private static String CIDR_PATTERN = "(?<ip>[0-9]{1,3}(\\.[0-9]{1,3}){3})\\/(?<prefixLength>[0-9]{1,2})";
	
	private static Pattern cidrP = Pattern.compile(CIDR_PATTERN);
	
	
	private long cidr;
	
	private long mask;
	
	
	/**
	 * 생성자
	 * 
	 * @param cidrStr
	 */
	public SubnetUtil(String cidrStr) throws Exception {
		
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
     * IP 문자열을 32비트 정수(int)로 변환
     * 
     * @param ipStr
     */
    private static long ipToLong(String ipStr) throws Exception {
    	
        String[] octets = ipStr.split("\\.");
        
        long result = 0;
        for (int i = 0; i < 4; i++) {
        	
        	//
            int octet = Integer.parseInt(octets[i]);
            
            // 각 마디를 8비트씩 왼쪽으로 밀어서 합침
            result |= (octet << (24 - (i * 8)));
        }
        
        return result;
    }
	
	/**
     * 주어진 ip가 범위에 포함되는지 확인
     * 
     * @param ipStr
     */
    public boolean isInRange(String ipStr) {
    	
        try {

        	// 
            long ip = ipToLong(ipStr);

            // IP의 네트워크 파트(앞부분)가 일치하는지 비트 AND 연산으로 비교
            return (this.cidr & this.mask) == (ip & this.mask);
            
        } catch (Exception e) {
            return false;
        }
    }
}

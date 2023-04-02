package com.jutools;

import java.util.ArrayList;

/**
 * byte array 처리 관련 utility 클래스
 * 
 * @author jmsohn
 */
public class BytesUtil {
	
	/**
	 * target Byte의 끝 부분과 지정한 접미사 일치 여부 반환<br>
	 * 일치할 경우 : true, 일치하지 않을 경우 : false
	 *
	 * @param target 확인 대상 byte array
	 * @param suffix 접미사 byte array
	 * @return target Byte의 끝 부분과 지정한 접미사 일치 여부
	 */
	public static boolean endsWith(byte[] target, byte[] suffix) throws Exception {

		// parameter null 체크
		if(target == null) {
			throw new NullPointerException("target array is null.");
		}

		if(suffix == null) {
			throw new NullPointerException("suffix array is null.");
		}

		// target이 접미사보다 작을 경우에는 항상 false 
		if(suffix.length > target.length) {
			return false;
		}

		// 비교를 시작할 target의 끝 부분 위치
		int start = target.length - suffix.length;

		// 각 Byte array의 index번째에 있는 byte가 같은지 확인
		for(int index = 0; index < suffix.length; index++) {
			if(target[start + index] != suffix[index]) {
				return false;
			}
		}
		
		return true;

	}
	
	/**
	 * 목표 배열(target) 내에 찾을 배열(lookup)의 첫번째 일치하는 위치를 반환<br>
	 * 만일 찾지 못하면 -1을 반환함
	 * 
	 * @param target 목표 배열
	 * @param start target의 검색 시작 지점
	 * @param lookup 찾을 배열
	 * @return 목표 배열 내에 첫번째 일치하는 위치
	 */
	public static int indexOf(byte[] target, int start, byte[] lookup) throws Exception {
		
		// 목표 배열의 크기(target.length - start)가 찾을 배열의 크기(lookup.length)
		// 보다 작은 경우에는 -1을 반환
		if(target == null || lookup == null || target.length - start < lookup.length) {
			return -1;
		}
		
		// 상태 변수 - 0:배열내 불일치 상태, 1:찾을 배열과 일치 중인 상태
		int status = 0;
		// 목표 배열내 검색 중인 위치 변수
		int pos = start;
		// 목표 배열내 찾을 배열과 최초로 일치하는 위치 저장용 변수
		int savePos = -1;
		// 찾을 배열내 위치 변수
		int lookupPos = 0;
		
		// 검색 위치가 목표 배열의 크기 보다 작을 경우 수행
		while(pos < target.length) {
			
			if(target[pos] == lookup[lookupPos]) {
				
				// 최초로 일치하는 경우
				// 현재 위치를 savePos에 저장
				if(status == 0) {
					savePos = pos;
					status = 1;
				}
				
				// 목표 배열내 검색 위치와 찾을 배열내 검색 위치를
				// 다음 byte로 이동
				pos++;
				lookupPos++;
				
				// 만일 찾을 배열의 모든 문자를 검색 완료하였으면
				// 일치 시작 위치(savePos)를 반환
				if(lookupPos >= lookup.length) {
					return savePos;
				}
				
			} else {
				
				if(status == 1) {
					
					// 기존 savePos로 이동하게 되면 다시 매치되기 때문에 +1 문자부터 검사하도록함
					pos = savePos + 1;
					
					// 초기화
					savePos = -1;
					lookupPos = 0;
					status = 0;
					
				} else if(status == 0) {
					
					// 검색 위치를 하나 증가하여 다음 byte를 비교
					pos++;
					
				} else {
					throw new Exception("Unexpected status:" + status);
				}
				
			}
		}
		
		// 목표 배열을 모두 확인하였으나
		// 찾지 못함
		return -1;
	}
	
	/**
	 * 목표 배열(target) 내에 찾을 배열(lookup)의 첫번째 일치하는 위치를 반환<br>
	 * 만일 찾지 못하면 -1을 반환함
	 * 
	 * @param target 목표 배열
	 * @param lookup 찾을 배열
	 * @return 목표 배열 내에 첫번째 일치하는 위치
	 */
	public static int indexOf(byte[] target, byte[] lookup) throws Exception {
		return indexOf(target, 0, lookup);
	}
	
	/**
	 * 목표 배열(target) 내에 찾을 배열(lookup)이 있는지 여부 반환
	 * 
	 * @param target 목표 배열
	 * @param start target의 검색 시작 지점
	 * @param lookup 찾을 배열
	 * @return 목표 배열 내에 찾을 배열이 있는지 여부
	 */
	public static boolean contains(byte[] target, int start, byte[] lookup) throws Exception {
		return indexOf(target, start, lookup) >= 0;
	}
	
	/**
	 * 목표 배열(target) 내에 찾을 배열(lookup)이 있는지 여부 반환
	 * 
	 * @param target 목표 배열
	 * @param lookup 찾을 배열
	 * @return 목표 배열 내에 찾을 배열이 있는지 여부
	 */
	public static boolean contains(byte[] target, byte[] lookup) throws Exception {
		return contains(target, 0, lookup);
	}
	
	/**
	 * 주어진 target byte array를 split 하는 메소드<br>
	 * target byte와 구분자의 byte를 비교하여<br>
	 * 구분자가 포함 됐을 경우 분리 함
	 *
	 * @param target target byte array
	 * @param split 구분자 byte array
	 * @param isLastInclude 배열의 마지막이 구분자(split)로 끝날 경우, 추가할 것인지 여부<br>
	 *                      ex) true로 설정되고 target : "123\n", split : "\n" 일 경우,<br>
	 *                          "123", "" 으로 분리됨<br>
	 *                          false일 경우, "123"으로만 분리됨
	 * @return 구분자에 의해 분리된 결과 목록
	 */
	public static ArrayList<byte[]> split(byte[] target, byte[] split, boolean isLastInclude) throws Exception {

		// parameter null 체크
		if(target == null) {
			throw new NullPointerException("target array is null.");
		}

		if(split == null) {
			throw new NullPointerException("split array is null.");
		}

		// 구분자에 의해 분리된 결과
		ArrayList<byte[]> splitedTarget = new ArrayList<>();
		
		// split이 발견된 곳의 위치 변수
		int index = -1;
		// 검사를 시작할 위치 변수
		int start = 0;
		
		do {
			
			// isLastInclude가 true이면,
			// 배열의 마지막이 구분자(split)로 끝날 경우 공백 배열을 추가함
			if(isLastInclude == false && start >= target.length) {
				break;
			}
			
			// 목표 배열 내 split이 발견된 곳의 위치 확인 
			index = indexOf(target, start, split);
			
			// 시작지점(start)부터 발견된 곳(index)의 배열을 splitedTarget에 추가 
			// index가 -1일 경우 시작지점 부터 끝까지의 배열을 추가
			byte[] splitedBytes = null;
			if(index >= 0) {
				splitedBytes = new byte[index - start];
			} else {
				splitedBytes = new byte[target.length - start];
			}
			
			System.arraycopy(target, start, splitedBytes, 0, splitedBytes.length);
			splitedTarget.add(splitedBytes);
			
			// 다음 시작위치 계산(현재발견된 위치 + split의 크기)
			start = index + split.length;
			
		} while(index >= 0);
    	
		// 최종 결과 반환
		return splitedTarget;

	}
	
	/**
	 * 주어진 target byte array를 split 하는 메소드
	 * target byte와 구분자의 byte를 비교하여
	 * 구분자가 포함 됐을 경우 분리 함
	 *
	 * @param target target byte array
	 * @param split 구분자 byte array
	 * @return 구분자에 의해 분리된 결과 목록
	 */
	public static ArrayList<byte[]> split(byte[] target, byte[] split) throws Exception {
		return split(target, split, false);
	}
	
	/**
	 * 여러 Byte array를 합침
	 *
	 * @param targets: 합칠 Byte array
	 * @return 합쳐진 Byte array
	 */
	public static byte[] concat(byte[]... srcs) throws Exception {
		
		// parameter null 체크
		if(srcs == null) {
			throw new NullPointerException("source array is null");
		}
		
		if(srcs.length == 0) {
			return new byte[0];
		}
		
		// 전체 목록의 크기 계산
		int totalSize = 0;
		for(byte[] src: srcs) {
			
			// 합칠 배열이 null일 경우, skip함
			if(src == null) {
				continue;
			}
			
			totalSize += src.length;
		}

		// 합쳐진 Byte 배열 변수
		// 두 Byte array 크기의 합 만큼의 사이즈 할당
		byte[] concatenatedArray = new byte[totalSize];

		// 배열을 합침
		int dstStart = 0;
		for(byte[] src: srcs) {
			
			// 합칠 배열이 null일 경우, skip함
			if(src == null) {
				continue;
			}

			// concatenatedArray에 src 배열 복사
			System.arraycopy(src, 0, concatenatedArray, dstStart, src.length);
			dstStart += src.length;
		}
		
		return concatenatedArray;
	}
	
	public static byte[] strToBytes(String str) throws Exception {
		
		byte[] bytes = new byte[str.length()/2];
		
		for(int index = 0; index < bytes.length; index++) {
			char ch = str.charAt(index * 2);
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytesToStr(byte[] bytes) throws Exception {
		
		if(bytes == null) {
			throw new NullPointerException("bytes is null");
		}
		
		StringBuilder builder = new StringBuilder("");
		
		for(int index = 0; index < bytes.length; index++) {
			builder.append(String.format("%02X", bytes[index]));
		}
		
		return builder.toString();
	}

}

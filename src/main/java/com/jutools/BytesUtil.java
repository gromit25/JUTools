package com.jutools;

import java.util.ArrayList;

/**
 * byte array 처리 관련 utility 클래스
 * 
 * @author jmsohn
 */
public class BytesUtil {
	
	/**
	 * target Byte의 끝 부분과 지정한 접미사 일치 여부 반환
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
	 * 
	 * @param target
	 * @param start
	 * @param lookup
	 * @return
	 */
	public static int indexOf(byte[] target, int start, byte[] lookup) throws Exception {
		
		if(target == null || lookup == null || target.length - start < lookup.length) {
			return -1;
		}
		
		//
		int status = 0;
		//
		int pos = start;
		//
		int savePos = -1;
		//
		int lookupPos = 0;
		
		while(pos < target.length) {
			
			if(target[pos] == lookup[lookupPos]) {
				
				//
				if(status == 0) {
					savePos = pos;
					status = 1;
				}
				
				//
				pos++;
				lookupPos++;
				
				//
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
					pos++;
				} else {
					throw new Exception("Unexpected status:" + status);
				}
				
			}
		}
		
		return -1;
	}
	
	/**
	 * 
	 * @param target
	 * @param lookup
	 * @return
	 */
	public static int indexOf(byte[] target, byte[] lookup) throws Exception {
		return indexOf(target, 0, lookup);
	}
	
	/**
	 * 
	 * @param target
	 * @param start
	 * @param lookup
	 * @return
	 */
	public static boolean contains(byte[] target, int start, byte[] lookup) throws Exception {
		return indexOf(target, start, lookup) >= 0;
	}
	
	/**
	 * 
	 * @param target
	 * @param lookup
	 * @return
	 */
	public static boolean contains(byte[] target, byte[] lookup) throws Exception {
		return contains(target, 0, lookup);
	}
	
	/**
	 * 주어진 target byte array를 split 하는 메소드
	 * target byte와 구분자의 byte를 비교하여
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
		
		//
		int index = 0;
		int start = 0;
		
		while(index >= 0) {
			
			//
			if(isLastInclude == false && start >= target.length) {
				break;
			}
			
			//
			index = indexOf(target, start, split);
			
			//
			byte[] splitedBytes = null;
			if(index >= 0) {
				splitedBytes = new byte[index - start];
			} else {
				splitedBytes = new byte[target.length - start];
			}
			
			System.arraycopy(target, start, splitedBytes, 0, splitedBytes.length);
			splitedTarget.add(splitedBytes);
			
			//
			start = index + split.length;
		}
    	
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

}

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
	 * 주어진 target byte array를 split 하는 메소드
	 * target byte와 구분자의 byte를 비교하여
	 * 구분자가 포함 됐을 경우 분리 함
	 *
	 * @param target target byte array
	 * @param split 구분자 byte array
	 * @return 구분자에 의해 분리된 결과 목록
	 */
	public static ArrayList<byte[]> split(byte[] target, byte[] split) throws Exception {

		// parameter null 체크
		if(target == null) {
			throw new NullPointerException("target array is null.");
		}

		if(split == null) {
			throw new NullPointerException("split array is null.");
		}

		// 구분자에 의해 분리된 결과
		ArrayList<byte[]> splitedTarget = new ArrayList<>();

		// 구분자와 동일하지 않아 분리된 byte
		byte[] pieceBuffer = new byte[target.length];
		int pieceBufferPos = 0;

		// 구분자와 동일한 byte를 저장해둠
		// -> 구분자의 문자가 일부 포함되어 있을 경우를 위함
		byte[] splitBuffer = new byte[split.length];
		int splitBufferPos = 0;

		// 동일한지 상태를 체크할 변수
		// 구분자와 동일할 경우 1, 아닐 경우 0
		int status = 0;
		// 구분자 위치
		int splitPos = 0;

		for(int index = 0; index < target.length; index++) {

			byte cur = target[index];

			// target과 구분자의 byte가 동일할 경우
			// - splitBuffer에 저장
			// - 다음 비교를 위해 target과 구분자의 위치를 한칸 이동
			// - 만약 구분자가 모두 포함되어 있을 경우 splitTarget에 추가

			if(cur == split[splitPos]) {

				status = 1; // 상태를 1로 바꿈

				// splitBuffer에 현재 byte 저장 후 한칸 이동
				splitBuffer[splitBufferPos++] = cur;

				// 구분자 한칸 이동
				splitPos++;

				// 구분자가 모두 포함되어 있을 경우
				// -> 지금까지 분리된 byte(pieceBuffer)를 splitTarget에 추가
				if(splitPos >= split.length) {

					// pieceBuffer 복사 후 추가
					byte[] piece = new byte[pieceBufferPos];
					System.arraycopy(pieceBuffer, 0, piece, 0, pieceBufferPos);

					if(pieceBufferPos != 0) {
						splitedTarget.add(piece);
					}

					// 분리에 필요한 변수 초기화
					splitBufferPos = 0;
					pieceBufferPos = 0;
					splitPos = 0;
					status = 0;
				}
	
			} else {

				// target과 구분자의 byte가 동일하지 않을 경우
				// - pieceBuffer에 byte를 추가
				// - 상태가 1일 경우 구분자에 일부 포함된 byte가 있었던 것이므로
				//   splitBuffer를 pieceBuffer에 추가

				if(status == 1) {
					// splitBuffer를 복사해 pieceBuffer에 추가함
					System.arraycopy(splitBuffer, 0, pieceBuffer, pieceBufferPos, splitBufferPos);

					// pieceBuffer의 위치를 splitBuffer 만큼 이동
					pieceBufferPos += splitBufferPos;
					splitBufferPos = 0;
				}

				// 상태는 그대로 0
				status = 0;
				splitPos = 0;

				// pieceBuffer에 현재 byte를 추가하고
				// pieceBufferPos 위치 한칸 이동
				pieceBuffer[pieceBufferPos++] = cur;
			}

		}
    	
		// 종료 처리
		if(pieceBufferPos != 0 || splitBufferPos != 0) {

			// 현재까지 분리된 Byte를 복사
			byte[] piece = new byte[pieceBufferPos + splitBufferPos];
			System.arraycopy(pieceBuffer, 0, piece, 0, pieceBufferPos);

			if(splitBufferPos != 0) {
				System.arraycopy(splitBuffer, 0, piece, pieceBufferPos, splitBufferPos);
			}

			//splitedTarget에 추가
			splitedTarget.add(piece);
		}
    	
		// 최종 결과 반환
		return splitedTarget;

	}

}

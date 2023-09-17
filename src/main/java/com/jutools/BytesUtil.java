package com.jutools;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import com.jutools.bytesmap.BytesMapper;
import com.jutools.common.OrderType;

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
	 * 주어진 target byte array를 split 하는 메소드<br>
	 * target byte와 구분자의 byte를 비교하여<br>
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
	 * 여러 Byte array를 합침<br>
	 * srcs[0]: {1, 2}, srcs[1]: {3}, srcs[2]: {4, 5} -> {1, 2, 3, 4, 5}
	 *
	 * @param targets: 합칠 Byte array
	 * @return 합쳐진 Byte array
	 */
	public static byte[] concat(byte[]... srcs) throws Exception {
		
		// parameter null 체크
		if(srcs == null) {
			throw new NullPointerException("source array is null.");
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
	
	/**
	 * byte 배열의 특정 위치를 잘라서 반환
	 * 
	 * @param array 대상 byte 배열
	 * @param start 자르기 시작 지점
	 * @param length 자를 크기
	 * @return 잘라진 배열
	 */
	public static byte[] cut(byte[] array, int start, int length) throws Exception {
		
		if(array == null) {
			throw new NullPointerException("array is null.");
		}
		
		if(start < 0 || start >= array.length) {
			throw new IllegalArgumentException("start is invalid:" + start);
		}
		
		if(length < 1) {
			throw new IllegalArgumentException("length is invalid:" + start);
		}

		byte[] cutArray = new byte[length];
		System.arraycopy(array, start, cutArray, 0, length);
		
		return cutArray;
	}
	
	/**
	 * 파일의 모든 바이트를 읽어 반환 
	 * 
	 * @param file 읽을 파일 객체
	 * @return 읽은 바이트 배열
	 */
	public static byte[] readAllBytes(File file) throws Exception {
		return readAllBytes(file, 1024 * 1024);
	}
	
	/**
	 * 파일의 모든 바이트를 읽어 반환 
	 * 
	 * @param file 읽을 파일 객체
	 * @param bufferSize 입력 스트림에서 데이터를 읽을 때 사용할 임시 버퍼의 크기
	 * @return 읽은 바이트 배열
	 */
	public static byte[] readAllBytes(File file, int bufferSize) throws Exception {
		
		if(file == null) {
			throw new NullPointerException("file is null.");
		}
		
		if(file.canRead() == false) {
			throw new IllegalAccessException("can't read file:" + file.getAbsolutePath());
		}
		
		// 파일 전체를 읽어서 반환
		try(FileInputStream fis = new FileInputStream(file)) {
			return readAllBytes(fis, bufferSize);
		}
	}
	
	/**
	 * 입력 스트림에서 모든 바이트를 읽어 반환<br>
	 * 읽을 데이터가 적을 경우 사용(많으면 성능 문제와 메모리 문제가 발생할 수 있음)<br>
	 * 임시 버퍼의 크기는 1024 * 1024 byte 임
	 * 
	 * @param is 읽을 입력 스트림
	 * @return 읽은 데이터
	 */
	public static byte[] readAllBytes(InputStream is) throws Exception {
		return readAllBytes(is, 1024 * 1024);
	}
	
	/**
	 * 입력 스트림에서 모든 바이트를 읽어 반환<br>
	 * 읽을 데이터가 적을 경우 사용(많으면 성능 문제와 메모리 문제가 발생할 수 있음)
	 * 
	 * @param is 읽을 입력 스트림
	 * @param bufferSize 입력 스트림에서 데이터를 읽을 때 사용할 임시 버퍼의 크기
	 * @return 읽은 데이터
	 */
	public static byte[] readAllBytes(InputStream is, int bufferSize) throws Exception {
		
		// buffer의 크기가 1보다 작을 경우 예외 발생
		if(bufferSize < 1) {
			throw new IllegalArgumentException("buffer size is invalid:" + bufferSize);
		}
		
		// 모든 데이터 읽기 버퍼 변수
		byte[] readAll = new byte[0];
		
		// 입력 스트림이 null 일 경우 빈 버퍼 반환
		if(is == null) {
			return readAll;
		}

		// buffer 설정
		byte[] buffer = new byte[bufferSize];
		
		// input stream의 모든 데이터를 읽음
		int readCount = -1;
		while((readCount = is.read(buffer)) != -1) {
			
			if(readCount == bufferSize) {
				readAll = concat(readAll, buffer);
			} else {
				readAll = concat(readAll, cut(buffer, 0, readCount));
			}
		}
		
		return readAll;
	}
	
	/**
	 * 입력 스트림에서 N 바이트를 읽어 반환<br>
	 * 만일, 스트림의 바이트 크기가 N보다 작으면 전체를 다 읽어 들임
	 * 
	 * @param file 읽을 파일 객체
	 * @param n 읽을 바이트 수
	 * @return 읽은 바이트 배열
	 */
	public static byte[] readNBytes(File file, int n) throws Exception {
		return readNBytes(file, n, 1024 * 1024);
	}
	
	/**
	 * 입력 스트림에서 N 바이트를 읽어 반환<br>
	 * 만일, 스트림의 바이트 크기가 N보다 작으면 전체를 다 읽어 들임
	 * 
	 * @param file 읽을 파일 객체
	 * @param n 읽을 바이트 수
	 * @param bufferSize 입력 스트림에서 데이터를 읽을 때 사용할 임시 버퍼의 크기
	 * @return 읽은 바이트 배열
	 */
	public static byte[] readNBytes(File file, int n, int bufferSize) throws Exception {
		
		if(file == null) {
			throw new NullPointerException("file is null.");
		}
		
		if(file.canRead() == false) {
			throw new IllegalAccessException("can't read file:" + file.getAbsolutePath());
		}
		
		// 파일에서 N바이트를 읽어서 반환
		try(FileInputStream fis = new FileInputStream(file)) {
			return readNBytes(fis, n, bufferSize);
		}
	}

	/**
	 * 입력 스트림에서 N 바이트를 읽어 반환<br>
	 * 만일, 스트림의 바이트 크기가 N보다 작으면 전체를 다 읽어 들임
	 * 
	 * @param is 읽을 입력 스트림
	 * @param n 읽을 바이트 수
	 * @return 읽은 데이터
	 */
	public static byte[] readNBytes(InputStream is, int n) throws Exception {
		return readNBytes(is, n, 1024 * 1024);
	}
	
	/**
	 * 입력 스트림에서 N 바이트를 읽어 반환<br>
	 * 만일, 스트림의 바이트 크기가 N보다 작으면 전체를 다 읽어 들임
	 * 
	 * @param is 읽을 입력 스트림
	 * @param n 읽을 바이트 수
	 * @param bufferSize 입력 스트림에서 데이터를 읽을 때 사용할 임시 버퍼의 크기
	 * @return 읽은 데이터
	 */
	public static byte[] readNBytes(InputStream is, int n, int bufferSize) throws Exception {
		
		// 읽을 바이트수(n)이 1보다 작으면 예외 발생
		if(n < 1) {
			throw new IllegalArgumentException("n is invalid:" + n);
		}
		
		// buffer의 크기가 1보다 작을 경우 예외 발생
		if(bufferSize < 1) {
			throw new IllegalArgumentException("buffer size is invalid:" + bufferSize);
		}
		
		// 모든 데이터 읽기 버퍼 변수
		byte[] readAll = new byte[0];
		
		// 입력 스트림이 null 일 경우 빈 버퍼 반환
		if(is == null) {
			return readAll;
		}

		// buffer 설정
		byte[] buffer = new byte[bufferSize];
		
		// input stream의 모든 데이터를 읽음
		int readCount = -1;
		int readSize = (n > buffer.length)?buffer.length:n;
		
		while(readSize > 0 && (readCount = is.read(buffer, 0, readSize)) != -1) {
			
			if(readCount == bufferSize) {
				readAll = concat(readAll, buffer);
			} else {
				readAll = concat(readAll, cut(buffer, 0, readCount));
			}
			
			// 다음 읽을 크기 재계산
			readSize = (n - readAll.length > buffer.length)?buffer.length:n - readAll.length;
		}
		
		return readAll;
	}
	
	/**
	 * 문자열을 byte 배열로 변환<br>
	 * ex) str:"1A03", order: OrderType.ASCEND -> byte[] {26, 3}<br>
	 *     str:"1A03", order: OrderType.DESCEND -> byte[] {3, 26}
	 * 
	 * @param str 문자열
	 * @param order 순서
	 * @return 변환된 byte 배열
	 */
	public static byte[] strToBytes(String str, OrderType order) throws Exception {
		
		// 입력값 검증
		if(str == null) {
			throw new NullPointerException("str is null.");
		}
		
		if(str.length() % 2 != 0) {
			throw new Exception("str must be even.");
		}
		
		// 변환된 byte 배열을 담을 변수
		byte[] bytes = new byte[str.length()/2];
		
		for(int index = 0; index < bytes.length; index++) {
			
			// 순서 설정(order)에 따른 저장 위치 변수
			int orderedIndex = index;
			if(order == OrderType.DESCEND) {
				orderedIndex = bytes.length - 1 - index;
			}
			
			// 상위 니블의 데이터를 가져옴
			byte b1 = getByte(str.charAt(index * 2));
			// 왼쪽으로 4 bit를 이동하여 상위 니블로 만듦
			b1 = (byte)(b1 << 4);
			
			// 하위 니블의 데이터를 가져옴
			byte b2 = getByte(str.charAt(index * 2 + 1));

			// 상위 니블(b1)과 하위니블(b2)를 합쳐서 저장
			bytes[orderedIndex] = (byte)(b1 + b2);
		}
		
		// 변환 결과를 반환
		return bytes;
	}
	
	/**
	 * 주어진 문자에 해당하는 byte를 반환하는 메소드
	 * 
	 * @param ch 문자
	 * @return 문자를 byte로 변환한 결과
	 */
	public static byte getByte(char ch) throws Exception {
		
		if(ch >= '0' && ch <= '9') {
			return (byte)(ch - '0');
		} else if(ch >= 'a' && ch <= 'z') {
			return (byte)(ch - 'a' + 10);
		} else if(ch >= 'A' && ch <= 'Z') {
			return (byte)(ch - 'A' + 10);
		} else {
			throw new Exception("Unexpected char:" + ch); 
		}
	}
	
	/**
	 * byte 배열을 문자열로 변환<br>
	 * ex) byte[] {26, 3}, order: OrderType.ASCEND -> "1A03"<br>
	 *     byte[] {26, 3}, order: OrderType.DESCEND -> "031A"<br>
	 * 
	 * @param bytes byte 배열
	 * @return 변환된 문자열
	 */
	public static String bytesToStr(byte[] bytes, OrderType order) throws Exception {
		
		if(bytes == null) {
			throw new NullPointerException("bytes is null.");
		}
		
		StringBuilder builder = new StringBuilder("");
		
		for(int index = 0; index < bytes.length; index++) {
			
			// 순서 설정(order)에 따른 참조 위치 변수
			int orderedIndex = index;
			if(order == OrderType.DESCEND) {
				orderedIndex = bytes.length - 1 - index;
			}
			
			// 문자열에 바이트 추가
			builder.append(String.format("%02X", bytes[orderedIndex]));
		}
		
		return builder.toString();
	}
	
	/**
	 * 
	 * 
	 * @param <T>
	 * @param bytes
	 * @param mappingClass
	 * @return
	 */
	public static <T> T mapping(byte[] bytes, Class<T> mappingClass) throws Exception {
		return BytesMapper.mapping(bytes, mappingClass);
	}
}

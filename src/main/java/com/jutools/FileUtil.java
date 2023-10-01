package com.jutools;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * File 처리 관련 Utility 클래스
 * 
 * @author jmsohn
 */
public class FileUtil {
	
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
				readAll = BytesUtil.concat(readAll, buffer);
			} else {
				readAll = BytesUtil.concat(readAll, BytesUtil.cut(buffer, 0, readCount));
			}
		}
		
		return readAll;
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
				readAll = BytesUtil.concat(readAll, buffer);
			} else {
				readAll = BytesUtil.concat(readAll, BytesUtil.cut(buffer, 0, readCount));
			}
			
			// 다음 읽을 크기 재계산
			readSize = (n - readAll.length > buffer.length)?buffer.length:n - readAll.length;
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
}

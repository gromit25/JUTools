package com.jutools;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * File 처리 관련 Utility 클래스
 * 
 * @author jmsohn
 */
public class FileUtil {
	
	/**
	 * 파일 확장자별 매직넘버 맵핑 목록<br>
	 * key - 파일확장자(ex: .pdf(점 포함))<br>
	 * value - 매직넘버 목록, 하나의 파일 확장자에 여러 매직넘버가 있을 수 있음
	 */
	private static HashMap<String, ArrayList<byte[]>> magicMap;
	
	static {
		
		// 파일 확장자별 매직넘버 맵핑 목록 초기화
		magicMap = new HashMap<>();
		
		// .rtf
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x7B, (byte)0x5C, (byte)0x71, (byte)0x74, (byte)0x66, (byte)0x31});
			magics.add(new byte[]{(byte)0x7B, (byte)0x5C, (byte)0x72, (byte)0x74, (byte)0x66, (byte)0x31});

			magicMap.put(".rtf", magics);
		}
		
		// .xls
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0xD0, (byte)0xCF, (byte)0x11, (byte)0xE0, (byte)0xA1, (byte)0xB1, (byte)0x1A, (byte)0xE1});

			magicMap.put(".xls", magics);
		}
		
		// .xlsx
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04});
			
			magicMap.put(".xlsx", magics);
		}
		
		// .xlt
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0xD0, (byte)0xCF, (byte)0x11, (byte)0xE0, (byte)0xA1, (byte)0xB1, (byte)0x1A, (byte)0xE1});

			magicMap.put(".xlt", magics);
		}
		
		// .xltx
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04});
			
			magicMap.put(".xltx", magics);
		}
		
		// .xltm
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04});
			
			magicMap.put(".xltm", magics);
		}
		
		// .ppt
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0xD0, (byte)0xCF, (byte)0x11, (byte)0xE0, (byte)0xA1, (byte)0xB1, (byte)0x1A, (byte)0xE1});

			magicMap.put(".ppt", magics);
		}
		
		// .pptx
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04});
			
			magicMap.put(".pptx", magics);
		}
		
		// .doc
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0xD0, (byte)0xCF, (byte)0x11, (byte)0xE0, (byte)0xA1, (byte)0xB1, (byte)0x1A, (byte)0xE1});

			magicMap.put(".doc", magics);
		}
		
		// .docx
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04});

			magicMap.put(".docx", magics);
		}
		
		// .docm
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04});

			magicMap.put(".docm", magics);
		}
		
		// .dot
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0xD0, (byte)0xCF, (byte)0x11, (byte)0xE0, (byte)0xA1, (byte)0xB1, (byte)0x1A, (byte)0xE1});

			magicMap.put(".dot", magics);
		}
		
		// .dotx
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04});

			magicMap.put(".dotx", magics);
		}
		
		// .dotm
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04});

			magicMap.put(".dotx", magics);
		}
		
		// .hwp
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0xD0, (byte)0xCF, (byte)0x11, (byte)0xE0, (byte)0xA1, (byte)0xB1, (byte)0x1A, (byte)0xE1});

			magicMap.put(".hwp", magics);
		}
		
		// .hwpx
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x4D, (byte)0x53, (byte)0x5F, (byte)0x56});

			magicMap.put(".hwp", magics);
		}
		
		// .pdf
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x25, (byte)0x50, (byte)0x44, (byte)0x46, (byte)0x2D});

			magicMap.put(".pdf", magics);
		}
		
		// .jpg
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0xFF, (byte)0xD8, (byte)0xFF, (byte)0xE1});

			magicMap.put(".jpg", magics);
		}
		
		// .jpeg
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0xFF, (byte)0xD8, (byte)0xFF, (byte)0xFE, (byte)0x00});

			magicMap.put(".jpeg", magics);
		}
		
		// .tiff
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x49, (byte)0x49, (byte)0x2A});
			magics.add(new byte[]{(byte)0x4D, (byte)0x4D, (byte)0x2A});

			magicMap.put(".tiff", magics);
		}
		
		// .gif
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x47, (byte)0x49, (byte)0x46, (byte)0x38, (byte)0x37, (byte)0x61});
			magics.add(new byte[]{(byte)0x47, (byte)0x49, (byte)0x46, (byte)0x38, (byte)0x39, (byte)0x61});

			magicMap.put(".gif", magics);
		}
		
		// .bmp
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x42, (byte)0x4D});

			magicMap.put(".bmp", magics);
		}
		
		// .png
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x89, (byte)0x50, (byte)0x4E, (byte)0x47, (byte)0x0D, (byte)0x0A, (byte)0x1A, (byte)0x0A});

			magicMap.put(".png", magics);
		}

		// .mp3
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x49, (byte)0x44, (byte)0x33, (byte)0x03});

			magicMap.put(".mp3", magics);
		}
		
		// .mov
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x6D, (byte)0x64, (byte)0x61, (byte)0x74});

			magicMap.put(".mov", magics);
		}
		
		// .avi
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x41, (byte)0x56, (byte)0x49, (byte)0x20});
			magics.add(new byte[]{(byte)0x52, (byte)0x49, (byte)0x46, (byte)0x46});
			magics.add(new byte[]{(byte)0x52, (byte)0x49, (byte)0x46, (byte)0x46, (byte)0x6A, (byte)0x42, (byte)0x01, (byte)0x00});

			magicMap.put(".avi", magics);
		}
		
		// .mpeg
		{
			ArrayList<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x00, (byte)0x00, (byte)0x01, (byte)0xB3});

			magicMap.put(".mpeg", magics);
		}
	}
	
	/**
	 * 확장자의 매직넘버를 검사할 수 있는지 여부 반환
	 * 
	 * @param ext 검사할 확장자("." 포함, ex: ".jpg")
	 * @return 검사가능 여부
	 */
	public static boolean isMagicNumberCheckableExtension(String ext) {
		return magicMap.containsKey(ext);
	}
	
	/**
	 * 확장자의 매직넘버를 검사할 수 있는지 여부 반환
	 * 
	 * @param file 확장자 검사할 파일
	 * @return 검사가능 여부
	 */
	public static boolean isMagicNumberCheckableExtension(File file) {
		
		// file이 null 일 경우, false 반환
		if(file == null) {
			return false;
		}
		
		// 읽을 수 없을 경우, false 반환
		if(file.canRead() == false) {
			return false;
		}
		
		// 파일 이름과 확장자 분리
		String[] nameAndExt = null;
		try {
			nameAndExt = StringUtil.splitLast(file.getName(), "\\.");
		} catch(Exception ex) {
			return false;
		}
		
		// 파일에 확장자가 없는 경우
		if(nameAndExt.length != 2) {
			return false;
		}
		
		// 확장자가 목록에 있는지 확인하여 반환
		String ext = "." + nameAndExt[1];
		return magicMap.containsKey(ext);
	}
	
	/**
	 * 파일의 확장자와 일치하는 매직 넘버가 있는 검사
	 * 
	 * @param file 검사할 파일
	 * @return 일치하는 매직 넘버가 있는지 여부
	 */
	public static boolean checkMagicNumber(File file) throws Exception {
		
		// 입력값 검증
		if(file == null) {
			throw new NullPointerException("file is null.");
		}
		
		if(file.canRead() == false) {
			throw new IllegalArgumentException("can't read file:" + file.getAbsolutePath());
		}
		
		// 파일 이름과 확장자 분리
		String[] nameAndExt = StringUtil.splitLast(file.getName(), "\\.");
		
		// 파일에 확장자가 없는 경우
		if(nameAndExt.length != 2) {
			throw new IllegalArgumentException("extension is not found:" + file.getAbsolutePath());
		}
		
		// 확장자가 목록에 있는지 확인
		String ext = "." + nameAndExt[1];
		if(magicMap.containsKey(ext) == false) {
			throw new IllegalArgumentException("extension is not checkable:" + file.getAbsolutePath());
		}
		
		// 파일의 앞부분 중 256 바이트 만큼 읽음
		// 파일의 크기가 256 바이트 이하이면 모두 읽음
		byte[] head = readNBytes(file, 256);
		
		// 파일의 매직 넘버 검사
		ArrayList<byte[]> magicNumbers = magicMap.get(ext);
		for(byte[] magicNumber: magicNumbers) {
			
			// 일치하는 매직 넘버가 있는 경우, true를 반환
			if(BytesUtil.startsWith(head, magicNumber) == true) {
				return true;
			}
		}
		
		// 일치하는 매직 넘버가 하나도 없을 경우, false를 반환 
		return false;
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

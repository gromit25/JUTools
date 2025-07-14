package com.jutools;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jutools.common.BoolType;

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
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x7B, (byte)0x5C, (byte)0x71, (byte)0x74, (byte)0x66, (byte)0x31});
			magics.add(new byte[]{(byte)0x7B, (byte)0x5C, (byte)0x72, (byte)0x74, (byte)0x66, (byte)0x31});

			magicMap.put(".rtf", magics);
		}
		
		// .xls
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0xD0, (byte)0xCF, (byte)0x11, (byte)0xE0, (byte)0xA1, (byte)0xB1, (byte)0x1A, (byte)0xE1});

			magicMap.put(".xls", magics);
		}
		
		// .xlsx
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04});
			
			magicMap.put(".xlsx", magics);
		}
		
		// .xlt
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0xD0, (byte)0xCF, (byte)0x11, (byte)0xE0, (byte)0xA1, (byte)0xB1, (byte)0x1A, (byte)0xE1});

			magicMap.put(".xlt", magics);
		}
		
		// .xltx
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04});
			
			magicMap.put(".xltx", magics);
		}
		
		// .xltm
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04});
			
			magicMap.put(".xltm", magics);
		}
		
		// .ppt
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0xD0, (byte)0xCF, (byte)0x11, (byte)0xE0, (byte)0xA1, (byte)0xB1, (byte)0x1A, (byte)0xE1});

			magicMap.put(".ppt", magics);
		}
		
		// .pptx
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04});
			
			magicMap.put(".pptx", magics);
		}
		
		// .doc
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0xD0, (byte)0xCF, (byte)0x11, (byte)0xE0, (byte)0xA1, (byte)0xB1, (byte)0x1A, (byte)0xE1});

			magicMap.put(".doc", magics);
		}
		
		// .docx
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04});

			magicMap.put(".docx", magics);
		}
		
		// .docm
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04});

			magicMap.put(".docm", magics);
		}
		
		// .dot
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0xD0, (byte)0xCF, (byte)0x11, (byte)0xE0, (byte)0xA1, (byte)0xB1, (byte)0x1A, (byte)0xE1});

			magicMap.put(".dot", magics);
		}
		
		// .dotx
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04});

			magicMap.put(".dotx", magics);
		}
		
		// .dotm
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x50, (byte)0x4B, (byte)0x03, (byte)0x04});

			magicMap.put(".dotx", magics);
		}
		
		// .hwp
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0xD0, (byte)0xCF, (byte)0x11, (byte)0xE0, (byte)0xA1, (byte)0xB1, (byte)0x1A, (byte)0xE1});

			magicMap.put(".hwp", magics);
		}
		
		// .hwpx
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x4D, (byte)0x53, (byte)0x5F, (byte)0x56});

			magicMap.put(".hwp", magics);
		}
		
		// .pdf
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x25, (byte)0x50, (byte)0x44, (byte)0x46, (byte)0x2D});

			magicMap.put(".pdf", magics);
		}
		
		// .jpg
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0xFF, (byte)0xD8, (byte)0xFF, (byte)0xE1});

			magicMap.put(".jpg", magics);
		}
		
		// .jpeg
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0xFF, (byte)0xD8, (byte)0xFF, (byte)0xFE, (byte)0x00});

			magicMap.put(".jpeg", magics);
		}
		
		// .tiff
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x49, (byte)0x49, (byte)0x2A});
			magics.add(new byte[]{(byte)0x4D, (byte)0x4D, (byte)0x2A});

			magicMap.put(".tiff", magics);
		}
		
		// .gif
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x47, (byte)0x49, (byte)0x46, (byte)0x38, (byte)0x37, (byte)0x61});
			magics.add(new byte[]{(byte)0x47, (byte)0x49, (byte)0x46, (byte)0x38, (byte)0x39, (byte)0x61});

			magicMap.put(".gif", magics);
		}
		
		// .bmp
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x42, (byte)0x4D});

			magicMap.put(".bmp", magics);
		}
		
		// .png
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x89, (byte)0x50, (byte)0x4E, (byte)0x47, (byte)0x0D, (byte)0x0A, (byte)0x1A, (byte)0x0A});

			magicMap.put(".png", magics);
		}

		// .mp3
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x49, (byte)0x44, (byte)0x33, (byte)0x03});

			magicMap.put(".mp3", magics);
		}
		
		// .mov
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x6D, (byte)0x64, (byte)0x61, (byte)0x74});

			magicMap.put(".mov", magics);
		}
		
		// .avi
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x41, (byte)0x56, (byte)0x49, (byte)0x20});
			magics.add(new byte[]{(byte)0x52, (byte)0x49, (byte)0x46, (byte)0x46});
			magics.add(new byte[]{(byte)0x52, (byte)0x49, (byte)0x46, (byte)0x46, (byte)0x6A, (byte)0x42, (byte)0x01, (byte)0x00});

			magicMap.put(".avi", magics);
		}
		
		// .mpeg
		{
			List<byte[]> magics = new ArrayList<>();
			magics.add(new byte[]{(byte)0x00, (byte)0x00, (byte)0x01, (byte)0xB3});

			magicMap.put(".mpeg", magics);
		}
	}
	
	/**
	 * 파일의 확장자와 일치하는 매직 넘버가 있는 검사
	 * 
	 * @param file 검사할 파일
	 * @return 일치하는 매직 넘버가 있는지 여부
	 */
	public static BoolType checkMagicNumber(File file) throws Exception {
		
		// 입력값 검증
		if(file == null) {
			throw new NullPointerException("file is null.");
		}
		
		if(file.canRead() == false) {
			throw new IllegalArgumentException("can't read file:" + file.getAbsolutePath());
		}
		
		// 확장자가 목록에 있는지 확인, 없을 경우 unknown 반환
		String ext = getExt(file);
		if(magicMap.containsKey(ext) == false) {
			return BoolType.UNKNOWN;
		}
		
		// 파일의 앞부분 중 256 바이트 만큼 읽음
		// 파일의 크기가 256 바이트 이하이면 모두 읽음
		byte[] head = readNBytes(file, 256);
		
		// 파일의 매직 넘버 검사
		ArrayList<byte[]> magicNumbers = magicMap.get(ext);
		for(byte[] magicNumber: magicNumbers) {
			
			// 일치하는 매직 넘버가 있는 경우, true를 반환
			if(BytesUtil.startsWith(head, magicNumber) == true) {
				return BoolType.TRUE;
			}
		}
		
		// 일치하는 매직 넘버가 하나도 없을 경우, false를 반환 
		return BoolType.FALSE;
	}
	
	/**
	 * 파일명과 확장자를 분리하여 반환함<br>
	 * ex) C:\data\test.txt -> {"C:\data\test", "txt"}
	 * 
	 * @param fileName 전체 파일명
	 * @return 파일명, 확장자
	 */
	public String[] splitFileNameAndExt(String fileName) {
		
		// 입력값 검증
		if(StringUtil.isEmpty(fileName) == true) {
			return new String[] {"", ""};
		}
		
		try {
			
			// 파일 이름과 확장자 분리
			String[] nameAndExt = StringUtil.splitLast(fileName, "\\.");
			
			// 파일에 확장자가 없는 경우
			if(nameAndExt.length == 2) {
				return nameAndExt;
			} else {
				return new String[] {fileName, ""};
			}
			
		} catch(Exception ex) {
			return new String[] {"", ""};
		}
	}
	
	/**
	 * 파일명과 확장자를 분리하여 반환함<br>
	 * ex) C:\data\test.txt -> {"C:\data\test", "txt"}
	 * 
	 * @param file 파일
	 * @return 파일명, 확장자
	 */
	public String[] splitFileNameAndExt(File file) {
		
		// 입력값 검증
		if(file == null) {
			return new String[] {"", ""};
		}
		
		return splitFileNameAndExt(file.getAbsolutePath());
	}
	
	/**
	 * 파일명의 확장자를 반환<br>
	 * 반환시, "." 포함, ex) "test.jpg" -> ".jpg"<br>
	 * file이 null이거나 확장자가 없는 경우, "" 반환
	 * 
	 * @param fileName 파일명
	 * @return 확장자
	 */
	public static String getExt(String fileName) {
		
		// 입력값 검증
		if(StringUtil.isEmpty(fileName) == true) {
			return "";
		}
		
		try {
			
			// 파일 이름과 확장자 분리
			String[] nameAndExt = StringUtil.splitLast(fileName, "\\.");
			
			// 파일에 확장자가 없는 경우
			if(nameAndExt.length != 2) {
				return "";
			}
			
			// 확장자를 반환
			return "." + nameAndExt[1];
			
		} catch(Exception ex) {
			return "";
		}
	}
	
	/**
	 * 파일의 확장자를 반환<br>
	 * 반환시, "." 포함, ex) "test.jpg" -> ".jpg"<br>
	 * file이 null이거나 확장자가 없는 경우, "" 반환
	 * 
	 * @param file 확장자를 가져올 파일
	 * @return 확장자
	 */
	public static String getExt(File file) {
		
		// 입력값 검증
		if(file == null) {
			return "";
		}
		
		return getExt(file.getName()); 
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
	
	/**
	 * stop 파일이 생성되거나 업데이트 될때까지 대기
	 *
	 * @param stopFile stop 파일
	 * @param pollingPeriod 폴링 시간
	 */
	public static void waitForFileTouched(File stopFile, long pollingPeriod) throws Exception {

		// 입력값 검증
		if(stopFile == null) {
			throw new IllegalArgumentException("stop file is null.");
		}
		
		if(stopFile.getParentFile() == null) {
			throw new IllegalArgumentException("stop file's parent is null.");
		}

		if(pollingPeriod < 100L) {
			throw new IllegalArgumentException("polling period must be greater than 100(0.1 sec): " + pollingPeriod);
		}

		// stop 파일명
		String stopFileName = stopFile.getName();
		
		// 파일 생성 및 업데이트 이벤트 수신을 위한 Watch 서비스 생성 및 등록
		Path parentPath = stopFile.toPath().getParent();
		WatchService parentWatchService = parentPath.getFileSystem().newWatchService();
		parentPath.register(parentWatchService
			, StandardWatchEventKinds.ENTRY_CREATE
			, StandardWatchEventKinds.ENTRY_MODIFY
			);

		while(true) {
			
			// WatchKey에 이벤트 들어올 때까지 대기
			WatchKey watchKey = parentWatchService.poll(pollingPeriod, TimeUnit.MILLISECONDS);
			if(watchKey == null) {
				continue;
			}

			try {
				
				// 생성 및 업데이트 이벤트 목록 확인
				List<WatchEvent<?>> eventList = watchKey.pollEvents();
				for (WatchEvent<?> event : eventList) {
			
					WatchEvent.Kind<?> kind = event.kind();   // 이벤트 종류
					Path eventFile = (Path) event.context();  // 이벤트가 발생한 파일

					// 파일이 새로 생성되거나 업데이트(ex. touch 명령어)되면 대기 중지
					if(kind == StandardWatchEventKinds.ENTRY_CREATE || kind == StandardWatchEventKinds.ENTRY_MODIFY) {
						if(stopFileName.equals(eventFile.toFile().getName()) == true) {
							return;
						}
					}
				}
				
			} finally {
				// WatchKey 초기화 꼭 해줘야 함
				watchKey.reset();
			}
		} // End of while
	}
	
	/**
	 * stop 파일이 생성되거나 업데이트 될때까지 대기
	 * polling period = 1초
	 *
	 * @param stopFile stop 파일
	 */
	public static void waitForFileTouched(File stopFile) throws Exception {
		waitForFileTouched(stopFile, 1000L);
	}
}

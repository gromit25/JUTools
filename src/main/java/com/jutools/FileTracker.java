package com.jutools;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import lombok.Getter;
import lombok.Setter;

/**
 * 파일의 변경사항에 대해 추적(Tracking)하는 클래스<br>
 * -> tail -f 와 같은 기능임
 * 
 * @author jmsohn
 */
public class FileTracker {
	
	// config 값
	/** 파일 경로 */
	private String pathStr;
	/** line 구분자 */
	private String lineSeparator = "\n";
	/** buffer의 크기 */
	private int bufferSize = 1024 * 1024;
	/** 파일을 읽을 때, 문자 인코딩 방식 */
	private Charset charset = Charset.defaultCharset();
	/** tracking polling time */
	private long pollingTime = 10;
	/** tracking polling time unit */
	private TimeUnit pollingTimeUnit = TimeUnit.SECONDS;
	/** tracking 중지 여부 */
	@Getter
	@Setter
	private boolean stop = false;

	// 내부 변수
	/** 트렉킹할 목표 파일의 경로 */
	private Path path;
	/** 파일 변경 사항을 확인하기 위한 watchService */
	private WatchService watchService;

	/**
	 * 생성자
	 * 
	 * @param pathStr 트레킹할 파일명
	 */
	protected FileTracker(String pathStr) throws Exception {

		// 입력값 검증
		if(StringUtil.isEmpty(pathStr) == true) {
			throw new Exception("path is not set");
		}
		
		// 타겟 파일 Path 객체 생성
		this.pathStr = pathStr;
		this.path = Paths.get(this.pathStr);

		// 타겟 파일이 있는 디렉토리의 Path 객체 생성
		Path parentPath = this.path.getParent();
		if(parentPath.toFile().exists() == false) {
        	throw new Exception("directory is not exists:" + parentPath.toFile().getAbsolutePath());
		}

		/* watchService 생성 */
		this.watchService =  parentPath.getFileSystem().newWatchService();

		// target을 watchService에 등록
		path.register(this.watchService
				, StandardWatchEventKinds.ENTRY_CREATE
				, StandardWatchEventKinds.ENTRY_DELETE
				, StandardWatchEventKinds.ENTRY_MODIFY);
	}
	
	/**
	 * FileTracker 생성 메소드
	 * 
	 * @param pathStr 트레킹할 파일명
	 * @return 생성된 FileTracker 
	 */
	public static FileTracker create(String pathStr) throws Exception {
		return new FileTracker(pathStr);
	}
	
	/**
	 * 파일의 변경사항에 대해 추적
	 * 
	 * @param action 변경사항을 처리할 Consumer 객체
	 */
	public void tracking(Consumer<String> action) throws Exception {
		
		// 모니터링할 파일을 읽기 위한 파일 채널 변수
		FileChannel readChannel = null;
	    
		// 파일이 이미 존재하면 채널을 생성함
		// 주의) InputStream이나 Reader로 읽으면 안됨 -> 파일에 Write Lock이 걸림
		if(this.path.toFile().exists() == true) {

			readChannel = FileChannel.open(this.path, StandardOpenOption.READ);

			// 파일을 읽기 시작하는 위치를
			// 파일의 마지막으로 설정함
			long offset = this.path.toFile().length();
			readChannel.position(offset);
		}
		
		// 읽은 데이터를 저장할 데이터 버퍼
		ByteBuffer readBuffer = ByteBuffer.allocateDirect(this.bufferSize);
		
		// 중지 설정이 될때까지 반복
		while(this.isStop() == false) {

			// WatchKey에 이벤트 들어올 때 까지 대기
			WatchKey watchKey = this.watchService.poll(this.pollingTime, this.pollingTimeUnit);
			
			if(this.isStop() == true) {
				break;
			}
			
			if(watchKey == null) {
				continue;
			}
	
			/* 이벤트 목록에 현재 tracking 파일이 있으면, 파일을 읽음 */
			List<WatchEvent<?>> events = watchKey.pollEvents();
			for (WatchEvent<?> event : events) {

				WatchEvent.Kind<?> kind = event.kind(); // 이벤트 종류
				Path eventFile = (Path) event.context(); // 이벤트가 발생한 파일

				// 이벤트 발생 파일이 tracking 파일(this.path)이 아닐 경우 다음 이벤트 처리
				String eventFileName = eventFile.toFile().getName();
				if(eventFileName.equals(this.path.toFile().getName()) == false) {
					continue;
				}

				// 새로 파일이 생성된 경우(원본 파일은 이름이 변경되고, 파일이 새로 생성될 경우),
				// -> 기존 채널을 닫고, 새로 생성함
				if(kind == StandardWatchEventKinds.ENTRY_CREATE) {

					// 기존 채널 닫음
					if (readChannel != null) {
						readChannel.close();
						readChannel = null;
					}

					// 새로 채널을 오픈함
					readChannel = FileChannel.open(this.path, StandardOpenOption.READ);
				}

				/* 데이터 읽기 및 처리 */
				// 끝나지 않은 데이터 임시 저장
				byte[] temp = null;

				// 더이상 읽을 바이트가 없을 때까지 반복
				while ((readChannel.read(readBuffer)) != -1) {

					NIOBufferUtil.flip(readBuffer);

					/* 지정한 bufferSize 만큼 받은 데이터를 lineSeparator 로 잘라 한 문장씩 처리
					   데이터가 끝나지 않은 경우 임시 저장 후 다음 데이터 앞에 붙임 */

					// ByteBuffer -> ByteArray
					byte[] buffer = new byte[readBuffer.remaining()];
					readBuffer.get(buffer);

					// 데이터 끝에 lineSeparator가 있는지 확인
					boolean isEndsWithLineSeparator = BytesUtil.endsWith(buffer, this.lineSeparator.getBytes());

					// lineSeparator로 데이터를 한 문장씩 split함
					ArrayList<byte[]> messages = BytesUtil.split(buffer, this.lineSeparator.getBytes());
					for(int index = 0; index < messages.size(); index++) {

						byte[] message = messages.get(index);

						// 임시 저장된 데이터가 있을 경우
						// 데이터 합친 뒤 임시 저장 초기화
						if(index == 0 && temp != null) {
							message = BytesUtil.concat(temp, message);
							temp = null;
						}

						// lineSeparator 를 통해 잘린 데이터는
						// 날짜 포맷 후 logMessageArr 에 추가
						if(index != messages.size() - 1) {
							action.accept(new String(message, this.charset));
						} else {
							if(isEndsWithLineSeparator == true) {
								action.accept(new String(message, this.charset));
							} else {
								// 데이터가 끝나지 않았을 경우 임시 저장
								temp = message;
							}
						}
					}
                    
					NIOBufferUtil.clear(readBuffer);
					
                } // End of while readChannel
				
			} // End of for events
			
			// 다음 이벤트를 얻기 위해 reset 수행함
			watchKey.reset();
			
		} // End of while
	}
	
	/**
	 * line 구분자 설정
	 * 
	 * @param lineSeparator line 구분자
	 * @return
	 */
	public FileTracker setLineSeparator(String lineSeparator) throws Exception {
		
		if(lineSeparator == null) {
			throw new NullPointerException("line separator is null");
		}
		
		this.lineSeparator = lineSeparator;
		return this;
	}
	
	/**
	 * 버퍼의 크기 설정
	 * 
	 * @param bufferSize 설정할 버퍼 크기
	 * @return 현재 객체
	 */
	public FileTracker setBufferSize(int bufferSize) throws Exception {
		
		if(bufferSize <= 0) {
			throw new Exception("buffer size is invalid:" + bufferSize);
		}
		
		this.bufferSize = bufferSize;
		return this;
	}
	
	/**
	 * 파일을 읽을때 사용할 charset 설정
	 * 
	 * @param cs 파일을 읽을때 사용할 charset
	 * @return 현재 객체
	 */
	public FileTracker setCharset(Charset cs) throws Exception {
		
		if(cs == null) {
			throw new NullPointerException("charset is null");
		}
		
		this.charset = cs;
		return this;
	}

}

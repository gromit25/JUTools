package com.jutools.filetracker;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.jutools.NIOBufferUtil;
import com.jutools.filetracker.trimmer.LineSplitTrimmer;

import lombok.Getter;
import lombok.Setter;

/**
 * 파일의 변경사항에 대해 추적(Tracking)하는 클래스<br>
 * -> tail -f 와 같은 기능임
 * 
 * @author jmsohn
 */
public class FileTracker<T> {
	
	// config 값
	
	/** 끊어읽기 reader */
	private Trimmer<T> trimmer;
	
	/** buffer의 크기 */
	private int bufferSize = 1024 * 1024;
	
	/** tracking polling time */
	private long pollingTime = 10;
	
	/** tracking polling time unit */
	private TimeUnit pollingTimeUnit = TimeUnit.SECONDS;
	
	/** tracking 중지 여부 */
	@Getter
	@Setter
	private volatile boolean stop = false;

	// 내부 변수
	/** 트렉킹할 목표 파일의 경로 */
	@Getter
	private Path path;
	
	/** 파일 변경 사항을 확인하기 위한 watchService */
	private WatchService watchSvc;

	
	/**
	 * 생성자
	 * 
	 * @param file 트레킹할 파일
	 * @param trimmer
	 */
	protected FileTracker(File file, Trimmer<T> trimmer) throws Exception {

		// 입력값 검증
		if(file == null) {
			throw new NullPointerException("'file' is null.");
		}
		
		if(trimmer == null) {
			throw new IllegalArgumentException("'trimmer' is null.");
		}
		
		// 타겟 파일 Path 객체 생성
		this.path = file.toPath();

		// 타겟 파일이 있는 디렉토리의 Path 객체 생성
		Path parentPath = this.path.getParent();
		if(parentPath.toFile().exists() == false) {
        	throw new Exception("directory is not exists:" + parentPath.toFile().getAbsolutePath());
		}

		// 리더 설정
		this.trimmer = trimmer;

		// watchService 생성
		this.watchSvc =  parentPath.getFileSystem().newWatchService();

		// target을 watchService에 등록
		parentPath.register(
			this.watchSvc
			, StandardWatchEventKinds.ENTRY_CREATE
			, StandardWatchEventKinds.ENTRY_DELETE
			, StandardWatchEventKinds.ENTRY_MODIFY
		);
		
	}
	
	/**
	 * FileTracker 생성 메소드
	 * 
	 * @param file 트레킹할 파일
	 * @return 생성된 FileTracker 
	 */
	public static FileTracker<String> create(File file) throws Exception {
		return new FileTracker<String>(file, new LineSplitTrimmer());
	}
	
	/**
	 * FileTracker 생성 메소드
	 * 
	 * @param file 트레킹할 파일
	 * @param reader 끊어읽기 reader
	 * @return 생성된 FileTracker
	 */
	public static <T> FileTracker<T> create(File file, Trimmer<T> reader) throws Exception {
		return new FileTracker<T>(file, reader);
	}
	
	/**
	 * FileTracker 생성 메소드
	 * 
	 * @param fileName 트레킹할 파일명
	 * @return 생성된 FileTracker 
	 */
	public static FileTracker<String> create(String fileName) throws Exception {
		return create(new File(fileName));
	}
	
	/**
	 * 파일의 변경사항에 대해 추적
	 * 
	 * @param action 변경사항을 처리할 Consumer 객체
	 */
	public void tracking(Consumer<T> action) throws Exception {
		
		// 모니터링할 파일을 읽기 위한 파일 채널 변수
		FileChannel readChannel = null;
	    
		try {
			
			// 파일이 이미 존재하면 채널을 생성함
			// 주의) InputStream이나 Reader로 읽으면 안됨 -> 파일에 Write Lock이 걸림
			if(this.path.toFile().exists() == true) {
	
				readChannel = FileChannel.open(this.path, StandardOpenOption.READ);
	
				// 파일을 읽기 시작하는 위치를
				// 파일의 마지막으로 설정함
				long offset = this.path.toFile().length();
				readChannel.position(offset);
			}
			
			// 읽은 데이터를 저장할 데이터 버퍼 변수
			ByteBuffer readBuffer = ByteBuffer.allocateDirect(this.bufferSize);
			
			// 중지 설정이 될때까지 반복
			while(this.stop == false) {
	
				// WatchKey에 이벤트 들어올 때 까지 대기
				WatchKey watchKey = this.watchSvc.poll(this.pollingTime, this.pollingTimeUnit);
				
				if(this.stop == true) {
					break;
				}
				
				if(watchKey == null) {
					continue;
				}
		
				try {
					
					// 이벤트 목록에 현재 tracking 파일이 있으면, 파일을 읽음
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
						// 더이상 읽을 바이트가 없을 때까지 반복
						while ((readChannel.read(readBuffer)) != -1) {
		
							NIOBufferUtil.flip(readBuffer);
		
							/*
							 지정한 bufferSize 만큼 받은 데이터를 끊어읽기 수행
							 데이터가 끝나지 않은 경우 임시 저장 후 다음 데이터 앞에 붙임
							*/
		
							// ByteBuffer -> ByteArray
							byte[] buffer = new byte[readBuffer.remaining()];
							readBuffer.get(buffer);
		
							// reader 끊어 읽기 수행
							this.trimmer.trim(buffer, action);
		                    
							NIOBufferUtil.clear(readBuffer);
							
						} // End of while readChannel
						
					} // End of for events
					
				} finally {
					// 다음 이벤트를 얻기 위해 reset 수행함
					watchKey.reset();
				}
				
			} // End of while
			
		} finally {
			
			// close 수행
			if(readChannel != null && readChannel.isOpen() == true) {
				readChannel.close();
			}
		}
	}
	
	/**
	 * 버퍼의 크기 설정
	 * 
	 * @param bufferSize 설정할 버퍼 크기
	 * @return 현재 객체
	 */
	public FileTracker<T> setBufferSize(int bufferSize) throws Exception {
		
		if(bufferSize <= 0) {
			throw new Exception("buffer size is invalid:" + bufferSize);
		}
		
		this.bufferSize = bufferSize;
		
		return this;
	}
}

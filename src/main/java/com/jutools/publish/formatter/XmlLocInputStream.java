package com.jutools.publish.formatter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * xml inputstream 클래스<br>
 * -> xml의 현재 parsing 위치 확인용으로 Formatter에 위치를 설정함<br>
 * &nbsp;&nbsp;&nbsp;FormatterException 발생시 발생위치 표시를 위함
 * 
 * @author jmsohn
 */
public class XmlLocInputStream extends InputStream {
	
	/** xml 읽기 위한 버퍼의 크기 */
	private static int BUFFER_SIZE = 1024;
	
	/** xml 파싱 상태 목록 */
	private enum ParsingStatus {
		/** xml 내부의 text 문자열 */
		CONTENTS,
		/** 테그 시작 */
		TAG_START,
		/** 테그 명 */
		TAG_NAME,
		/** 테그 내 tag명 이후의 속성등 */
		TAG_CONTENTS,
		/** 테그 종료 */
		TAG_END,
		/** 싱글 쿼터(') 문자열 시작 */
		QUOTO_STRING,
		/** 싱글 쿼터(') 이스케이프 */
		QUOTO_ESCAPE,
		/** 더블 쿼터(") 문자열 시작 */
		D_QUOTO_STRING,
		/** 더블 쿼터(") 이스케이프 */
		D_QUOTO_ESCAPE
	}
	
	/** 테그 시작 위치 */
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private Loc startTagLoc;
	
	/** 라인 번호 */
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private int lineNum;
	
	/** xml의 input stream */
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private InputStream orgInputStream;

	/** 라인의 컬럼을 읽는 버퍼 */
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private ByteBuffer columnBuffer;
	
	/** 테그명 버퍼 */
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private ByteBuffer tagNameBuffer;

	/** 현재 파싱 상태 */
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private ParsingStatus status;

	/**
	 * 테그별 위치정보(Loc)<br>
	 * ex) "PrintFomatter" -> {Loc-1, Loc-2, Loc-3}<br>
	 * <br>
	 * 테그별 위치정보를 저장하는 이유는 SAX 파서 파싱 후 callback되는 시점과<br>
	 * InputStream에서 읽어가는 시점이 달라서, 테그별로 위치를 임시 저장함<br>
	 * 다만, 호출순서는 먼저 나온것이 먼저 호출되는 구조라 Queue 형태로 만듦
	 */
	private Map<String, Queue<Loc>> tagMap;
	
	/**
	 * 생성자
	 * 
	 * @param is
	 */
	public XmlLocInputStream(InputStream is) {
		
		this.setOrgInputStream(is);
		this.setLineNum(1);
		this.setColumnBuffer(ByteBuffer.allocate(BUFFER_SIZE));
		this.setTagNameBuffer(ByteBuffer.allocate(BUFFER_SIZE));
		this.setStatus(ParsingStatus.CONTENTS);
	}

	@Override
	public int read() throws IOException {
		
		// XML 스트림에서 한문자를 읽어옴
		int read = this.getOrgInputStream().read();
		
		// 읽어온 문자가 0 이상일 경우 처리함
		// -> 0 미만은 Stream 종료를 의미하기 때문에 처리하지 않음
		if(read >= 0) {
			
			if('\n' == (char)read) {
				
				// 새로운 라인 시작일 경우,
				// line number를 하나 올리고
				// buffer와 임시컬럼길이 변수(tempColumnNum)를 초기화함
				this.setLineNum(this.getLineNum() + 1);
				this.getColumnBuffer().clear();
				
			} else {
				
				// 새로운 라인이 아닐 경우,
				// parsing status를 업데이트 함
				this.updateParsingStatus(read);
				
				// buffer에 현재 읽은 내용을 추가함
				// -> 추후 column 수를 계산하기 위함임
				this.putToColumnBuffer((byte)read);
			}
			
		}
		
		return read;
	}
	
	/**
	 * xml 파싱시 상태 업데이트
	 * 
	 * @param read 현재 읽은 바이트
	 */
	private void updateParsingStatus(int read) {
		
		char ch = (char)read;
		
		switch(this.getStatus()) {
		case CONTENTS:
			if(ch == '<') {
				// 현재 위치(line/column number)를 테그 시작지점으로 설정
				// -> 테그 명이 확정 되는 시점에 사용하기 위해 임시 설정
				this.setStartTagLoc(this.getLineNum(), this.getColumnNumInBuffer());
				this.setStatus(ParsingStatus.TAG_START);
			}
			break;
			
		case TAG_START:
			if((ch >= 'a' && ch <= 'z') ||
				(ch >= 'A' && ch <= 'Z') ||
				ch == '_') {
				
				this.putToTagNameBuffer((byte)read);
				this.setStatus(ParsingStatus.TAG_NAME);
				
			} else {
				this.setStatus(ParsingStatus.CONTENTS);
			}
			break;
			
		case TAG_NAME:
			if((ch >= 'a' && ch <= 'z') ||
				(ch >= 'A' && ch <= 'Z') ||
				(ch >= '0' && ch <= '9') ||
				ch == '_' || ch == '-' || ch =='.' || ch == ':') {
				
				this.putToTagNameBuffer((byte)read);
				
			} else {
				
				// Tag 이름이 끝나는 경우,
				// Tag 이름에 현재 위치를 저장함
				String tagName = new String(this.getTagNameBuffer().array(), 0, this.getTagNameBuffer().position());
				this.addTagMap(tagName, this.getStartTagLoc());
				
				// 다음 Tag 이름을 저장하기 위해,
				// Tag 이름 Buffer를 클리어함
				this.getTagNameBuffer().clear();
				
				if(ch == '/') {
					this.setStatus(ParsingStatus.TAG_END);
				} else if(ch == '>') {
					this.setStatus(ParsingStatus.CONTENTS);
				} else {
					this.setStatus(ParsingStatus.TAG_CONTENTS);
				}
			}
			
			break;
			
		case TAG_CONTENTS:
			if(ch == '/') {
				this.setStatus(ParsingStatus.TAG_END);
			} else if(ch == '>') {
				this.setStatus(ParsingStatus.CONTENTS);
			} else if(ch == '\'') {
				this.setStatus(ParsingStatus.QUOTO_STRING);
			} else if(ch == '"') {
				this.setStatus(ParsingStatus.D_QUOTO_STRING);
			}
			
			break;
			
		case TAG_END:
			//
			this.setStatus(ParsingStatus.CONTENTS);
			break;
			
		case QUOTO_STRING:
			if(ch == '\\') {
				this.setStatus(ParsingStatus.QUOTO_ESCAPE);
			} else if(ch == '\'') {
				this.setStatus(ParsingStatus.TAG_CONTENTS);
			}
			break;
			
		case QUOTO_ESCAPE:
			this.setStatus(ParsingStatus.QUOTO_STRING);
			break;
			
		case D_QUOTO_STRING:
			if(ch == '\\') {
				this.setStatus(ParsingStatus.D_QUOTO_ESCAPE);
			} else if(ch == '"') {
				this.setStatus(ParsingStatus.TAG_CONTENTS);
			}
			break;
			
		case D_QUOTO_ESCAPE:
			this.setStatus(ParsingStatus.D_QUOTO_STRING);
			break;
			
		default:
			// do nothing
			break;
		}
	}
	
	/**
	 * buffer 내의 컬럼의 길이를 산출하여 반환
	 * 
	 * @return buffer 내의 컬럼의 길이
	 */
	private int getColumnNumInBuffer() {
		
		// byte buffer내의 데이터를 
		// 문자열로 치환하여 길이를 계산함
		// -> 한글의 경우 한자로 세기위해서임
		byte[] byteBuffer = this.getColumnBuffer().array();
		int columnNum = new String(byteBuffer, 0, this.getColumnBuffer().position()).length();
		
		return columnNum;
	}
	
	/**
	 * 컬럼 버퍼에 읽은 바이트 추가
	 * 
	 * @param b 읽은 바이트
	 */
	private void putToColumnBuffer(byte b) {
		
		// 현재 buffer가 다 찻으면,
		// buffer의 크기를 BUFFER_SIZE만큼 증가시키고,
		// 기존 데이터를 복사해 넣음
		if(false == this.getColumnBuffer().hasRemaining()) {
			
			ByteBuffer temp = this.getColumnBuffer();
			this.setColumnBuffer(ByteBuffer.allocate(temp.position() + BUFFER_SIZE));
			this.getColumnBuffer().put(temp.array());
		}
		
		// buffer에 현재 읽은 내용을 추가함
		// -> 추후 column 수를 계산하기 위함임
		this.getColumnBuffer().put(b);
	}
	
	/**
	 * 테그명 버퍼에 읽은 바이트 추가
	 * 
	 * @param b 읽은 바이트
	 */
	private void putToTagNameBuffer(byte b) {
		
		// 현재 buffer가 다 찻으면,
		// buffer의 크기를 BUFFER_SIZE만큼 증가시키고,
		// 기존 데이터를 복사해 넣음
		if(false == this.getTagNameBuffer().hasRemaining()) {
			
			ByteBuffer temp = this.getTagNameBuffer();
			this.setTagNameBuffer(ByteBuffer.allocate(temp.position() + BUFFER_SIZE));
			this.getTagNameBuffer().put(temp.array());
		}
		
		// buffer에 현재 읽은 내용을 추가함
		// -> 추후 column 수를 계산하기 위함임
		this.getTagNameBuffer().put(b);
	}
	
	/**
	 * 테그 시작 위치 저장(임시 저장임)
	 * 
	 * @param lineNum 라인 번호
	 * @param columnNum 컬럼 번호
	 */
	private void setStartTagLoc(int lineNum, int columnNum) {
		this.setStartTagLoc(new Loc(lineNum, columnNum));
	}

	/**
	 * 테그별 위치정보에 위치정보 추가
	 * 
	 * @param tagName 테그명
	 * @param loc 위치정보
	 */
	private void addTagMap(String tagName, Loc loc) {
		this.getTagStartLocQueue(tagName).add(loc);
	}
	
	/**
	 * 테그명에 해당하는 위치 정보를 반환<br>
	 * 큐에서 순서대로 앞쪽 부터 빼서 반환함
	 * 
	 * @param tagName 테그명
	 * @return 테그명에 해당하는 위치 정보
	 */
	Loc getTagStartLoc(String tagName) throws Exception {
		
		// 입력값 검증
		if(tagName == null) {
			throw new Exception("tagName is null.");
		}
		
		//
		Loc tagStartLoc = this.getTagStartLocQueue(tagName).poll();
		 
		return tagStartLoc;
	}
	
	/**
	 * 테그별 위치 정보에서 테그명에 해당하는 위치 정보 큐를 반환
	 * 
	 * @param tagName 테그명
	 * @return 테그명에 해당하는 위치 정보 큐
	 */
	private Queue<Loc> getTagStartLocQueue(String tagName) {
		
		Queue<Loc> tagStartLocQueue = this.getTagMap().get(tagName);
		
		if(tagStartLocQueue == null) {
			tagStartLocQueue = new ConcurrentLinkedQueue<Loc>();
			this.getTagMap().put(tagName, tagStartLocQueue);
		}
		
		return tagStartLocQueue;
	}
	
	/**
	 * 테그별 위치 정보 객체 반환
	 * 
	 * @return 테그별 위치 정보 객체
	 */
	private Map<String, Queue<Loc>> getTagMap() {
		
		// null 일 경우, 생성하여 반환
		if(this.tagMap == null) {
			this.tagMap = new ConcurrentHashMap<String, Queue<Loc>>();
		}
		
		return this.tagMap;
	}
}

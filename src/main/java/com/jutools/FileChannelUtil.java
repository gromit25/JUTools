package com.jutools;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * 
 * @author jmsohn
 */
public class FileChannelUtil {
	
	/** */
	private FileChannel chnl;
	/** */
	private ByteBuffer buffer;
	/** */
	private Charset charset;
	
	//
	/** */
	private Queue<String> lines = new LinkedList<String>(); 
	/** */
	private byte[] preRead;
	
	/**
	 * 
	 * 
	 * @param chnl
	 * @param buffer
	 */
	public FileChannelUtil(FileChannel chnl, ByteBuffer buffer, Charset charset) throws Exception {
		
		if(chnl == null) {
			throw new NullPointerException("");
		}
		
		if(buffer == null) {
			throw new NullPointerException("");
		}
		
		if(charset == null) {
			throw new NullPointerException("");
		}
		
		this.chnl = chnl;
		this.buffer = buffer;
		this.charset = charset;
	}
	
	/**
	 * 
	 * @param chnl
	 * @param capacity
	 */
	public FileChannelUtil(FileChannel chnl, int capacity, Charset charset) throws Exception {
		this(chnl, ByteBuffer.allocateDirect(capacity), charset);
	}
	
	/**
	 * 
	 * @param chnl
	 * @param buffer
	 */
	public FileChannelUtil(FileChannel chnl, ByteBuffer buffer) throws Exception {
		this(chnl, buffer, Charset.defaultCharset());
	}
	
	/**
	 * 
	 * 
	 * @param chnl
	 * @param capacity
	 */
	public FileChannelUtil(FileChannel chnl, int capacity) throws Exception {
		this(chnl, capacity, Charset.defaultCharset());
	}
	
	/**
	 * 
	 * @param chnl
	 */
	public FileChannelUtil(FileChannel chnl) throws Exception {
		this(chnl, 1024 * 1024);
	}

	/**
	 * 
	 * 
	 * @param buffer
	 * @param msg
	 */
	public void write(String msg) throws Exception {

		byte[] msgBytes = msg.getBytes();

		int start = 0;
		int remains = msgBytes.length;

		while(remains > 0) {

			int length = msgBytes.length - start;

			if(length > buffer.limit()) {
				length = this.buffer.limit();
			}

			this.buffer.clear();
			this.buffer.put(msgBytes, start, length);
			this.buffer.flip();

			while(buffer.hasRemaining() == true) {
				this.chnl.write(buffer);
			}

			remains -= length;
			start += length;
			
		} // end of while

	}
	
	/**
	 * 
	 * @param lineEnd
	 * @return
	 */
	public String readLine(String lineEnd) throws Exception {
		
		// 입력값 검증
		if(lineEnd == null) {
			throw new NullPointerException("line end is null"); 
		}
		
		if(lineEnd.isEmpty() == true) {
			throw new Exception("line end is not defined");
		}
		
		// 이전에 읽은 line이 있으면,
		// 이전 읽은 line을 반환
		if(this.lines.isEmpty() == false) {
			return this.lines.poll();
		}
		
		// 1. lineEnd가 포함될 때까지 읽거나 채널의 데이터를 끝날때까지 읽음
		byte[] read = this.preRead;
		byte[] lineEndBytes = lineEnd.getBytes();
		
		boolean isFirst = true;
		
		do {
			
			// -1이면 더이상 읽을 데이터가 없음
			if(this.chnl.read(this.buffer) == -1) {
				
				// 처음 부터 읽을 것이 없으면 즉시 null 리턴
				if(isFirst == true) {
					return null;
				}
				
				break;
			}
			
			isFirst = false;
			
			// buffer의 데이터를 byte 배열에 복사
			this.buffer.flip();  // Limit:Position, Position:0 로 변경  
			byte[] readNow = new byte[this.buffer.remaining()];  // remain: Limit - Position
			this.buffer.get(readNow);
			this.buffer.clear(); // buffer를 비움
			
			//
			read = BytesUtil.concat(read, readNow);
			
		} while(BytesUtil.contains(read, lineEndBytes) == false); // 읽은 문자열 내에 lineEnd가 없는 경우 다시 읽음 
		
		// 2.
		String line = null;
		ArrayList<byte[]> byteslines = BytesUtil.split(read, lineEndBytes, true);
		
		for(int index = 0; index < byteslines.size(); index++) {
			
			byte[] bytesline = byteslines.get(index);
			
			if(index == 0) {
				
				//
				line = new String(bytesline, this.charset);
				
			} else {
				
				//
				if(index - 1 == byteslines.size()) {
					this.preRead = bytesline;
				} else {
					this.lines.offer(new String(bytesline, this.charset));
				}
			}
			
		}
		
		return line;
	}
	
	/**
	 * 
	 * @return
	 */
	public String readLine() throws Exception {
		return this.readLine("\n");
	}

}
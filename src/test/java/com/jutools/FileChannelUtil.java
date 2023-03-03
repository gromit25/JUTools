package com.jutools;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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
	
	/**
	 * 
	 * 
	 * @param chnl
	 * @param buffer
	 */
	public FileChannelUtil(FileChannel chnl, ByteBuffer buffer) throws Exception {
		this.chnl = chnl;
		this.buffer = buffer;
	}
	
	/**
	 * 
	 * @param chnl
	 * @param capacity
	 */
	public FileChannelUtil(FileChannel chnl, int capacity) throws Exception {
		this(chnl, ByteBuffer.allocateDirect(capacity));
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

}

package com.jutools.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * 
 * 
 * @author jmsohn
 */
public class SocketChannelUtil extends ChannelUtil {
	
	/**
	 * 생성자
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param buffer 입출력에 사용할 byte buffer
	 * @param charset 입출력에 사용할 character set
	 */
	public SocketChannelUtil(SocketChannel chnl, ByteBuffer buffer, Charset charset) throws Exception {
		super(chnl, buffer, charset);
	}
	
	/**
	 * 생성자
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param capacity 입출력에 사용할 byte buffer의 크기
	 * @param charset 입출력에 사용할 character set
	 */
	public SocketChannelUtil(SocketChannel chnl, int capacity, Charset charset) throws Exception {
		this(chnl, ByteBuffer.allocateDirect(capacity), charset);
	}
	
	/**
	 * 생성자
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param buffer 입출력에 사용할 byte buffer
	 */
	public SocketChannelUtil(SocketChannel chnl, ByteBuffer buffer) throws Exception {
		this(chnl, buffer, Charset.defaultCharset());
	}
	
	/**
	 * 생성자
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param capacity 입출력에 사용할 byte buffer의 크기
	 */
	public SocketChannelUtil(SocketChannel chnl, int capacity) throws Exception {
		this(chnl, capacity, Charset.defaultCharset());
	}
	
	/**
	 * 생성자
	 * 
	 * @param chnl 입출력을 위한 file channel
	 */
	public SocketChannelUtil(SocketChannel chnl) throws Exception {
		this(chnl, 1024 * 1024);
	}

	@Override
	protected int write(ByteBuffer src) throws IOException {
		return this.getChannel().write(src);
	}

	@Override
	protected int read(ByteBuffer dst) throws IOException {
		return this.getChannel().read(dst);
	}

	/**
	 * 
	 * @return
	 */
	private SocketChannel getChannel() {
		return (SocketChannel)this.getChnl();
	}
}

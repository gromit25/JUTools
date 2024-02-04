package com.jutools.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * socket channel 처리 wrapper 클래스
 * 
 * @author jmsohn
 */
public class SocketChannelWrapper extends ChannelWrapper {
	
	/**
	 * 생성자
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param buffer 입출력에 사용할 byte buffer
	 * @param charset 입출력에 사용할 character set
	 */
	public SocketChannelWrapper(SocketChannel chnl, ByteBuffer buffer, Charset charset) throws Exception {
		super(chnl, buffer, charset);
	}
	
	/**
	 * 생성자
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param capacity 입출력에 사용할 byte buffer의 크기
	 * @param charset 입출력에 사용할 character set
	 */
	public SocketChannelWrapper(SocketChannel chnl, int capacity, Charset charset) throws Exception {
		this(chnl, ByteBuffer.allocateDirect(capacity), charset);
	}
	
	/**
	 * 생성자
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param buffer 입출력에 사용할 byte buffer
	 */
	public SocketChannelWrapper(SocketChannel chnl, ByteBuffer buffer) throws Exception {
		this(chnl, buffer, Charset.defaultCharset());
	}
	
	/**
	 * 생성자
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param capacity 입출력에 사용할 byte buffer의 크기
	 */
	public SocketChannelWrapper(SocketChannel chnl, int capacity) throws Exception {
		this(chnl, capacity, Charset.defaultCharset());
	}
	
	/**
	 * 생성자
	 * 
	 * @param chnl 입출력을 위한 file channel
	 */
	public SocketChannelWrapper(SocketChannel chnl) throws Exception {
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
	 * 설정된 Socket Channel을 반환
	 * 
	 * @return 설정된 Socket Channel
	 */
	private SocketChannel getChannel() {
		return (SocketChannel)this.getChnl();
	}
}

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

package com.jutools.channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * file channel 처리 wrapper 클래스
 * 
 * @author jmsohn
 */
public class FileChannelWrapper extends ChannelWrapper {
	
	/**
	 * 생성자
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param buffer 입출력에 사용할 byte buffer
	 * @param charset 입출력에 사용할 character set
	 */
	public FileChannelWrapper(FileChannel chnl, ByteBuffer buffer, Charset charset) throws Exception {
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
	 * 설정된 File Channel을 반환
	 * 
	 * @return 설정된 File Channel
	 */
	private FileChannel getChannel() {
		return (FileChannel)this.getChnl();
	}
}
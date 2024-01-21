package com.jutools;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.OpenOption;

/**
 * file channel 처리 관련 utility 클래스
 * 
 * @author jmsohn
 */
public class FileChannelUtil extends ChannelUtil {
	
	/**
	 * 생성자
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param buffer 입출력에 사용할 byte buffer
	 * @param charset 입출력에 사용할 character set
	 */
	public FileChannelUtil(FileChannel chnl, ByteBuffer buffer, Charset charset) throws Exception {
		super(chnl, buffer, charset);
	}
	
	/**
	 * 생성자
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param capacity 입출력에 사용할 byte buffer의 크기
	 * @param charset 입출력에 사용할 character set
	 */
	public FileChannelUtil(FileChannel chnl, int capacity, Charset charset) throws Exception {
		this(chnl, ByteBuffer.allocateDirect(capacity), charset);
	}
	
	/**
	 * 생성자
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param buffer 입출력에 사용할 byte buffer
	 */
	public FileChannelUtil(FileChannel chnl, ByteBuffer buffer) throws Exception {
		this(chnl, buffer, Charset.defaultCharset());
	}
	
	/**
	 * 생성자
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param capacity 입출력에 사용할 byte buffer의 크기
	 */
	public FileChannelUtil(FileChannel chnl, int capacity) throws Exception {
		this(chnl, capacity, Charset.defaultCharset());
	}
	
	/**
	 * 생성자
	 * 
	 * @param chnl 입출력을 위한 file channel
	 */
	public FileChannelUtil(FileChannel chnl) throws Exception {
		this(chnl, 1024 * 1024);
	}
	
	/**
	 * 생성자
	 * 
	 * @param file 입출력 file
	 * @param buffer 입출력에 사용할 byte buffer
	 * @param charset 입출력에 사용할 character set
	 * @param options channel open options
	 */
	public FileChannelUtil(File file, ByteBuffer buffer, Charset charset, OpenOption... options) throws Exception {
		this(FileChannel.open(file.toPath(), options), buffer, charset);
	}

	/**
	 * 생성자
	 * 
	 * @param file 입출력 file
	 * @param capacity 입출력에 사용할 byte buffer의 크기
	 * @param charset 입출력에 사용할 character set
	 * @param options channel open options
	 */
	public FileChannelUtil(File file, int capacity, Charset charset, OpenOption... options) throws Exception {
		this(FileChannel.open(file.toPath(), options), capacity, charset);
	}
	
	/**
	 * 생성자
	 * 
	 * @param file 입출력 file
	 * @param buffer 입출력에 사용할 byte buffer
	 * @param options channel open options
	 */
	public FileChannelUtil(File file, ByteBuffer buffer, OpenOption... options) throws Exception {
		this(FileChannel.open(file.toPath(), options), buffer);
	}
	
	/**
	 * 생성자
	 * 
	 * @param file 입출력 file
	 * @param capacity 입출력에 사용할 byte buffer의 크기
	 * @param options channel open options
	 */
	public FileChannelUtil(File file, int capacity, OpenOption... options) throws Exception {
		this(FileChannel.open(file.toPath(), options), capacity);
	}
	
	/**
	 * 생성자
	 * 
	 * @param file 입출력 file
	 * @param options channel open options
	 */
	public FileChannelUtil(File file, OpenOption... options) throws Exception {
		this(FileChannel.open(file.toPath(), options));
	}
	
	/**
	 * 
	 * @return
	 */
	private FileChannel getChannel() {
		return (FileChannel)this.getChnl();
	}

	@Override
	protected int write(ByteBuffer src) throws IOException {
		return this.getChannel().write(src);
	}

	@Override
	protected int read(ByteBuffer dst) throws IOException {
		return this.getChannel().read(dst);
	}
}
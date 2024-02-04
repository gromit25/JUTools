package com.jutools;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.file.OpenOption;

import com.jutools.channel.FileChannelWrapper;
import com.jutools.channel.SocketChannelWrapper;

/**
 * Channel 관련 Utility 클래스
 * 
 * @author jmsohn
 */
public class ChannelUtil {
	
	/**
	 * file channel 처리 wrapper 객체 생성
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param buffer 입출력에 사용할 byte buffer
	 * @param charset 입출력에 사용할 character set
	 * @return file channel 처리 wrapper 객체
	 */
	public static FileChannelWrapper create(FileChannel chnl, ByteBuffer buffer, Charset charset) throws Exception {
		return new FileChannelWrapper(chnl, buffer, charset);
	}
	
	/**
	 * file channel 처리 wrapper 객체 생성
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param capacity 입출력에 사용할 byte buffer의 크기
	 * @param charset 입출력에 사용할 character set
	 * @return file channel 처리 wrapper 객체
	 */
	public static FileChannelWrapper create(FileChannel chnl, int capacity, Charset charset) throws Exception {
		return create(chnl, ByteBuffer.allocateDirect(capacity), charset);
	}
	
	/**
	 * file channel 처리 wrapper 객체 생성
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param buffer 입출력에 사용할 byte buffer
	 * @return file channel 처리 wrapper 객체
	 */
	public static FileChannelWrapper create(FileChannel chnl, ByteBuffer buffer) throws Exception {
		return create(chnl, buffer, Charset.defaultCharset());
	}
	
	/**
	 * file channel 처리 wrapper 객체 생성
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param capacity 입출력에 사용할 byte buffer의 크기
	 * @return file channel 처리 wrapper 객체
	 */
	public static FileChannelWrapper create(FileChannel chnl, int capacity) throws Exception {
		return create(chnl, capacity, Charset.defaultCharset());
	}
	
	/**
	 * file channel 처리 wrapper 객체 생성
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @return file channel 처리 wrapper 객체
	 */
	public static FileChannelWrapper create(FileChannel chnl) throws Exception {
		return create(chnl, 1024 * 1024);
	}
	
	/**
	 * file channel 처리 wrapper 객체 생성
	 * 
	 * @param file 입출력 file
	 * @param buffer 입출력에 사용할 byte buffer
	 * @param charset 입출력에 사용할 character set
	 * @param options channel open options
	 * @return file channel 처리 wrapper 객체
	 */
	public static FileChannelWrapper create(File file, ByteBuffer buffer, Charset charset, OpenOption... options) throws Exception {
		return create(FileChannel.open(file.toPath(), options), buffer, charset);
	}
	
	/**
	 * file channel 처리 wrapper 객체 생성
	 * 
	 * @param file 입출력 file
	 * @param capacity 입출력에 사용할 byte buffer의 크기
	 * @param charset 입출력에 사용할 character set
	 * @param options channel open options
	 * @return file channel 처리 wrapper 객체
	 */
	public static FileChannelWrapper create(File file, int capacity, Charset charset, OpenOption... options) throws Exception {
		return create(FileChannel.open(file.toPath(), options), capacity, charset);
	}
	
	/**
	 * file channel 처리 wrapper 객체 생성
	 * 
	 * @param file 입출력 file
	 * @param buffer 입출력에 사용할 byte buffer
	 * @param options channel open options
	 * @return file channel 처리 wrapper 객체
	 */
	public static FileChannelWrapper create(File file, ByteBuffer buffer, OpenOption... options) throws Exception {
		return create(FileChannel.open(file.toPath(), options), buffer);
	}
	
	/**
	 * file channel 처리 wrapper 객체 생성
	 * 
	 * @param file 입출력 file
	 * @param capacity 입출력에 사용할 byte buffer의 크기
	 * @param options channel open options
	 * @return file channel 처리 wrapper 객체
	 */
	public static FileChannelWrapper create(File file, int capacity, OpenOption... options) throws Exception {
		return create(FileChannel.open(file.toPath(), options), capacity);
	}
	
	/**
	 * file channel 처리 wrapper 객체 생성
	 * 
	 * @param file 입출력 file
	 * @param options channel open options
	 * @return file channel 처리 wrapper 객체
	 */
	public static FileChannelWrapper create(File file, OpenOption... options) throws Exception {
		return create(FileChannel.open(file.toPath(), options));
	}
	
	/**
	 * socket channel 처리 wrapper 객체 생성
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param buffer 입출력에 사용할 byte buffer
	 * @param charset 입출력에 사용할 character set
	 * @return socket channel 처리 wrapper 객체
	 */
	public static SocketChannelWrapper create(SocketChannel chnl, ByteBuffer buffer, Charset charset) throws Exception {
		return new SocketChannelWrapper(chnl, buffer, charset);
	}
	
	/**
	 * socket channel 처리 wrapper 객체 생성
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param capacity 입출력에 사용할 byte buffer의 크기
	 * @param charset 입출력에 사용할 character set
	 * @return socket channel 처리 wrapper 객체
	 */
	public static SocketChannelWrapper create(SocketChannel chnl, int capacity, Charset charset) throws Exception {
		return create(chnl, ByteBuffer.allocateDirect(capacity), charset);
	}
	
	/**
	 * socket channel 처리 wrapper 객체 생성
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param buffer 입출력에 사용할 byte buffer
	 * @return socket channel 처리 wrapper 객체
	 */
	public static SocketChannelWrapper create(SocketChannel chnl, ByteBuffer buffer) throws Exception {
		return create(chnl, buffer, Charset.defaultCharset());
	}
	
	/**
	 * socket channel 처리 wrapper 객체 생성
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @param capacity 입출력에 사용할 byte buffer의 크기
	 * @return socket channel 처리 wrapper 객체
	 */
	public static SocketChannelWrapper create(SocketChannel chnl, int capacity) throws Exception {
		return create(chnl, capacity, Charset.defaultCharset());
	}
	
	/**
	 * socket channel 처리 wrapper 객체 생성
	 * 
	 * @param chnl 입출력을 위한 file channel
	 * @return socket channel 처리 wrapper 객체
	 */
	public static SocketChannelWrapper create(SocketChannel chnl) throws Exception {
		return create(chnl, 1024 * 1024);
	}
}

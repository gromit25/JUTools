package com.jutools.parserfw;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

import lombok.Getter;

/**
 * 
 * 
 * @author jmsohn
 */
class ExpReader extends PushbackReader {
	
	@Getter
	private int pos;

	/**
	 * 
	 * @param in
	 */
	public ExpReader(Reader in) {
		super(in);
		this.pos = 0;
	}
	
	/**
	 * 
	 * @param in
	 * @param size
	 */
	public ExpReader(Reader in, int size) {
		super(in, size);
		this.pos = 0;
	}
	
	@Override
	public int read() throws IOException {
		
		this.pos++;
		
		try {
			
			return super.read();
			
		} catch(IOException ioex) {
			
			this.pos--;
			throw ioex;
		}
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		
		this.pos += len;
		
		try {
			
			return super.read(cbuf, off, len);
			
		} catch(IOException ioex) {
			
			this.pos -= len;
			throw ioex;
		}
	}
	
	@Override
	public void unread(int c) throws IOException {
		
		this.pos--;
		
		try {
			
			super.unread(c);
			
		} catch(IOException ioex) {
			
			this.pos++;
			throw ioex;
		}
	}
	
	@Override
	public void unread(char[] cbuf, int off, int len) throws IOException {
		
		this.pos -= len;
		
		try {
			
			super.unread(cbuf, off, len);
			
		} catch(IOException ioex) {
			
			this.pos += len;
			throw ioex;
		}
	}
	
	@Override
	public void unread(char[] cbuf) throws IOException {
        this.unread(cbuf, 0, cbuf.length);
    }
	
	@Override
	public long skip(long n) throws IOException {
		
		this.pos += n;
		
		try {
			
			return super.skip(n);
			
		} catch(IOException ioex) {
			
			this.pos -= n;
			throw ioex;
		}
	}
}

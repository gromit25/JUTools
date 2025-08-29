package com.jutools.mail;

import java.util.Properties;

import com.jutools.StringUtil;

/**
 * 
 * 
 * @author jmsohn
 */
public class MailSenderFactory {
	
	/** */
	private Properties props = new Properties();
	
	/** */
	private String charset = "UTF-8";

	/** */
	private InetAddress sender;
	
	/** */
	private String password;
	
	/**
	 * 
	 */
	public MailSenderFactory() {
		this.props.put(MailConstant.AUTH, "true");
		this.props.put(MailConstant.PORT, "25");
		this.props.put(MailConstant.DEBUG, "false");
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public MailSender build() throws Exception {
		return new MailSender(this.props, this.password, this.charset);
	}
	
	/**
	 * 
	 * 
	 * @param host
	 * @return
	 */
	public MailSenderFactory host(String host) throws Exception {
		
		if(StringUtil.isBlank(host) == true) {
			throw new IllegalArgumentException("host name is null or blank.");
		}
		
		this.props.put(MailConstant.HOST, host);
		return this;
	}
	
	/**
	 * 
	 * 
	 * @param port
	 * @return
	 */
	public MailSenderFactory port(int port) throws Exception {
		
		if(port < 0) {
			throw new IllegalArgumentException("port number is invalid: " + port);
		}
		
		this.props.put(MailConstant.PORT, Integer.toString(port));
		return this;
	}
	
	/**
	 * 
	 * 
	 * @param debug
	 * @return
	 */
	public MailSenderFactory debug(boolean debug) {
		this.props.put(MailConstant.DEBUG, Boolean.toString(debug));
		return this;
	}
	
	/**
	 * 
	 * 
	 * @param ssl
	 * @return
	 */
	public MailSenderFactory ssl(boolean ssl) {
		this.props.put(MailConstant.SSL, Boolean.toString(ssl));
		return this;
	}
	
	/**
	 * 
	 * 
	 * @param tls
	 * @return
	 */
	public MailSenderFactory tls(boolean tls) {
		this.props.put(MailConstant.TLS, Boolean.toString(tls));
		return this;
	}

	/**
	 * 
	 * 
	 * @param sender
	 * @return
	 */
	public MailSenderFactory sender(String sender) throws Exception {

		if(StringUtil.isBlank(sender) == true) {
			throw new IllegalArgumentException("sender is null or blank.");
		}
		
		this.sender = new InetAddress(sender);
		return this;
	}
	
	/**
	 * 
	 * 
	 * @param password
	 * @return
	 */
	public MailSenderFactory password(String password) throws Exception {
		
		if(StringUtil.isBlank(password) == true) {
			throw new IllegalArgumentException("password is null or blank.");
		}
		
		this.password = password;
		return this;
	}
	
	/**
	 * 
	 * @param charset
	 * @return
	 */
	public MailSenderFactory charset(String charset) throws Exception {
		
		if(StringUtil.isBlank(charset) == true) {
			throw new IllegalArgumentException("charset is null or blank.");
		}
		
		this.charset = charset;
		return this;
	}
}

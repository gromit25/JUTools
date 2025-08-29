package com.jutools.mail;

import java.util.Properties;

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
	private String password;
	
	/**
	 * 
	 */
	public MailSenderFactory() {
		this.props.put(MailConstant.AUTH, "true");
	}
	
	public MailSenderFactory host(String host) {
		this.props.put(MailConstant.HOST, host);
		return this;
	}
	
	public MailSenderFactory port(int port) {
		this.props.put(MailConstant.PORT, Integer.toString(port));
		return this;
	}
	
	public MailSenderFactory debug(boolean debug) {
		this.props.put(MailConstant.DEGUG, Boolean.toString(debug));
		return this;
	}
	
	public MailSenderFactory ssl(boolean ssl) {
		this.props.put(MailConstant.SSL, Boolean.toString(ssl));
		return this;
	}
	
	public MailSenderFactory tls(boolean tls) {
		this.props.put(MailConstant.TLS, Boolean.toString(tls));
		return this;
	}
	
	public MailSenderFactory password(String password) {
		this.password = password;
		return this;
	}
	
	public MailSenderFactory charset(String charset) {
		this.charset = charset;
		return this;
	}
	
	public MailSender build() throws Exception {
		return new MailSender(this.props, this.password, this.charset);
	}
}

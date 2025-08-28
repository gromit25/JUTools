package com.jutools.mail;

import java.io.File;
import java.util.List;
import java.util.Properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * 
 * @author jmsohn
 */
public class MailSender {
	
	/** */
	private Properties props;
	
	/** */
	@Getter
	private String sender;
	
	/** */
	private List<String> receiverList;
	
	/** */
	@Getter
	private String subject;
	
	/** */
	@Getter
	private String body;
	
	/** */
	private List<File> attachFileList;
	
	/**
	 * 
	 * 
	 * @param props
	 */
	MailSender(Properties props) throws Exception {
	}
	
	/**
	 * 
	 * 
	 * @param sender
	 * @return
	 */
	public MailSender setSender(String sender) throws Exception {
		this.sender = sender;
		return this;
	}
	
	public MailSender addReceiver(String... receiverAry) throws Exception {
		return this;
	}
}

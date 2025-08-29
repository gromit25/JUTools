package com.jutools.mail;

import java.util.Properties;

import com.jutools.StringUtil;

import jakarta.mail.internet.InternetAddress;

/**
 * MailSender 팩토리 클래스
 * 
 * @author jmsohn
 */
public class MailSenderFactory {
	
	/** 메일 서버 연결을 위한 설정 프로퍼티 */
	private Properties props = new Properties();
	
	/** 캐릭터 셋 */
	private String charset = "UTF-8";

	/** 발신인 이메일 주소 */
	private InternetAddress sender;
	
	/** 메일 서버 접속용 발신인 패스워드 */
	private String password;

	
	/**
	 * 생성자
	 */
	public MailSenderFactory() {

		// 디폴트 값 설정
		this.props.put(MailConstant.AUTH, "true");
		this.props.put(MailConstant.PORT, "25");
		this.props.put(MailConstant.DEBUG, "false");
	}

	/**
	 * 설정된 값으로 MailSender 객체 생성 후 반환
	 * 
	 * @return 생성된 MailSender 객체
	 */
	public MailSender build() throws Exception {
		return new MailSender(this.props, this.sender, this.password, this.charset);
	}
	
	/**
	 * 메일 서버 설정 - 필수
	 * 
	 * @param host 메일 서버
	 * @return 현재 객체
	 */
	public MailSenderFactory host(String host) throws Exception {
		
		if(StringUtil.isBlank(host) == true) {
			throw new IllegalArgumentException("host name is null or blank.");
		}
		
		this.props.put(MailConstant.HOST, host);
		return this;
	}
	
	/**
	 * 메일 서버 포트 설정 - 옵션(디폴트: 25)
	 * 
	 * @param port 메일 서버 포트
	 * @return 현재 객체
	 */
	public MailSenderFactory port(int port) throws Exception {
		
		if(port <= 0 || port > 65535) {
			throw new IllegalArgumentException("port number is invalid: " + port);
		}
		
		this.props.put(MailConstant.PORT, Integer.toString(port));
		return this;
	}
	
	/**
	 * 디버그 모드 사용 여부 설정 - 옵션(디폴트 : false)
	 * 
	 * @param debug 디버그 모드 사용 여부
	 * @return 현재 객체
	 */
	public MailSenderFactory debug(boolean debug) {
		this.props.put(MailConstant.DEBUG, Boolean.toString(debug));
		return this;
	}
	
	/**
	 * SSL 사용여부 설정 - 옵편(디폴트 : false)
	 * 
	 * @param ssl SSL 사용 여부
	 * @return 현재 객체
	 */
	public MailSenderFactory ssl(boolean ssl) {
		this.props.put(MailConstant.SSL, Boolean.toString(ssl));
		return this;
	}
	
	/**
	 * TLS 사용여부 설정 - 옵편(디폴트 : false)
	 * 
	 * @param tls TLS 사용 여부
	 * @return 현재 객체
	 */
	public MailSenderFactory tls(boolean tls) {
		this.props.put(MailConstant.TLS, Boolean.toString(tls));
		return this;
	}

	/**
	 * 발신인 이메일 주소 설정
	 * 
	 * @param sender 발신인 이메일 주소
	 * @return 현재 객체
	 */
	public MailSenderFactory sender(String sender) throws Exception {

		if(StringUtil.isBlank(sender) == true) {
			throw new IllegalArgumentException("sender is null or blank.");
		}
		
		this.sender = new InternetAddress(sender);
		return this;
	}
	
	/**
	 * 메일 서버 접속용 발신인 패스워드 설정
	 * 
	 * @param password 메일 서버 접속용 발신인 패스워드
	 * @return 현재 객체
	 */
	public MailSenderFactory password(String password) throws Exception {
		
		if(StringUtil.isBlank(password) == true) {
			throw new IllegalArgumentException("password is null or blank.");
		}
		
		this.password = password;
		return this;
	}
	
	/**
	 * 캐릭터 셋 설정
	 * 
	 * @param charset 캐릭터 셋
	 * @return 현재 객체
	 */
	public MailSenderFactory charset(String charset) throws Exception {
		
		if(StringUtil.isBlank(charset) == true) {
			throw new IllegalArgumentException("charset is null or blank.");
		}
		
		this.charset = charset;
		return this;
	}
}

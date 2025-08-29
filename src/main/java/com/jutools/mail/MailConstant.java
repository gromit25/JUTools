package com.jutools.mail;

/**
 * mail 관련 상수 클래스
 * 
 * @author jmsohn
 */
public class MailConstant {

	/** 메일 서버 */
	public static final String HOST = "mail.smtp.host";

	/** 메일 서버 포트 */
	public static final String PORT = "mail.smtp.port";

	/** 인증 여부 */
	public static final String AUTH = "mail.smtp.auth";

	/** 메일 발송시 디버그 메시지 출력 여부 */
	public static final String DEBUG = "mail.debug";

	/** TLS 사용 여부 설정 */
	public static final String TLS = "mail.smtp.starttls.enable";

	/** SSL 사용 여부 설정 */
	public static final String SSL = "mail.smtp.ssl.enable";
}

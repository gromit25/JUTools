package com.jutools.jmx;

import java.io.Closeable;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.jutools.StringUtil;

/**
 * JMX 서비스 객체
 * 
 * @author jmsohn
 */
public class JMXService implements Closeable {
	
	/**
	 * JMX 연결 타입 enum
	 */
	private enum JMXConnectionType {
		INTERNAL,
		EXTERNAL
	}
	
	/** JMX 연결 타입 */
	private JMXConnectionType connType;
	
	/** JMX 연결 */
	private JMXConnector jmxConnector;

	/**
	 * 생성자 - 외부 JMX 서버 연결용
	 * 
	 * @param host JMX 호스트 정보
	 * @param port JMX 포트 번호
	 * @param username 접속용 사용자 명
	 * @param password 접속용 패스워드
	 */
	public JMXService(String host, int port, String username, String password) throws Exception {
		
		// jmx 타입 설정
		this.connType = JMXConnectionType.EXTERNAL;
		
		// jmx url 생성
		String url = String.format("service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi", host, port);
		JMXServiceURL jmxUrl = new JMXServiceURL(url);
        
		// 접속용 환경 변수 설정
		Map<String, Object> env = new HashMap<>();
        
		// 아이디/패스워드가 있을 경우 환경 변수에 설정
		if(StringUtil.isBlank(username) == false && StringUtil.isBlank(password) == false) {
			String[] credentials = new String[] {username, password };
			env.put(JMXConnector.CREDENTIALS, credentials);
		}

		this.jmxConnector = JMXConnectorFactory.connect(jmxUrl, env);
	}
	
	/**
	 * 생성자 - 외부 JMX 서버 연결용
	 * 
	 * @param host JMX 호스트 정보
	 * @param port JMX 포트 번호
	 */
	public JMXService(String host, int port) throws Exception {
		this(host, port, null, null);
	}
	
	/**
	 * 생성자 - 내부 JXM 연결용
	 */
	public JMXService() throws Exception {
		this.connType = JMXConnectionType.INTERNAL;
	}
	
	/**
	 * MBean Server Connection 객체 반환<br>
	 * 내부/외부 접속에 따라 반환함
	 * 
	 * @return MBean Server Connection 객체
	 */
	private MBeanServerConnection getMBeanConnection() throws Exception {
		
		if(this.connType == JMXConnectionType.INTERNAL) {
			
			return ManagementFactory.getPlatformMBeanServer();
			
		} else if(this.connType == JMXConnectionType.EXTERNAL){
			
			if(this.jmxConnector != null) {
				return this.jmxConnector.getMBeanServerConnection();
			} else {
				throw new Exception("jmxConnector is null.");
			}
			
		} else {
			throw new Exception("connType is invalid.");
		}
	}
	
	/**
	 * JMX 객체의 속성 값 반환 
	 * 
	 * @param <T> 속성 값 타입
	 * @param objectNameStr 객체 명
	 * @param attrNameStr 속성 명
	 * @param returnType 속성 값 반환 타입
	 * @return 속성 값
	 */
	public <T> T get(String objectNameStr, String attrNameStr, Class<T> returnType) throws Exception {

		// jmx 연결이 없는 경우 null 반환
		if(this.jmxConnector == null) {
			return null;
		}
		
		// 입력 값 검증
		if(StringUtil.isBlank(objectNameStr) == true) {
			throw new IllegalArgumentException("objectNameStr is null or blank.");
		}
		
		if(StringUtil.isBlank(attrNameStr) == true) {
			throw new IllegalArgumentException("attrNameStr is null or blank.");
		}
		
		if(returnType == null) {
			throw new IllegalArgumentException("returnType is null.");
		}

		// MBeanServerConnection 변수
		MBeanServerConnection mbeanServerConnection = this.getMBeanConnection();

		// ObjectName 설정
		ObjectName objectName = new ObjectName(objectNameStr);

		// JMX 값 획득 및 반환
		Object value = mbeanServerConnection.getAttribute(objectName, attrNameStr);
		return returnType.cast(value);
	}

	@Override
	public void close() throws IOException {
		
		if(this.jmxConnector == null) {
			return;
		}
		
		if(this.connType == JMXConnectionType.INTERNAL) {
			return;
		}
		
		this.jmxConnector.close();
	}
}

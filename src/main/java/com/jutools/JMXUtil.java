package com.jutools;

import java.io.File;
import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

/**
 * JMX(Java Management eXtension) Utility 클래스
 * 
 * @author jmsohn
 */
public class JMXUtil {

	/**
	 * VM 목록에서 display 명에 해당하는 VM 객체를 반환<br>
	 * 못찾을 경우 null 을 반환함
	 * 
	 * @param displayName display 명
	 * @return VM 객체
	 */
	public static VirtualMachine getVM(String displayName) throws Exception {
		
		// 입력 값 검증
		if(displayName == null) {
			throw new IllegalArgumentException("displayName is null.");
		}
		
		// vm 목록에서 display 명이 일치하는 VM을 찾아서 반환
		List<VirtualMachineDescriptor> vmDescList = VirtualMachine.list();
		for(VirtualMachineDescriptor vmDesc: vmDescList) {
			
			if(vmDesc.displayName().equals(displayName) == true) {
				return VirtualMachine.attach(vmDesc.id());
			}
		}

		// display 명이 일치하는 것이 없을 경우 null 을 반환
		return null;
	}
	
	/**
	 * VM의 JMX 연결 반환
	 * 
	 * @param vm JMX를 연결할 VM
	 * @return JMX 연결
	 */
	public static JMXConnector getJMXConnector(VirtualMachine vm) throws Exception {
		
		// 입력값 검증
		if(vm == null) {
			throw new IllegalArgumentException("vm(virtual machine) is null.");
		}

		// JMX 연결 주소 획득
		String connectorAddress = vm.getAgentProperties().getProperty("com.sun.management.jmxremote.localConnectorAddress");
		
		if (connectorAddress == null) {
			
			try {
				
				// JDK 9+
				connectorAddress = vm.startLocalManagementAgent();
				
			} catch (NoSuchMethodError e) {
				
				// JDK 8
				String agentPath = vm.getSystemProperties().getProperty("java.home")
						+ File.separator + "lib" + File.separator + "management-agent.jar";
				vm.loadAgent(agentPath);
				connectorAddress = vm.getAgentProperties()
						.getProperty("com.sun.management.jmxremote.localConnectorAddress");
			}
		}

		// JMX 연결 URL 생성
		JMXServiceURL url = new JMXServiceURL(connectorAddress);
		
		// JMX 연결 생성 후 반환
		return JMXConnectorFactory.connect(url);
    }
	
	/**
	 * attribute 값을 반환
	 * 
	 * @param mbsc MBean 서버 연결
	 * @param objectName object 명
	 * @param attributeName attribute 명
	 * @return attribute 반환
	 */
	public static <T> T getAttribute(
		MBeanServerConnection mbsc,
		String objectName,
		String attributeName,
		Class<T> returnType
	) throws Exception {

		// 입력값 검증
		if(mbsc == null) {
			throw new IllegalArgumentException("MBean Server Connection is null.");
		}

		if(objectName == null) {
			throw new IllegalArgumentException("objectName is null.");
		}

		if(attributeName == null) {
			throw new IllegalArgumentException("attributeName is null.");
		}
    	
		if(returnType == null) {
			throw new IllegalArgumentException("returnType is null.");
		}
    	
		// object의 attribute 번환
		ObjectName beanName = new ObjectName(objectName);
		return returnType.cast(mbsc.getAttribute(beanName, attributeName));
	}
}

package com.jutools;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

import com.sun.management.*;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.util.Enumeration;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 시스템 정보 관련 Utility 클래스
 * 
 * @author jmsohn
 */
@SuppressWarnings("restriction")
public class SysUtil {
	
	/** 시스템 정보를 얻기 위한 OperationSystemMXBean */
	private static OperatingSystemMXBean osBean;
	
	static {
		// OperatingSystemMXBean를 가져옴
		// 주의!! java.lang.management.OperatingSystemMXBean 이 아님
		//       com.sun.management.OperatingSystemMXBean 임
		SysUtil.osBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
	}
	
	/**
	 * 시스템의 이름을 반환하는 메소드<br>
	 * 
	 * @return 시스템 명
	 */
	public static String getHostname() throws Exception {
		return InetAddress.getLocalHost().getHostName();
	}
	
	/**
	 * 시스템의 ip 목록을 반환하는 메소드<br>
	 * ex) 192.168.0.1,192.168.0.2
	 * 
	 * @return 시스템의 ip 목록
	 */
	public static String getIps() throws Exception {
		
		StringBuilder ips = new StringBuilder("");
		
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		boolean isFirst = true;
		
		// Network Interface별 IP를 가져옴
		while(interfaces.hasMoreElements() == true) {
			
		    NetworkInterface iface = interfaces.nextElement();
		    if (iface.isLoopback() == true || iface.isUp() == false) {
		        continue;
		    }
		    
		    Enumeration<InetAddress> addresses = iface.getInetAddresses();
		    while(addresses.hasMoreElements() == true) {
		    	
		        InetAddress addr = addresses.nextElement();
		        
		        // loopback 제외
		        if(addr.isLoopbackAddress() == true) {
		        	continue;
		        }
		        
		        // IpV6 제외
		        if(addr.getHostAddress().contains(":") == true) {
		        	continue;
		        }
		        
		        if(isFirst == true) {
		        	ips.append(addr.getHostAddress());
		        	isFirst = false;
		        } else {
		        	ips.append(",").append(addr.getHostAddress());
		        }
		    }
		}
		
		return ips.toString();
	}
	
	/**
	 * 시스템 성능 정보를 반환
	 * 
	 * @return 시스템 성능 정보(JSON type)
	 */
	public static String getSysPerp() throws Exception {
		
		// system 정보 수집을 위한 JSON 객체
		JSONObject perp = new JSONObject();
		
		// system 정보 수집
		double cpuLoad = osBean.getCpuLoad();
		
		// system 정보 body 메시지 설정
		perp.put("hostname", getHostname());
		perp.put("ips", getIps());
		perp.put("cpu", cpuLoad * 100);
		perp.put("mem", getMemUsage());
		perp.put("disk", getDiskUsage());
		
		// JSON 문자열 반환
		return perp.toString();
		
	}
	
	/**
	 * 메모리 사용량 정보 획득
	 * 
	 * @return 메모리 사용량 정보 JSON 형태로 반환
	 */
	private static JSONObject getMemUsage() throws Exception {
		
		JSONObject memUsage = new JSONObject();
		
		memUsage
			.put("total", osBean.getTotalMemorySize())
			.put("free", osBean.getFreeMemorySize());

		return memUsage;
	}
	
	/**
	 * 디스크 사용량 정보 획득
	 * 
	 * @return 디스크 사용량 정보 JSON 형태로 반환
	 */
	private static JSONArray getDiskUsage() throws Exception {
		
		JSONArray diskUsage = new JSONArray();

		for(FileStore store : FileSystems.getDefault().getFileStores()) {

			// Storage 정보 획득
			String name = store.toString();
			String type = store.type();

			long total = store.getTotalSpace();
			long used = total - store.getUsableSpace();
			double usedPercent = ((double) used / total) * 100.0;
            
			// JSON 객체 생성
			JSONObject storeUsage = new JSONObject();
			storeUsage
				.put("name", name)
				.put("type", type)
				.put("total", total)
				.put("used", used)
				.put("percent", usedPercent);

			// disk 정보에 추가
			diskUsage.put(storeUsage);
		}

		return diskUsage;
	}

}

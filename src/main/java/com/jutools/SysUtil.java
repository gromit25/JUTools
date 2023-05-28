package com.jutools;

import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.Charset;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Enumeration;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.management.OperatingSystemMXBean;

import lombok.Builder;
import lombok.Getter;

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
	public static String[] getIps() throws Exception {
		
		ArrayList<String> ips = new ArrayList<>();
		
		// Network Interface별 IP를 가져옴
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		
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
				
				ips.add(addr.getHostAddress());
			}
		}
		
		return ips.stream().toArray(String[]::new);
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
		double cpuLoad = osBean.getSystemCpuLoad();
		
		// system 정보 body 메시지 설정
		perp.put("hostname", getHostname());
		perp.put("ips", StringUtil.join(",", getIps()));
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
			.put("total", osBean.getTotalPhysicalMemorySize())
			.put("free", osBean.getFreePhysicalMemorySize());

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
	
	/**
	 * 시스템 command를 실행하고 결과를 반환하는 메소드
	 * ex) cmd = "cmd /c dir \"C:\\Program Files\""
	 * 
	 * @param cmd 명령어 문자열
	 * @return 명령어 실행 결과
	 */
	public static SysCmdResult execute(String cmd) throws Exception {
		
		// command 목록으로 분리
		String[] cmdList = splitCmd(cmd);
		
		// 명령어 실행 객체 생성 및 설정
		ProcessBuilder processBuilder = new ProcessBuilder(cmdList);
		processBuilder.redirectErrorStream(true);
		
		// 명령어 실행
		Process process = processBuilder.start();
		
		// 출력 결과 저장
		String output = "";
		try(InputStream is = process.getInputStream()) {
			output = new String(BytesUtil.readAllBytes(is), Charset.defaultCharset());
		}
		
		// 실행 결과 기다림
		int result = process.waitFor();
		
		// 실행 결과 객체 생성
		return SysCmdResult.builder()
				.result(result)
				.output(output)
				.build();
	}
	
	/**
	 * command 실행 결과 클래스
	 * 
	 * @author jmsohn 
	 */
	@Builder
	public static class SysCmdResult {
		
		/** 실행 결과 값 */
		@Getter
		private int result;
		/** 실행 출력 문자열 */
		@Getter
		private String output;
		
		/**
		 * 생성자
		 * 
		 * @param result 실행 결과 값
		 * @param output 실행 출력 문자열
		 */
		public SysCmdResult(int result, String output) {
			this.result = result;
			this.output = output;
		}
	}
	
	/**
	 * 명령어 문자열을 분할하여 문자열 목록으로 반환
	 * -> 스페이스 문자(' ', '\t') 로 분리하지만 문자열 타입("abc def")은
	 *    하나의 명령어 문자열로 분할해야함
	 * ex) cmd \c dir "C:\Program Files" 일 경우
	 *     cmd, \c, dir, C:\Program Files 로 분해됨 
	 * 
	 * @param cmdStr 명
	 * @return 분할된 cmd 목록
	 */
	private static String[] splitCmd(String cmdStr) throws Exception {
		
		// 입력값 검증
		if(StringUtil.isEmpty(cmdStr) == true) {
			throw new Exception("command string is empty");
		}
		
		// 분할된 cmd 목록
		ArrayList<String> cmds = new ArrayList<>(); 
		
		// cmd를 임시 저장할 변수
		StringBuilder cmd = new StringBuilder("");
		
		// 문자열 내인지 확인하는 상태값
		// 0 - 문자열 아님
		// 1 - 문자열 내
		int status = 0;
		
		for(int index = 0; index < cmdStr.length(); index++) {
			
			char ch = cmdStr.charAt(index);
			
			if(status == 0) {
				
				if(ch == ' ' || ch == '\t' || ch == '"') {
					
					// cmd가 비어 있지 않으면 cmd 목록(cmds)에 추가
					if(cmd.length() != 0) {
						cmds.add(cmd.toString());
						cmd.setLength(0); // 내용 삭제
					}
					
					// 문자열 상태로 변경
					if(ch == '"') {
						status = 1; 
					}
					
				} else {
					cmd.append(ch);
				}
				
			} else {
				
				if(ch == '"') {
					// 문자열 종료
					status = 0;
				} else {
					cmd.append(ch);
				}
				
			}
			
		} // End of for 
		
		// 종료 상태가 정상인지 확인
		// status가 0이 아니면 문자열 상태 중에 종료된 것이므로 예외 발생
		if(status != 0) {
			throw new Exception("Unexpected end:" + status);
		}
		
		// cmd가 비어 있지 않으면 cmd 목록(cmds)에 추가
		if(cmd.length() != 0) {
			cmds.add(cmd.toString());
		}
		
		// 명령어 목록 반환
		return cmds.stream().toArray(String[]::new);
	}

}

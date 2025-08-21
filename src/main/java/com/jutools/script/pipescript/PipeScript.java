package com.jutools.script.pipescript;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jutools.StringUtil;
import com.jutools.ThreadUtil;

/**
 * 파이프 스크립트<br>
 * ex) type=='cpu' and host=='server1' | stat(usage) | print()
 *
 * @author jmsohn
 */
public class PipeScript {

	/** 중단 여부 */
	private volatile boolean stop;

	/** 파이프 스크립트 목록 */
	private List<ScriptRunner> scriptRunnerList;

	/** 각 파이프 스크립트를 실행하는 실행자 */
	private ExecutorService execSvc;

	
	/**
	 * 생성자
	 *
	 * @param pipeScript 파이프 스크립트 문자열
	 * @param methodClassAry 메소드 클래스 목록 
	 */
	private PipeScript(String pipeScript, Class<?>[] methodClassAry) throws Exception {

		// 입력값 검증
		if(StringUtil.isBlank(pipeScript) == true) {
			throw new IllegalArgumentException("pipe script is null or blank.");
		}

		// script에서 파이프(|)를 분리하여 script 객체 목록 생성
		String[] scriptStrAry = pipeScript.split("\\|");
		this.scriptRunnerList = new ArrayList<>();
		
		ScriptRunner preRunner = null;
		
		for(String scriptStr: scriptStrAry) {
			
			// script runner 생성 후 목록에 추가
			ScriptRunner scriptRunner = new ScriptRunner(scriptStr, methodClassAry);
			this.scriptRunnerList.add(scriptRunner);
			
			// script runner 간 파이프 연결
			if(preRunner != null) {
				preRunner.setNext(scriptRunner);
			}
			
			preRunner = scriptRunner;
		}

		// 상태 설정
		this.stop = true;
	}

	/**
	 * 파이프 스크립트 문자열 컴파일 후 객체 반환
	 *
	 * @param pipeScript 파이프 스크립트 문자열
	 * @param methodClassAry 메소드 클래스 목록
  	 * @return 생성된 파이프 스크립트 객체
  	 */
	public static PipeScript compile(
		String pipeScript,
		Class<?>... methodClassAry
	) throws Exception {
		
		return new PipeScript(pipeScript, methodClassAry);
	}

	/**
 	 * 파이프 스크립트 실행
   	 */
	public void run() throws Exception {

		// 실행 상태 검사
		if(this.stop == false) {
			throw new IllegalStateException("pipe script is already started.");
		}

		if(this.scriptRunnerList == null || this.scriptRunnerList.size() == 0) {
			throw new IllegalStateException("pipe script is not set.");
		}
		
		// 실행 상태를 실행으로 변경
		this.stop = false;
		
		// 스크립트 실행
		this.execSvc = Executors.newFixedThreadPool(this.scriptRunnerList.size());
		for(ScriptRunner scriptRunner: this.scriptRunnerList) {
			this.execSvc.submit(scriptRunner);
		}
	}

	/**
 	 * 파이프 스크립트 실행 중단
   	 */
	public void stop() throws Exception {

		// 중단 여부 확인
		if(this.stop == true) {
			return;
		}

		// 각 Script Runner 중단
		for(ScriptRunner scriptRunner: scriptRunnerList) {
			try {
				scriptRunner.stop();
			} catch(Exception ex) {}
		}

		// 스레드 shutdown
		ThreadUtil.shutdown(this.execSvc);

		// 상태 변경
		this.stop = true;
	}
	
	/**
	 * 파이프 스크립트의 입력 큐 반환
	 * 
	 * @return 파이프 스크립트 입력 큐
	 */
	public BlockingQueue<Map<String, Object>> getInQueue() {
		return this.scriptRunnerList.get(0).getInQ();
	}
}

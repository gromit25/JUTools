package com.jutools.script.pipescript;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.jutools.StringUtil;
import com.jutools.script.olexp.OLExp;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 파이프 스크립트<br>
 * ex) type=='cpu' and host=='server1' | stat(usage) | print()
 *
 * @author jmsohn
 */
@Slf4j
public class PipeScript {

	/** 변수 컨테이너의 Script Runner 객체 키 문자열 */
	public static final String SCRIPT_RUNNER = "<script_runner>";
	
	
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

		if(this.stop == false) {
			throw new IllegalStateException("pipe script is already started.");
		}

		if(this.scriptRunnerList == null || this.scriptRunnerList.size() == 0) {
			throw new IllegalStateException("pipe script is not set.");
		}
		
		this.execSvc = Executors.newFixedThreadPool(this.scriptRunnerList.size());
		for(ScriptRunner scriptRunner: this.scriptRunnerList) {
			this.execSvc.submit(scriptRunner);
		}

		this.stop = false;
	}

	/**
 	 * 파이프 스크립트 실행 중단
   	 */
	public void stop() {

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
		return this.scriptRunnerList.get(0).inQ;
	}
	
	/**
	 * 변수 컨테이너에서 현재 Script Runner를 반환함
	 * 
	 * @param map 변수 컨테이너
	 * @return Script Runner
	 */
	public static ScriptRunner getScriptRunner(Map<String, ?> map) throws Exception {
		
		if(map == null) {
			throw new IllegalArgumentException("map is null.");
		}
		
		if(map.containsKey(SCRIPT_RUNNER) == false) {
			throw new IllegalArgumentException("script runner(" + SCRIPT_RUNNER + ") is not found.");
		}
		
		Object scriptRunner = map.containsKey(SCRIPT_RUNNER);
		if(scriptRunner instanceof ScriptRunner == false) {
			throw new IllegalArgumentException("script runner is unexpected type: " + scriptRunner.getClass());
		}

		return (ScriptRunner)scriptRunner;
	}
	
	/**
	 * 파이프 스크립트의 실행 객체<br>
  	 * 파이프로 연결된 하나의 스크립트 실행 객체임
  	 *
	 * @author jmsohn
	 */
	public static class ScriptRunner implements Runnable {

		/** 중단 여부 */
		private volatile boolean stop = true;
		
		/** 파이프 입력 큐 */
		private BlockingQueue<Map<String, Object>> inQ;
		
		/** 파이프 출력 큐 */
		@Getter
		private BlockingQueue<Map<String, Object>> outQ;
		
		/** 실행할 스크립트 객체 */
		private OLExp script;

		/** 스크립트 실행 스레드 관리 객체 */
		private ExecutorService scriptExecSvc = Executors.newCachedThreadPool();

		/** 스크립트 Heap - 상태 저장용 */
		@Getter
		private Map<String, Object> heap;

		/** 데이터 수신 타이머 */
		private Timer timer;

		
		/**
  		 * 생성자
		 *
		 * @param scirptStr 스크립트 문자열
		 * @param methodClassAry 메소드 클래스 배열
		 */
		ScriptRunner(String scriptStr, Class<?>[] methodClassAry) throws Exception {

			// 스크립트 컴파일
			this.script = OLExp.compile(scriptStr, methodClassAry);

			// 입력/출력 큐 설정
			this.inQ = new LinkedBlockingQueue<>();
			this.outQ = null;
			
			// Heap 생성
			this.heap = new ConcurrentHashMap<>();
		}

		/**
  		 * 실행 메소드
   		 */
		@Override
		public void run() {

			this.stop = false;

			while(this.stop == false) {

				try {
					
					// 입력 큐에서 값을 전달 받음
					Map<String, Object> values = this.inQ.poll(1, TimeUnit.SECONDS);;
					if(values == null) {
						continue;
					}

					// 타이머가 있는 경우 touch
					if(this.timer != null) {
						this.timer.touch();
					}

					// 스크립트 실행
					this.scriptExecSvc.submit(() -> {
					
						// 1. script 수행
						values.put(SCRIPT_RUNNER, this);
						Object result = this.script.execute(values).pop(Object.class);
						values.remove(SCRIPT_RUNNER);
					
						// 2. 결괏값의 종류에 처리 수행
						if(result instanceof Boolean) {
							if(((Boolean)result) == false) {
								return;
							}
						} else if(result instanceof Map) {
							@SuppressWarnings("unchecked")
							values.putAll((Map<String, Object>)result);
						}
					
						// 3. 다음 컴포넌트로 데이터 전달
						if(this.outQ != null) {
							this.outQ.put(values);
						}
					});
					
				} catch(InterruptedException iex) {
					log.error("interrupt is occured.", iex);
					this.stop = true;
				} catch(Exception ex) {
					log.error("script error: " + script.getScript(), ex);
				}
			}
		}

		/**
		 * 파이프를 통해 연결될 다음 스크립트 객체 설정
   		 *
		 * @param next 다음 스크립트 객체
		 * @return 현재 객체
		 */
		ScriptRunner setNext(ScriptRunner next) throws Exception {

			if(next == null) {
				throw new IllegalArgumentException("next component is null.");
			}

			this.outQ = next.inQ;

			return this;
		}

		/**
		 * 타이머 설정 및 실행
		 *
		 * @param timeout 타임 아웃 설정
		 * @param listenerAry 리스너 목록
		 */
		public <T extends TimeoutEvent> void runTimer(
			long timeout,
			Consumer<T>... listenerAry
		) throws Exception {

			// 타이머 객체 생성
			this.timer = new Timer<T>(timeout);

			// 타이머에 리스너 추가
			if(listenerAry != null && listenerAry.length > 0) {
				for(Consumer<? extends TimeoutEvent> listener: listenerAry) {
					this.timer.add(listener);
				}
			}

			// 타이머 실행
			this.timer.run();
		}

		/**
		 * 스레드 중지
		 */
		public void stop() throws Exception {

			// 이미 중단 되어 있으면 반환
			if(this.stop == true) {
				return;
			}

			// 타이머 중단
			if(this.timer != null) {
				this.timer.stop();
			}

			// 스레드 중단
			ThreadUtil.shutdown(this.scriptExecSvc);

			// 중단 상태로 변경
			this.stop = true;
		}
	} // end of ScriptRunner
}

package com.jutools.script.engine;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.jutools.script.parser.AbstractParser;
import com.jutools.script.engine.instructions.BuiltInMethods;
import com.jutools.script.engine.instructions.INVOKE;
import com.jutools.script.engine.instructions.Instruction;
import com.jutools.script.engine.instructions.MethodAlias;

import lombok.Getter;

/**
 * 스크립트 엔진의 추상 클래스
 * 
 * @author jmsohn
 */
public abstract class AbstractEngine {
	
	/** 스크립트 문자열 */
	@Getter
	protected String script;
	
	/** 스크립트 명령어 목록 */
	protected List<Instruction> insts;
	
	/** 스크립트 스레드 */
	@Getter
	protected ThreadLocal<ScriptThread> thread = ThreadLocal.withInitial(ScriptThread::new);
	
	/** 스크립트 내의 alias 메소드의 실제 메소드 - K: 메소드 alias 명, V: 실제 수행 메소드 */
	protected Map<String, MethodHandle> methods = new HashMap<>();
	
	/**
	 * 스크립트 엔진의 스레드
	 * 
	 * @author jmsohn
	 */
	class ScriptThread {
		
		/** 스크립트 수행시 사용할 Stack */
		Stack<Object> stack = new Stack<Object>();
		
		/** Program Counter : 현재 실행 위치 */
		int pc = 0;
		
		/**
		 * 스레드 초기화
		 */
		void clear() {
			this.stack.clear();
			this.pc = 0;
		}
	}

	/**
	 * root parser 반환
	 * -> root parser : 가장 처음 호출되는 parser
	 * 
	 * @return root parser
	 */
	protected abstract AbstractParser<Instruction> getRootParser() throws Exception;
	
	/**
	 * 생성자
	 * 
	 * @param script 스크립트
	 */
	protected AbstractEngine(String script) throws Exception {
		
		if(script == null) {
			throw new NullPointerException("script is null");
		}
		
		// 스크립트 설정
		this.script = script;
		
		// 기본 built in method 설정
		this.setMethod(BuiltInMethods.class);
		
		// 스크립트 파싱
		try {
			
			AbstractParser<Instruction> parser = this.getRootParser();
			this.insts = parser.parse(script).travelPostOrder();
			
		} catch(Exception ex) {
			
			Throwable t = ex;
			while(t.getCause() != null) {
				t = t.getCause();
			}
			
			throw (Exception)t;
		}
		
		// 메소드 링킹
		this.linkMethod();
	}

	/**
	 * 실제 실행할 메소드 설정
	 * 
	 * @param methodClass 실제 실행할 메소드를 가지는 클래스(MethodAlias Annotation 사용)
	 * @return 현재 객체(fluent 코딩용)
	 */
	public AbstractEngine setMethod(Class<?> methodClass) throws Exception {

		// 입력값 검증
		if(methodClass == null) {
			throw new NullPointerException("method class is null");
		}
		
		// Method Handle 을 획득하기 위한 Lookup 객체 생성
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		
		// 각 메소드 별로 검사하여 메소드 추가
		for(Method method: methodClass.getMethods()) {
			
			// 메소드에 MethodAlias Annotation을 가져옴
			MethodAlias methodMap  = method.getAnnotation(MethodAlias.class);
			if(methodMap == null) {
				continue;
			}
			
			// 메소드는 public static 이어야 함
			int modifier = method.getModifiers();
			
			if(Modifier.isStatic(modifier) == false) {
				throw new Exception(methodClass.getCanonicalName() + "." + method.getName() + " method is not static");
			}
			
			if(Modifier.isPublic(modifier) == false) {
				throw new Exception(methodClass.getCanonicalName() + "." + method.getName() + " method is not public");
			}
			
			// Method Handle 획득
			MethodHandle methodHandle = lookup.unreflect(method);
			
			// 메소드 추가
			this.methods.put(methodMap.alias(), methodHandle);
		}
		
		return this;
	}
	
	/**
	 * 메소드 호출(INVOKE) 명령어의 alias에 실제 메소드를 연결(linking)
	 * 
	 * @return 현재 객체(fluent 코딩용)
	 */
	private AbstractEngine linkMethod() throws Exception {
		
		// 명령어 목록이 없는 경우 예외 발생
		if(this.insts == null) {
			throw new NullPointerException("instruction list is null");
		}
		
		// 명령어 목록의 명령어 에서 메소드 호출(INVOKE) 탐색하여 link 작업 수행
		for(Instruction inst: this.insts) {
			
			if(inst instanceof INVOKE == false) {
				continue;
			}
			
			// method link 작업 수행
			INVOKE invokeInst = (INVOKE)inst;
			
			String alias = invokeInst.getMethodAlias(); // method alias
			MethodHandle method = this.getMethod(alias); // get aliased method
			
			invokeInst.setMethod(method);
		}
		
		return this;
	}
	
	/**
	 * alias에 해당하는 메소드를 반환하는 메소드
	 * 
	 * @param alias 검색할 alias
	 * @return 메소드 핸들
	 */
	private MethodHandle getMethod(String alias) throws Exception {
		
		if(this.methods.containsKey(alias) == false) {
			throw new Exception("method is not found:" + alias);
		}
		
		return this.methods.get(alias);
	}
	
	/**
	 * 스크립트 명령어 수행
	 * 
	 * @param values 
	 * @return 현재 객체(fluent 코딩용)
	 */
	public AbstractEngine execute(Map<String, ?> values) throws Exception {

		// 스크립트 스레드 획득 및 초기화
		ScriptThread t = this.thread.get();
		t.clear();
		
		// 각 명령어 별로 실행
		while(t.pc < this.insts.size()) {
			
			Instruction inst = this.insts.get(t.pc);
			t.pc += inst.execute(t.stack, values);
		}
		
		return this;
	}
	
	/**
	 * 스크립트 명령어 수행(디버그 모드)
	 * 
	 * @param values 
	 * @return 현재 객체(fluent 코딩용)
	 */
	public AbstractEngine executeForDebug(Map<String, ?> values) throws Exception {

		// 스크립트 스레드 획득 및 초기화
		ScriptThread t = this.thread.get();
		t.clear();
		
		// 각 명령어 별로 실행
		while(t.pc < this.insts.size()) {
			
			Instruction inst = this.insts.get(t.pc);
			t.pc += inst.execute(t.stack, values);
			
			System.out.println(t.pc + ":" + inst.toString());
		}
		
		return this;
	}
	
	/**
	 * stack의 최상단 값을 뽑아서 반환 
	 * 
	 * @param type stack의 값을 casting할 타입
	 * @return stack의 최상단 값
	 */
	public <T> T pop(Class<T> type) throws Exception {
		
		// 스크립트 스레드 획득
		ScriptThread t = this.thread.get();
		
		if(t == null || t.stack.isEmpty() == true) {
			
			return null;
			
		} else {
			
			Object obj = t.stack.pop();
			return type.cast(obj);
		}
	}
	
	@Override
	public String toString() {
		
		StringBuilder toString = new StringBuilder("");
		
		toString
			.append("INSTRUCTION LIST\n")
			.append("-------------------------\n");
		
		if(this.insts != null && this.insts.size() != 0) {
			for(Instruction inst: this.insts) {
				toString.append(inst).append("\n");
			}
		} else {
			toString.append("instruction list is empty.\n");
		}
		
		return toString.toString();
	}
}

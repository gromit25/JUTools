package com.jutools.olexp.parser;

import com.jutools.instructions.AND;
import com.jutools.instructions.DUP;
import com.jutools.instructions.IF_FALSE;
import com.jutools.instructions.IF_TRUE;
import com.jutools.instructions.Instruction;
import com.jutools.instructions.OR;
import com.jutools.parserfw.AbstractParser;
import com.jutools.parserfw.EndStatusType;
import com.jutools.parserfw.TransferBuilder;
import com.jutools.parserfw.TransferEventHandler;
import com.jutools.parserfw.TreeNode;

/**
 * boolean 연산(and, or) 파싱 수행
 * 
 * @author jmsohn
 */
public class BooleanParser extends AbstractParser<Instruction> {
	
	/** and,or 연산의 첫번째 파라미터의 tree node */
	private TreeNode<Instruction> p1;
	
	/** and,or 연산의 두번째 파라미터의 tree node */
	private TreeNode<Instruction> p2;
	
	/** and,or 연산 tree node */
	private TreeNode<Instruction> op;

	/**
	 * 생성자
	 */
	public BooleanParser() throws Exception {
		super();
	}
	
	/**
	 * 시작상태 반환
	 */
	@Override
	protected String getStartStatus() {
		return "START";
	}
	
	/**
	 * 초기화 수행
	 */
	@Override
	protected void init() throws Exception {
		
		// 속성 변수 초기화
		this.p1 = null;
		this.p2 = null;
		this.op = null;
		
		// 상태 전이 맵 설정
		this.putTransferMap("START", new TransferBuilder()
				.add(" \t\r\n", "START")
				.add("^ \t\r\n", "EQUALITY_1", -1)
				.build());
		
		// --- P1
		this.putTransferMap("EQUALITY_1", new TransferBuilder()
				.add(" \t\r\n", "EQUALITY_1")
				.add("aA", "AND_OP_1")
				.add("oO", "OR_OP_1")
				.add("^ \t\r\naAoO", "END", -1)
				.build());
		
		// --- AND 오퍼레이션
		this.putTransferMap("AND_OP_1", new TransferBuilder()
				.add("nN", "AND_OP_2")
				.add("^nN", "ERROR", -1)
				.build());
		
		this.putTransferMap("AND_OP_2", new TransferBuilder()
				.add("dD", "AND_OP_3")
				.add("^dD", "ERROR", -1)
				.build());
		
		this.putTransferMap("AND_OP_3", new TransferBuilder()
				.add(" \t\r\n", "AND_OP_END")
				.add("(", "BOOLEAN", -1)
				.add("^ \t\r\n(", "ERROR", -1)
				.build());
		
		this.putTransferMap("AND_OP_END", new TransferBuilder()
				.add(" \t\r\n", "AND_OP_END")
				.add("(", "BOOLEAN", -1)
				.add("^ \t\r\n(", "EQUALITY_2", -1)
				.build());
		
		// --- OR 오퍼레이션
		this.putTransferMap("OR_OP_1", new TransferBuilder()
				.add("rR", "OR_OP_2")
				.add("^ \t\r\n(", "ERROR", -1)
				.build());
		
		this.putTransferMap("OR_OP_2", new TransferBuilder()
				.add(" \t\r\n", "OR_OP_END")
				.add("(", "BOOLEAN", -1)
				.add("^ \t\r\n(", "ERROR", -1)
				.build());
		
		this.putTransferMap("OR_OP_END", new TransferBuilder()
				.add(" \t\r\n", "OR_OP_END")
				.add("(", "BOOLEAN", -1)
				.add("^ \t\r\n(", "EQUALITY_2", -1)
				.build());
		
		// --- P2 
		this.putTransferMap("EQUALITY_2", new TransferBuilder()
				.add(" \t\r\n", "EQUALITY_2")
				.add("aA", "AND_OP_1")
				.add("oO", "OR_OP_1")
				.add("^ \t\r\naAoO", "END", -1)
				.build());
		
		// 종료 상태 추가
		this.putEndStatus("EQUALITY_1");
		this.putEndStatus("EQUALITY_2");
		this.putEndStatus("BOOLEAN");
		this.putEndStatus("END", EndStatusType.IMMEDIATELY_END);
		this.putEndStatus("ERROR", EndStatusType.ERROR);
	}
	
	/**
	 * p1 상태 전이 핸들러 메소드
	 * 
	 * @param event 상태 전이 이벤트 정보
	 */
	@TransferEventHandler(
			source={"START"},
			target={"EQUALITY_1"}
	)
	public void handleP1(Event event) throws Exception {
		
		EqualityParser parser = new EqualityParser();
		this.p1 = parser.parse(event.getReader());
	}
	
	/**
	 * and equality 핸들러 메소드
	 * 
	 * @param event 상태 전이 이벤트 정보
	 */
	@TransferEventHandler(
			source={"AND_OP_END"},
			target={"EQUALITY_2"}
	)
	public void handleAndOpEquality(Event event) throws Exception {
		
		// and 오퍼레이션 생성
		this.op = new TreeNode<>(new AND());
		
		// p2 파싱
		EqualityParser parser = new EqualityParser();
		this.p2 = parser.parse(event.getReader());

		// short circuit 구성
		this.composeShortCircuit();
	}

	/**
	 * and boolean 핸들러 메소드
	 * 
	 * @param event 상태 전이 이벤트 정보
	 */
	@TransferEventHandler(
			source={"AND_OP_3", "AND_OP_END"},
			target={"BOOLEAN"}
	)
	public void handleAndOpBoolean(Event event) throws Exception {
		
		// and 오퍼레이션 생성
		this.op = new TreeNode<>(new AND());
		
		// p2 파싱
		BooleanParser parser = new BooleanParser();
		this.p2 = parser.parse(event.getReader());
		
		// short circuit 구성
		this.composeShortCircuit();
	}
	
	/**
	 * or equality 핸들러 메소드
	 * 
	 * @param event 상태 전이 이벤트 정보
	 */
	@TransferEventHandler(
			source={"OR_OP_END"},
			target={"EQUALITY_2"}
	)
	public void handleOrOpEquality(Event event) throws Exception {
		
		// or 오퍼레이션 생성
		this.op = new TreeNode<>(new OR());
		
		// p2 파싱
		EqualityParser parser = new EqualityParser();
		this.p2 = parser.parse(event.getReader());
		
		// short circuit 구성
		this.composeShortCircuit();
	}
	
	/**
	 * or boolean 핸들러 메소드
	 * 
	 * @param event 상태 전이 이벤트 정보
	 */
	@TransferEventHandler(
			source={"OR_OP_2", "OR_OP_END"},
			target={"BOOLEAN"}
	)
	public void handleOrOpBoolean(Event event) throws Exception {
		
		// or 오퍼레이션 생성
		this.op = new TreeNode<>(new OR());
		
		// p2 파싱
		BooleanParser parser = new BooleanParser();
		this.p2 = parser.parse(event.getReader());
		
		// short circuit 구성
		this.composeShortCircuit();
	}
	
	/**
	 * and, or 의 short circuit 구성 메소드
	 */
	private void composeShortCircuit() throws Exception {
		
		// short circuit 생성 및 추가 
		TreeNode<Instruction> shortCircuit = new TreeNode<>();
		
		// and, or 연산에 따라 점프 오퍼레이션 설정
		if(this.op.getData() instanceof AND) {
			
			shortCircuit.setData(
				new IF_TRUE(1, this.p2.getChildCount() + 3) // AND 다음 연산까지 이동(+3)
			);
			
		} else if(this.op.getData() instanceof OR) {
			
			shortCircuit.setData(
				new IF_FALSE(1, this.p2.getChildCount() + 3) // OR 다음 연산까지 이동(+3)
			);
			
		} else {
			throw new Exception("unexpected operation:" + this.op.getClass());
		}
		
		// p1 추가
		shortCircuit.addChild(this.p1);
		
		// dup 명령어(스택 최상단 복사) 추가 -> p1 결과 복사용
		shortCircuit.addChild(new TreeNode<Instruction>(new DUP()));
		
		// p2 추가
		this.op.addChild(shortCircuit);
		this.op.addChild(this.p2);
		
		// 오퍼레이션 노드를 p1 노드로 설정
		// 삼항 이상의 연산에서 p1 이 되기 때문
		// ex) true and false or false -> (true and false) or false
		this.p1 = this.op;
	}
	
	/**
	 * 파싱 종료 처리
	 */
	@Override
	protected void exit() throws Exception {
		
		// 파싱 노드 설정
		this.setNode(this.p1);
	}
}

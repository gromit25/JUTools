package com.jutools.script.olexp.parser;

import com.jutools.script.engine.instructions.AND;
import com.jutools.script.engine.instructions.DUP;
import com.jutools.script.engine.instructions.IF_TRUE;
import com.jutools.script.engine.instructions.Instruction;
import com.jutools.script.parser.AbstractParser;
import com.jutools.script.parser.EndStatusType;
import com.jutools.script.parser.TransferBuilder;
import com.jutools.script.parser.TransferEventHandler;
import com.jutools.script.parser.TreeNode;

/**
 * Boolean AND 연산 파서 클래스
 * 
 * @author jmsohn
 */
public class BoolAndParser extends AbstractParser<Instruction> {
	
	
	/** and 연산의 첫번째 파라미터의 tree node */
	private TreeNode<Instruction> p1;
	
	/** and 연산의 두번째 파라미터의 tree node */
	private TreeNode<Instruction> p2;
	
	/** and 연산 tree node */
	private TreeNode<Instruction> op;
	

	/**
	 * 생성자
	 */
	public BoolAndParser() throws Exception {
		super();
	}

	@Override
	protected String getStartStatus() {
		return "START";
	}

	@Override
	protected void init() throws Exception {
		
		// 속성 변수 초기화
		this.p1 = null;
		this.p2 = null;
		
		// 상태 전이 맵 설정
		this.putTransferMap("START", new TransferBuilder()
				.add(" \t\r\n", "START")
				.add("^ \t\r\n", "P_1", -1)
				.build());
		
		// --- P1
		this.putTransferMap("P_1", new TransferBuilder()
				.add(" \t\r\n", "P_1")
				.add("aA", "AND_OP_1")
				.add("^ \t\r\naA", "END", -1)
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
				.add("(", "STMT", -1)
				.add("^ \t\r\n(", "ERROR", -1)
				.build());
		
		this.putTransferMap("AND_OP_END", new TransferBuilder()
				.add(" \t\r\n", "AND_OP_END")
				.add("(", "STMT", -1)
				.add("^ \t\r\n(", "P_2", -1)
				.build());
		
		// --- P2 
		this.putTransferMap("P_2", new TransferBuilder()
				.add(" \t\r\n", "P_2")
				.add("aA", "AND_OP_1")
				.add("^ \t\r\naA", "END", -1)
				.build());
		
		// 종료 상태 추가
		this.putEndStatus("P_1");
		this.putEndStatus("P_2");
		this.putEndStatus("STMT");
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
			target={"P_1"}
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
			target={"P_2"}
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
			target={"STMT"}
	)
	public void handleAndOpBoolean(Event event) throws Exception {
		
		// and 오퍼레이션 생성
		this.op = new TreeNode<>(new AND());
		
		// p2 파싱
		BoolOrParser parser = new BoolOrParser();
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
		shortCircuit.setData(
			new IF_TRUE(1, this.p2.getTotChildCount() + 3) // AND 다음 연산까지 이동(+3)
		);
		
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

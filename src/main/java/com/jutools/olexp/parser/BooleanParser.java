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
	/** and,or 연산 */
	private Instruction op;

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
				.add(" \t", "START")
				.add("^ \t", "EQUALITY", -1)
				.build());
		
		this.putTransferMap("EQUALITY", new TransferBuilder()
				.add(" \t", "EQUALITY")
				.add("aA", "AND_OP_1")
				.add("oO", "OR_OP_1")
				.add("^ \taoAO", "END", -1)
				.build());
		
		this.putTransferMap("AND_OP_1", new TransferBuilder()
				.add("nN", "AND_OP_2")
				.add("^nN", "ERROR", -1)
				.build());
		
		this.putTransferMap("AND_OP_2", new TransferBuilder()
				.add("dD", "AND_OP_3")
				.add("^dD", "ERROR", -1)
				.build());
		
		this.putTransferMap("AND_OP_3", new TransferBuilder()
				.add(" \t", "BOOLEAN")
				.add("(", "BOOLEAN", -1)
				.add("^ \t(", "ERROR", -1)
				.build());
		
		this.putTransferMap("OR_OP_1", new TransferBuilder()
				.add("rR", "OR_OP_2")
				.add("^ \t(", "ERROR", -1)
				.build());
		
		this.putTransferMap("OR_OP_2", new TransferBuilder()
				.add(" \t", "BOOLEAN")
				.add("(", "BOOLEAN", -1)
				.add("^ \t(", "ERROR", -1)
				.build());
		
		// 종료 상태 추가
		this.putEndStatus("EQUALITY");
		this.putEndStatus("BOOLEAN", EndStatusType.IMMEDIATELY_END);
		this.putEndStatus("END", EndStatusType.IMMEDIATELY_END);
		this.putEndStatus("ERROR", EndStatusType.ERROR);
	}
	
	/**
	 * and,or 의 첫번째 파라미터 상태로 전이시 핸들러 메소드
	 * 
	 * @param event 상태 전이 이벤트 정보
	 */
	@TransferEventHandler(
			source={"START"},
			target={"EQUALITY"}
	)
	public void handleP1(Event event) throws Exception {
		
		EqualityParser parser = new EqualityParser();
		this.p1 = parser.parse(event.getReader());

	}
	
	/**
	 * and 의 연산자 상태로 전이시 핸들러 메소드
	 * 
	 * @param event 상태 전이 이벤트 정보
	 */
	@TransferEventHandler(
			source={"AND_OP_3"},
			target={"BOOLEAN"}
	)
	public void handleAndOp(Event event) throws Exception {
		
		this.op = new AND();
		
		BooleanParser parser = new BooleanParser();
		this.p2 = parser.parse(event.getReader());
	}
	
	/**
	 * or 의 연산자 상태로 전이시 핸들러 메소드
	 * 
	 * @param event 상태 전이 이벤트 정보
	 */
	@TransferEventHandler(
			source={"OR_OP_2"},
			target={"BOOLEAN"}
	)
	public void handleOrOp(Event event) throws Exception {
		
		this.op = new OR();
		
		BooleanParser parser = new BooleanParser();
		this.p2 = parser.parse(event.getReader());
	}
	
	/**
	 * 파싱 종료 처리
	 */
	@Override
	protected void exit() throws Exception {
		
		if(this.op != null && this.p2 != null) {
			
			// and, or 에 따라 short circuit 구성
			TreeNode<Instruction> shortCircuit = new TreeNode<>();
			
			if(this.op instanceof AND) {
				
				shortCircuit.setData(
					new IF_TRUE(1, this.p2.getChildCount() + 2) // AND 다음 연산까지 이동(+2)
				);
				
			} else if(this.op instanceof OR) {
				
				shortCircuit.setData(
					new IF_FALSE(1, this.p2.getChildCount() + 2) // OR 다음 연산까지 이동(+2)
				);
				
			} else {
				throw new Exception("unexpected operation:" + this.op.getClass());
			}
			
			shortCircuit.addChild(this.p1);
			shortCircuit.addChild(new TreeNode<Instruction>(new DUP()));
			
			// and,or 연산이 존재하는 경우
			this.setNodeData(this.op);
			this.addChild(shortCircuit);
			this.addChild(this.p2);
			
		} else {
			
			// and,or 연산이 존재하지 않는 경우
			this.setNode(this.p1);
		}
	}
}

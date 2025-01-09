package com.jutools.olexp.parser;

import com.jutools.parserfw.AbstractParser;
import com.jutools.parserfw.EndStatusType;
import com.jutools.parserfw.TransferBuilder;
import com.jutools.parserfw.TransferEventHandler;
import com.jutools.parserfw.TreeNode;
import com.jutools.script.instructions.DIV;
import com.jutools.script.instructions.Instruction;
import com.jutools.script.instructions.MOD;
import com.jutools.script.instructions.MUL;

/**
 * *,/,% 연산 파싱 수행
 * 
 * @author jmsohn
 */
public class TermParser extends AbstractParser<Instruction> {
	
	/** *,/,% 연산의 첫번째 파라미터의 tree node */
	private TreeNode<Instruction> p1;
	
	/** *,/,% 연산의 두번째 파라미터의 tree node */
	private TreeNode<Instruction> p2;
	
	/** *,/,% 연산 tree node */
	private TreeNode<Instruction> op;

	/**
	 * 생성자
	 */
	public TermParser() throws Exception {
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
				.add("^ \t", "FACTOR_1", -1)
				.build());
		
		this.putTransferMap("FACTOR_1", new TransferBuilder()
				.add(" \t", "FACTOR_1")
				.add("\\*\\/\\%", "OPERATION")
				.add("^ \t\\*\\/", "END", -1)
				.build());
		
		this.putTransferMap("OPERATION", new TransferBuilder()
				.add(" \t", "OPERATION")
				.add("^ \t", "FACTOR_2", -1)
				.build());
		
		this.putTransferMap("FACTOR_2", new TransferBuilder()
				.add(" \t", "FACTOR_2")
				.add("\\*\\/\\%", "OPERATION")
				.add("^ \t\\*\\/", "END", -1)
				.build());
		
		// 종료 상태 추가
		this.putEndStatus("FACTOR_1");
		this.putEndStatus("FACTOR_2");
		this.putEndStatus("END", EndStatusType.IMMEDIATELY_END);
	}
	
	/**
	 * *,/,% 의 첫번째 파라미터 상태로 전이시 핸들러 메소드
	 * 
	 * @param event
	 */
	@TransferEventHandler(
			source={"START"},
			target={"FACTOR_1"}
	)
	public void handleP1(Event event) throws Exception {
		
		FactorParser parser = new FactorParser();
		this.p1 = parser.parse(event.getReader());
	}
	
	/**
	 * *,/,%의 연산자 상태로 전이시 핸들러 메소드
	 * 
	 * @param event
	 */
	@TransferEventHandler(
			source={"FACTOR_1", "FACTOR_2"},
			target={"OPERATION"}
	)
	public void handleOp(Event event) throws Exception {
		
		if(event.getCh() == '*') {
			this.op = new TreeNode<>(new MUL());
		} else if(event.getCh() == '/') {
			this.op = new TreeNode<>(new DIV());
		} else if(event.getCh() == '%') {
			this.op = new TreeNode<>(new MOD());
		} else {
			throw new Exception("Unexpected operation:" + event.getCh());
		}
		
		// 첫번째 파라미터 추가
		this.op.addChild(this.p1);
		
		// 
		this.p1 = this.op;
	}
	
	/**
	 * *,/,%의 첫번째 파라미터 상태로 전이시 핸들러 메소드
	 * 
	 * @param event
	 */
	@TransferEventHandler(
			source={"OPERATION"},
			target={"FACTOR_2"}
	)
	public void handleP2(Event event) throws Exception {
		
		FactorParser parser = new FactorParser();
		this.p2 = parser.parse(event.getReader());
		
		// 두번째 파라미터 추가
		this.op.addChild(this.p2);
	}
	
	/**
	 * 파싱 종료 처리
	 */
	@Override
	protected void exit() throws Exception {
		
		// 현재 노드 설정
		this.setNode(this.p1);
	}
}

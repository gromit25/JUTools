package com.jutools.mathexp.parser.script;

import com.jutools.mathexp.instructions.ADD;
import com.jutools.mathexp.instructions.Instruction;
import com.jutools.mathexp.instructions.MINUS;
import com.jutools.mathexp.parser.AbstractParser;
import com.jutools.mathexp.parser.TransferBuilder;
import com.jutools.mathexp.parser.TransferEventHandler;
import com.jutools.mathexp.parser.TreeNode;

/**
 * 
 * 
 * @author jmsohn
 */
public class ArithmaticParser extends AbstractParser<Instruction> {
	
	/** */
	private TreeNode<Instruction> p1;
	/** */
	private TreeNode<Instruction> p2;
	/** */
	private Instruction operation;

	public ArithmaticParser() throws Exception {
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
		this.operation = null;
		
		// 상태 변환 맵 추가
		this.putTransferMap("START", new TransferBuilder()
				.add(" \t", "START")
				.add("^ \t", "TERM_1", true)
				.build());
		
		this.putTransferMap("TERM_1", new TransferBuilder()
				.add(" \t", "TERM_1")
				.add("\\+\\-", "OPERATION")
				.add("^ \t\\+\\-", "ARITHMATIC_END", true)
				.build());
		
		this.putTransferMap("OPERATION", new TransferBuilder()
				.add(" \t", "OPERATION")
				.add("^ \t", "TERM_2", true)
				.build());
		
		this.putTransferMap("TERM_2", new TransferBuilder()
				.add(" \t", "TERM_2")
				.add("\\+\\-", "OPERATION")
				.add("^ \t\\+\\-", "ARITHMATIC_END", true)
				.build());
		
		// 종료 상태 추가
		this.putEndStatus("TERM_1");
		this.putEndStatus("TERM_2");
		this.putEndStatus("ARITHMATIC_END", 1); // ARITHMATIC_END 상태로 들어오면 Parsing을 중지
	}

	/**
	 * 
	 * @param event
	 */
	@TransferEventHandler(
			source={"START", "TERM_1"},
			target={"TERM_1"}
	)
	public void handleP1(Event event) throws Exception {
		
		TermParser parser = new TermParser();
		this.p1 = parser.parse(event.getReader());

	}
	
	/**
	 * 
	 * @param event
	 */
	@TransferEventHandler(
			source={"TERM_1"},
			target={"OPERATION"}
	)
	public void handleOp(Event event) throws Exception {
		
		if(event.getChar() == '+') {
			this.operation = new ADD();
		} else if(event.getChar() == '-') {
			this.operation = new MINUS();
		} else {
			throw new Exception("Unexpected operation:" + event.getChar());
		}
		
	}
	
	/**
	 * 
	 * @param event
	 */
	@TransferEventHandler(
			source={"OPERATION"},
			target={"TERM_2"}
	)
	public void handleP2(Event event) throws Exception {
		//
		TermParser parser = new TermParser();
		this.p2 = parser.parse(event.getReader());
	}
	
	/**
	 * 삼항 이상 연산 처리<br>
	 * <pre>
	 * ex) 2 +    3           -       4
	 *        (TERM_2 -> OPERATION)
	 * </pre>
	 *   
	 * @param event
	 */
	@TransferEventHandler(
			source={"TERM_2"},
			target={"OPERATION"}
	)
	public void handleAdditionOp(Event event) throws Exception {
		
		if(this.operation == null) {
			throw new Exception("operation is null");
		}
		
		if(this.p1 == null) {
			throw new Exception("p1 is null");
		}
		
		if(this.p2 == null) {
			throw new Exception("p2 is null");
		}
		
		TreeNode<Instruction> newP1 = new TreeNode<Instruction>(this.operation);
		newP1.addChild(this.p1);
		newP1.addChild(this.p2);

		this.handleOp(event); // this.operation 설정
		this.p1 = newP1;
		this.p2 = null;
	}

	/**
	 * 
	 */
	public void exit() {
		
		//
		if(this.operation != null && this.p2 != null) {
			
			this.setNodeData(this.operation);
			this.addChild(this.p1);
			this.addChild(this.p2);
			
		} else {
			this.setNode(this.p1);
		}
	}
}

package com.jutools.script.olexp.parser;

import com.jutools.script.engine.instructions.GREATER_EQUAL;
import com.jutools.script.engine.instructions.GREATER_THAN;
import com.jutools.script.engine.instructions.Instruction;
import com.jutools.script.engine.instructions.LESS_EQUAL;
import com.jutools.script.engine.instructions.LESS_THAN;
import com.jutools.script.parser.AbstractParser;
import com.jutools.script.parser.EndStatusType;
import com.jutools.script.parser.TransferBuilder;
import com.jutools.script.parser.TransferEventHandler;
import com.jutools.script.parser.TreeNode;

/**
 * 비교(<, >, <=, >=) 연산 파싱 수행<br>
 * 비교 연산은 삼항 연산이 없음<br>
 * ex) 1>2>3 -> true>3(?!)
 * 
 * @author jmsohn
 */
public class ComparisonParser extends AbstractParser<Instruction> {
	
	/** 비교 연산의 첫번째 파라미터의 tree node */
	private TreeNode<Instruction> p1;
	
	/** 비교 연산의 두번째 파라미터의 tree node */
	private TreeNode<Instruction> p2;
	
	/** 비교 연산 */
	private TreeNode<Instruction> op;
	
	/** 비교 연산 버퍼 */
	private StringBuffer opBuffer;

	/**
	 * 생성자
	 */
	public ComparisonParser() throws Exception {
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
		this.op = null;
		
		this.opBuffer = new StringBuffer("");
		
		// 상태 전이 맵 설정
		this.putTransferMap("START", new TransferBuilder()
				.add(" \t\r\n", "START")
				.add("^ \t\r\n", "ARITHMATIC_1", -1)
				.build());
		
		this.putTransferMap("ARITHMATIC_1", new TransferBuilder()
				.add(" \t\r\n", "ARITHMATIC_1")
				.add("\\<\\>", "OPERATION")
				.add("^ \t\r\n\\<\\>", "END", -1)
				.build());
		
		this.putTransferMap("OPERATION", new TransferBuilder()
				.add("\\=", "ARITHMATIC_2")
				.add("^\\=", "ARITHMATIC_2", -1)
				.build());
		
		this.putTransferMap("ARITHMATIC_2", new TransferBuilder()
				.add(" \t\r\n", "ARITHMATIC_2")
				.add("^ \t\r\n", "END", -1)
				.build());
		
		// 종료 상태 추가
		this.putEndStatus("ARITHMATIC_1");
		this.putEndStatus("ARITHMATIC_2");
		this.putEndStatus("END", EndStatusType.IMMEDIATELY_END); // END 상태로 들어오면 Parsing을 중지
		
	}
	
	/**
	 * 비교 연산의 첫번째 파라미터 상태로 전이시 핸들러 메소드
	 * 
	 * @param event 상태 전이 이벤트 정보
	 */
	@TransferEventHandler(
			source={"START"},
			target={"ARITHMATIC_1"}
	)
	public void handleP1(Event event) throws Exception {
		
		ArithmaticParser parser = new ArithmaticParser();
		this.p1 = parser.parse(event.getReader());
	}
	
	/**
	 * 비교 연산자 상태로 전이시 핸들러 메소드
	 * 
	 * @param event 상태 전이 이벤트 정보
	 */
	@TransferEventHandler(
			source={"ARITHMATIC_1"},
			target={"OPERATION"}
	)
	public void handleOp(Event event) throws Exception {
		this.opBuffer.append(event.getCh());
	}
	
	/**
	 * 비교 연산자 상태로 전이시 핸들러 메소드
	 * 
	 * @param event 상태 전이 이벤트 정보
	 */
	@TransferEventHandler(
			source={"OPERATION"},
			target={"ARITHMATIC_2"}
	)
	public void handleP2(Event event) throws Exception {
		
		// 비교 연산 중 같은(=) 조건 있을 경우 추가
		// -> <=, >=
		if(event.getCh() == '=') {
			this.opBuffer.append(event.getCh());
		}
		
		// 비교 연산 생성
		String compareOp = this.opBuffer.toString();
		switch(compareOp) {
		case ">":
			this.op = new TreeNode<>(new GREATER_THAN());
			break;
		case ">=":
			this.op = new TreeNode<>(new GREATER_EQUAL());
			break;
		case "<":
			this.op = new TreeNode<>(new LESS_THAN());
			break;
		case "<=":
			this.op = new TreeNode<>(new LESS_EQUAL());
			break;
		default:
			throw new Exception("Unexpected operation: " + compareOp);	
		}
		
		// p2 생성
		ArithmaticParser parser = new ArithmaticParser();
		this.p2 = parser.parse(event.getReader());
	}
	
	/**
	 * 파싱 종료 처리
	 */
	@Override
	protected void exit() throws Exception {
		
		if(this.op != null && this.p2 != null) {
		
			// 비교 연산이 존재하는 경우
			this.setNode(this.op);
			this.addChild(this.p1);
			this.addChild(this.p2);
			
		} else {
			
			// 비교 연산이 존재하지 않는 경우
			this.setNode(this.p1);
		}
	}
}

package com.jutools.olexp.parser;

import com.jutools.instructions.Instruction;
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
				.add("^ \t", "EQUALITY_1", -1)
				.build());
		
		this.putTransferMap("EQUALITY_1", new TransferBuilder()
				.add(" \t", "EQUALITY_1")
				.add("aA", "AND_OP_1")
				.add("oO", "OR_OP_1")
				.add("^ \tao", "END", -1)
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
				.add(" \t", "EQUALITY_2")
				.add("(", "EQUALITY_2", -1)
				.add("^ \t(", "ERROR", -1)
				.build());
		
		this.putTransferMap("OR_OP_1", new TransferBuilder()
				.add("rR", "OR_OP_2")
				.add("^ \t(", "ERROR", -1)
				.build());
		
		this.putTransferMap("OR_OP_2", new TransferBuilder()
				.add(" \t", "EQUALITY_2")
				.add("(", "EQUALITY_2", -1)
				.add("^ \t(", "ERROR", -1)
				.build());
		
		this.putTransferMap("EQUALITY_2", new TransferBuilder()
				.build());
		
		// 종료 상태 추가
		this.putEndStatus("EQUALITY_1");
		this.putEndStatus("EQUALITY_2");
		this.putEndStatus("END", EndStatusType.IMMEDIATELY_END); // END 상태로 들어오면 Parsing을 중지
		this.putEndStatus("ERROR", EndStatusType.ERROR);
	}
	
	/**
	 * and,or 의 첫번째 파라미터 상태로 전이시 핸들러 메소드
	 * 
	 * @param event 상태 전이 이벤트 정보
	 */
	@TransferEventHandler(
			source={"START"},
			target={"EQUALITY_1"}
	)
	public void handleP1(Event event) throws Exception {
		
		TermParser parser = new TermParser();
		this.p1 = parser.parse(event.getReader());

	}
}

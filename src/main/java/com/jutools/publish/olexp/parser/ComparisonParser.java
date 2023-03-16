package com.jutools.publish.formatter.eval.parser;

import com.jutools.instructions.Instruction;
import com.jutools.parserfw.AbstractParser;
import com.jutools.parserfw.EndStatusType;
import com.jutools.parserfw.TransferBuilder;
import com.jutools.parserfw.TreeNode;

public class ComparisonParser extends AbstractParser<Instruction> {
	
	/** 비교 연산의 첫번째 파라미터의 tree node */
	private TreeNode<Instruction> p1;
	/** 비교 연산의 두번째 파라미터의 tree node */
	private TreeNode<Instruction> p2;
	/** 비교 연산 */
	private Instruction operation;

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
		this.operation = null;
		
		// 상태 전이 맵 설정
		this.putTransferMap("START", new TransferBuilder()
				.add(" \t", "START")
				.add("^ \t", "ARITHMATIC_1", -1)
				.build());
		
		this.putTransferMap("ARITHMATIC_1", new TransferBuilder()
				.add(" \t", "ARITHMATIC_1")
				.add("\\<\\>", "OPERATION")
				.add("^ \t\\<\\>", "END", -1)
				.build());
		
		this.putTransferMap("OPERATION", new TransferBuilder()
				.add("\\=", "OPERATION_2")
				.add("^\\=", "ARITHMATIC_2", -1)
				.build());
		
		this.putTransferMap("OPERATION_2", new TransferBuilder()
				.add(" \t", "OPERATION_2")
				.add("^ \t", "ARITHMATIC_2", -1)
				.build());
		
		this.putTransferMap("ARITHMATIC_2", new TransferBuilder()
				.add(" \t", "ARITHMATIC_2")
				.add("\\+\\-", "OPERATION")
				.add("^ \t\\+\\-", "END", -1)
				.build());
		
		// 종료 상태 추가
		this.putEndStatus("ARITHMATIC_1");
		this.putEndStatus("ARITHMATIC_2");
		this.putEndStatus("END", EndStatusType.IMMEDIATELY_END); // END 상태로 들어오면 Parsing을 중지
		
	}

}

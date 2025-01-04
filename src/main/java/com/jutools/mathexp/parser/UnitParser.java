package com.jutools.mathexp.parser;

import com.jutools.MathUtil;
import com.jutools.instructions.Instruction;
import com.jutools.instructions.LOAD_NUMBER;
import com.jutools.instructions.LOAD_STRING;
import com.jutools.instructions.MUL;
import com.jutools.instructions.NOP;
import com.jutools.parserfw.AbstractParser;
import com.jutools.parserfw.EndStatusType;
import com.jutools.parserfw.TransferBuilder;
import com.jutools.parserfw.TransferEventHandler;
import com.jutools.parserfw.TreeNode;

/**
 * 단위 파싱 수행
 * 
 * @author jmsohn
 */
public class UnitParser extends AbstractParser<Instruction> {
	
	/** 값에 대한 파싱 트리 객체 */
	private TreeNode<Instruction> value;
	/** 단위에 대한 버퍼 */
	private StringBuffer unitBuffer;

	/**
	 * 생성자
	 */
	public UnitParser() throws Exception {
		super();
	}

	@Override
	protected String getStartStatus() {
		return "START";
	}

	@Override
	protected void init() throws Exception {
		
		// 속성 변수 초기화
		this.value = null;
		this.unitBuffer = new StringBuffer("");
		
		// 상태 변환 맵 추가
		this.putTransferMap("START", new TransferBuilder()
				.add(" \t", "START")
				.add("^ \t", "ARITHMATIC", -1)
				.build());
		
		this.putTransferMap("ARITHMATIC", new TransferBuilder()
				.add(" \t", "ARITHMATIC")
				.add("a-zA-Zμ", "UNIT")
				.add("^ \ta-zA-Zμ", "ERROR")
				.build());
		
		this.putTransferMap("UNIT", new TransferBuilder()
				.add("a-zA-Z", "UNIT")
				.add("^a-zA-Z", "END")
				.build());
		
		// 종료 상태 추가
		this.putEndStatus("ARITHMATIC");
		this.putEndStatus("UNIT");
		this.putEndStatus("END", EndStatusType.IMMEDIATELY_END); // END 상태로 들어오면 Parsing을 중지
		this.putEndStatus("ERROR", EndStatusType.ERROR);

	}
	
	/**
	 * 값에 대한 처리 수행
	 * 
	 * @param event
	 */
	@TransferEventHandler(
			source={"START"},
			target={"ARITHMATIC"}
	)
	public void handleValue(Event event) throws Exception {
		ArithmaticParser parser = new ArithmaticParser();
		this.value = parser.parse(event.getReader());
	}

	
	/**
	 * 단위에 대한 처리 수행
	 * 
	 * @param event
	 */
	@TransferEventHandler(
			source={"ARITHMATIC", "UNIT"},
			target={"UNIT"}
	)
	public void handleUnit(Event event) throws Exception {
		this.unitBuffer.append(event.getCh());
	}

	/**
	 * 파싱 종료 처리
	 */
	@Override
	protected void exit() throws Exception {
		
		// 단위(unit)를 접두사(unit prefix)와 기본단위(base unit)로 분리함
		String unit = this.unitBuffer.toString();
		String[] unitPrefixAndBase = MathUtil.splitUnitToPrefixAndBase(unit);
		String unitPrefix = unitPrefixAndBase[0];
		String baseUnit = unitPrefixAndBase[1];

		// 
		this.setNodeData(new NOP());
		this.addChildData(new LOAD_STRING(baseUnit));
		
		//
		TreeNode<Instruction> valueNode = new TreeNode<Instruction>(new MUL());
		
		//
		valueNode.addChild(this.value);
		
		//
		double factor = MathUtil.unitPrefixToFactor(unitPrefix);
		TreeNode<Instruction> factorNode = new TreeNode<Instruction>(
			new LOAD_NUMBER(factor)
		);
		
		valueNode.addChild(factorNode);
		
		this.addChild(valueNode);
	}

}

package com.jutools.mathexp.parser.script;

import com.jutools.MathUtil;
import com.jutools.mathexp.instructions.Instruction;
import com.jutools.mathexp.instructions.LOAD_NUMBER;
import com.jutools.mathexp.instructions.LOAD_STRING;
import com.jutools.mathexp.instructions.MUL;
import com.jutools.mathexp.instructions.NOP;
import com.jutools.mathexp.parser.AbstractParser;
import com.jutools.mathexp.parser.EndStatusType;
import com.jutools.mathexp.parser.TransferBuilder;
import com.jutools.mathexp.parser.TransferEventHandler;
import com.jutools.mathexp.parser.TreeNode;

/**
 * 
 * 
 * @author jmsohn
 */
public class UnitParser extends AbstractParser<Instruction> {
	
	/** */
	private TreeNode<Instruction> value;
	/** */
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
				.add("^ \t", "ARITHMATIC", true)
				.build());
		
		this.putTransferMap("ARITHMATIC", new TransferBuilder()
				.add(" \t", "ARITHMATIC")
				.add("a-zA-Zμ", "UNIT")
				.add("^ \ta-zA-Zμ", "ERROR")
				.build());
		
		this.putTransferMap("UNIT", new TransferBuilder()
				.add("a-zA-Z", "UNIT")
				.add("^a-zA-Z", "UNIT_END")
				.build());
		
		// 종료 상태 추가
		this.putEndStatus("ARITHMATIC");
		this.putEndStatus("UNIT");
		this.putEndStatus("UNIT_END", EndStatusType.IMMEDIATELY_END); // UNIT_END 상태로 들어오면 Parsing을 중지
		this.putEndStatus("ERROR", EndStatusType.ERROR);

	}
	
	/**
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
	 * 
	 */
	@Override
	protected void exit() throws Exception {
		
		// 단위(unit)을 접두사(unit prefix)와 기본단위(base unit)로 분리함
		String unit = this.unitBuffer.toString();
		String[] unitPrefixAndBase = MathUtil.devideUnitToPrefixAndBase(unit);
		String unitPrefix = unitPrefixAndBase[0];
		String baseUnit = unitPrefixAndBase[1];

		//
		this.setNodeData(new NOP());
		this.addChildData(new LOAD_STRING().addParam(baseUnit));
		
		//
		TreeNode<Instruction> valueNode = new TreeNode<Instruction>(new MUL());
		
		//
		valueNode.addChild(this.value);
		
		//
		double factor = MathUtil.unitPrefixToFactor(unitPrefix);
		TreeNode<Instruction> factorNode = new TreeNode<Instruction>(
			new LOAD_NUMBER().addParam(Double.toString(factor))
		);
		
		valueNode.addChild(factorNode);
		
		this.addChild(valueNode);
	}

}

package com.jutools.mathexp.parser.script;

import com.jutools.MathUtil;
import com.jutools.mathexp.instructions.Instruction;
import com.jutools.mathexp.instructions.LOAD_NUMBER;
import com.jutools.mathexp.instructions.MUL;
import com.jutools.mathexp.parser.AbstractParser;
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
		this.putEndStatus("UNIT_END", 1); // UNIT_END 상태로 들어오면 Parsing을 중지

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
		this.unitBuffer.append(event.getChar());
	}

	/**
	 * 
	 */
	@Override
	protected void exit() throws Exception {
		
		//
		this.setNodeData(new MUL());
		
		//
		this.addChild(this.value);
		
		//
		String unit = this.unitBuffer.toString();
		String unitPrefix = "";
		
		if(unit.startsWith("da") && unit.length() > 2) {
			
			// SI 단위계 유일한 2자 접두어
			unitPrefix = "da";
			
		} else if(unit.charAt(1) == 'i' && unit.length() > 2) {
			
			// 바이트 단위에서 사용하는 Ki(Kibi), Mi(Mibi) ...
			unitPrefix = unit.substring(0, 2);
			
		} else if(unit.length() > 1) {
			
			unitPrefix = unit.substring(0, 1);
			
		}
		
		//
		double factor = MathUtil.unitPrefixToFactor(unitPrefix);
		this.addChildData(
			new LOAD_NUMBER().addParam(Double.toString(factor))
		);
		
	}

}

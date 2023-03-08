package com.jutools.mathexp.parser.script;

import com.jutools.mathexp.instructions.Instruction;
import com.jutools.mathexp.instructions.LOAD_VAR;
import com.jutools.mathexp.parser.AbstractParser;
import com.jutools.mathexp.parser.TransferBuilder;
import com.jutools.mathexp.parser.TransferEventHandler;

/**
 * 
 * 
 * @author jmsohn
 */
public class VarParser extends AbstractParser<Instruction> {
	
	/** */
	private StringBuffer buffer;

	public VarParser() throws Exception {
		super();
		this.buffer = new StringBuffer();
	}

	@Override
	protected String getStartStatus() {
		return "START";
	}

	@Override
	protected void init() throws Exception {
		
		// 상태 변환 맵 추가
		this.putTransferMap("START", new TransferBuilder()
				.add(" \t", "START")
				.add("a-zA-Z\\_", "VAR")
				.add("^ \ta-zA-Z\\_", "ERROR")
				.build());
		
		this.putTransferMap("VAR", new TransferBuilder()
				.add("a-zA-Z0-9\\_", "VAR")
				.add("^a-zA-Z0-9\\_", "VAR_END", true)
				.build());
		
		// 종료 상태 추가
		this.putEndStatus("VAR");
		this.putEndStatus("VAR_END", 1); // END 상태로 들어오면 Parsing을 중지
	}
	
	@TransferEventHandler(
			source={"START", "VAR"},
			target={"VAR"}
	)
	public void handleNumber(Event event) {
		this.buffer.append(event.getChar());
	}
	
	/**
	 * 
	 */
	protected void exit() throws Exception {
		
		// LOAD_VAR 변수명
		LOAD_VAR inst = new LOAD_VAR();
		inst.addParam(this.buffer.toString());
		this.setNodeData(inst);
	}

}

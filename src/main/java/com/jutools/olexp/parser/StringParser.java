package com.jutools.olexp.parser;

import com.jutools.StringUtil;
import com.jutools.instructions.Instruction;
import com.jutools.instructions.LOAD_STRING;
import com.jutools.parserfw.AbstractParser;
import com.jutools.parserfw.EndStatusType;
import com.jutools.parserfw.TransferBuilder;
import com.jutools.parserfw.TransferEventHandler;

/**
 * 
 * 
 * @author jmsohn
 */
public class StringParser extends AbstractParser<Instruction> {
	
	private StringBuffer strBuffer;

	public StringParser() throws Exception {
		super();
	}

	@Override
	protected String getStartStatus() {
		return "START";
	}

	@Override
	protected void init() throws Exception {
		
		//
		this.strBuffer = new StringBuffer("");
		
		// 상태 변환 맵 추가
		this.putTransferMap("START", new TransferBuilder()
				.add("'", "IN_STR")
				.add(" \t", "START")
				.add("^' \t", "ERROR")
				.build());
		
		this.putTransferMap("IN_STR", new TransferBuilder()
				.add("\\\\", "ESCAPE")
				.add("'", "STR_END")
				.add("^\\\\'", "IN_STR")
				.build());
		
		this.putTransferMap("ESCAPE", new TransferBuilder()
				.add(".", "IN_STR")
				.build());
		
		// 종료 상태 설정
		this.putEndStatus("STR_END", EndStatusType.IMMEDIATELY_END);
	}

	@TransferEventHandler(
			source={"IN_STR", "ESCAPE"},
			target={"IN_STR", "ESCAPE"}
	)
	public void handleStr(Event event) throws Exception {
		this.strBuffer.append(event.getCh());
	}
	
	@Override
	protected void exit() throws Exception {
		this.setNodeData(
			new LOAD_STRING().addParam(StringUtil.escape(this.strBuffer.toString()))
		);
	}
}

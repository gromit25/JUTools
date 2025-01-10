package com.jutools.script.olexp.parser;

import com.jutools.StringUtil;
import com.jutools.script.engine.instructions.Instruction;
import com.jutools.script.engine.instructions.LOAD_STRING;
import com.jutools.script.parser.AbstractParser;
import com.jutools.script.parser.EndStatusType;
import com.jutools.script.parser.TransferBuilder;
import com.jutools.script.parser.TransferEventHandler;

/**
 * 문자열 파싱 수행
 * 
 * @author jmsohn
 */
public class StringParser extends AbstractParser<Instruction> {
	
	/** 문자열 저장 버퍼 */
	private StringBuffer strBuffer;

	/**
	 * 생성자
	 */
	public StringParser() throws Exception {
		super();
	}

	@Override
	protected String getStartStatus() {
		return "START";
	}

	@Override
	protected void init() throws Exception {
		
		// 문자열 임시 저장 버퍼 생성
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
		
		// 문자열 생성 (Escape 문자열 처리)
		String str = StringUtil.escape(this.strBuffer.toString());
		
		this.setNodeData(
			new LOAD_STRING(str)
		);
	}
}

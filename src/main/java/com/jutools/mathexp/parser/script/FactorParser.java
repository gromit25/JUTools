package com.jutools.mathexp.parser.script;

import com.jutools.mathexp.instructions.Instruction;
import com.jutools.mathexp.parser.AbstractParser;
import com.jutools.mathexp.parser.TransferBuilder;
import com.jutools.mathexp.parser.TransferEventHandler;

/**
 * 
 * 
 * @author jmsohn
 */
public class FactorParser extends AbstractParser<Instruction> {

	/**
	 * 
	 */
	public FactorParser() throws Exception {
		super();
	}

	@Override
	protected String getStartStatus() {
		return "START";
	}

	/**
	 * 파싱전 초기화 수행
	 */
	@Override
	protected void init() throws Exception {
		
		// 상태 변환 맵 추가
		this.putTransferMap("START", new TransferBuilder()
				.add(" \t", "START")
				.add("0-9\\-", "NUMBER", true)
				.add("(", "EXPRESSION")
				.add("^ \t0-9\\-(", "ERROR")
				.build());
		
		this.putTransferMap("EXPRESSION", new TransferBuilder()
				.add(" \t", "EXPRESSION")
				.add("0-9\\-", "NUMBER", true)
				.add(")", "EXPRESSION_END")
				.build());
		
		this.putTransferMap("NUMBER", new TransferBuilder()
				.add(")", "EXPRESSION_END")
				.build());
		
		// 종료 상태 추가
		this.putEndStatus("NUMBER");
		this.putEndStatus("EXPRESSION_END", 1); // EXPRESSION_END 상태로 들어오면 Parsing을 중지
	}

	@TransferEventHandler(
			source={"START", "EXPRESSION"},
			target={"NUMBER"}
	)
	public void handleNumber(Event event) throws Exception {
		NumberParser parser = new NumberParser();
		this.setNode(parser.parse(event.getReader()));
	}
}

package com.jutools.olexp.parser;

import com.jutools.instructions.Instruction;
import com.jutools.instructions.LOAD_NUMBER;
import com.jutools.parserfw.AbstractParser;
import com.jutools.parserfw.EndStatusType;
import com.jutools.parserfw.TransferBuilder;
import com.jutools.parserfw.TransferEventHandler;

/**
 * 
 * @author jmsohn
 */
public class NumberParser extends AbstractParser<Instruction> {
	
	/** */
	private StringBuffer buffer;

	/**
	 * 
	 */
	public NumberParser() throws Exception {
		super();
		this.buffer = new StringBuffer();
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
				.add("0-9", "NUMBER")
				.add("\\-", "SIGN")
				.add("^0-9\\-", "ERROR")
				.build());
		
		this.putTransferMap("SIGN", new TransferBuilder()
				.add("0-9", "NUMBER")
				.add("^0-9", "ERROR", -1)
				.build());
		
		this.putTransferMap("NUMBER", new TransferBuilder()
				.add("0-9", "NUMBER")
				.add("\\.", "DOT")
				.add("^0-9\\.", "END", -1)
				.build());
		
		this.putTransferMap("DOT", new TransferBuilder()
				.add("0-9", "FLOATING_NUMBER")
				.add("^0-9", "END", -1)
				.build());
		
		this.putTransferMap("FLOATING_NUMBER", new TransferBuilder()
				.add("0-9", "FLOATING_NUMBER")
				.add("^0-9", "END", -1)
				.build());
		
		// 종료 상태 추가
		this.putEndStatus("NUMBER");
		this.putEndStatus("FLOATING_NUMBER");
		this.putEndStatus("END", EndStatusType.IMMEDIATELY_END); // END 상태로 들어오면 Parsing을 중지
		this.putEndStatus("ERROR", EndStatusType.ERROR);
	}
	
	@TransferEventHandler(
			source={"START", "SIGN", "NUMBER", "DOT", "FLOATING_NUMBER"},
			target={"SIGN", "NUMBER", "DOT", "FLOATING_NUMBER"}
	)
	public void handleNumber(Event event) {
		this.buffer.append(event.getCh());
	}
	
	/**
	 * 파싱 종료 처리
	 */
	@Override
	protected void exit() throws Exception {
		
		// LOAD_NUMBER "숫자"
		LOAD_NUMBER inst = new LOAD_NUMBER();
		inst.addParam(this.buffer.toString());
		this.setNodeData(inst);
	}

}
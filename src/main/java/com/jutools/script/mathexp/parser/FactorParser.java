package com.jutools.script.mathexp.parser;

import com.jutools.script.engine.instructions.Instruction;
import com.jutools.script.parserfw.AbstractParser;
import com.jutools.script.parserfw.EndStatusType;
import com.jutools.script.parserfw.TransferBuilder;
import com.jutools.script.parserfw.TransferEventHandler;

/**
 * 변수, 메소드, 숫자 등 항목에 대한 파싱 수행<br>
 * 실제 파싱은 각 파서에서 수행함, 여기에서는 종류별로 나누어 주는 역활
 * 
 * @author jmsohn
 */
public class FactorParser extends AbstractParser<Instruction> {

	/**
	 * 생성자
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
				.add("0-9\\-", "NUMBER", -1)
				.add("a-zA-Z\\_", "VAR", -1)
				.add("(", "ARITHMATIC")					//TODO
				.add("^ \t0-9\\-a-zA-Z\\_(", "ERROR")
				.build());
		
		this.putTransferMap("ARITHMATIC", new TransferBuilder()  //TODO
				.add(" \t", "ARITHMATIC")
				.add(")", "END")
				.build());
		
		// 종료 상태 추가
		this.putEndStatus("NUMBER", EndStatusType.IMMEDIATELY_END);
		this.putEndStatus("VAR", EndStatusType.IMMEDIATELY_END);
		this.putEndStatus("END", EndStatusType.IMMEDIATELY_END); // END 상태로 들어오면 Parsing을 중지
		this.putEndStatus("ERROR", EndStatusType.ERROR);
	}

	/**
	 * 숫자 파싱 
	 * 
	 * @param event
	 */
	@TransferEventHandler(
			source={"START"},
			target={"NUMBER"}
	)
	public void handleNumber(Event event) throws Exception {
		NumberParser parser = new NumberParser();
		this.setNode(parser.parse(event.getReader()));
	}
	
	/**
	 * 변수, 메소드 파싱
	 * 
	 * @param event
	 */
	@TransferEventHandler(
			source={"START"},
			target={"VAR"}
	)
	public void handleVar(Event event) throws Exception {
		VarParser parser = new VarParser();
		this.setNode(parser.parse(event.getReader()));
	}
	
	/**
	 * 새로운 괄호가 시작되었을 때 파싱
	 * 
	 * @param event
	 */
	@TransferEventHandler(
			source={"START"},
			target={"ARITHMATIC"}
	)
	public void handleExp(Event event) throws Exception {
		ArithmaticParser parser = new ArithmaticParser();
		this.setNode(parser.parse(event.getReader()));
	}

}

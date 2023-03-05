package com.jutools.mathexp.parser.script;

import java.util.ArrayList;
import java.util.HashMap;

import com.jutools.mathexp.instructions.Instruction;
import com.jutools.mathexp.parser.AbstractParser;
import com.jutools.mathexp.parser.Transfer;
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

	@Override
	protected void init() throws Exception {
		
		// 종료 상태 추가
		this.putEndStatus("NUMBER");
		this.putEndStatus("EXPRESSION_END", 1); // EXPRESSION_END 상태로 들어오면 Parsing을 중지
	}

	@Override
	protected HashMap<String, ArrayList<Transfer>> getTransferMap() throws Exception {
		HashMap<String, ArrayList<Transfer>> transferMap = new HashMap<String, ArrayList<Transfer>>();
		
		transferMap.put("START", new TransferBuilder()
				.add(" \t", "START")
				.add("0-9\\-", "NUMBER", true)
				.add("(", "EXPRESSION")
				.add("^ \t0-9\\-(", "ERROR")
				.build());
		
		transferMap.put("EXPRESSION", new TransferBuilder()
				.add(" \t", "EXPRESSION")
				.add("0-9\\-", "NUMBER", true)
				.add(")", "EXPRESSION_END")
				.build());
		
		transferMap.put("NUMBER", new TransferBuilder()
				.add(")", "EXPRESSION_END")
				.build());
		
		return transferMap;
	}
	
	@TransferEventHandler(
			source={"START", "EXPRESSION"},
			target={"NUMBER"}
	)
	public void handleNumberAAA(Event event) throws Exception {
		NumberParser parser = new NumberParser();
		this.setNode(parser.parse(event.getReader()));
	}
}

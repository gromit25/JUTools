package com.jutools.mathexp.parser.script;

import java.util.ArrayList;
import java.util.HashMap;

import com.jutools.mathexp.instructions.Instruction;
import com.jutools.mathexp.instructions.LOAD;
import com.jutools.mathexp.parser.AbstractParser;
import com.jutools.mathexp.parser.Transfer;
import com.jutools.mathexp.parser.TransferBuilder;
import com.jutools.mathexp.parser.TransferEventHandler;

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

	@Override
	protected String[] getEndStatus() {
		return new String[] {
				"NUMBER",
				"FLOATING_NUMBER",
				"END"
		};
	}

	@Override
	protected HashMap<String, ArrayList<Transfer>> getTransferMap() throws Exception {
		
		HashMap<String, ArrayList<Transfer>> transferMap = new HashMap<String, ArrayList<Transfer>>();
		
		transferMap.put("START", new TransferBuilder()
				.add("0-9", "NUMBER")
				.build());
		
		transferMap.put("NUMBER", new TransferBuilder()
				.add("0-9", "NUMBER")
				.add(".", "DOT")
				.add("^0-9.", "END")
				.build());
		
		transferMap.put("DOT", new TransferBuilder()
				.add("0-9", "FLOATING_NUMBER")
				.build());
		
		transferMap.put("FLOATING_NUMBER", new TransferBuilder()
				.add("0-9", "FLOATING_NUMBER")
				.add("^0-9", "END")
				.build());
		
		return transferMap;
	}
	
	@TransferEventHandler(
			source={"START", "NUMBER", "DOT", "FLOATING_NUMBER"},
			target={"NUMBER", "DOT", "FLOATING_NUMBER"}
	)
	public void handleNumber(Event event) {
		this.buffer.append(event.getChar());
	}
	
	/**
	 * 
	 */
	protected void exit() throws Exception {
		
		LOAD inst = new LOAD();
		inst.addParam(this.buffer.toString());
		this.setNodeData(inst);
	}

}
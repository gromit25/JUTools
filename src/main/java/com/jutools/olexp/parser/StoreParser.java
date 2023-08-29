package com.jutools.olexp.parser;

import com.jutools.instructions.Instruction;
import com.jutools.instructions.LOAD_VAR;
import com.jutools.instructions.STORE;
import com.jutools.parserfw.AbstractParser;
import com.jutools.parserfw.TransferBuilder;
import com.jutools.parserfw.TransferEventHandler;

/**
 * 
 * 
 * @author jmsohn
 */
public class StoreParser extends AbstractParser<Instruction> {
	
	/** */
	private StringBuffer LValueBuffer;

	/**
	 * 생성자
	 */
	public StoreParser() throws Exception {
		super();
	}

	@Override
	protected String getStartStatus() {
		return "START";
	}

	@Override
	protected void init() throws Exception {
		
		// 속성 초기화
		this.LValueBuffer = new StringBuffer("");
		
		// 상태 변환 맵 추가
		this.putTransferMap("START", new TransferBuilder()
				.add(" \t", "START")
				.add("a-zA-Z\\_", "L_VAR")
				.add("^ a-zA-Z\\_", "NOT_STORE_OP", Integer.MIN_VALUE)
				.build());
		
		this.putTransferMap("L_VAR", new TransferBuilder()
				.add("a-zA-Z0-9\\_", "L_VAR")
				.add(" \t", "L_VAR_BLANK")
				.add("=", "STORE_OP")
				.add("^ a-zA-Z0-9\\_=", "NOT_STORE_OP", Integer.MIN_VALUE)
				.build());
		
		this.putTransferMap("L_VAR_BLANK", new TransferBuilder()
				.add(" \t", "L_VAR_BLANK")
				.add("=", "STORE_OP")
				.add("^ \t=", "NOT_STORE_OP", Integer.MIN_VALUE)
				.build());
		
		this.putTransferMap("STORE_OP", new TransferBuilder()
				.add("^=", "STORE_OP_END", -1)
				.add("=", "NOT_STORE_OP", Integer.MIN_VALUE)
				.build());
		
		// 종료 상태 설정
		this.putEndStatus("L_VAR");
		this.putEndStatus("STORE_OP_END");
		this.putEndStatus("NOT_STORE_OP");
	}
	
	/**
	 * 
	 * @param event
	 */
	@TransferEventHandler(
			source={"START", "L_VAR"},
			target={"L_VAR"}
	)
	public void handleVar(Event event) throws Exception {
		this.LValueBuffer.append(event.getCh());
	}
	
	/**
	 * 
	 * @param event
	 */
	@TransferEventHandler(
			source={"STORE_OP"},
			target={"STORE_OP_END"}
	)
	public void handleStoreOp(Event event) throws Exception {
		
		this.setNodeData(
			new STORE().addParam(this.LValueBuffer.toString())
		);
		
		this.addChild(
			new EqualityParser().parse(event.getReader())
		);
	}
	
	/**
	 * 
	 * @param event
	 */
	@TransferEventHandler(
			source={"START", "L_VAR", "L_VAR_BLANK", "STORE_OP"},
			target={"NOT_STORE_OP"}
	)
	public void handleNotStoreOp(Event event) throws Exception {
		this.setNode(
			new EqualityParser().parse(event.getReader())
		);
	}
	
	@Override
	protected void exit() throws Exception {
		
		if(this.getStatus().equals("L_VAR") == true) {
			this.setNodeData(
				new LOAD_VAR().addParam(this.LValueBuffer.toString())
			);
		}
	}
}

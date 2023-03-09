package com.jutools.mathexp.parser.script;

import java.util.ArrayList;

import com.jutools.mathexp.instructions.INVOKE;
import com.jutools.mathexp.instructions.Instruction;
import com.jutools.mathexp.instructions.LOAD_VAR;
import com.jutools.mathexp.parser.AbstractParser;
import com.jutools.mathexp.parser.EndStatusType;
import com.jutools.mathexp.parser.TransferBuilder;
import com.jutools.mathexp.parser.TransferEventHandler;
import com.jutools.mathexp.parser.TreeNode;

/**
 * 
 * 
 * @author jmsohn
 */
public class VarParser extends AbstractParser<Instruction> {
	
	/** */
	private StringBuffer buffer;
	/** */
	private boolean isMethod;
	/** */
	private ArrayList<TreeNode<Instruction>> params;

	public VarParser() throws Exception {
		super();
	}

	@Override
	protected String getStartStatus() {
		return "START";
	}

	@Override
	protected void init() throws Exception {

		// 속성 초기화
		this.buffer = new StringBuffer();
		this.isMethod = false;
		this.params = new ArrayList<TreeNode<Instruction>>();
		
		// 상태 변환 맵 추가
		this.putTransferMap("START", new TransferBuilder()
				.add(" \t", "START")
				.add("a-zA-Z\\_", "VAR")
				.add("^ \ta-zA-Z\\_", "ERROR")
				.build());
		
		this.putTransferMap("VAR", new TransferBuilder()
				.add("a-zA-Z0-9\\_", "VAR")
				.add("\\(", "PARAM_START")
				.add("^a-zA-Z0-9\\_\\(", "VAR_END", true)
				.build());
		
		this.putTransferMap("PARAM_START", new TransferBuilder()
				.add(" \t", "PARAM_START")
				.add("\\)", "PARAM_END")
				.add("^ \t\\)", "PARAM", true)
				.build());
		
		this.putTransferMap("PARAM", new TransferBuilder()
				.add(" \t", "PARAM")
				.add("\\,", "COMMA")
				.add("\\)", "PARAM_END")
				.add("^ \t\\,\\)", "ERROR")
				.build());
		
		this.putTransferMap("COMMA", new TransferBuilder()
				.add(" \t", "COMMA")
				.add("^ \t)", "PARAM", true)
				.build());
		
		// 종료 상태 추가
		this.putEndStatus("VAR");
		this.putEndStatus("VAR_END", EndStatusType.IMMEDIATELY_END); // VAR_END 상태로 들어오면 Parsing을 중지
		this.putEndStatus("PARAM_END", EndStatusType.IMMEDIATELY_END); // PARAM_END 상태로 들어오면 Parsing을 중지
		this.putEndStatus("ERROR", EndStatusType.ERROR);
	}
	
	@TransferEventHandler(
			source={"START", "VAR"},
			target={"VAR"}
	)
	public void handleNumber(Event event) throws Exception {
		this.buffer.append(event.getCh());
	}
	
	@TransferEventHandler(
			source={"VAR"},
			target={"PARAM_START"}
	)
	public void handleParamStart(Event event) throws Exception {
		this.isMethod = true;
	}
	
	@TransferEventHandler(
			source={"PARAM_START", "COMMA"},
			target={"PARAM"}
	)
	public void handleParam(Event event) throws Exception {
		ArithmaticParser parser = new ArithmaticParser();
		this.params.add(parser.parse(event.getReader()));
	}
	
	/**
	 * 
	 */
	protected void exit() throws Exception {
		
		if(this.isMethod == false) {
			
			// LOAD_VAR 변수명
			LOAD_VAR inst = new LOAD_VAR();
			inst.addParam(this.buffer.toString());
			
			// Node로 설정
			this.setNodeData(inst);
			
		} else {
			
			// INVOKE 메소드명 파라미터수
			TreeNode<Instruction> instNode = new TreeNode<Instruction>(
				new INVOKE()
					.addParam(this.buffer.toString())	// 메소드명
					.addParam(Integer.toString(this.params.size()))	// 파라미터수
			);
			
			// 메소드 parameter 추가
			for(TreeNode<Instruction> param: this.params) {
				instNode.addChild(param);
			}
			
			// Node로 설정
			this.setNode(instNode);
		}
	}

}

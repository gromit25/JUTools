package com.jutools.mathexp.parser;

import java.util.ArrayList;

import com.jutools.olexp.parser.ArithmaticParser;
import com.jutools.parserfw.AbstractParser;
import com.jutools.parserfw.EndStatusType;
import com.jutools.parserfw.TransferBuilder;
import com.jutools.parserfw.TransferEventHandler;
import com.jutools.parserfw.TreeNode;
import com.jutools.scriptengine.INVOKE;
import com.jutools.scriptengine.Instruction;
import com.jutools.scriptengine.LOAD_VAR;

/**
 * 변수 및 메소드 파서 수행
 * 
 * @author jmsohn
 */
public class VarParser extends AbstractParser<Instruction> {
	
	/** 변수명 혹은 메소드명 */
	private StringBuffer buffer;
	/** 메소드 여부 */
	private boolean isMethod;
	/** 메소드의 파라미터 목록 */
	private ArrayList<TreeNode<Instruction>> params;

	/**
	 * 생성자
	 */
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
				.add("^a-zA-Z0-9\\_\\(", "VAR_END", -1)
				.build());
		
		this.putTransferMap("PARAM_START", new TransferBuilder()
				.add(" \t", "PARAM_START")
				.add("\\)", "PARAM_END")
				.add("^ \t\\)", "PARAM", -1)
				.build());
		
		this.putTransferMap("PARAM", new TransferBuilder()
				.add(" \t", "PARAM")
				.add("\\,", "COMMA")
				.add("\\)", "PARAM_END")
				.add("^ \t\\,\\)", "ERROR")
				.build());
		
		this.putTransferMap("COMMA", new TransferBuilder()
				.add(" \t", "COMMA")
				.add("^ \t)", "PARAM", -1)
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
	 * 파싱 종료 처리
	 */
	@Override
	protected void exit() throws Exception {
		
		if(this.isMethod == false) {
			
			// LOAD_VAR 명령어 Node로 설정
			this.setNodeData(
				new LOAD_VAR(this.buffer.toString())
			);
			
		} else {
			
			// INVOKE 메소드명 파라미터수
			TreeNode<Instruction> instNode = new TreeNode<Instruction>(
				new INVOKE(
					this.buffer.toString(),	// 메소드명
					this.params.size()	// 파라미터수
				)
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
package com.jutools.olexp.parser;

import java.util.ArrayList;
import java.util.List;

import com.jutools.instructions.INVOKE;
import com.jutools.instructions.Instruction;
import com.jutools.instructions.LOAD_VAR;
import com.jutools.instructions.NOP;
import com.jutools.parserfw.AbstractParser;
import com.jutools.parserfw.EndStatusType;
import com.jutools.parserfw.TransferBuilder;
import com.jutools.parserfw.TransferEventHandler;
import com.jutools.parserfw.TreeNode;

/**
 * 변수 및 메소드 파서 수행
 * 
 * @author jmsohn
 */
public class VarParser extends AbstractParser<Instruction> {
	
	/** 변수명 혹은 메소드명 */
	private StringBuffer varName;
	/** 메소드 여부 */
	private boolean isMethod;
	/** 메소드의 파라미터 목록 */
	private List<TreeNode<Instruction>> params;
	/** 컬렉션(List, Map) 또는 객체 속성 노드 */
	private TreeNode<Instruction> tailNode;

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
		this.varName = new StringBuffer();
		this.isMethod = false;
		this.params = new ArrayList<TreeNode<Instruction>>();
		this.tailNode = null;
		
		// 상태 변환 맵 추가
		this.putTransferMap("START", new TransferBuilder()
				.add(" \t", "START")
				.add("a-zA-Z\\_", "VAR")
				.add("^ \ta-zA-Z\\_", "ERROR")
				.build());
		
		this.putTransferMap("VAR", new TransferBuilder()
				.add("a-zA-Z0-9\\_", "VAR")
				.add("\\.", "ATTR")
				.add("\\[", "ELEMENT", -1)
				.add("\\(", "PARAM_START")
				.add("^a-zA-Z0-9\\_\\.\\[\\(", "VAR_END", -1)
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
		this.putEndStatus("ATTR", EndStatusType.IMMEDIATELY_END);
		this.putEndStatus("ELEMENT", EndStatusType.IMMEDIATELY_END);
		this.putEndStatus("VAR_END", EndStatusType.IMMEDIATELY_END); // VAR_END 상태로 들어오면 Parsing을 중지
		this.putEndStatus("PARAM_END", EndStatusType.IMMEDIATELY_END); // PARAM_END 상태로 들어오면 Parsing을 중지
		this.putEndStatus("ERROR", EndStatusType.ERROR);
	}
	
	@TransferEventHandler(
			source={"START", "VAR"},
			target={"VAR"}
	)
	public void handleVar(Event event) throws Exception {
		this.varName.append(event.getCh());
	}
	
	@TransferEventHandler(
			source={"VAR"},
			target={"ATTR"}
	)
	public void handleAttr(Event event) throws Exception {
		AttrParser parser = new AttrParser();
		this.tailNode = parser.parse(event.getReader());
	}
	
	@TransferEventHandler(
			source={"VAR"},
			target={"ELEMENT"}
	)
	public void handleElement(Event event) throws Exception {
		ElementParser parser = new ElementParser();
		this.tailNode = parser.parse(event.getReader());
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
		BooleanParser parser = new BooleanParser();
		this.params.add(parser.parse(event.getReader()));
	}
	
	/**
	 * 파싱 종료 처리
	 */
	@Override
	protected void exit() throws Exception {
		
		// VAR 노드
		TreeNode<Instruction> varNode = null;
		
		// 메소드 여부에 따라 처리
		if(this.isMethod == false) {
			
			// LOAD_VAR 명령어 VAR 노드로 설정
			varNode = new TreeNode<Instruction>(
				new LOAD_VAR().addParam(this.varName.toString())
			);
			
		} else {
			
			// INVOKE 명령어 생성
			TreeNode<Instruction> invokeNode = new TreeNode<Instruction>(
				new INVOKE()
					.addParam(this.varName.toString())	// 메소드명
					.addParam(Integer.toString(this.params.size()))	// 파라미터수
			);
			
			// 메소드 parameter 추가
			for(TreeNode<Instruction> param: this.params) {
				invokeNode.addChild(param);
			}
			
			// VAR 노드로 설정
			varNode = invokeNode;
		}
		
		//
		if(this.tailNode == null) {
			
			//
			this.setNode(varNode);
			
		} else {
			
			//
			TreeNode<Instruction> rootNode = new TreeNode<>(new NOP());
			rootNode.addChild(varNode);
			rootNode.addChild(this.tailNode);
			
			this.setNode(rootNode);
		}
	}
}

package com.jutools.script.olexp.parser;

import com.jutools.script.engine.instructions.Instruction;
import com.jutools.script.engine.instructions.NEW_LIST;
import com.jutools.script.parser.AbstractParser;
import com.jutools.script.parser.EndStatusType;
import com.jutools.script.parser.TransferBuilder;
import com.jutools.script.parser.TransferEventHandler;
import com.jutools.script.parser.TreeNode;

/**
 * 목록 정의 파싱 수행<br>
 * ex) [1, 'test', exp + 1]
 * 
 * @author jmsohn
 */
public class ListParser extends AbstractParser<Instruction> {
	
	/** */
	private NEW_LIST newList;

	/**
	 * 생성자
	 */
	public ListParser() throws Exception {
		super();
	}

	@Override
	protected String getStartStatus() {
		return "START";
	}

	@Override
	protected void init() throws Exception {
		
		// 목록 생성 명령어 생성 및 현재 노드로 설정
		this.newList = new NEW_LIST();
		this.setNodeData(newList);
		
		System.out.println("DEBUG 000:" + this.getChildCount());
		
		// 상태 변환 맵 추가
		this.putTransferMap("START", new TransferBuilder()
				.add("[", "START_ELEMENT")
				.add("^[", "LIST_ERROR")
				.build());
		
		this.putTransferMap("START_ELEMENT", new TransferBuilder()
				.add(" \t", "START_ELEMENT")
				.add("]", "ELEMENT_END")
				.add("^[", "ELEMENT", -1)
				.build());
		
		this.putTransferMap("ELEMENT", new TransferBuilder()
				.add(" \t", "SEPARATOR")
				.add(",", "ELEMENT")
				.add("]", "ELEMENT_END")
				.add("^ \t,]", "LIST_ERROR")
				.build());

		this.putTransferMap("SEPARATOR", new TransferBuilder()
				.add(" \t", "SEPARATOR")
				.add(",", "ELEMENT")
				.add("]", "ELEMENT_END")
				.add("^ \t,]", "LIST_ERROR")
				.build());
		
		// 종료 상태 추가
		this.putEndStatus("ELEMENT_END", EndStatusType.IMMEDIATELY_END); // END 상태로 들어오면 Parsing을 중지
		this.putEndStatus("LIST_ERROR", EndStatusType.ERROR);
	}
	
	@TransferEventHandler(
			source={"START_ELEMENT", "ELEMENT", "SEPARATOR"},
			target={"ELEMENT"}
	)
	public void handleElement(Event event) throws Exception {
		
		// element 노드 생성
		BooleanParser elementParser = new BooleanParser();
		TreeNode<Instruction> element = elementParser.parse(event.getReader());
		
		// 현재 노드에 추가
		if(element.getData() != null) {
			this.addChild(element);
		}
	}
	
	@Override
	protected void exit() throws Exception {

		// 목록 개수 설정
		this.newList.setCount(this.getChildCount());
	}
}

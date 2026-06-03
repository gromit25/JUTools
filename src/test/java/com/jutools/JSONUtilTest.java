package com.jutools;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class JSONUtilTest {
	
	@Test
	public void testMapToJSON1() throws Exception {
		
		Map<String, Object> map = Map.of(
			"item1", "apple",
			"item2", 12.3
		);
		
		String jsonStr = JSONUtil.toJSON(map);
		System.out.println(jsonStr);
	}
	
	@Test
	public void testMapToJSON2() throws Exception {
		
		Map<String, Object> map = Map.of(
			"item1", "apple",
			"item2", 12.3,
			"item3", List.of("banna", "A", 34.5, 12)
		);
		
		String jsonStr = JSONUtil.toJSON(map);
		System.out.println(jsonStr);
	}
	
	@Test
	public void testMapToJSON3() throws Exception {
		
		Map<String, Object> map = Map.of(
			"item1", "apple",
			"item2", 12.3,
			"item3", List.of("banna", "A", 34.5, 12),
			"item4", Map.of("name", "test", "addr", "test2")
		);
		
		String jsonStr = JSONUtil.beautifyJSON(map);
		System.out.println(jsonStr);
	}
	
	@Test
	public void testJSONMapParse1() throws Exception {
		
		String jsonMsg = "{}";
		Map<String, Object> map = JSONUtil.parseMap(jsonMsg);
		
		assertEquals("{}", map.toString());
	}

	@Test
	public void testJSONMapParse2() throws Exception {
		
		String jsonMsg = "{\"id\": 12}";
		Map<String, Object> map = JSONUtil.parseMap(jsonMsg);
		
		assertEquals("{id=12}", map.toString());
	}
	
	@Test
	public void testJSONMapParse3() throws Exception {
		
		String jsonMsg = "{\"id\": 12.345}";
		Map<String, Object> map = JSONUtil.parseMap(jsonMsg);
		
		assertEquals("{id=12.345}", map.toString());
	}
	
	@Test
	public void testJSONMapParse4() throws Exception {
		
		String jsonMsg = "{\"id\": \"test 입니다.\"}";
		Map<String, Object> map = JSONUtil.parseMap(jsonMsg);
		
		assertEquals("{id=test 입니다.}", map.toString());
	}
	
	@Test
	public void testJSONMapParse5() throws Exception {
		
		String jsonMsg = "{\"id\": \"test \\\"  \\n 입니다.\"}";
		Map<String, Object> map = JSONUtil.parseMap(jsonMsg);
		
		assertEquals("{id=test \\\"  \\n 입니다.}", map.toString());
	}
	
	@Test
	public void testJSONMapParse6() throws Exception {
		
		String jsonMsg = "{\"id\": [12, \"삼사\", 56.78]}";
		Map<String, Object> map = JSONUtil.parseMap(jsonMsg);
		
		assertEquals("{id=[12, 삼사, 56.78]}", map.toString());
	}
	
	@Test
	public void testJSONMapParse7() throws Exception {
		
		String jsonMsg = "{\"id\": {\"item1\": 12, \"item2\": \"삼사\", \"item3\": 56.78}}";
		Map<String, Object> map = JSONUtil.parseMap(jsonMsg);
		
		assertEquals("{id={item2=삼사, item1=12, item3=56.78}}", map.toString());
	}
	
	@Test
	public void testJSONListParse1() throws Exception {
		
		String jsonMsg = "[]";
		List<Object> list = JSONUtil.parseList(jsonMsg);
		
		assertEquals("[]", list.toString());
	}
	
	@Test
	public void testJSONListParse2() throws Exception {
		
		String jsonMsg = "[12, \"삼사\", 56.78]";
		List<Object> list = JSONUtil.parseList(jsonMsg);
		
		assertEquals("[12, 삼사, 56.78]", list.toString());
	}
	
	@Test
	public void testJSONListParse3() throws Exception {
		
		String jsonMsg = "[12, \"삼사\", 56.78, {\"item1\": 12, \"item2\": \"삼사\", \"item3\": 56.78}]";
		List<Object> list = JSONUtil.parseList(jsonMsg);
		
		assertEquals("[12, 삼사, 56.78, {item2=삼사, item1=12, item3=56.78}]", list.toString());
	}
}

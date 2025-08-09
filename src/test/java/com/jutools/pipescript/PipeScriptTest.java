package com.jutools.pipescript;


import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.jutools.script.pipescript.PipeScript;

public class PipeScriptTest {

	@Test
	public void test() throws Exception {
		
		Map<String, Object> values = new HashMap<>();
		values.put("test", 10);
		
		PipeScript script = PipeScript.compile("test == 10 | test != 1");
		script.run();
		
		Thread.sleep(1000);
		
		script.getInQueue().put(values);
		
		Thread.sleep(1000);
	}
}

package com.jutools;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class PublishUtilTest {

	@Test
	public void test() throws Exception {
		
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("test message 1");
		messages.add("test message 2");
		
		Map<String, Object> values = new HashMap<>();
		values.put("messages", messages);
		
		
		File formatFile = new File("resources/publisher/testformat.xml");
		System.out.println(formatFile.canRead());
		
		byte[] read = BytesUtil.readAllBytes(formatFile);
		System.out.println(new String(read));
		
		try {
			PublishUtil.publishToConsole(formatFile);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}

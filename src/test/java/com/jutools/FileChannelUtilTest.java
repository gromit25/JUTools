package com.jutools;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

import org.junit.jupiter.api.Test;

class FileChannelUtilTest {

	@Test
	void testRead1() throws Exception {
		
		File file = new File("resources/read_test.txt");
		
		try(FileChannel chnl = FileChannel.open(file.toPath(), StandardOpenOption.READ)) {
			
			FileChannelUtil util = new FileChannelUtil(chnl);
			String read = null;
			while( (read = util.readLine()) != null) {
				System.out.println(read);
			}
			
			assertTrue(true);
			
		} catch(Exception ex) {
			fail("exception is occured");
		}
	}
	
	@Test
	void testRead2() throws Exception {
		
		File file = new File("resources/read_test.txt");
		
		try(FileChannel chnl = FileChannel.open(file.toPath(), StandardOpenOption.READ)) {
			
			FileChannelUtil util = new FileChannelUtil(chnl, 10);
			String read = null;
			while( (read = util.readLine()) != null) {
				System.out.println(read);
			}
			
			assertTrue(true);
			
		} catch(Exception ex) {
			fail("exception is occured");
		}
	}

}

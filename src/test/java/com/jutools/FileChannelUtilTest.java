package com.jutools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

/**
 * FileChannelUtil 클래스의 테스트 케이스
 * 
 * @author jmsohn
 */
public class FileChannelUtilTest {
	
	/**
	 * 람다 함수 테스트용 클래스
	 * 
	 * @author jmsohn
	 */
	class ReadCnt {
		int count;
		
		ReadCnt() {
			this.count = 0;
		}
	}
	
	@Test
	public void testWrite1() throws Exception {
		
		File file = new File("resources/write_test.txt");
		OpenOption[] options = {StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE};
		
		try(
			FileChannelUtil util = new FileChannelUtil(file, options);
		) {
			util.write("Hello World!");
			assertTrue(true);
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}
	
	@Test
	public void testWrite2() throws Exception {
		
		File file = new File("resources/write_test.txt");
		OpenOption[] options = {StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE};
		
		try(
			FileChannelUtil util = new FileChannelUtil(file, options);
		) {
			util.write("안녕하세요.");
			assertTrue(true);
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}

	@Test
	public void testRead1() throws Exception {
		
		File file = new File("resources/read_test.txt");
		
		try(
			FileChannel chnl = FileChannel.open(file.toPath(), StandardOpenOption.READ);
			FileChannelUtil util = new FileChannelUtil(chnl);
		) {
			
			int readCnt = 0;
			while(util.readLine() != null) {
				readCnt++;
			}
			
			assertEquals(46, readCnt);
			
		} catch(Exception ex) {
			fail("exception is occured");
		}
	}
	
	@Test
	public void testRead2() throws Exception {
		
		File file = new File("resources/read_test.txt");
		
		try(
			FileChannel chnl = FileChannel.open(file.toPath(), StandardOpenOption.READ);
			FileChannelUtil util = new FileChannelUtil(chnl, 10);
		) {
			
			int readCnt = 0;
			while(util.readLine() != null) {
				readCnt++;
			}
			
			assertEquals(46, readCnt);
			
		} catch(Exception ex) {
			fail("exception is occured");
		}
	}
	
	@Test
	public void testRead3() throws Exception {
		
		File file = new File("resources/read_test.txt");
		
		try(
			FileChannelUtil util = new FileChannelUtil(file, StandardOpenOption.READ);
		) {
			
			int readCnt = 0;
			while(util.readLine() != null) {
				readCnt++;
			}
			
			assertEquals(46, readCnt);
			
		} catch(Exception ex) {
			fail("exception is occured");
		}
	}
	
	@Test
	public void testRead4() throws Exception {
		
		File file = new File("resources/read_test.txt");
		
		try(
			FileChannelUtil util = new FileChannelUtil(file, 10, StandardOpenOption.READ);
		) {
			
			int readCnt = 0;
			while(util.readLine() != null) {
				readCnt++;
			}
			
			assertEquals(46, readCnt);
			
		} catch(Exception ex) {
			fail("exception is occured");
		}
	}
	
	@Test
	public void testRead5() throws Exception {
		
		File file = new File("resources/read_test.txt");
		
		try(
			FileChannelUtil util = new FileChannelUtil(file, StandardOpenOption.READ);
		) {
			
			ReadCnt readCnt = new ReadCnt();
			util.readLine(read -> {
				readCnt.count++;
			});
			
			assertEquals(46, readCnt.count);
			
		} catch(Exception ex) {
			fail("exception is occured");
		}
	}
	
}

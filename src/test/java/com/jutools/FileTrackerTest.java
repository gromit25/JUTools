package com.jutools;

import static org.junit.Assert.*;

import org.junit.Test;

public class FileTrackerTest {

	@Test
	public void test() throws Exception {
		
		try {
			System.out.println("tracking started");
			FileTracker.create("C:\\apps\\test.log").tracking(
					msg -> {
						System.out.println(msg);
					});
			System.out.println("tracking end");
		} catch(Exception ex) {
			ex.printStackTrace();
			fail("exception is occured");
		}
	}

}

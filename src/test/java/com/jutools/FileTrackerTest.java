package com.jutools;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FileTrackerTest {

	@Test
	void test() throws Exception {
		
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

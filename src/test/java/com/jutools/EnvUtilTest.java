package com.jutools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetEnvironmentVariable;

class EnvUtilTest {

	@Test
	@SetEnvironmentVariable(key = "config.name", value = "john doe")
	void testEnv1() {
		
		try {
			
			EnvUtil.set(Config.class);
			assertEquals("john doe", Config.NAME);
			
		} catch(Exception ex) {
			
			ex.printStackTrace();
			fail("exception is ocurred");
		}
	}
	
	@Test
	@SetEnvironmentVariable(key = "config.name_list", value = "john doe, hong gil-dong, jang gil-san")
	void testEnv2() {
		
		try {
			
			EnvUtil.set(Config.class);
			assertEquals(3, Config.NAME.length());
			
		} catch(Exception ex) {
			
			ex.printStackTrace();
			fail("exception is ocurred");
		}
	}

}

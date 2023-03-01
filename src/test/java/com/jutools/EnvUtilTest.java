package com.jutools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetEnvironmentVariable;

class EnvUtilTest {

	@Test
	@SetEnvironmentVariable(key = "config.name", value = "john doe")
	void testEnvPrimitiveString() {
		
		try {
			
			EnvUtil.set(Config.class);
			assertEquals("john doe", Config.NAME);
			
		} catch(Exception ex) {
			
			ex.printStackTrace();
			fail("exception is ocurred");
		}
	}
	
	@Test
	@SetEnvironmentVariable(key = "config.boolean", value = "true")
	void testEnvPrimitiveBoolean() {
		
		try {
			
			EnvUtil.set(Config.class);
			assertEquals(true, Config.BOOLEAN_VALUE);
			
		} catch(Exception ex) {
			
			ex.printStackTrace();
			fail("exception is ocurred");
		}
	}
	
	@Test
	@SetEnvironmentVariable(key = "config.int", value = "123")
	void testEnvPrimitiveInt() {
		
		try {
			
			EnvUtil.set(Config.class);
			assertEquals(123, Config.INT_VALUE);
			
		} catch(Exception ex) {
			
			ex.printStackTrace();
			fail("exception is ocurred");
		}
	}
	
	@Test
	@SetEnvironmentVariable(key = "config.float", value = "123")
	void testEnvPrimitiveFloat() {
		
		try {
			
			EnvUtil.set(Config.class);
			assertEquals((float)123, Config.FLOAT_VALUE);
			
		} catch(Exception ex) {
			
			ex.printStackTrace();
			fail("exception is ocurred");
		}
	}
	
	@Test
	@SetEnvironmentVariable(key = "config.class", value = "java.lang.Integer")
	void testEnvMethod() {
		
		try {
			
			EnvUtil.set(Config.class);
			assertEquals(Integer.class, Config.CLASS_VALUE);
			
		} catch(Exception ex) {
			
			ex.printStackTrace();
			fail("exception is ocurred");
		}
	}
	
	@Test
	@SetEnvironmentVariable(key = "config.str_list", value = "john doe, hong gil-dong, jang gil-san")
	void testEnvStrArray() {
		
		try {
			
			EnvUtil.set(Config.class);
			
			assertEquals(3, Config.STR_LIST.length);
			assertEquals("john doe", Config.STR_LIST[0]);
			assertEquals("hong gil-dong", Config.STR_LIST[1]);
			assertEquals("jang gil-san", Config.STR_LIST[2]);
			
		} catch(Exception ex) {
			
			ex.printStackTrace();
			fail("exception is ocurred");
		}
	}
	
	@Test
	@SetEnvironmentVariable(key = "config.int_list", value = "1, 2, 3")
	void testEnvIntArray() {
		
		try {
			
			EnvUtil.set(Config.class);
			
			assertEquals(3, Config.INT_LIST.length);
			assertEquals(1, Config.INT_LIST[0]);
			assertEquals(2, Config.INT_LIST[1]);
			assertEquals(3, Config.INT_LIST[2]);
			
		} catch(Exception ex) {
			
			ex.printStackTrace();
			fail("exception is ocurred");
		}
	}
	
	@Test
	@SetEnvironmentVariable(key = "config.classes", value = "java.lang.Integer, java.lang.Float")
	void testEnvMethodArray() {
		
		try {
			
			EnvUtil.set(Config.class);
			
			assertEquals(2, Config.CLASSES_VALUE.length);
			assertEquals(Integer.class, Config.CLASSES_VALUE[0]);
			assertEquals(Float.class, Config.CLASSES_VALUE[1]);
			
		} catch(Exception ex) {
			
			ex.printStackTrace();
			fail("exception is ocurred");
		}
	}

}

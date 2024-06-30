package com.jutools;

import lombok.Getter;

/**
 * 
 * 
 * @author jmsohn
 */
public class KoreanUnit {
	
	/**
	 * 
	 * 
	 * @author jmsohn
	 */
	private enum KoreanPrefix {
		
		만(10000.0),
		억(10000.0 * 10000.0),
		조(10000.0 * 10000.0 * 10000.0),
		경(10000.0 * 10000.0 * 10000.0 * 10000.0),
		해(10000.0 * 10000.0 * 10000.0 * 10000.0 * 10000.0);
		
		// -----------
		/** */
		@Getter
		private double factor;
		
		/**
		 * 생성자
		 * 
		 * @param factor
		 */
		KoreanPrefix(double factor) {
			this.factor = factor;
		}		
	}
	
	/**
	 * 
	 * 
	 * @author jmsohn
	 */
	private enum KoreanSubPrefix {
		
		십(10.0),
		백(100.0),
		천(1000.0);
		
		// -----------
		/** */
		@Getter
		private double factor;
		
		/**
		 * 생성자
		 * 
		 * @param factor
		 */
		KoreanSubPrefix(double factor) {
			this.factor = factor;
		}		

	}
	
	/**
	 * 
	 * @author jmsohn
	 */
	private enum KoreanNum {
		
		일(1.0),
		이(2.0),
		삼(3.0),
		사(4.0),
		오(5.0),
		육(6.0),
		칠(7.0),
		팔(8.0),
		구(9.0);
		
		// -----------
		/** */
		@Getter
		private double value;
		
		/**
		 * 생성자
		 * 
		 * @param factor
		 */
		KoreanNum(double value) {
			this.value = value;
		}
	}

}

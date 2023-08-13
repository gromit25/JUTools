package com.jutools;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jutools.stat.StatisticRT;

public class StatTest {

	@Test
	public void test() throws Exception {
		
		double[] values = {10, 12, 14, 18, 20, 22, 24, 25, 28, 30};
		
		StatisticRT stat = new StatisticRT();
		for(double value: values) {
			stat.add(value);
		}
		
		assertEquals(10, stat.getCount());
		assertEquals(203.0, stat.getSum(), 0.0);
		assertEquals(20.3, stat.getMean(), 0.0);
		assertEquals(41.21, stat.getVariance(), 0.01);
		assertEquals(6.419, stat.getStd(), 0.01);
		assertEquals(-0.966, stat.getSkewness(), 0.01);
		assertEquals(1.805, stat.getKurtosis(), 0.01);
	}

}

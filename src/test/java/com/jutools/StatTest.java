package com.jutools;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jutools.stat.RTParameter;
import com.jutools.stat.RTStatistic;

public class StatTest {

	@Test
	public void testRTParameter1_1() throws Exception {
		
		double[] values = {10, 12, 14, 18, 20, 22, 24, 25, 28, 30};
		
		RTParameter stat = new RTParameter();
		stat.addAll(values);
		
		assertEquals(10, stat.getCount());
		assertEquals(203.0, stat.getSum(), 0.0);
		assertEquals(20.3, stat.getMean(), 0.0);
		assertEquals(41.21, stat.getVariance(), 0.01);
		assertEquals(6.419, stat.getStd(), 0.01);
		assertEquals(-0.1505, stat.getSkewness(), 0.01);
		assertEquals(-1.1948, stat.getKurtosis(), 0.01);
	}
	
	@Test
	public void testRTParameter2_1() throws Exception {
		
		RTParameter stat = new RTParameter(203, 4533, 108353, 2715441, 10);
		
		assertEquals(10, stat.getCount());
		assertEquals(203.0, stat.getSum(), 0.0);
		assertEquals(20.3, stat.getMean(), 0.0);
		assertEquals(41.21, stat.getVariance(), 0.01);
		assertEquals(6.419, stat.getStd(), 0.01);
		assertEquals(-0.1505, stat.getSkewness(), 0.01);
		assertEquals(-1.1948, stat.getKurtosis(), 0.01);
	}
	
	@Test
	public void testRTStatistic1_1() throws Exception {
		
		double[] values = {10, 12, 14, 18, 20, 22, 24, 25, 28, 30};
		
		RTStatistic stat = new RTStatistic();
		stat.addAll(values);
		
		assertEquals(10, stat.getCount());
		assertEquals(203.0, stat.getSum(), 0.0);
		assertEquals(20.3, stat.getMean(), 0.0);
		assertEquals(45.7888, stat.getVariance(), 0.01);
		assertEquals(6.7667, stat.getStd(), 0.01);
		assertEquals(-0.1784, stat.getSkewness(), 0.01);
		assertEquals(-1.1480, stat.getKurtosis(), 0.01);
	}
	
	@Test
	public void testRTStatistic1_2() throws Exception {
		
		double[] values = {10};
		
		RTStatistic stat = new RTStatistic();
		stat.addAll(values);
		
		assertEquals(1, stat.getCount());
		assertEquals(10.0, stat.getSum(), 0.0);
		assertEquals(10.0, stat.getMean(), 0.0);
		assertEquals(0.0, stat.getVariance(), 0.01);
		assertEquals(0.0, stat.getStd(), 0.01);
		assertEquals(0.0, stat.getSkewness(), 0.01);
		assertEquals(0.0, stat.getKurtosis(), 0.01);
	}
	
	@Test
	public void testRTStatistic2_1() throws Exception {
		
		RTStatistic stat = new RTStatistic(203, 4533, 108353, 2715441, 10);
		
		assertEquals(10, stat.getCount());
		assertEquals(203.0, stat.getSum(), 0.0);
		assertEquals(20.3, stat.getMean(), 0.0);
		assertEquals(45.7888, stat.getVariance(), 0.01);
		assertEquals(6.7667, stat.getStd(), 0.01);
		assertEquals(-0.1784, stat.getSkewness(), 0.01);
		assertEquals(-1.1480, stat.getKurtosis(), 0.01);
	}

}

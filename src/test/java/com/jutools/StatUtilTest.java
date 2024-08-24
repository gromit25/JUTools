package com.jutools;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jutools.stat.Parameter;
import com.jutools.stat.Statistic;
import com.jutools.stat.PairedTTest;

/**
 * PublishUtil 클래스의 테스트 케이스
 * 
 * @author jmsohn
 */
public class StatUtilTest {

	@Test
	public void testParameter1_1() throws Exception {
		
		double[] values = {10, 12, 14, 18, 20, 22, 24, 25, 28, 30};
		
		Parameter stat = StatUtil.newParameter();
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
	public void testParameter2_1() throws Exception {
		
		Parameter stat = StatUtil.newParameter(203, 4533, 108353, 2715441, 10);
		
		assertEquals(10, stat.getCount());
		assertEquals(203.0, stat.getSum(), 0.0);
		assertEquals(20.3, stat.getMean(), 0.0);
		assertEquals(41.21, stat.getVariance(), 0.01);
		assertEquals(6.419, stat.getStd(), 0.01);
		assertEquals(-0.1505, stat.getSkewness(), 0.01);
		assertEquals(-1.1948, stat.getKurtosis(), 0.01);
	}
	
	@Test
	public void testStatistic1_1() throws Exception {
		
		double[] values = {10, 12, 14, 18, 20, 22, 24, 25, 28, 30};
		
		Statistic stat = StatUtil.newStatistic();
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
	public void testStatistic1_2() throws Exception {
		
		double[] values = {10};
		
		Statistic stat = StatUtil.newStatistic();
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
	public void testStatistic2_1() throws Exception {
		
		Statistic stat = StatUtil.newStatistic(203, 4533, 108353, 2715441, 10);
		
		assertEquals(10, stat.getCount());
		assertEquals(203.0, stat.getSum(), 0.0);
		assertEquals(20.3, stat.getMean(), 0.0);
		assertEquals(45.7888, stat.getVariance(), 0.01);
		assertEquals(6.7667, stat.getStd(), 0.01);
		assertEquals(-0.1784, stat.getSkewness(), 0.01);
		assertEquals(-1.1480, stat.getKurtosis(), 0.01);
	}
	
	@Test
	public void testPairedTTest1() throws Exception {
		
		double[] controlData = {
				1660.0,	1576.0,	1572.0,	1613.0, 1519.0,
				1572.0, 1579.0, 1625.0, 1849.0, 1600.0
		};
		
		double[] experimentalData = {
				1549.0, 1615.0, 1582.0, 1643.0, 1672.0,
				1658.0, 1603.0, 1572.0, 1628.0, 1570.0
		};
		
		PairedTTest tTest = StatUtil.newPairedTTest();
		
		for(int index = 0; index < controlData.length; index++) {
			tTest.add(controlData[index], experimentalData[index]);
		}
		
		System.out.println(tTest);
		
		assertEquals(0.233, tTest.getTValue(), 0.0005);
	}
}

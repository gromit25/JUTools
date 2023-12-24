package com.jutools;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Test;

import com.jutools.thread.BundleExecutor;

/**
 * 분할 처리 테스트 클래스
 * 
 * @author jmsohn
 */
public class BundleExecutorTest {

	@Test
	public void test() throws Exception {

		// 결과를 넣을 객체 생성
		Queue<Long> results = new ConcurrentLinkedQueue<>();
		
		// 분할 처리 객체 생성
		BundleExecutor executor = new BundleExecutor(8, (list, bundleId, start, end) -> {
			
			long sum = 0;
			
			for(int index = start; index < end; index++) {
				sum += (Long)list.get(index);
			}
			
			results.add(sum);
		});
		
		// 1 ~ 1000000 까지 더할 데이터 생성 
		List<Long> data = new ArrayList<>();
		for(long index = 1; index <= 100000000; index++) {
			data.add(index);
		}
		
		long start = System.currentTimeMillis();
		// 합계 실행
		executor.run(data).join();
		System.out.println("DEBUG:" + (System.currentTimeMillis() - start));
		
		// 분할 처리된 합계를 다시 합침
		long total = 0;
		for(long sum: results) {
			total += sum;
		}
		
		// 결과 확인
		assertEquals(5000000050000000L, total); 
	}
}

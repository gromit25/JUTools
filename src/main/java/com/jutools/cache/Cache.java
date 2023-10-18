package com.jutools.cache;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jutools.CronJob;

import lombok.Getter;

/**
 * 캐시 클래스
 * 
 * @author jmsohn
 */
public class Cache<T> {
	
	/** 캐시 데이터 저장 객체 */
	private Map<String, T> values = new ConcurrentHashMap<>();
	
	/** 캐시 저장 데이터의 최종 참조 시간 */
	private Map<String, Long> valuesLastRefTime = new ConcurrentHashMap<>();

	/** 데이터 공급자 */
	private Loader<? extends T> loader;

	/** 캐시 보유기간 */
	@Getter
	private long retainTime;
	
	/** 캐시 보유 기간 문자열 */
	@Getter
	private String cleanUpPeriod;
	
	/** 캐시 보유 기간이 지난 데이터 정리 job */
	private CronJob cleanUpJob;
	
	/**
	 * 생성자
	 * 
	 * @param loader 캐시 데이터 공급 객체
	 * @param retainTime 캐시 보유기간, 단위: millisecond
	 * @param cleanUpPeriod 캐시 정리 주기
	 */
	public Cache(Loader<? extends T> loader, long retainTime, String cleanUpPeriod) throws Exception {
		
		// 입력값 검증
		if(loader == null) {
			throw new NullPointerException("loader is null.");
		}

		if(retainTime < 0) {
			throw new IllegalArgumentException("retain time must be greater than 0:" + retainTime);
		}
		
		if(cleanUpPeriod == null) {
			throw new NullPointerException("clean up period is null.");
		}

		// 데이터 공급자 설정
		this.loader = loader;

		// 보유기간 설정
		this.retainTime = retainTime;
		
		// 캐시 보유 기간 문자열 설정
		this.cleanUpPeriod = cleanUpPeriod; 
		
		// 캐시 보유 기간이 초과된 데이터 정리 job 설정 및 실행
		this.cleanUpJob = new CronJob(cleanUpPeriod, new Runnable() {
			
			@Override
			public void run() {
				
				// 캐시 보유 기간이 초과되어 삭제된 key 목록 변수
				ArrayList<String> removedKeys = new ArrayList<>();
				// 현재 시간
				long curTime = System.currentTimeMillis();
				
				// 캐시 보유 기간이 초과된 데이터 삭제
				valuesLastRefTime.forEach((key, refTime) -> {
					
					if(retainTime > curTime - refTime) {
						removedKeys.add(key);
						values.remove(key);
					}
				});
				
				// 삭제된 키에 대해 참조 시간에서 삭제
				removedKeys.forEach(key -> {
					valuesLastRefTime.remove(key);
				});
			} // End of run
		});
		
		this.cleanUpJob.run();
		
	}
	
	/**
	 * key에 해당하는 데이터 반환
	 * 
	 * @param key 캐시에서 가져올 key 값
	 * @return key에 해당하는 데이터
	 */
	public T get(String key) throws Exception {
		
		// 키가 null 이면, null 반환
		if(key == null) {
			return null;
		}
		
		// 없을 경우, 로드해 옴
		if(this.values.containsKey(key) == false) {
			this.put(key, this.loader.get(key));
		}
		
		// 최종 참조시간 업데이트
		this.valuesLastRefTime.put(key, System.currentTimeMillis());
		
		// 키의 값을 반환함
		return this.values.get(key);
	}
	
	/**
	 * 캐시에 데이터 저장
	 * 
	 * @param key 캐시에 들어갈 key
	 * @param value key에 해당되는 데이터
	 * @return 현재 객체
	 */
	public Cache<T> put(String key, T value) throws Exception {
		
		// 입력값 검증
		if(key == null) {
			throw new NullPointerException("key is null.");
		}
		
		if(value == null) {
			throw new NullPointerException("value is null.(key:" + key + ")");
		}
		
		// 데이터 저장
		this.values.put(key, value);
		this.valuesLastRefTime.put(key, System.currentTimeMillis());
		
		return this;
	}
	
	/**
	 * map 데이터를 설정
	 * 
	 * @param map 설정할 map 데이터
	 * @return 현재 객체
	 */
	public Cache<T> putAll(Map<String, T> map) throws Exception {
		
		// 입력 데이터가 null 일이거나, 엘리먼트가 없을 경우 반환
		if(map == null || map.size() == 0) {
			return this;
		}
		
		// 데이터를 모두 입력
		for(String key: map.keySet()) {
			this.put(key, map.get(key));
		}
		
		return this;
	}
	
	/**
	 * 캐시의 크기 반환
	 * 
	 * @return 캐시 크기
	 */
	public int size() {
		return this.values.size();
	}
}

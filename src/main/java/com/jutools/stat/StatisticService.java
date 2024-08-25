package com.jutools.stat;

import com.jutools.CronJob;
import com.jutools.StringUtil;

/**
 * 통계량 서비스 클래스<br>
 * 수집 저장 주기에 따라 데이터를 수집 메소드 콜백 후 통계량 계산<br>
 * 계산 결과에 대해 저잘할 수 있도록 저장 메소드 콜백<br>
 * 리셋 주기에 저장된 통계량의 값을 모두 삭제함<br>
 * 만일, 수집 주기와 리셋 주기가 겹칠 경우 수집 저장 후 리셋을 수행함
 * 
 * @author jmsohn
 */
public class StatisticService {
	
	/** 통계량 계산 객체 */
	private Statistic stat;
	
	/** 수집 주기 객체 */
	private CronJob acquisitorCron;
	/** 리셋 주기 객체 */
	private CronJob resetCron;
	
	/** 데이터 수집 객체 */
	private DataAcquistor acquisitor;
	/** 데이터 저장 객체 */
	private DataLoader loader;
	
	/**
	 * acquisitor 작업 락<br>
	 * acquisitor가 작업 중일 경우 clear 작업을 하지 못하도록 락을 설정
	 */
	boolean isLock = false;
	
	/**
	 * 통계량 서비스 객체 생성자
	 * 
	 * @param acquisitorCronExp 데이터 수집 주기 설정 문자열
	 * @param resetCronExp 데이터 리셋 주기 설정 문자열
	 * @param acquisitor 데이터 수집 객체
	 * @param loader 데이터 저장 객체 - optional, null일 경우 수행하지 않음
	 */
	public StatisticService(String acquisitorCronExp, String resetCronExp
			, DataAcquistor acquisitor, DataLoader loader) throws Exception {
		
		// 입력값 검증
		if(StringUtil.isBlank(acquisitorCronExp) == true) {
			throw new IllegalArgumentException("acquisitor cron exp is null or blank.");
		}
		
		if(StringUtil.isBlank(resetCronExp) == true) {
			throw new IllegalArgumentException("clear cron exp is null or blank.");
		}
		
		if(acquisitor == null) {
			throw new IllegalArgumentException("acquisitor is null.");
		}

		// 통계량 계산 객체 생성
		this.stat = new Statistic();

		// 데이터 수집 객체 설정
		this.acquisitor = acquisitor;
		
		// 데이터 저장 객체 설정
		this.loader = loader;
		
		// 수집 주기 객체 생성
		this.acquisitorCron = new CronJob(acquisitorCronExp, () -> {
			
			// 수집 시작시 수집 락을 설정
			this.isLock = true;
			
			// 수집 및 저장 실행
			this.acquireAndLoad();
			
			// 수집 시간과 리셋 시간이 같을 경우
			// 수집 락을 계속 설정함 -> 수집 저장 이후 리셋을 하도록 하기 위함
			long acquireTime = this.acquisitorCron.getNextTime();
			long resetTime = this.resetCron.getNextTime();
			
			if(acquireTime != resetTime) {
				this.isLock = false;
			}
		});
		
		// 리셋 주기 객체 설정
		this.resetCron = new CronJob(resetCronExp, () -> {
			
			// acquire 실행 락이 설정되어 있는지 확인
			while(this.isLock == true) {
				try {
					Thread.sleep(300);	// acquire 실행 중일 경우 대기
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			// 통계량 리셋
			this.reset();
		});
	}
	
	/**
	 * 데이터 수집 저장 메소드 콜백
	 */
	private void acquireAndLoad() {
		
		// 데이터 수집 메소드 콜백
		double data = this.acquisitor.acquire();
		this.stat.add(data);
		
		// 데이터 로드 메소드 콜백
		if(this.loader != null) {
			this.loader.load(this.acquisitorCron.getCurrentBaseTime(), data, this.stat);
		}
	}
	
	/**
	 * 통계량 초기화
	 */
	private void reset() {
		if(this.stat != null) {
			this.stat.reset();
		}
	}
	
	/**
	 * 데이터 수집 및 통계량 계산 시작
	 */
	public void start() throws Exception {
		this.acquisitorCron.run();
		this.resetCron.run();
	}
	
	/**
	 * 데이터 수집 및 통계량 수집 중단
	 */
	public void stop() throws Exception {
		this.acquisitorCron.stop();
		this.resetCron.stop();
	}
	
	/**
	 * 통계량 객체 반환
	 * 
	 * @return 통계량 객체
	 */
	public Statistic getStatistic() {
		return this.stat;
	}
	
	@Override
	public String toString() {
		return this.stat.toString();
	}
}

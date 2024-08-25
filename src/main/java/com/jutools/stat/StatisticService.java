package com.jutools.stat;

import com.jutools.CronJob;
import com.jutools.StringUtil;

/**
 * 
 * 
 * @author jmsohn
 */
public class StatisticService {
	
	/** */
	private Statistic stats;
	
	/** */
	private CronJob acquisitorCron;
	/** */
	private CronJob resetCron;
	
	/** */
	private DataAcquistor acquisitor;
	/** */
	private DataLoader loader;
	
	/**
	 * acquisitor 작업 락<br>
	 * acquisitor가 작업 중일 경우 clear 작업을 하지 못하도록 락을 설정
	 */
	boolean isLock = false;
	
	/**
	 * 
	 * 
	 * @param acquisitorCronExp
	 * @param resetCronExp
	 * @param acquisitor
	 * @param loader
	 */
	public StatisticService(String acquisitorCronExp, String resetCronExp
			, DataAcquistor acquisitor, DataLoader loader) throws Exception {
		
		//
		if(StringUtil.isBlank(acquisitorCronExp) == true) {
			throw new IllegalArgumentException("acquisitor cron exp is null or blank.");
		}
		
		if(StringUtil.isBlank(resetCronExp) == true) {
			throw new IllegalArgumentException("clear cron exp is null or blank.");
		}
		
		if(acquisitor == null) {
			throw new IllegalArgumentException("acquisitor is null.");
		}

		//
		this.stats = new Statistic();

		//
		this.acquisitor = acquisitor;
		
		//
		this.loader = loader;
		
		//
		this.acquisitorCron = new CronJob(acquisitorCronExp, () -> {
			
			//
			this.isLock = true;
			
			//
			this.acquireAndLoad();
			
			//
			long acquireTime = this.acquisitorCron.getNextTime();
			long resetTime = this.resetCron.getNextTime();
			
			if(acquireTime != resetTime) {
				this.isLock = false;
			}
		});
		
		//
		this.resetCron = new CronJob(resetCronExp, () -> {
			
			// acquire 실행 락이 설정되어 있는지 확인
			while(this.isLock == true) {
				try {
					Thread.sleep(300);
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			// 통계량 리셋
			this.reset();
		});
	}
	
	/**
	 * 
	 */
	private void acquireAndLoad() {
		
		//
		double data = this.acquisitor.acquire();
		this.stats.add(data);
		
		//
		if(this.loader != null) {
			this.loader.load(this.stats);
		}
	}
	
	/**
	 * 
	 */
	private void reset() {
		if(this.stats != null) {
			System.out.println("RESET");
			this.stats.reset();
		}
	}
	
	/**
	 * 
	 */
	public void start() throws Exception {
		this.acquisitorCron.run();
		this.resetCron.run();
	}
	
	/**
	 * 
	 */
	public void stop() throws Exception {
		this.acquisitorCron.stop();
		this.resetCron.stop();
	}
	
	@Override
	public String toString() {
		return this.stats.toString();
	}
}

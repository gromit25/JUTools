package com.jutools.stat;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.jutools.CronJob;
import com.jutools.StringUtil;

import lombok.AccessLevel;
import lombok.Getter;

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
	
	/** 데이터 수집 객체 */
	private DataAcquistor acquisitor;
	/** 데이터 저장 객체 */
	private DataLoader loader;
	
	private String acquisitorCronExp;
	/** */
	private CronJob resetCron;
	/** */
	private List<CronBlock> cronBlocks = new Vector<>(); 
	
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
		
		//
		this.acquisitorCronExp = acquisitorCronExp;

		// 데이터 수집 객체 설정
		this.acquisitor = acquisitor;
		
		// 데이터 저장 객체 설정
		this.loader = loader;
		
		// 리셋 주기 객체 설정
		this.resetCron = new CronJob(resetCronExp, () -> {
			
			// ----- 새로운 cron block 생성 및 시작
			try {
				this.startNewCronBlock();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			// ----- 종료된 cron block 삭제
			
			// 종료된 cron block 목록 생성
			List<CronBlock> removeList = new ArrayList<>();
			for(CronBlock cronBlock: this.cronBlocks) {
				if(cronBlock.isTerminate() == true) {
					removeList.add(cronBlock);
				}
			}
			
			// 종료된 cron block 목록에 있는 cron block을 삭제
			for(CronBlock cronBlock: removeList) {
				this.cronBlocks.remove(cronBlock);
			}
		}); 
	}
	
	/**
	 * 
	 */
	private void startNewCronBlock() throws Exception {
		
		//
		CronBlock block = new CronBlock(
							this.acquisitorCronExp, this.resetCron.getNextTime()
							, this.acquisitor, this.loader);
		block.start();
		
		//
		this.cronBlocks.add(block);
	}
	
	/**
	 * 데이터 수집 및 통계량 계산 시작
	 */
	public void start() throws Exception {
		this.resetCron.run();
		this.startNewCronBlock();	// 주의! 실행 순서 중요. 꼭, resetCron 이 시작된 이후에 실행되어야 함
	}
	
	/**
	 * 데이터 수집 및 통계량 수집 중단
	 */
	public void stop() throws Exception {
		
		this.resetCron.stop();
		
		for(CronBlock cronBlock: this.cronBlocks) {
			cronBlock.stop();
		}
	}
}

/**
 * 
 * 
 * @author jmsohn
 */
class CronBlock {
	
	/** 통계량 계산 객체 */
	@Getter(AccessLevel.PACKAGE)
	private Statistic stat;
	
	/** 데이터 수집 객체 */
	private DataAcquistor acquisitor;
	/** 데이터 저장 객체 */
	private DataLoader loader;
	
	/** 수집 주기 객체 */
	private CronJob acquisitorCron;
	/** 현재 크론 블럭의 리셋 시간 - 종료시간 */
	private long resetTime;
	
	/** */
	@Getter(AccessLevel.PACKAGE)
	private boolean isTerminate = false;
	
	/**
	 * 생성자
	 */
	CronBlock(String acquisitorCronExp, long resetTime
			, DataAcquistor acquisitor, DataLoader loader) throws Exception {

		// 통계량 계산 객체 생성
		this.stat = new Statistic();
		
		// 수집 주기 객체 생성
		this.acquisitorCron = new CronJob(acquisitorCronExp, () -> {
			// 수집 및 저장 실행
			this.acquireAndLoad();
		});

		//
		this.resetTime = resetTime;

		//
		this.acquisitor = acquisitor;
		
		//
		this.loader = loader;
	}
	
	/**
	 * 데이터 수집 저장 메소드 콜백
	 */
	private void acquireAndLoad() {
		
		// 데이터 수집 메소드 콜백
		double data = this.acquisitor.acquire();
		this.stat.add(data);
		
		// 최종 실행 여부
		boolean isLast = (this.acquisitorCron.getNextTime() > this.resetTime);
		
		// 데이터 로드 메소드 콜백
		if(this.loader != null) {
			this.loader.load(this.acquisitorCron.getBaseTime(), data, this.stat, isLast);
		}
		
		//
		if(isLast == true) {
			this.stop();
		}
	}
	
	/**
	 * 
	 */
	void start() {
		this.acquisitorCron.run();
	}
	
	/**
	 * 
	 */
	void stop() {
		this.acquisitorCron.stop();
		this.isTerminate = true;
	}
}

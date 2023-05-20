package com.jutools;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.Getter;

/**
 * DB 처리 관련 Utility 클래스
 * 
 * @author jmsohn
 */
public class DBUtil {

	/**
	 * DB 종류별 JDBC 드라이버 클래스명 설정
	 *
	 * @author jmsohn
	 */
	public static enum DBDriver {

		POSTGRESQL("postgresql", "org.postgresql.Driver"),
		SQLSERVER("sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
		MARIADB("mariadb", "org.mariadb.jdbc.Driver"),
		ORACLE("oracle", "oracle.jdbc.driver.OracleDriver"),
		MYSQL("mysql", "com.mysql.cj.jdbc.Driver");

		/**
		 * db별 driver class 맵
		 * key: db 명, value: driver class 맵
		 */
		private static Hashtable<String, DBDriver> driverMap = new Hashtable<>();
		
		/*
		 * 초기화 수행
		 */
		static {
			for(DBDriver dbDriver: DBDriver.values()) {
				driverMap.put(dbDriver.name(), dbDriver);
			}
		}

		/**
		 * DB 명에 따른 JDBC 드라이버 클래스명 찾기
		 *
		 * @param name DB 명
		 * @return DB driver 종류
		 */
		public static DBDriver findByRdbKind(String name) {
			return driverMap.get(name);
		}

		/** db 명 */
		@Getter
		private String name;
		/** jdbc driver class 명 */
		@Getter
		private String driver;

		/**
		 * 생성자
		 * 
		 * @param name db 명 
		 * @param driver db driver class 명
		 */
		DBDriver(String name, String driver) {
			this.name = name;
			this.driver = driver;
		}
	}
	
	/**
	 * Hikari Connection Pool 생성
	 *
	 * @param url DB JDBC URL
	 * @param id DB 사용자 이름
	 * @param pwd DB 비밀번호
	 * @param config 설정
	 * @return HikariCP 데이터소스
	 */
    public static HikariDataSource createDataSource(String url, String id, String pwd, HikariConfig config) throws Exception {

    	// RDB 종류 확인
    	Pattern pattern = Pattern.compile("^jdbc:(?<db>postgresql|sqlserver|oracle|mysql|mariadb):.*$");
    	Matcher matcher = pattern.matcher(url);

    	String dbKind = null;
    	if (matcher.matches()) {
    		dbKind = matcher.group("db");
    	} else {
    		throw new IllegalArgumentException("invalid url:" + url);
    	}

    	// RDB 종류에 따른 JDBC 드라이버 클래스명 선택
    	DBDriver dbDriverEnum = DBDriver.findByRdbKind(dbKind);
    	String dbDriver = dbDriverEnum.getDriver();

    	// 빈 값인지 확인
    	if(StringUtil.isEmpty(dbDriver) == true) {
    		throw new IllegalArgumentException("dbDriver cannot be null or empty:" + dbKind);
    	}

    	// 설정값 구성
    	if(config == null) {
    		
    		config = new HikariConfig();
    		
        	// 디폴트 커넥션풀 설정
        	config.setMaximumPoolSize(5);            // 최대 pool size
        	config.setMinimumIdle(2);                // idle 상태에서 유지할 최소 커넥션 수, 이하로 떨어지면 maximumPoolSize 만큼 커넥션 생성 가능
        	config.setConnectionTimeout(30000);      // 풀에서 커넥션을 가져올 때 소요되는 최대 대기시간 (30초, ms)
        	config.setIdleTimeout(600000);           // 풀에 존재하는 커넥션 중에서 사용되지 않는 커넥션 객체를 커넥션 풀에서 제거하는 시간 (10분)
        	config.setMaxLifetime(1800000);          // 풀에서 관리되는 커넥션의 최대 생명주기, 이 값을 초과하면 해당 커넥션은 커넥션풀에서 제거됨 (30분)
        	config.setAutoCommit(false);             // (default: false)
        	config.setLeakDetectionThreshold(60000); // HikariCP가 커넥션 누수를 감지하기 위한 임계값 설정 (이 임계값을 초과하면 누수로 간주, 0ms는 비활성화)
    	}

    	// 접근 정보 설정
    	config.setJdbcUrl(url);
    	config.setUsername(id);
    	config.setPassword(pwd);
    	config.setDriverClassName(dbDriver);

    	return new HikariDataSource(config);
    }
	
	/**
	 * Hikari Connection Pool 생성
	 *
	 * @param url DB JDBC URL
	 * @param id DB 사용자 이름
	 * @param pwd DB 비밀번호
	 * @return HikariCP 데이터소스
	 */
    public static HikariDataSource createDataSource(String url, String id, String pwd) throws Exception {
    	return createDataSource(url, id, pwd, null);
    }
}

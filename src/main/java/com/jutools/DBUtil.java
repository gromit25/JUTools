package com.jutools;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.Getter;

/**
 * 
 */
public class DBUtil {
	
	/**
	 * DB 종류별 JDBC 드라이버 클래스명 설정
	 *
	 * @author jmsohn
	 */
	public static enum DbDriverEnum {

		POSTGRESQL("postgresql", "org.postgresql.Driver"),
	    SQLSERVER("sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
	    MARIADB("mariadb", "org.mariadb.jdbc.Driver"),
	    ORACLE("oracle", "oracle.jdbc.driver.OracleDriver"),
	    MYSQL("mysql", "com.mysql.cj.jdbc.Driver");

		/**
		 * db별 driver class 맵
		 * key: db 명, value: driver class 맵
		 */
		private static Hashtable<String, DbDriverEnum> driverMap = new Hashtable<>();
		
		/*
		 * 초기화 수행
		 */
		static {
			for(DbDriverEnum dbDriver: DbDriverEnum.values()) {
				driverMap.put(dbDriver.getDbKind(), dbDriver);
			}
		}
		
		/**
		 * DB 종류에 따른 JDBC 드라이버 클래스명 찾기
		 *
		 * @param dbKind:
		 * @return RdbDriverEnum:
		 */
		public static DbDriverEnum findByRdbKind(String dbKind) {
			return driverMap.get(dbKind);
	    }

		/** db 종류 */
	    @Getter
		private String dbKind;
	    /** jdbc driver class 명 */
	    @Getter
		private String driver;

		/**
		 * 생성자
		 * 
		 * @param dbKind db 종류 
		 * @param driver db driver class 명
		 */
		DbDriverEnum(String dbKind, String driver) {
			this.dbKind = dbKind;
			this.driver = driver;
		}
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

        // RDB 종류 확인
        Pattern pattern = Pattern.compile("^jdbc:(postgresql|sqlserver|oracle|mysql|mariadb):.*$");
        Matcher matcher = pattern.matcher(url);

        String dbKind = null;
        if (matcher.matches()) {
            dbKind = matcher.group(1);
        }

        // RDB 종류에 따른 JDBC 드라이버 클래스명 선택
        DbDriverEnum dbDriverEnum = DbDriverEnum.findByRdbKind(dbKind);
        String dbDriver = dbDriverEnum.getDriver();

        // 빈 값인지 확인
        if(StringUtil.isEmpty(dbDriver) == true) {
            throw new IllegalArgumentException("dbDriver cannot be null or empty:" + dbKind);
        }

        // 설정값 구성
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(id);
        config.setPassword(pwd);
        config.setDriverClassName(dbDriver);

        // 커넥션풀 설정
        config.setMaximumPoolSize(5);            // 최대 pool size
        config.setMinimumIdle(2);                // idle 상태에서 유지할 최소 커넥션 수, 이하로 떨어지면 maximumPoolSize 만큼 커넥션 생성 가능
        config.setConnectionTimeout(30000);      // 풀에서 커넥션을 가져올 때 소요되는 최대 대기시간 (30초, ms)
        config.setIdleTimeout(600000);           // 풀에 존재하는 커넥션 중에서 사용되지 않는 커넥션 객체를 커넥션 풀에서 제거하는 시간 (10분)
        config.setMaxLifetime(1800000);          // 풀에서 관리되는 커넥션의 최대 생명주기, 이 값을 초과하면 해당 커넥션은 커넥션풀에서 제거됨 (30분)
        config.setAutoCommit(false);             // (default: false)
        config.setLeakDetectionThreshold(60000); // HikariCP가 커넥션 누수를 감지하기 위한 임계값 설정 (이 임계값을 초과하면 누수로 간주, 0ms는 비활성화)

        return new HikariDataSource(config);
    }
}

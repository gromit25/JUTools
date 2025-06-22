package com.jutools;

import java.util.Hashtable;
import java.util.Map;

import lombok.Getter;

/**
 * DB 종류별 JDBC 드라이버 클래스명 설정
 *
 * @author jmsohn
 */
public enum DBDriver {

	ORACLE("oracle") {
		
		@Override
		public String getDriver() {
			return "oracle.jdbc.driver.OracleDriver";
		}

		@Override
		public String getUrl(String host, int port, String database) throws Exception {
			
			if(StringUtil.isBlank(host) == true) {
				throw new IllegalArgumentException("host is null or blank.");
			}
			
			if(port <= 0) {
				throw new IllegalArgumentException("port is negative.");
			}
			
			if(StringUtil.isBlank(database) == true) {
				throw new IllegalArgumentException("database is null or blank.");
			}
			
			return "jdbc:oracle:thin:@//" + host + ":" + port + "/" + database;
		}
	},
	MYSQL("mysql") {
		
		@Override
		public String getDriver() {
			return "com.mysql.cj.jdbc.Driver";
		}
		
		@Override
		public String getUrl(String host, int port, String database) {
			
			if(StringUtil.isBlank(host) == true) {
				throw new IllegalArgumentException("host is null or blank.");
			}
			
			if(port <= 0) {
				throw new IllegalArgumentException("port is negative.");
			}
			
			if(StringUtil.isBlank(database) == true) {
				throw new IllegalArgumentException("database is null or blank.");
			}
			
			return "jdbc:mysql://" + host + ":" + port + "/" + database;
		}
	},
	POSTGRESQL("postgresql") {
		
		@Override
		public String getDriver() {
			return "org.postgresql.Driver";
		}
		
		@Override
		public String getUrl(String host, int port, String database) {
			
			if(StringUtil.isBlank(host) == true) {
				throw new IllegalArgumentException("host is null or blank.");
			}
			
			if(port <= 0) {
				throw new IllegalArgumentException("port is negative.");
			}
			
			if(StringUtil.isBlank(database) == true) {
				throw new IllegalArgumentException("database is null or blank.");
			}
			
			return "jdbc:postgresql://" + host + ":" + port + "/" + database;
		}
	},
	SQLSERVER("sqlserver") {
		
		@Override
		public String getDriver() {
			return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		}
		
		@Override
		public String getUrl(String host, int port, String database) {
			
			if(StringUtil.isBlank(host) == true) {
				throw new IllegalArgumentException("host is null or blank.");
			}
			
			if(port <= 0) {
				throw new IllegalArgumentException("port is negative.");
			}
			
			if(StringUtil.isBlank(database) == true) {
				throw new IllegalArgumentException("database is null or blank.");
			}
			
			return "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + database;
		}
	},
	MARIADB("mariadb") {
		
		@Override
		public String getDriver() {
			return "org.mariadb.jdbc.Driver";
		}
		
		@Override
		public String getUrl(String host, int port, String database) {
			
			if(StringUtil.isBlank(host) == true) {
				throw new IllegalArgumentException("host is null or blank.");
			}
			
			if(port <= 0) {
				throw new IllegalArgumentException("port is negative.");
			}
			
			if(StringUtil.isBlank(database) == true) {
				throw new IllegalArgumentException("database is null or blank.");
			}
			
			return "jdbc:mariadb://" + host + ":" + port + "/" + database;
		}
	};

	/**
	 * db별 driver class 맵
	 * key: db 명, value: driver class 맵
	 */
	private static Map<String, DBDriver> driverMap = new Hashtable<>();
	
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
	public static DBDriver find(String name) {
		return driverMap.get(name);
	}

	/** db 명 */
	@Getter
	private String name;
	
	/**
	 * 생성자
	 * 
	 * @param name db 명 
	 */
	DBDriver(String name) {
		this.name = name;
	}
	
	/**
	 * 드라이버 명을 반환
	 * 
	 * @return 드라이버 명
	 */
	public abstract String getDriver();
	
	/**
	 * jdbc 접속 url 생성 후 반환
	 * 
	 * @param host 서버명 or ip
	 * @param port 포트 번호
	 * @param database 데이터베이스 명
	 * @return jdbc 접속 url
	 */
	public abstract String getUrl(String host, int port, String database) throws Exception;
}


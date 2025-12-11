package com.jutools;

/**
 * DB 종류별 JDBC 드라이버 클래스명 설정
 *
 * @author jmsohn
 */
public enum DBDriverType {

	ORACLE {
		
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
	
	MYSQL {
		
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
	
	POSTGRESQL {
		
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
	
	SQLSERVER {
		
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
	
	MARIADB {
		
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


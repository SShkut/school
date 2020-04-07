package com.foxminded.school.util;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionsPool {
	
	private static HikariDataSource dataSource;
	
	static {
		String configFile = "src/main/resources/db.properties";
		HikariConfig config = new HikariConfig(configFile);
		dataSource = new HikariDataSource(config);
	}
	
	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
}

package com.foxminded.school.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionProvider {
	
	private HikariDataSource dataSource;

	public ConnectionProvider(String fileName) {
		String configFile = fileName;
		HikariConfig config = new HikariConfig(configFile);
		this.dataSource = new HikariDataSource(config);
	}
	
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
	public DataSource getDataSource() {
		return this.dataSource;
	}
}

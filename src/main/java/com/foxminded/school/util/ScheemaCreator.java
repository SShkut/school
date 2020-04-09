package com.foxminded.school.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ScheemaCreator {
	
	private final ConnectionProvider provider;
	
	public ScheemaCreator(ConnectionProvider provider) {
		this.provider = provider;
	}
	
	public void create() throws SQLException, IOException {
		FileReader fileReader = new FileReader();
		String[] instructions = fileReader.readFile("create_tables.sql").split(";");
		try (Connection connection = provider.getConnection(); Statement statement = connection.createStatement()) {
			connection.setAutoCommit(false);
			for (String instruction : instructions) {
				statement.addBatch(instruction);
			}
			statement.executeBatch();
			connection.commit();
		}
	}
}

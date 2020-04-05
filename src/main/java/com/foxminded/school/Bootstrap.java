package com.foxminded.school;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Bootstrap {
	
	public void createSchema() throws SQLException, IOException {
		FileReader fileReader = new FileReader();
		String[] instructions = fileReader.readFile("create_tables.sql").split(";");
		try (Connection connection = ConnectionsPool.getConnection(); Statement statement = connection.createStatement()) {
			connection.setAutoCommit(false);
			for (String instruction : instructions) {
				statement.addBatch(instruction);
			}
			statement.executeBatch();
			connection.commit();
		}
	}
}

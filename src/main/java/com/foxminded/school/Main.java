package com.foxminded.school;

import java.io.IOException;
import java.sql.SQLException;

import com.foxminded.school.util.ConnectionProvider;
import com.foxminded.school.util.Menu;
import com.foxminded.school.util.SchemaCreator;
import com.foxminded.school.util.StartupDataCreator;

public class Main {
	
	public static void main(String[] args) throws SQLException, IOException {
		String fileName = "/db.properties";
		ConnectionProvider provider = new ConnectionProvider(fileName);
		SchemaCreator schemaCreator = new SchemaCreator(provider);
		schemaCreator.create();
		
		StartupDataCreator startup = new StartupDataCreator(provider);
		startup.bootstrap();
		
		Menu menu = new Menu(provider);
		menu.createMenu();
	}
}

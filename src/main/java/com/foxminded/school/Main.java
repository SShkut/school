package com.foxminded.school;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
	
	public static void main(String[] args) throws SQLException, IOException {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.createSchema();
	}
}

package com.foxminded.school;

import java.io.IOException;
import java.sql.SQLException;

import com.foxminded.school.dao.CourseDAO;
import com.foxminded.school.dao.GroupDAO;
import com.foxminded.school.dao.StudentDAO;
import com.foxminded.school.util.Menu;
import com.foxminded.school.util.ScheemaCreator;
import com.foxminded.school.util.StartupDataCreator;

public class Main {
	
	public static void main(String[] args) throws SQLException, IOException {
		ScheemaCreator scheemaCreator = new ScheemaCreator();
		scheemaCreator.create();
		
		StartupDataCreator startup = new StartupDataCreator(new GroupDAO(), new StudentDAO(), new CourseDAO());
		startup.bootstrap();
		
		Menu menu = new Menu(new CourseDAO(), new StudentDAO(), new GroupDAO());
		menu.createMenu();
	}
}

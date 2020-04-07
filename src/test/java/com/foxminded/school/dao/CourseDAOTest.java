package com.foxminded.school.dao;

import static org.junit.jupiter.api.Assertions.fail;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourseDAOTest {
	
	private CourseDAO courseDAO;

	@BeforeEach
	void setUp() throws Exception {
		IDatabaseTester tester = new JdbcDatabaseTester("org.postgresql.ds.PGSimpleDataSource", "jdbc:postgresql://localhost:5432/school", "admin", "admin");
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResource("CourseTestData.xml"));
		tester.setDataSet(dataSet);
		tester.onSetup();
		courseDAO = new CourseDAO();
	}

	@Test
	void givenExistentId_whenFindById_thenGetCourse() {
		fail("Not yet implemented");
	}

}

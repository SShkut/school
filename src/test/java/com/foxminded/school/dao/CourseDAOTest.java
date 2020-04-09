package com.foxminded.school.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.foxminded.school.model.Course;
import com.foxminded.school.model.Student;
import com.foxminded.school.util.ConnectionProvider;
import com.foxminded.school.util.ScheemaCreator;

class CourseDAOTest {
	
	private CourseDao courseDao;

	@BeforeEach
	void setUp() throws Exception {
		ConnectionProvider provider = new ConnectionProvider("/dbTest.properties");
		ScheemaCreator scheemaCreator = new ScheemaCreator(provider);
		scheemaCreator.create();
		IDatabaseTester tester = new DataSourceDatabaseTester(provider.getDataSource());
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResource("schoolTestData.xml"));
		tester.setDataSet(dataSet);
		tester.onSetup();	
		courseDao = new CourseDao(provider);
	}

	@Test
	void givenExistentId_whenFindById_thenReturnOptional() {
		Optional<Course> expected = Optional.of(new Course(3, "physics", "physics course"));
		
		Optional<Course> actual = courseDao.findById(3);
		
		assertEquals(expected, actual);
	}

	@Test
	void givenNonExistentId_whenFindById_thenReturnEmptyOptional() {
		Optional<Course> expected = Optional.empty();
		
		Optional<Course> actual = courseDao.findById(-1);		
		
		assertEquals(expected, actual);
	}
	
	@Test
	void whenFindAll_thenReturnListOfCourses() {
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(1, "biology", "biology course"));
		expected.add(new Course(2, "chemistry", "chemistry course"));
		expected.add(new Course(3, "physics", "physics course"));
		
		List<Course> actual = courseDao.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudent_whenFindAllCoursesOfStudent_thenReturnListOfCourses() {
		Student student = new Student(5, "James", "Richard");
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(1, "biology"));
		expected.add(new Course(2, "chemistry"));
		
		List<Course> actual = courseDao.findAllCoursesOfStudent(student);
		
		assertEquals(expected, actual);
	}
}

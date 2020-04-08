package com.foxminded.school.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.foxminded.school.model.Course;
import com.foxminded.school.model.Student;

class CourseDAOTest {
	
	private CourseDAO courseDAO;

	@BeforeEach
	void setUp() throws Exception {
		IDatabaseTester tester = new JdbcDatabaseTester("org.postgresql.ds.PGSimpleDataSource", "jdbc:postgresql://localhost:5432/school", "admin", "admin");
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResource("StudentCourseTestData.xml"));
		tester.setDataSet(dataSet);
		tester.onSetup();
		courseDAO = new CourseDAO();
	}

	@Test
	void givenExistentId_whenFindById_thenReturnCourse() {
		Course expected = new Course(3, "physics", "physics course");
		
		Course actual = courseDAO.findById(3);
		
		assertEquals(expected, actual);
	}

	@Test
	void givenNonExistentId_whenFindById_thenReturnEmptyCourse() {
		Course expected = new Course();
		
		Course actual = courseDAO.findById(-1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void whenFindAll_thenReturnListOfCourses() {
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(1, "biology", "biology course"));
		expected.add(new Course(2, "chemistry", "chemistry course"));
		expected.add(new Course(3, "physics", "physics course"));
		
		List<Course> actual = courseDAO.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenExistentStudent_whenIsExists_thenReturnTrue() {
		Course course = new Course(3, "physics", "physics course");
		
		assertTrue(courseDAO.isExists(course));
	}
	
	@Test
	void givenNonExistentStudent_whenIsExists_thenReturnFalse() {
		Course course = new Course(-1, "physics", "physics course");
		
		assertFalse(courseDAO.isExists(course));
	}
	
	@Test
	void givenStudent_whenFindAllCoursesOfStudent_thenReturnListOfCourses() {
		Student student = new Student(5, "James", "Richard");
		List<Course> expected = new ArrayList<>();
		expected.add(new Course(1, "biology"));
		expected.add(new Course(2, "chemistry"));
		
		List<Course> actual = courseDAO.findAllCoursesOfStudent(student);
		
		assertEquals(expected, actual);
	}
}

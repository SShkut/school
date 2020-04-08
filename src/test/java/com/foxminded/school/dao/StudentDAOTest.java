package com.foxminded.school.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.foxminded.school.model.Course;
import com.foxminded.school.model.Group;
import com.foxminded.school.model.Student;

class StudentDAOTest {

	private StudentDAO studentDAO;

	@BeforeEach
	void setUp() throws Exception {
		IDatabaseTester tester = new JdbcDatabaseTester("org.postgresql.ds.PGSimpleDataSource", "jdbc:postgresql://localhost:5432/school", "admin", "admin");
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResource("StudentCourseTestData.xml"));
		tester.setDataSet(dataSet);
		tester.onSetup();
		studentDAO = new StudentDAO();
	}

	@Test
	void givenExistentId_whenFindById_thenReturnStudent() {
		Student expected = new Student(3, "David", "Nagel", 1);
		
		Student actual = studentDAO.findById(3);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenNonExistentId_whenFindById_thenReturnEmptyStudent() {		
		Student expected = new Student();
		
		Student actual = studentDAO.findById(-1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void whenFindAll_thenReturnListOfStudents() {
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(1, "Michael", "Gaer", 1));
		expected.add(new Student(2, "Linda", "MacCubbin", 1));
		expected.add(new Student(3, "David", "Nagel", 1));
		expected.add(new Student(4, "David", "Aaron", 3));
		expected.add(new Student(5, "James", "Richard", 2));
		expected.add(new Student(6, "Charles", "Ulloa", 2));
		
		List<Student> actual = studentDAO.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenId_whenDeleteById_thenDelteStudent() {
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(2, "Linda", "MacCubbin", 1));
		expected.add(new Student(3, "David", "Nagel", 1));
		expected.add(new Student(4, "David", "Aaron", 3));
		expected.add(new Student(5, "James", "Richard", 2));
		expected.add(new Student(6, "Charles", "Ulloa", 2));
		
		studentDAO.deleteById(1);
		
		List<Student> actual = studentDAO.findAll();		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenExistentStudent_whenIsExists_thenReturnTrue() {
		Student group = new Student(2, "Linda", "MacCubbin", 1);
		
		assertTrue(studentDAO.isExists(group));
	}
	
	@Test
	void givenNonExistentStudent_whenIsExists_thenReturnFalse() {
		Student group = new Student(-1, "Linda", "MacCubbin", 1);
		
		assertFalse(studentDAO.isExists(group));
	}
	
	@Test
	void givenStudentAndGroup_whenAssignToGroup_thenNewGroupForStudent() {
		Student studentToUpdate = new Student(2, "Linda", "MacCubbin", 1);
		Student expected = new Student(2, "Linda", "MacCubbin", 2);		
		Group group = new Group(2, "BB-12");
		
		studentDAO.assignToGroup(studentToUpdate, group);
		
		Student actual = studentDAO.findById(2);		
		assertEquals(expected, actual);
	}
	
	@Test 
	void givenSudentAndCourse_whenAssignToCourse_thenAddCourseToStudent() {
		List<Student> expected = new ArrayList<>();
		expected.add(new Student(1, "Michael", "Gaer"));
		expected.add(new Student(2, "Linda", "MacCubbin"));
		expected.add(new Student(6, "Charles", "Ulloa"));
		Student student = new Student(1, "Michael", "Gaer");
		Course course = new Course(3, "physics");
		
		studentDAO.assignToCourse(student, course);
		
		List<Student> actual = studentDAO.findAllStudentsRelatedToCourse(3);
		assertEquals(expected, actual);
	}
	
	@Test
	void givenCourseId_whenFindAllStudentsRelatedToCourse_thenReturnListOfStudents() {
		List <Student> expected = new ArrayList<>(Arrays.asList(new Student(2, "Linda", "MacCubbin"), new Student(6, "Charles", "Ulloa")));
		
		List<Student> actual = studentDAO.findAllStudentsRelatedToCourse(3);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenCourseId_whenFindAllStudentWithoutGivenCourse_thenReturnListOfStuedents() {
		List<Student> expected = new ArrayList<>(Arrays.asList(new Student(1, "Michael", "Gaer")));
		
		List<Student> actual = studentDAO.findAllStudentWithoutGivenCourse(1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudentAndCourse_whenRemoveStudentFromCourse_thenRemoveStudentFromCourse() {
		List <Student> expected = new ArrayList<>(Arrays.asList(new Student(2, "Linda", "MacCubbin")));
		
		studentDAO.removeStudentFromCourse(new Student(6, "Charles", "Ulloa"), new Course(3, "physics"));
		
		List<Student> actual = studentDAO.findAllStudentsRelatedToCourse(3);		
		assertEquals(expected, actual);
	}
}

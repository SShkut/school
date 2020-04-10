package com.foxminded.school.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.foxminded.school.model.Course;
import com.foxminded.school.model.Group;
import com.foxminded.school.model.Student;
import com.foxminded.school.util.ConnectionProvider;
import com.foxminded.school.util.SchemaCreator;

class StudentDaoTest {

	private StudentDao studentDao;

	@BeforeEach
	void setUp() throws Exception {
		ConnectionProvider provider = new ConnectionProvider("/dbTest.properties");
		SchemaCreator schemaCreator = new SchemaCreator(provider);
		schemaCreator.create();
		IDatabaseTester tester = new DataSourceDatabaseTester(provider.getDataSource());
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResource("schoolTestData.xml"));
		tester.setDataSet(dataSet);
		tester.onSetup();
		studentDao = new StudentDao(provider);
	}

	@Test
	void givenExistentId_whenFindById_thenReturnOptional() {
		Optional<Student> expected = Optional.of(new Student(3, "David", "Nagel", 1));
		
		Optional<Student> actual = studentDao.findById(3);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenNonExistentId_whenFindById_thenReturnEmptyOptional() {		
		Optional<Student> expected = Optional.empty();
		
		Optional<Student> actual = studentDao.findById(-1);
		
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
		
		List<Student> actual = studentDao.findAll();
		
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
		
		studentDao.deleteById(1);
		
		List<Student> actual = studentDao.findAll();		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudentAndGroup_whenAssignToGroup_thenNewGroupForStudent() {
		Student studentToUpdate = new Student(2, "Linda", "MacCubbin", 1);
		Student expected = new Student(2, "Linda", "MacCubbin", 2);		
		Group group = new Group(2, "BB-12");
		
		studentDao.assignToGroup(studentToUpdate, group);
		
		Student actual = studentDao.findById(2).get();		
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
		
		studentDao.assignToCourse(student, course);
		
		List<Student> actual = studentDao.findAllStudentsRelatedToCourse(3);
		assertEquals(expected, actual);
	}
	
	@Test
	void givenCourseId_whenFindAllStudentsRelatedToCourse_thenReturnListOfStudents() {
		List <Student> expected = new ArrayList<>(Arrays.asList(new Student(2, "Linda", "MacCubbin"), new Student(6, "Charles", "Ulloa")));
		
		List<Student> actual = studentDao.findAllStudentsRelatedToCourse(3);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenCourseId_whenFindAllStudentWithoutGivenCourse_thenReturnListOfStuedents() {
		List<Student> expected = new ArrayList<>(Arrays.asList(new Student(1, "Michael", "Gaer")));
		
		List<Student> actual = studentDao.findAllStudentWithoutGivenCourse(1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudentAndCourse_whenRemoveStudentFromCourse_thenRemoveStudentFromCourse() {
		List <Student> expected = new ArrayList<>(Arrays.asList(new Student(2, "Linda", "MacCubbin")));
		
		studentDao.removeStudentFromCourse(new Student(6, "Charles", "Ulloa"), new Course(3, "physics"));
		
		List<Student> actual = studentDao.findAllStudentsRelatedToCourse(3);		
		assertEquals(expected, actual);
	}
}

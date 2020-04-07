package com.foxminded.school.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.foxminded.school.dao.CourseDAO;
import com.foxminded.school.dao.GroupDAO;
import com.foxminded.school.dao.StudentDAO;
import com.foxminded.school.model.Course;
import com.foxminded.school.model.Group;
import com.foxminded.school.model.Student;

public class StartupDataCreator {
	
	private final static Random rand = new Random();
	
	private final GroupDAO groupDAO;
	private final StudentDAO studentDAO;
	private final CourseDAO courseDAO;
	
	public StartupDataCreator(GroupDAO groupDAO, StudentDAO studentDAO, CourseDAO courseDAO) {
		this.groupDAO = groupDAO;
		this.studentDAO = studentDAO;
		this.courseDAO = courseDAO;
	}
	
	public void bootstrap() {
		createGroups(10);
		createCourses();
		createStudents(200);
		assignStudentsToGroups();
		assignStudentsToCourses();
	}

	private void createGroups(int groupsNumber) {
		StringBuilder groupName = new StringBuilder();
		for (int i = 0; i < groupsNumber; ++i) {
			groupName.append(getRandomCharacter())
				.append(getRandomCharacter())
				.append("-")
				.append(rand.nextInt(9))
				.append(rand.nextInt(9));
			Group group = new Group(groupName.toString());
			groupDAO.save(group);
			groupName.setLength(0);
		}
	}
	
	private char getRandomCharacter() {
		return (char) ('A' + rand.nextInt(26));
	}
	
	private void createCourses() {
		Map<String, String> courses = new HashMap<>();
		courses.put("math", "math course");
		courses.put("biology", "biology course");
		courses.put("chemistry", "chemistry course");
		courses.put("physics", "physics course");
		courses.put("geography", "geography course");
		courses.put("digital electronics", "digital electronics course");
		courses.put("analogue electronics", "analogue electronics course");
		courses.put("numerical analysis", "numerical analysis course");
		courses.put("probabilistic theory", "probabilistic theory course");
		courses.put("networking", "networking course");
		courses.forEach((k, v) -> courseDAO.save(new Course(k, v)));
	}
	
	private void createStudents(int studentsNumber) {
		List<String> firstNames = new ArrayList<>(Arrays.asList("James", "Mary", "John", "Patricia", "Robert"
				, "Jennifer", "Michael", "Linda", "William", "Elizabeth", "David", "Barbara", "Richard", "Susan", "Joseph"
				, "Jessica", "Thomas", "Sarah", "Charles", "Karen"));
		List<String> lastNames = new ArrayList<>(Arrays.asList("Aaron", "Bacino", "Cadogan", "D’Acquisto", "Eager"
				, "Fabbro", "Gaer", "Haaland", "Iannaccone", "MacCubbin", "Nagel", "O’Brien", "Padgitt", "Quattro", "James"
				, "Rack", "Sacchi", "Tague", "Ulloa", "Valek"));
		for (int i = 0; i < studentsNumber; ++i) {
			studentDAO.save(new Student(firstNames.get(rand.nextInt(19)), lastNames.get(rand.nextInt(19))));
		}
		
	}
	
	private void assignStudentsToGroups() {
		List<Student> students = studentDAO.findAll();
		List<Group> groups = groupDAO.findAll();
		for (Group group : groups) {
			List<Student> studentsForGroup = getStudentNumTimes(rand.nextInt(20) + 10, students);
			studentsForGroup.forEach(student -> studentDAO.assignToGroup(student, group));
		}
		
	}
	
	private List<Student> getStudentNumTimes(int num, List<Student> students) {
		List<Student> result = new ArrayList<>();
		for (int i = 0; i < num; ++i) {
			result.add(students.get(rand.nextInt(students.size())));
		}
		return result;
	}
	
	private void assignStudentsToCourses() {
		List<Student> students = studentDAO.findAll();
		List<Course> courses = courseDAO.findAll();
		for (Student student : students) {
			List<Course> coursesForStudents = getCoursesNumTimes(rand.nextInt(3), courses);
			coursesForStudents.forEach(course -> studentDAO.assignToCourse(student, course));
		}
	}
	
	private List<Course> getCoursesNumTimes(int num, List<Course> courses) {
		List<Course> result = new ArrayList<>();
		for(int i = 0; i < num; ++i) {
			result.add(courses.get(rand.nextInt(courses.size())));
		}
		return result;
	}
}

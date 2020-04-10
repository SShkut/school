package com.foxminded.school.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.foxminded.school.dao.CourseDao;
import com.foxminded.school.dao.GroupDao;
import com.foxminded.school.dao.StudentDao;
import com.foxminded.school.model.Course;
import com.foxminded.school.model.Group;
import com.foxminded.school.model.Student;

public class StartupDataCreator {
	
	private static final Random rand = new Random();
	
	private final GroupDao groupDao;
	private final StudentDao studentDao;
	private final CourseDao courseDao;
	
	public StartupDataCreator(ConnectionProvider provider) {
		this.groupDao = new GroupDao(provider);
		this.studentDao = new StudentDao(provider);
		this.courseDao = new CourseDao(provider);
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
			groupDao.save(group);
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
		courses.forEach((k, v) -> courseDao.save(new Course(k, v)));
	}
	
	private void createStudents(int studentsNumber) {
		List<String> firstNames = Arrays.asList("James", "Mary", "John", "Patricia", "Robert"
				, "Jennifer", "Michael", "Linda", "William", "Elizabeth", "David", "Barbara", "Richard", "Susan", "Joseph"
				, "Jessica", "Thomas", "Sarah", "Charles", "Karen");
		List<String> lastNames = Arrays.asList("Aaron", "Bacino", "Cadogan", "D’Acquisto", "Eager"
				, "Fabbro", "Gaer", "Haaland", "Iannaccone", "MacCubbin", "Nagel", "O’Brien", "Padgitt", "Quattro", "James"
				, "Rack", "Sacchi", "Tague", "Ulloa", "Valek");
		for (int i = 0; i < studentsNumber; ++i) {
			studentDao.save(new Student(firstNames.get(rand.nextInt(19)), lastNames.get(rand.nextInt(19))));
		}
		
	}
	
	private void assignStudentsToGroups() {
		List<Student> students = studentDao.findAll();
		List<Group> groups = groupDao.findAll();
		for (Group group : groups) {
			List<Student> studentsForGroup = getStudentNumTimes(rand.nextInt(20) + 10, students);
			studentsForGroup.forEach(student -> studentDao.assignToGroup(student, group));
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
		List<Student> students = studentDao.findAll();
		List<Course> courses = courseDao.findAll();
		for (Student student : students) {
			List<Course> coursesForStudents = getCoursesNumTimes(rand.nextInt(3), courses);
			coursesForStudents.forEach(course -> studentDao.assignToCourse(student, course));
		}
	}
	
	private List<Course> getCoursesNumTimes(int num, List<Course> courses) {
		List<Course> result = new ArrayList<>();
		for(int i = 0; i <= num; ++i) {
			result.add(courses.get(rand.nextInt(courses.size())));
		}
		return result;
	}
}

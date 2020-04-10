package com.foxminded.school.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.foxminded.school.dao.CourseDao;
import com.foxminded.school.dao.GroupDao;
import com.foxminded.school.dao.StudentDao;
import com.foxminded.school.model.Course;
import com.foxminded.school.model.Group;
import com.foxminded.school.model.Student;

public class Menu {
	
	private static final Scanner scanner = new Scanner(System.in);

	private final CourseDao courseDao;
	private final StudentDao studentDao;
	private final GroupDao groupDao;	
	
	public Menu(ConnectionProvider provider) {
		this.courseDao = new CourseDao(provider);
		this.studentDao = new StudentDao(provider);
		this.groupDao = new GroupDao(provider);
	}

	public void createMenu() {

		showPrompt();
		while (scanner.hasNext()) {
			String menuItem = scanner.next();
			if (menuItem.equalsIgnoreCase("a")) {
				findAllGroupsWithLessOrEqualStudentCount();
			} else if (menuItem.equalsIgnoreCase("b")) {
				findAllStudentsRelatedToCourse();
			} else if (menuItem.equalsIgnoreCase("c")) {
				save();
			} else if (menuItem.equalsIgnoreCase("d")) {
				deleteById();
			} else if (menuItem.equalsIgnoreCase("e")) {
				assignToCourse();
			} else if (menuItem.equalsIgnoreCase("f")) {
				removeStudentFromCourse();
			} else {
				System.out.println("Should be a valid menu identificator");
				showPrompt();
			}
		}
		scanner.close();
	}
	
	private void findAllGroupsWithLessOrEqualStudentCount() {
		System.out.print("Enter the student count ");
		int studentCount = scanner.nextInt();
		List<Group> groups = groupDao.findAllGroupsWithLessOrEqualStudentCount(studentCount);
		groups.forEach(System.out::println);
		showPrompt();
	}
	
	private void findAllStudentsRelatedToCourse() {
		List<Course> courses = courseDao.findAll();
		courses.forEach(System.out::println);
		System.out.print("Chose course by printing id of the course: ");
		int courseId = scanner.nextInt();
		List<Student> students = studentDao.findAllStudentsRelatedToCourse(courseId);
		if (students.isEmpty()) {
			System.out.println("There is no such course or there are no students assigned to this course");
		} else {
			students.forEach(System.out::println);
		}
		showPrompt();
	}
	
	private void save() {
		System.out.print("Enter student's first name: ");
		String firstName = scanner.next();
		System.out.print("Enter student's last name: ");
		String lastName = scanner.next();
		studentDao.save(new Student(firstName, lastName));
		showPrompt();
	}
	
	private void deleteById() {
		System.out.print("Enter student's id: ");
		int id = scanner.nextInt();
		if (studentDao.findById(id).isPresent()) {
			studentDao.deleteById(id);
		} else {					
			System.out.println("There is no student with given id.");
		}
		showPrompt();
	}
	
	private void assignToCourse() {
		List<Course> courses = courseDao.findAll();
		courses.forEach(System.out::println);
		System.out.print("Chose course by printing id of the course: ");
		int courseId = scanner.nextInt();
		Optional<Course> course = courses.stream().filter((c) -> c.getId().equals(courseId)).findFirst();
		if (course.isPresent()) {
			List<Student> studentsWithoutCourse = studentDao.findAllStudentWithoutGivenCourse(courseId);
			studentsWithoutCourse.forEach(System.out::println);
			System.out.print("Chose student by printing id: ");
			int id = scanner.nextInt();
			Optional<Student> student = studentDao.findById(id);
			if (student.isPresent()) {
				studentDao.assignToCourse(student.get(), course.get());
			} else {
				System.out.println("There is no such student");
			}
		} else {
			System.out.println("There is no course with given name");
		}
		showPrompt();
	}

	private void removeStudentFromCourse() {
		List<Student> students = studentDao.findAll();
		students.forEach(System.out::println);
		System.out.print("Chose student by printing id: ");
		int studentId = scanner.nextInt();
		Optional<Student> student = studentDao.findById(studentId);
		List<Course> courses = new ArrayList<>();
		if (student.isPresent()) {
			courses = courseDao.findAllCoursesOfStudent(student.get());
		}
		if (!courses.isEmpty()) {
			courses.forEach(System.out::println);
			System.out.print("Chose course by printing id of the course: ");
			int courseId = scanner.nextInt();
			Optional<Course> course = courses.stream().filter((c) -> c.getId() == courseId).findFirst();
			if (course.isPresent()) {
				studentDao.removeStudentFromCourse(student.get(), course.get());
				System.out.println("Student " + student + " removed from course " + course);
			}
			showPrompt();
		} else {
			showPrompt();
		}
	}
	
	private void showPrompt() {
		System.out.println();
		System.out.println("-----------------------------------------------------------");
		System.out.println("a. Find all groups with less or equals student count");
		System.out.println("b. Find all students related to course with given name");
		System.out.println("c. Add new student");
		System.out.println("d. Delete student by STUDENT_ID");
		System.out.println("e. Add a student to the course (from a list)");
		System.out.println("f. Remove the student from one of his or her courses");
		System.out.println("-----------------------------------------------------------");
		System.out.print("Choose one of the menu item by printing appropriate letter: ");
	}
}

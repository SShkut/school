package com.foxminded.school.util;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.foxminded.school.dao.CourseDAO;
import com.foxminded.school.dao.GroupDAO;
import com.foxminded.school.dao.StudentDAO;
import com.foxminded.school.model.Course;
import com.foxminded.school.model.Group;
import com.foxminded.school.model.Student;

public class Menu {

	private final CourseDAO courseDAO;
	private final StudentDAO studentDAO;
	private final GroupDAO groupDAO;	
	
	public Menu(CourseDAO courseDAO, StudentDAO studentDAO, GroupDAO groupDAO) {
		this.courseDAO = courseDAO;
		this.studentDAO = studentDAO;
		this.groupDAO = groupDAO;
	}

	public void createMenu() {
		Scanner scanner = new Scanner(System.in);
		showPrompt();
		while (scanner.hasNext()) {
			String menuItem = scanner.next();
			if (menuItem.equalsIgnoreCase("a")) {
				System.out.print("Enter the student count ");
				int studentCount = scanner.nextInt();
				List<Group> groups = groupDAO.findAllGroupsWithLessOrEqualStudentCount(studentCount);
				groups.forEach(System.out::println);
				showPrompt();
			} else if (menuItem.equalsIgnoreCase("b")) {
				List<Course> courses = courseDAO.findAll();
				courses.forEach(System.out::println);
				System.out.print("Chose course by printing id of the course: ");
				int courseId = scanner.nextInt();
				List<Student> students = studentDAO.findAllStudentsRelatedToCourse(courseId);
				if (students.isEmpty()) {
					System.out.println("There is no such course or there are no students assigned to this course");
				} else {
					students.forEach(System.out::println);
				}
				showPrompt();
			} else if (menuItem.equalsIgnoreCase("c")) {
				System.out.print("Enter student's first name: ");
				String firstName = scanner.next();
				System.out.print("Enter student's last name: ");
				String lastName = scanner.next();
				studentDAO.save(new Student(firstName, lastName));
				showPrompt();
			} else if (menuItem.equalsIgnoreCase("d")) {
				System.out.print("Enter student's id: ");
				int id = scanner.nextInt();
				if (!studentDAO.isExists(new Student(id))) {
					System.out.println("There is no student with given id.");
				} else {
					studentDAO.deleteById(id);
				}
				showPrompt();
			} else if (menuItem.equalsIgnoreCase("e")) {
				List<Course> courses = courseDAO.findAll();
				courses.forEach(System.out::println);
				System.out.print("Chose course by printing id of the course: ");
				int courseId = scanner.nextInt();
				Optional<Course> courseOpt = courses.stream().filter((c) -> c.getId().equals(courseId)).findFirst();
				Course course;
				if (courseOpt.isPresent()) {
					course = courseOpt.get();
					List<Student> studentsWithoutCourse = studentDAO.findAllStudentWithoutGivenCourse(courseId);
					studentsWithoutCourse.forEach(System.out::println);
					System.out.print("Chose student by printing id: ");
					int id = scanner.nextInt();
					Student student = studentDAO.findById(id);
					studentDAO.assignToCourse(student, course);
				} else {
					System.out.println("There is no course with given name");
				}
				showPrompt();
			} else if (menuItem.equalsIgnoreCase("f")) {
				List<Student> students = studentDAO.findAll();
				students.forEach(System.out::println);
				System.out.print("Chose student by printing id: ");
				int studentId = scanner.nextInt();
				Student student = studentDAO.findById(studentId);
				List<Course> courses = courseDAO.findAllCoursesOfStudent(student);
				if (!courses.isEmpty()) {
					courses.forEach(System.out::println);
					System.out.print("Chose course by printing id of the course: ");
					int courseId = scanner.nextInt();
					Optional<Course> courseOpt = courses.stream().filter((c) -> c.getId() == courseId).findFirst();
					Course course;
					if (courseOpt.isPresent()) {
						course = courseOpt.get();
						studentDAO.removeStudentFromCourse(student, course);
						System.out.println("Student " + student + " removed from course " + course);
					}
					showPrompt();
				} else {
					showPrompt();
				}
			} else {
				System.out.println("Should be a valid menu identificator");
				showPrompt();
			}
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

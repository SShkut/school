package com.foxminded.school.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.foxminded.school.model.Course;
import com.foxminded.school.model.Group;
import com.foxminded.school.model.Student;
import com.foxminded.school.util.ConnectionProvider;

public class StudentDao {
	
	private static final String SAVE = "INSERT INTO students (first_name, last_name) values(?, ?)";
	private static final String FIND_BY_ID = "SELECT * FROM students where id = ?";
	private static final String FIND_ALL = "SELECT * FROM students ORDER BY id";
	private static final String DELETE_BY_ID = "DELETE FROM students WHERE id = ?";
	private static final String ASSIGN_TO_GROUP = "UPDATE students SET group_id = ? where id = ?";
	private static final String ASSIGN_TO_COURSE = "INSERT INTO students_courses (student_id, course_id) values (?, ?)";
	private static final String IS_ALREADY_ASSIGNED_TO_COURSE = "SELECT FROM students_courses WHERE student_id = ? AND course_id = ?";
	private static final String STUDENTS_RELATED_TO_COURSE = "SELECT s.id, s.first_name, s.last_name " 
			+ "FROM courses c " 
			+ "JOIN students_courses sc ON sc.course_id = c.id AND c.id = ? "
			+ "JOIN students s ON sc.student_id = s.id "
			+ "ORDER BY s.id";
	private static final String STUDENTS_WITHOUT_GIVEN_COURSE = "SELECT DISTINCT s.id, s.first_name, s.last_name "
			+ "FROM students s "
			+ "LEFT JOIN students_courses sc ON sc.student_id = s.id " 
			+ "LEFT JOIN courses c ON c.id = sc.course_id "
			+ "WHERE s.id NOT IN (SELECT student_id FROM students_courses WHERE course_id = ?)"
			+ "ORDER BY s.id";
	private static final String REMOVE_STUDENT_FROM_COURSE = "DELETE FROM students_courses WHERE student_id = ? AND course_id = ?";
	
	private final ConnectionProvider provider;
	private final GroupDao groupDao;
	private final CourseDao courseDao;
	
	public StudentDao(ConnectionProvider provider) {
		this.provider = provider;
		this.groupDao = new GroupDao(provider);
		this.courseDao = new  CourseDao(provider);
	}

	public void save(Student student) {
		try (Connection connection = provider.getConnection(); PreparedStatement statement = connection.prepareStatement(SAVE, PreparedStatement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, student.getFirstName());
			statement.setString(2, student.getLastName());
			int id = statement.executeUpdate();
			student.setId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Optional<Student> findById(Integer id) {
		try(Connection connection = provider.getConnection(); PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {			
			statement.setInt(1, id);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					return Optional.of(createStudent(result));
				}
				return Optional.empty();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}
	
	public List<Student> findAll() {
		List<Student> students = new ArrayList<>();
		try(Connection connection = provider.getConnection(); PreparedStatement statement = connection.prepareStatement(FIND_ALL)) {
			try (ResultSet result = statement.executeQuery()) {
				while (result.next()) {
					students.add(createStudent(result));
				}
				return students;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return students;
		}
	}
	
	public void deleteById(Integer id) {
		try (Connection connection = provider.getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID)) {
			statement.setInt(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void assignToGroup(Student student, Group group) {
		if (findById(student.getId()).isPresent() && groupDao.findById(group.getId()).isPresent()) {
			try (Connection connection = provider.getConnection(); PreparedStatement statement = connection.prepareStatement(ASSIGN_TO_GROUP)) {
				statement.setInt(1, group.getId());
				statement.setInt(2, student.getId());
				statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void assignToCourse(Student student, Course course) {
		if (findById(student.getId()).isPresent() && courseDao.findById(course.getId()).isPresent() && !isStudentAlreadyAssignedToCourse(student, course)) {
			try (Connection connection = provider.getConnection(); PreparedStatement statement = connection.prepareStatement(ASSIGN_TO_COURSE)) {
				statement.setInt(1, student.getId());
				statement.setInt(2, course.getId());
				statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean isStudentAlreadyAssignedToCourse(Student student, Course course) {
		try(Connection connection = provider.getConnection(); PreparedStatement statement = connection.prepareStatement(IS_ALREADY_ASSIGNED_TO_COURSE)) {
			statement.setInt(1, student.getId());
			statement.setInt(2, course.getId());
			try(ResultSet result = statement.executeQuery()) {
				return result.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		}
	}
	
	public List<Student> findAllStudentsRelatedToCourse(int courseId) {
		List<Student> students = new ArrayList<>();
		try(Connection connection = provider.getConnection(); PreparedStatement statement = connection.prepareStatement(STUDENTS_RELATED_TO_COURSE)) {
			statement.setInt(1, courseId);
			try (ResultSet result = statement.executeQuery()) {
				while (result.next()) {
					students.add(new Student(result.getInt("id"), result.getString("first_name"), result.getString("last_name")));
				}
				return students;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return students;
		}
	}
	
	public List<Student> findAllStudentWithoutGivenCourse(int courseId) {
		List<Student> students = new ArrayList<>();
		try(Connection connection = provider.getConnection(); PreparedStatement statement = connection.prepareStatement(STUDENTS_WITHOUT_GIVEN_COURSE)) {
			statement.setInt(1, courseId);
			try (ResultSet result = statement.executeQuery()) {
				while (result.next()) {
					students.add(new Student(result.getInt("id") ,result.getString("first_name"), result.getString("last_name")));
				}
				return students;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return students;
		}
	}
	
	public void removeStudentFromCourse(Student student, Course course) {
		try(Connection connection = provider.getConnection(); PreparedStatement statement = connection.prepareStatement(REMOVE_STUDENT_FROM_COURSE)) {
			statement.setInt(1, student.getId());
			statement.setInt(2, course.getId());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}	

	private Student createStudent(ResultSet result) throws SQLException {
		return new Student(result.getInt("id"), result.getString("first_name"), result.getString("last_name"), result.getInt("group_id"));
	}
}

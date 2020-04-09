package com.foxminded.school.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.foxminded.school.model.Course;
import com.foxminded.school.model.Student;
import com.foxminded.school.util.ConnectionProvider;

public class CourseDao {
	
	private final ConnectionProvider provider;
	
	public CourseDao(ConnectionProvider provider) {
		this.provider = provider;
	}
	
	private static final String SAVE = "INSERT INTO courses (course_name, course_description) values (?, ?)";
	private static final String FINDBYID = "SELECT * FROM courses WHERE id = ?";
	private static final String FINDALL = "SELECT * FROM courses";
	private static final String COURSESOFSTUDENT = "SELECT c.id, c.course_name "
			+ "FROM courses c "
			+ "JOIN students_courses sc ON c.id = sc.course_id "
			+ "JOIN students s ON s.id = sc.student_id AND s.id = ?";

	public void save(Course course) {
		try (Connection connection = provider.getConnection(); PreparedStatement statement = connection.prepareStatement(SAVE, PreparedStatement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, course.getName());
			statement.setString(2, course.getDescription());
			int id = statement.executeUpdate();
			course.setId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Optional<Course> findById(Integer id) {
		try (Connection connection = provider.getConnection(); PreparedStatement statement = connection.prepareStatement(FINDBYID)) {
			statement.setInt(1, id);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					return Optional.of(createCourse(result));
				}
				return Optional.empty();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return Optional.empty();
		}
	}

	public List<Course> findAll() {
		List<Course> courses = new ArrayList<>();
		try (Connection connection = provider.getConnection(); PreparedStatement statement = connection.prepareStatement(FINDALL)) {
			try (ResultSet result = statement.executeQuery()) {
				while (result.next()) {
					courses.add(createCourse(result));
				}
				return courses;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return courses;
		}
	}
	
	public List<Course> findAllCoursesOfStudent(Student student) {
		List<Course> courses = new ArrayList<>();
		try (Connection connection = provider.getConnection(); PreparedStatement statement = connection.prepareStatement(COURSESOFSTUDENT)) {
			statement.setInt(1, student.getId());
			try (ResultSet result = statement.executeQuery()) {
				while (result.next()) {
					courses.add(new Course(result.getInt("id"), result.getString("course_name")));
				}
				return courses;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return courses;
		}
	}
	
	private Course createCourse(ResultSet result) throws SQLException {
		return new Course(result.getInt("id"), result.getString("course_name"), result.getString("course_description"));
	}
}

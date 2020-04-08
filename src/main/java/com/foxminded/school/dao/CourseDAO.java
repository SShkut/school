package com.foxminded.school.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.foxminded.school.model.Course;
import com.foxminded.school.model.Student;
import com.foxminded.school.util.ConnectionsPool;

public class CourseDAO {

	public Integer save(Course course) {
		String sql = "INSERT INTO courses (course_name, course_description) values (?, ?)";
		try (Connection connection = ConnectionsPool.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, course.getCourseName());
			statement.setString(2, course.getCourseDescription());
			return statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public Course findById(Integer id) {
		String sql = "SELECT * FROM courses WHERE id = ?";
		try (Connection connection = ConnectionsPool.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, id);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					return new Course(result.getInt("id"), result.getString("course_name"), result.getString("course_description"));
				}
				return new Course();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return new Course();
		}
	}
	
	public List<Course> findAll() {
		String sql = "SELECT * FROM courses";
		List<Course> courses = new ArrayList<>();
		try (Connection connection = ConnectionsPool.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			try (ResultSet result = statement.executeQuery()) {
				while (result.next()) {
					courses.add(new Course(result.getInt("id"), result.getString("course_name"), result.getString("course_description")));
				}
				return courses;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return courses;
		}
	}
	
	public boolean isExists(Course course) {
		String sql = "SELECT FROM courses WHERE id = ?";
		try(Connection connection = ConnectionsPool.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, course.getId());
			try(ResultSet result = statement.executeQuery()) {
				return result.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}				
	}
	
	public List<Course> findAllCoursesOfStudent(Student student) {
		String sql = "select c.id, c.course_name " + 
				"from courses c " + 
				"join students_courses sc on c.id = sc.course_id " + 
				"join students s on s.id = sc.student_id and s.id = ?";
		List<Course> courses = new ArrayList<>();
		try (Connection connection = ConnectionsPool.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
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
}

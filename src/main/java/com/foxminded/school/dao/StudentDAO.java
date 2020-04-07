package com.foxminded.school.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.foxminded.school.model.Course;
import com.foxminded.school.model.Group;
import com.foxminded.school.model.Student;
import com.foxminded.school.util.ConnectionsPool;

public class StudentDAO {

	public Integer save(Student student) {
		String sql = "INSERT INTO students (first_name, last_name) values(?, ?)";
		try (Connection connection = ConnectionsPool.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, student.getFirstName());
			statement.setString(2, student.getLastName());
			return statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public Student findById(Integer id) {
		String sql = "SELECT * FROM students where id = ?";
		try(Connection connection = ConnectionsPool.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {			
			statement.setInt(1, id);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					return new Student(result.getInt("id"), result.getString("first_name"), result.getString("last_name"), result.getInt("group_id"));
				}
				return new Student();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new Student();
		}
	}
	
	public List<Student> findAll() {
		String sql = "SELECT * FROM students";
		List<Student> students = new ArrayList<>();
		try(Connection connection = ConnectionsPool.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			try (ResultSet result = statement.executeQuery()) {
				while (result.next()) {
					students.add(new Student(result.getInt("id"), result.getString("first_name"), result.getString("last_name"), result.getInt("group_id")));
				}
				return students;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return students;
		}
	}
	
	public void deleteById(Integer id) {
		String sql = "DELETE FROM students WHERE id = ?";
		try (Connection connection = ConnectionsPool.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isExists(Student student) {
		String sql = "SELECT FROM students WHERE id = ?";
		try(Connection connection = ConnectionsPool.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, student.getId());
			try(ResultSet result = statement.executeQuery()) {
				return result.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}		
	}
	
	public void assignToGroup(Student student, Group group) {
		GroupDAO groupDAO = new GroupDAO();
		StudentDAO studentDAO = new StudentDAO();
		if (studentDAO.isExists(student) && groupDAO.isExists(group)) {
			String sql = "UPDATE students SET group_id = ? where id = ?";
			try (Connection connection = ConnectionsPool.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setInt(1, group.getId());
				statement.setInt(2, student.getId());
				statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void assignToCourse(Student student, Course course) {
		CourseDAO coursedao = new CourseDAO();
		StudentDAO studentDAO = new StudentDAO();
		if (coursedao.isExists(course) && studentDAO.isExists(student) && !isStudentAlreadyAssignedToCourse(student, course)) {
			String sql = "INSERT INTO students_courses (student_id, course_id) values (?, ?)";
			try (Connection connection = ConnectionsPool.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setInt(1, student.getId());
				statement.setInt(2, course.getId());
				statement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean isStudentAlreadyAssignedToCourse(Student student, Course course) {
		String sql = "SELECT FROM students_courses WHERE student_id = ? AND course_id = ?";
		try(Connection connection = ConnectionsPool.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
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
}

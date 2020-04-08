package com.foxminded.school.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.foxminded.school.model.Group;
import com.foxminded.school.util.ConnectionsPool;

public class GroupDAO {

	public Integer save(Group group) {
		String sql = "INSERT INTO groups (group_name) values (?)";
		try (Connection connection = ConnectionsPool.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, group.getGroupName());
			return statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public Group findById(Integer id) { 
		String sql = "SELECT * FROM groups WHERE id = ?";
		try (Connection connection = ConnectionsPool.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, id);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					return new Group(result.getInt("id"), result.getString("group_name"));
				}
				return new Group();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return new Group();
		}
	}
	
	public List<Group> findAll() { 
		String sql = "SELECT * FROM groups";
		List<Group> groups = new ArrayList<>();
		try (Connection connection = ConnectionsPool.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			try (ResultSet result = statement.executeQuery()) {
				while (result.next()) {
					groups.add(new Group(result.getInt("id"), result.getString("group_name")));
				}
				return groups;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return groups;
		}
	}
	
	public boolean isExists(Group group) {
		String sql = "SELECT FROM groups WHERE id = ?";
		try(Connection connection = ConnectionsPool.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, group.getId());
			try(ResultSet result = statement.executeQuery()) {
				return result.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}		
	}
	
	public List<Group> findAllGroupsWithLessOrEqualStudentCount(int studentCount) {
		String sql = "select group_name from groups g join students s on g.id = s.group_id group by g.group_name having count(s.id) <= ?";
		List<Group> groups = new ArrayList<>();
		try (Connection connection = ConnectionsPool.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, studentCount);
			try (ResultSet result = statement.executeQuery()) {
				while (result.next()) {
					groups.add(new Group(result.getString("group_name")));
				}
				return groups;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return groups;
		}
	}
}

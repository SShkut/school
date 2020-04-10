package com.foxminded.school.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.foxminded.school.model.Group;
import com.foxminded.school.util.ConnectionProvider;

public class GroupDao {	

	private static final String SAVE = "INSERT INTO groups (group_name) values (?)";
	private static final String FIND_BY_ID = "SELECT * FROM groups WHERE id = ?";
	private static final String FIND_ALL = "SELECT * FROM groups";
	private static final String GROUPS_WITH_LESS_OR_EQUAL_STUDENTS = "SELECT group_name "
			+ "FROM groups g "
			+ "JOIN students s ON g.id = s.group_id "
			+ "GROUP BY g.group_name "
			+ "HAVING count(s.id) <= ?";
	
	private final ConnectionProvider provider;
	
	public GroupDao(ConnectionProvider provider) {
		this.provider = provider;
	}

	public void save(Group group) {
		try (Connection connection = provider.getConnection(); PreparedStatement statement = connection.prepareStatement(SAVE, PreparedStatement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, group.getName());
			int id = statement.executeUpdate();
			group.setId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Optional<Group> findById(Integer id) { 
		try (Connection connection = provider.getConnection(); PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
			statement.setInt(1, id);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					return Optional.of(createGroup(result));
				}
				return Optional.empty();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return Optional.empty();
		}
	}
	
	public List<Group> findAll() { 
		List<Group> groups = new ArrayList<>();
		try (Connection connection = provider.getConnection(); PreparedStatement statement = connection.prepareStatement(FIND_ALL)) {
			try (ResultSet result = statement.executeQuery()) {
				while (result.next()) {
					groups.add(createGroup(result));
				}
				return groups;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return groups;
		}
	}
	
	public List<Group> findAllGroupsWithLessOrEqualStudentCount(int studentCount) {
		List<Group> groups = new ArrayList<>();
		try (Connection connection = provider.getConnection(); PreparedStatement statement = connection.prepareStatement(GROUPS_WITH_LESS_OR_EQUAL_STUDENTS)) {
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

	private Group createGroup(ResultSet result) throws SQLException {
		return new Group(result.getInt("id"), result.getString("group_name"));
	}
}

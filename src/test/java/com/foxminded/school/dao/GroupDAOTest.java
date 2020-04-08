package com.foxminded.school.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.foxminded.school.model.Group;

class GroupDAOTest {
	
	private GroupDAO groupDAO;

	@BeforeEach
	void setUp() throws Exception {
		IDatabaseTester tester = new JdbcDatabaseTester("org.postgresql.ds.PGSimpleDataSource", "jdbc:postgresql://localhost:5432/school", "admin", "admin");
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResource("GroupTestData.xml"));
		tester.setDataSet(dataSet);
		tester.onSetup();
		groupDAO = new GroupDAO();
	}

	@Test
	void givenExistentId_whenFindById_thenReturnGroup() {		
		Group expected = new Group(2, "BB-12");
		
		Group actual = groupDAO.findById(2);
		
		assertEquals(expected, actual);
	}

	@Test
	void givenNonExistentId_whenFindById_thenReturnEmptyObject() {
		Group expected = new Group();
		
		Group actual = groupDAO.findById(-1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenExistentGroup_whenIsExists_thenReturnTrue() {
		Group group = new Group(2, "BB-12");
		
		assertTrue(groupDAO.isExists(group));
	}
	
	@Test
	void givenNonExistentGroup_whenIsExists_thenReturnFalse() {
		Group group = new Group(-1, "BB-12");
		
		assertFalse(groupDAO.isExists(group));
	}
	
	@Test
	void givenStudentCount_whenFindAllGroupsWithLessOrEqualStudentCount_thenReturnAppropriateGroups() {
		List<Group> expected = new ArrayList<>(Arrays.asList(new Group("BB-12"), new Group("CC-13")));
		
		List<Group> actual = groupDAO.findAllGroupsWithLessOrEqualStudentCount(2);
		
		assertEquals(expected, actual);
	}
	
	@Test 
	void whenFindAll_thenReturnListOfGroups() {
		List<Group> expected = new ArrayList<>(Arrays.asList(new Group(1, "AA-11"), new Group(2, "BB-12"), new Group(3, "CC-13")));
		
		List<Group> actual = groupDAO.findAll();
		
		assertEquals(expected, actual);
	}
}

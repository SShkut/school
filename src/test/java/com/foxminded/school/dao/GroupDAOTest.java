package com.foxminded.school.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.foxminded.school.model.Group;
import com.foxminded.school.util.ConnectionProvider;
import com.foxminded.school.util.ScheemaCreator;

class GroupDAOTest {
	
	private GroupDao groupDao;

	@BeforeEach
	void setUp() throws Exception {
		ConnectionProvider provider = new ConnectionProvider("/dbTest.properties");
		ScheemaCreator scheemaCreator = new ScheemaCreator(provider);
		scheemaCreator.create();
		IDatabaseTester tester = new DataSourceDatabaseTester(provider.getDataSource());
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResource("schoolTestData.xml"));
		tester.setDataSet(dataSet);
		tester.onSetup();
		groupDao = new GroupDao(provider);
	}

	@Test
	void givenExistentId_whenFindById_thenReturnOptional() {		
		Optional<Group> expected = Optional.of(new Group(2, "BB-12"));
		
		Optional<Group> actual = groupDao.findById(2);
		
		assertEquals(expected, actual);
	}

	@Test
	void givenNonExistentId_whenFindById_thenReturnEmptyOptional() {
		Optional<Group> expected = Optional.empty();
		
		Optional<Group> actual = groupDao.findById(-1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenStudentCount_whenFindAllGroupsWithLessOrEqualStudentCount_thenReturnAppropriateGroups() {
		List<Group> expected = new ArrayList<>(Arrays.asList(new Group("BB-12"), new Group("CC-13")));
		
		List<Group> actual = groupDao.findAllGroupsWithLessOrEqualStudentCount(2);
		
		assertEquals(expected, actual);
	}
	
	@Test 
	void whenFindAll_thenReturnListOfGroups() {
		List<Group> expected = new ArrayList<>(Arrays.asList(new Group(1, "AA-11"), new Group(2, "BB-12"), new Group(3, "CC-13")));
		
		List<Group> actual = groupDao.findAll();
		
		assertEquals(expected, actual);
	}
}

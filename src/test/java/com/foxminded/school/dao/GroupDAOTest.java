package com.foxminded.school.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.foxminded.school.model.Group;

class GroupDAOTest {
	
	private GroupDAO groupDAO;
	private IDatabaseTester tester;

	@BeforeEach
	void setUp() throws Exception {
		tester = new JdbcDatabaseTester("org.postgresql.ds.PGSimpleDataSource", "jdbc:postgresql://localhost:5432/school", "admin", "admin");
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResource("GroupTestData.xml"));
		tester.setDataSet(dataSet);
		tester.onSetup();
		groupDAO = new GroupDAO();
	}

	@Test
	void givenExistentId_whenFindById_thenGetGroup() {
		
		Group expected = new Group(2, "BB-12");
		
		Group actual = groupDAO.findById(2);
		
		assertEquals(expected, actual);
	}

	@Test
	void givenNonExistentId_whenFindById_thenReturnEmptyObject() {
		Group expected = new Group();
		
		Group actual = groupDAO.findById(3);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenExistentGroup_whenIsExists_thenReturnTrue() {
		Group group = new Group(2, "BB-12");
		
		assertTrue(groupDAO.isExists(group));
	}
	
	@Test
	void givenNonExistentGroup_whenIsExists_thenReturnFalse() {
		Group group = new Group(3, "BB-12");
		
		assertFalse(groupDAO.isExists(group));
	}
	
	@Test
	void givenGroup_whenSave_thenInsertGroup() throws IOException, DataSetException {
		groupDAO.save(new Group("CC-13"));
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(this.getClass().getClassLoader().getResource("GroupTestDataExpected.xml"));
		ITable expected = expectedDataSet.getTable("groups");
		ITable actualTable = tester.getDataSet().getTable("groups");
		String[] filter = new String[1];
		filter[0] = "group_name";
		ITable actual = DefaultColumnFilter.includedColumnsTable(actualTable, filter);
		
		assertEquals(expected, actual);
	}
}

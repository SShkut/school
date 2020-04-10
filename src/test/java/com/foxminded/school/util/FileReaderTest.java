package com.foxminded.school.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileReaderTest {
	
	private FileReader fileReader;

	@BeforeEach
	void setUp() throws Exception {
		fileReader = new FileReader();
	}

	@Test
	void givenExistentFile_whenReadFile_thenReturnString() throws IOException {
		String expected = "SELECT * FROM table1;SELECT * FROM table2;";
		
		String actual = fileReader.readFile("ReaderFileTest.sql");
		
		assertEquals(expected, actual);
	}
	
	@Test
	void givenNotExistentFile_whenReadFile_thenThrowFileNotFound() {
		assertThrows(FileNotFoundException.class, () -> fileReader.readFile("NotExistentFile.sql"));
	}

}

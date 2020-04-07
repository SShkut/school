package com.foxminded.school.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReader {

	public String readFile(String fileName) throws IOException {
		Path filePath = getAbsolutePath(fileName);
		if (filePath == null) {
			throw new FileNotFoundException();
		}
		try (Stream<String> stream = Files.lines(filePath)) {
			return stream.collect(Collectors.joining());
		} catch (IOException e) {
			return "";
		}
	}
	
	private Path getAbsolutePath(String fileName) {
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		try {
			URL url = classLoader.getResource(fileName);
			if (url == null) {
				return null;
			}
			return Paths.get(url.toURI());
		} catch (URISyntaxException e) {
			return null;
		}
	}
}

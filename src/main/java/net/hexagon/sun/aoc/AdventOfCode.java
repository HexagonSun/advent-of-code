package net.hexagon.sun.aoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AdventOfCode {

	private static final String BASE_PATH = "./input/";

	public abstract void runTask1 ();
	public abstract void runTask2 ();

	protected char [] getInputArray() {
		return getInputAsString().toCharArray();
	}

	protected String getInputAsString() {
		return getInputAsString(getResourceNameFromClass());
	}

	protected List<String> getInputLines() {
		return getInputLines(getResourceNameFromClass());
	}

	protected char[] getInputArray(String resource) {
		return getInputAsString(resource).toCharArray();
	}

	protected String getInputAsString(String resource) {
		return getInputLines(resource)
					   .stream()
					   .collect(Collectors.joining("\n"));
	}

	protected List<String> getInputLines(String resource) {
		List<String> list = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(resource)))) {
			String line;
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return list;
	}

	private String getResourceNameFromClass() {
		String suffix= this.getClass().getSimpleName().toLowerCase();
		return getResourceName(suffix);
	}

	private String getResourceName(String suffix) {
		return BASE_PATH + suffix;
	}
}

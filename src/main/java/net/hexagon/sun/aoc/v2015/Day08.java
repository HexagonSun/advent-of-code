package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/8
public class Day08 extends AdventOfCode {

	@Test
	@Override
	public void runTask1() {
		assertThat(solveTask1(getInputLines()), is(1333));
	}

	@Test
	public void runExample1() {
		String input= "\"\"";
		assertThat(nbCharsOfCode(input), is(2));
		assertThat(nbCharsInMemory(input), is(0));

		assertThat(solveTask1(input), is(2));
	}

	@Test
	public void runExample2() {
		String input= "\"abc\"";
		assertThat(nbCharsOfCode(input), is(5));
		assertThat(nbCharsInMemory(input), is(3));

		assertThat(solveTask1(input), is(2));
	}

	@Test
	public void runExample3() {
		String input= "\"aaa\\\"aaa\"";
		assertThat(nbCharsOfCode(input), is(10));
		assertThat(nbCharsInMemory(input), is(7));

		assertThat(solveTask1(input), is(3));
	}

	@Test
	public void runExample4() {
		String input= "\"\\x27\"";
		assertThat(nbCharsOfCode(input), is(6));
		assertThat(nbCharsInMemory(input), is(1));

		assertThat(solveTask1(input), is(5));
	}

	@Test
	public void runExampleTask1All() {
		List<String> input = Arrays.asList(
				"\"\"",
				"\"abc\"",
				"\"aaa\\\"aaa\"",
				"\"\\x27\"");
		assertThat(solveTask1(input), is(12));
	}

	@Test
	public void runTask1_A () {
		assertThat(solveTask1(getInputLines("./input/day08.a")), is(1350));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solveTask2(getInputLines()), is(2046));
	}

	@Test
	public void runTask2Example1() {
		String input= "\"\"";
		assertThat(nbEncodedCharsInMemory(input), is(6));
		assertThat(nbCharsOfCode(input), is(2));

		assertThat(solveTask2(input), is(4));
	}

	@Test
	public void runTask2Example2() {
		String input = "\"abc\"";
		assertThat(nbEncodedCharsInMemory(input), is(9));
		assertThat(nbCharsOfCode(input), is(5));

		assertThat(solveTask2(input), is(4));
	}

	@Test
	public void runTask2Example3() {
		String input = "\"aaa\\\"aaa\"";
		assertThat(nbEncodedCharsInMemory(input), is(16));
		assertThat(nbCharsOfCode(input), is(10));

		assertThat(solveTask2(input), is(6));
	}

	@Test
	public void runTask2Example4() {
		String input = "\"\\x27\"";
		assertThat(nbEncodedCharsInMemory(input), is(11));
		assertThat(nbCharsOfCode(input), is(6));

		assertThat(solveTask2(input), is(5));
	}

	@Test
	public void runTask2_A () {
		assertThat(solveTask2(getInputLines("./input/day08.a")), is(2085));
	}

	private int solveTask1(List<String> input) {
		int sum= 0;
		for (String i : input) {
			sum += solveTask1(i);
		}
		return sum;
	}

	private int solveTask1(String input) {
		return nbCharsOfCode(input) - nbCharsInMemory(input);
	}

	private int solveTask2(String input) {
		return nbEncodedCharsInMemory(input) - nbCharsOfCode(input);
	}

	private int solveTask2(List<String> input) {
		int sum= 0;
		for (String i : input) {
			sum += solveTask2(i);
		}
		return sum;
	}

	private int nbCharsOfCode(String input) {
		return input.length();
	}

	private int nbCharsInMemory(String input) {
		String quoteRemoved= input.substring(1, input.length()-1);
		String hexReplaced= quoteRemoved.replaceAll("\\\\x[\\d[a-f]]{2}", ".");
		String backslashReplaced= hexReplaced.replaceAll("\\\\\\\\", ".");
		String quoteReplaced= backslashReplaced.replaceAll("\\\\\"", ".");
		return quoteReplaced.length();
	}

	private int nbEncodedCharsInMemory(String input) {
		String withEncodedQuotes= "|||" + input.substring(1, input.length()-1) + "|||";
		String hexReplaced= withEncodedQuotes.replaceAll("\\\\x[\\d[a-f]]{2}", "12345");
		String backslashReplaced= hexReplaced.replaceAll("\\\\\\\\", "6789");
		String quoteReplaced= backslashReplaced.replaceAll("\\\\\"", "____");

		return quoteReplaced.length();
	}
}

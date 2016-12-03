package net.hexagon.sun.aoc;

import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/3
public class Day03 extends AdventOfCode {

	private static final Pattern TRIANGLE_PATTERN= Pattern.compile("\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)\\s*");

	@Test
	@Override
	public void runTask1 () {
		int solution= 1050;
		assertThat(solveTask1(getInputLines()), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
		int solution= -1;
		assertThat(solveTask2(getInputLines()), is(solution));
	}

	@Test
	public void runExample1() {
		List<String> input= Collections.singletonList("  5  10  25");
		assertThat(solveTask1(input), is(0));
	}

	@Test
	public void runExample2() {
		List<String> input= Collections.singletonList("  30  10  25");
		assertThat(solveTask1(input), is(1));
	}

	@Test
	public void runTask2Example1() {
		List<String> input= Collections.singletonList("  5  10  25");
		assertThat(solveTask2(input), is(0));
	}

	private int solveTask1 (List<String> input) {
		return (int) input.parallelStream()
				.filter(this::isTriangle)
				.count();
	}

	private boolean isTriangle(String line) {
		Matcher m = TRIANGLE_PATTERN.matcher(line);
		if (m.find()) {
			return isTriangle(
					Integer.valueOf(m.group(1)),
					Integer.valueOf(m.group(2)),
					Integer.valueOf(m.group(3)));
		}
		return false;
	}

	private boolean isTriangle(int a, int b, int c) {
		boolean valid= a + b > c;
		valid &= a + c > b;
		valid &= b + c > a;
		return valid;
	}

	private int solveTask2 (List<String> input) {
		return -1;
	}

}

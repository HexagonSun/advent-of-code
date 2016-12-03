package net.hexagon.sun.aoc;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
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
		int solution= 1921;
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
		List<String> input= Arrays.asList(
				"101 301 501",
				"102 302 502",
				"103 303 503",
				"201 401 601",
				"202 402 602",
				"203 403 603");
		assertThat(solveTask2(input), is(6));
	}

	private int solveTask1 (List<String> input) {
		return (int) input.parallelStream()
				.filter(this::isTriangle)
				.count();
	}

	private boolean isTriangle(String line) {
		Matcher m = TRIANGLE_PATTERN.matcher(line);
		if (m.find()) {
			return isTriangle(m.group(1), m.group(2), m.group(3));
		}
		return false;
	}

	private boolean isTriangle(String a, String b, String c) {
		return isTriangle(
				Integer.valueOf(a),
				Integer.valueOf(b),
				Integer.valueOf(c));
	}

	private boolean isTriangle(int a, int b, int c) {
		boolean valid= a + b > c;
		valid &= a + c > b;
		valid &= b + c > a;
		return valid;
	}

	private int solveTask2 (List<String> input) {
		int count= 0;
		// we checked the input length and know that we can read in groups of three lines
		for (Iterator<String> it= input.iterator(); it.hasNext();) {
			String line1 = it.next();
			String line2 = it.next();
			String line3 = it.next();

			Matcher m1= TRIANGLE_PATTERN.matcher(line1);
			Matcher m2= TRIANGLE_PATTERN.matcher(line2);
			Matcher m3= TRIANGLE_PATTERN.matcher(line3);

			if (m1.find() && m2.find() && m3.find()) {
				count += isTriangle(m1.group(1), m2.group(1), m3.group(1)) ? 1 : 0;
				count += isTriangle(m1.group(2), m2.group(2), m3.group(2)) ? 1 : 0;
				count += isTriangle(m1.group(3), m2.group(3), m3.group(3)) ? 1 : 0;
			} else {
				System.out.println("failed to parse input data");
				return -1;
			}
		}
		return count;
	}

}

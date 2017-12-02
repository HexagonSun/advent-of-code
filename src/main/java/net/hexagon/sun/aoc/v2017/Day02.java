package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day02 extends AdventOfCode {

	@Test
	@Override
	public void runTask1() {
		assertThat(solveTask1(getInputLines()), is(51833));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solveTask2(getInputLines()), is(288));
	}

	@Test
	public void runExample1() {
		String input = "5	1	9	5\n" +
					   "7	5	3\n" +
					   "2	4	6	8";
		List<String> lines= Arrays.asList(input.split("\n"));
		assertThat(solveTask1(lines), is(18));
	}

	@Test
	public void runTask2Example1() {
		String input = "5	9	2	8\n" +
					   "9	4	7	3\n" +
					   "3	8	6	5";
		List<String> lines= Arrays.asList(input.split("\n"));
		assertThat(solveTask2(lines), is(9));
	}

	private int solveTask1(List<String> lines) {
		return solve(lines, this::maxDiff);
	}

	private int solveTask2(List<String> lines) {
		return solve(lines, this::evenlyDivisible);
	}

	private int solve(List<String> lines, Function<String[], Integer> algorithm) {
		return lines.stream()
					   .map(s -> s.split("\t"))
					   .map(algorithm)
					   .mapToInt(i -> i)
					   .sum();
	}

	private Integer maxDiff(String[] raw) {
		int min= Integer.MAX_VALUE;
		int max= Integer.MIN_VALUE;
		for (String s : raw) {
			Integer i= Integer.valueOf(s);
			min= i < min ? i : min;
			max= i > max ? i : max;
		}
		return max - min;
	}

	private Integer evenlyDivisible(String[] raw) {
		int sum= 0;
		for (int i = 0; i < raw.length; i++) {
			Integer a= Integer.valueOf(raw[i]);

			for (int j = 0; j < raw.length; j++) {
				if (i == j) { continue; }
				Integer b= Integer.valueOf(raw[j]);
				if (a < b) { continue; }

				if ((a % b) == 0) {
					sum+= a / b;
				}
			}
		}
		return sum;
	}

}

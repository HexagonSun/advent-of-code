package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day02 extends AdventOfCode {

	@Test
	@Override
	public void runTask1() {
		assertThat(solveTask1(getInputLines()), is(18));
	}

	@Test
	@Override
	public void runTask2() {
	}

	@Test
	public void runExample1() {
		String input = "5	1	9	5\n" +
					   "7	5	3\n" +
					   "2	4	6	8";
		List<String> lines= Arrays.asList(input.split("\n"));
		assertThat(solveTask1(lines), is(18));
	}

	private int solveTask1(List<String> lines) {
		return lines.stream()
					   .map(this::maxDiff)
					   .mapToInt(i -> i)
					   .sum();
	}

	private Integer maxDiff(String line) {
		String[] raw= line.split("\t");
		int min= Integer.MAX_VALUE;
		int max= Integer.MIN_VALUE;
		for (String s : raw) {
			Integer i= Integer.valueOf(s);
			min= i < min ? i : min;
			max= i > max ? i : max;
		}
		return max - min;
	}

}

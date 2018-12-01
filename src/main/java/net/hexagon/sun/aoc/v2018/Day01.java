package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day01 extends AdventOfCode {

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(toIntList(getInputLines())), is(538));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(toIntList(getInputLines())), is(77271));
	}

	@Test
	public void runExample1 () {
		List<Integer> input = Arrays.asList(+1, -2, +3, +1);
		assertThat(solveTask1(input), is(3));
	}

	@Test
	public void runExample2 () {
		List<Integer> input = Arrays.asList(+1, +1, +1);
		assertThat(solveTask1(input), is(3));
	}

	@Test
	public void runExample3 () {
		List<Integer> input = Arrays.asList(+1, +1, -2);
		assertThat(solveTask1(input), is(0));
	}

	@Test
	public void runExample4 () {
		List<Integer> input = Arrays.asList(-1, -2, -3);
		assertThat(solveTask1(input), is(-6));
	}

	@Test
	public void runTask2Example1 () {
		List<Integer> input = Arrays.asList(+1, -2, +3, +1);
		assertThat(solveTask2(input), is(2));
	}

	@Test
	public void runTask2Example2 () {
		List<Integer> input = Arrays.asList(+1, -1);
		// Website says expected is 0, but it's actually 1
		assertThat(solveTask2(input), is(1));
	}

	@Test
	public void runTask2Example3 () {
		List<Integer> input = Arrays.asList(+3, +3, +4, -2, -4);
		assertThat(solveTask2(input), is(10));
	}

	@Test
	public void runTask2Example4 () {
		List<Integer> input = Arrays.asList(-6, +3, +8, +5, -6);
		assertThat(solveTask2(input), is(5));
	}

	@Test
	public void runTask2Example5 () {
		List<Integer> input = Arrays.asList(+7, +7, -2, -7, -4);
		assertThat(solveTask2(input), is(14));
	}

	private int solveTask1 (List<Integer> input) {
		int initialFrequenzy = 0;
		return input.stream().reduce(initialFrequenzy, (freq, i) -> freq + i);
	}

	private int solveTask2 (List<Integer> input) {
		Set<Integer> seen = new HashSet<>();
		int freq = 0;
		while (true) {
			for (Integer i : input) {
				freq += i;
				if (seen.contains(freq)) {
					return freq;
				}
				seen.add(freq);
			}
		}
	}

	private List<Integer> toIntList (List<String> input) {
		return input.stream().mapToInt(Integer::valueOf).boxed().collect(Collectors.toList());
	}

}

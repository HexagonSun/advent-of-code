package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day05 extends AdventOfCode {

	@Test
	@Override
	public void runTask1() {
		assertThat(solve(getInputLines(), false), is(359348));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solve(getInputLines(), true), is(27688760));
	}

	@Test
	public void runExample1() {
		List<String> input= Arrays.asList("0",
										  "3",
										  "0",
										  "1",
										  "-3");
		assertThat(solve(input, false), is(5));
	}

	@Test
	public void runTask2Example1() {
		List<String> input= Arrays.asList("0",
				"3",
				"0",
				"1",
				"-3");
		assertThat(solve(input, true), is(10));
	}

	private int solve(List<String> input, boolean task2) {
		Integer[] instructions= input.stream().map(Integer::valueOf).toArray(Integer[]::new);

		int index= 0;
		int nbSteps= 0;
		while (index >= 0 && index < instructions.length) {
			int offset= instructions[index];
			int nextIdx= index + offset;
			if (task2 && offset >= 3) {
				instructions[index]--;
			} else {
				instructions[index]++;
			}
			index= nextIdx;
			nbSteps++;
		}
		return nbSteps;
	}

}

package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day09 extends AdventOfCode {

	@Test
	@Override
	public void runTask1() {
		assertThat(solveTask1(getInputArray()), is(9662));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solveTask2(getInputArray()), is(4903));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1("{<>}".toCharArray()), is(1));
		assertThat(solveTask1("{<random characters>}".toCharArray()), is(1));
		assertThat(solveTask1("{<<<<>}".toCharArray()), is(1));
		assertThat(solveTask1("{<{!>}>}".toCharArray()), is(1));
		assertThat(solveTask1("{<!!>}".toCharArray()), is(1));
		assertThat(solveTask1("{<!!!>>}".toCharArray()), is(1));
		assertThat(solveTask1("{<{o\"i!a,<{i<a>}".toCharArray()), is(1));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask1("{}".toCharArray()), is(1));
		assertThat(solveTask1("{{{}}}".toCharArray()), is(6));
		assertThat(solveTask1("{{},{}}".toCharArray()), is(5));
		assertThat(solveTask1("{{{},{},{{}}}}".toCharArray()), is(16));
//		assertThat(solveTask1("{<{},{},{{}}>}".toCharArray()), is(1));
		assertThat(solveTask1("{<a>,<a>,<a>,<a>}".toCharArray()), is(1));
		assertThat(solveTask1("{{<ab>},{<ab>},{<ab>},{<ab>}}".toCharArray()), is(9));
		assertThat(solveTask1("{{<!!>},{<!!>},{<!!>},{<!!>}}".toCharArray()), is(9));
		assertThat(solveTask1("{{<a!>},{<a!>},{<a!>},{<ab>}}".toCharArray()), is(3));
	}

	@Test
	public void runExample1Task2() {
		assertThat(solveTask2("{<>}".toCharArray()), is(0));
		assertThat(solveTask2("{<random characters>}".toCharArray()), is(17));
		assertThat(solveTask2("{<<<<>}".toCharArray()), is(3));
		assertThat(solveTask2("{<{!>}>}".toCharArray()), is(2));
		assertThat(solveTask2("{<!!>}".toCharArray()), is(0));
		assertThat(solveTask2("{<!!!>>}".toCharArray()), is(0));
		assertThat(solveTask2("{<{o\"i!a,<{i<a>}".toCharArray()), is(10));
	}

	private int solveTask1(char[] input) {
		return solve(input, false);
	}

	private int solveTask2(char[] input) {
		return solve(input, true);
	}

	private int solve(char[] input, boolean partTwo) {
		int score= 0;
		int nestingLevel= 0;
		boolean skipNext= false;
		boolean inGarbage= false;
		int amountGarbage= 0;

		for (char current : input) {
			if (skipNext) {
				skipNext = false;
				continue;
			}
			if (current == '>') {
				inGarbage = false;
				continue;
			}
			if (inGarbage) {
				if (current == '!') {
					skipNext= true;
				} else {
					amountGarbage++;
				}
				continue;
			}


			switch (current) {
				case '!': {
					skipNext = true;
					continue;
				}
				case '<': {
					inGarbage = true;
					continue;
				}
				case '{': {
					nestingLevel++;
					continue;
				}
				case '}': {
					// a group finishes
					score+= nestingLevel;
					nestingLevel--;
					continue;
				}
				default: {
					// do nothing
				}
			}
		}
		return partTwo ? amountGarbage : score;
	}

}

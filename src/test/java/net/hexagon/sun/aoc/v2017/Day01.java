package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day01 extends AdventOfCode {

	@Test
	@Override
	public void runTask1() {
		assertThat(solveTask1(getInputArray()), is(997));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solveTask2(getInputArray()), is(1358));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1("1122".toCharArray()), is(3));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask1("1111".toCharArray()), is(4));
	}

	@Test
	public void runExample3() {
		assertThat(solveTask1("1234".toCharArray()), is(0));
	}

	@Test
	public void runExample4() {
		assertThat(solveTask1("91212129".toCharArray()), is(9));
	}

	@Test
	public void runTask2Example1() {
		assertThat(solveTask2("1212".toCharArray()), is(6));
	}

	@Test
	public void runTask2Example2() {
		assertThat(solveTask2("1221".toCharArray()), is(0));
	}

	@Test
	public void runTask2Example3() {
		assertThat(solveTask2("123425".toCharArray()), is(4));
	}

	@Test
	public void runTask2Example4() {
		assertThat(solveTask2("123123".toCharArray()), is(12));
	}

	@Test
	public void runTask2Example5() {
		assertThat(solveTask2("12131415".toCharArray()), is(4));
	}

	private int solveTask1(char[] input) {
		return solve(input, 1);
	}

	private int solveTask2(char[] input) {
		return solve(input, input.length / 2);
	}

	private int solve(char[] input, int step) {
		int sum= 0;
		int len= input.length;
		for (int i = 0; i < len; i++) {
			char current= input[i];
			int nextIndex= (i + step) % len;
			char next= input[nextIndex];
			if (current == next) {
				sum+= current - '0';
			}
		}
		return sum;
	}

}

package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/1
public class Day01 extends AdventOfCode {

	@Test
	@Override
	public void runTask1 () {
		char[] input = getInputArray();
		int position= solveTask1(input);
		System.out.println(position);

		// solution
		assertThat(position, is(280));
	}

	@Test
	@Override
	public void runTask2 () {
		char[] input = getInputArray();
		int position= solveTask2(input);
		System.out.println(position);

		// solution
		assertThat(position, is(1797));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1("(())"), is(0));
		assertThat(solveTask1("()()"), is(0));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask1("((("), is(3));
		assertThat(solveTask1("(()(()("), is(3));
	}

	@Test
	public void runExample3() {
		assertThat(solveTask1("))((((("), is(3));
	}

	@Test
	public void runExample4() {
		assertThat(solveTask1("())"), is(-1));
		assertThat(solveTask1("))("), is(-1));
	}

	@Test
	public void runExample5() {
		assertThat(solveTask1(")))"), is(-3));
		assertThat(solveTask1(")())())"), is(-3));
	}


	@Test
	public void runTask2Example1() {
		assertThat(solveTask2(")"), is(1));
	}

	@Test
	public void runTask2Example2() {
		assertThat(solveTask2("()())"), is(5));
	}

	private int solveTask1(String input) {
		return solveTask1(input.toCharArray());
	}

	private int solveTask1(char[] input) {
		int position= 0;
		for (char c : input) {
			position += ('(' == c ? 1 : -1);
		}
		return position;
	}

	private int solveTask2(String input) {
		return solveTask2(input.toCharArray());
	}

	private int solveTask2(char[] input) {
		int position= 0;
		for (int i = 0; i < input.length; i++) {
			char c= input[i];
			position += ('(' == c ? 1 : -1);
			if (position < 0) {
				return i+1;
			}
		}
		return position;
	}

}

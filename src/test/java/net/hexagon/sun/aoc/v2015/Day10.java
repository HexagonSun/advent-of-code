package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/10
public class Day10 extends AdventOfCode {

	@Test
	@Override
	public void runTask1() {
		String result= solve("1113222113", 40);
		assertThat(result.length(), is(252594));
	}

	@Test
	public void runExample1() {
		String result= solve("1", 1);
		assertThat(result, is("11"));
		assertThat(result.length(), is(2));
	}

	@Test
	public void runExample2() {
		assertThat(solve("11", 1), is("21"));
	}

	@Test
	public void runExample3() {
		assertThat(solve("21", 1), is("1211"));
	}

	@Test
	public void runExample4() {
		assertThat(solve("1211", 1), is("111221"));
	}

	@Test
	public void runExample5() {
		assertThat(solve("111221", 1), is("312211"));
	}

	@Test
	public void runExample1All() {
		assertThat(solve("1", 5), is("312211"));
	}

	@Test
	@Override
	public void runTask2() {
		String result= solve("1113222113", 50);
		assertThat(result.length(), is(3579328));

	}

	private String solve(String input, int iterations) {
		String iteration= input;
		for (int i = 0; i < iterations; i++) {
			iteration= solveIteration(iteration);
		}
		return iteration;
	}

	private String solveIteration(String input) {
		char[] list= input.toCharArray();
		StringBuilder sb= new StringBuilder();

		int cnt= 1;
		char current= list[0];
		for (int i= 1; i < list.length; i++) {
			char c= list[i];
			if (c == current) {
				cnt++;
			} else {
				// change of chars
				sb.append(cnt).append(current);
				current= c;
				cnt= 1;
			}
		}
		// process last
		sb.append(cnt).append(current);
		return sb.toString();
	}
}

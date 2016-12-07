package net.hexagon.sun.aoc;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/7
public class Day07 extends AdventOfCode {

	@Test
	@Override
	public void runTask1 () {
		int solution= 110;
		assertThat((int)solveTask1(getInputLines()), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1("abba[mnop]qrst"), is(1));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask1("abcd[bddb]xyyx"), is(0));
	}

	@Test
	public void runExample3() {
		assertThat(solveTask1("aaaa[qwer]tyui"), is(0));
	}

	@Test
	public void runExample4() {
		assertThat(solveTask1("ioxxoj[asdfgh]zxcvbn"), is(1));
	}

	@Test
	public void runExample5() {
		assertThat(solveTask1("ioxxoj[asdfgh]ioxxoj[asdfgh]ioxxoj[asdfgh]"), is(1));
	}

	@Test
	public void runExample6() {
		assertThat(solveTask1("ioxxoj[asdfgh]ioxxoj[aabb]asdfgh[ioxxoj]"), is(0));
	}


	@Test
	public void runExample7() {
		assertThat(solveTask1("nojldommqhxo]oxbcsksyguwkkdugg"), is(0));
	}

	@Test
	public void runTask2Example1() {
	}

	private long solveTask1 (List<String> input) {
		return input.stream()
					   .map(this::solveTask1)
					   .filter(i -> i > 0)
					   .count();
	}

	private int solveTask1 (String input) {
		int inBrackets= 0;
		int matchFound= 0;

		char a= 0;
		char b= 0;
		char b2= 0;
		char a2= 0;
		for (char c : input.toCharArray()) {
			if ('[' == c) {
				inBrackets++;
				a=b=b2=a2=0;
				continue;
			}
			if (']' == c) {
				inBrackets--;
				a=b=b2=a2=0;
				continue;
			}

			a= b;
			b= b2;
			b2= a2;
			a2= c;

			if (b2 == 0) {
				// sliding window not complete yet
				continue;
			}

			if (a == a2 && b == b2 && a != b) {
				if (inBrackets > 0) {
					// in-bracket match -> abort
					return 0;
				}
				matchFound= 1;
			}
		}

		return matchFound;
	}

	private long solveTask2 (List<String> input) {
		return input.stream().map(this::solveTask2).count();
	}

	private int solveTask2 (String input) {
		return 0;
	}

}

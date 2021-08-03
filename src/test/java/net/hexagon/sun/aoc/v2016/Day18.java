package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/18
public class Day18 extends AdventOfCode {

	@Test
	@Override
	public void runTask1 () {
		int solution= 1963;
		assertThat(solve(getInputAsString(), 40), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
		int solution= 20009568;
		assertThat(solve(getInputAsString(), 400000), is(solution));
	}

	@Test
	public void runExample1() {
		assertThat(solve("..^^.", 3), is(6));
	}
	@Test
	public void runExample2() {
		assertThat(solve(".^^.^.^^^^", 10), is(38));
	}

	private int solve(String input, int nbRows) {
		boolean debugPrint= false;
		int safeTiles= 0;
		char[] line= input.toCharArray();
		nbRows--;

		for (char c : line) {
			if (c == '.') {
				safeTiles++;
			}
		}
		if (debugPrint) {
			System.out.println(line);
		}

		while(nbRows-- > 0) {
			char[] nextRow= new char[line.length];

			for (int i = 0; i < line.length; i++) {
				char left= i <= 0 ? '.' : line[i-1];
				char center= line[i];
				char right= i >= line.length - 1 ? '.' : line[i+1];

				char next= '.';
				if (left == '^' && center == '^' && right == '.') {
					next= '^';
				}
				if (center == '^' && right == '^' && left == '.') {
					next= '^';
				}
				if (left == '^' && center == '.' && right == '.') {
					next= '^';
				}
				if (left == '.' && center == '.' && right == '^') {
					next= '^';
				}

				nextRow[i]= next;
				if (next == '.') {
					safeTiles++;
				}
			}

			if (debugPrint) {
				System.out.println(line);
			}
			line= nextRow;
		}


		return safeTiles;
	}

}

package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/25
public class Day25 extends AdventOfCode {

	@Test
	@Override
	public void runTask1() {
		// row 2947, column 3029
		assertThat(solveTask1(2947, 3029), is(19980801L));
	}

	@Test
	@Override
	public void runTask2() {

	}

	@Test
	public void runExample1() {
		long[] line = { 20151125, 18749137, 17289845, 30943339, 10071777, 33511524 };
		for (int i = 1; i <= 6; i++) {
			assertThat(solveTask1(1, i), is(line[i - 1]));
		}
	}

	@Test
	public void runExample2() {
		// line 2
		long[] line = { 31916031, 21629792, 16929656, 7726640, 15514188, 4041754 };
		for (int i = 1; i <= 6; i++) {
			assertThat(solveTask1(2, i), is(line[i - 1]));
		}
	}

	@Test
	public void runExample3() {
		// line 2
		long[] line = { 16080970, 8057251, 1601130, 7981243, 11661866, 16474243 };
		for (int i = 1; i <= 6; i++) {
			assertThat(solveTask1(3, i), is(line[i - 1]));
		}
	}

	@Test
	public void runExample4() {
		// line 2
		long[] line = { 24592653, 32451966, 21345942, 9380097, 10600672, 31527494 };
		for (int i = 1; i <= 6; i++) {
			assertThat(solveTask1(4, i), is(line[i - 1]));
		}
	}

	@Test
	public void runExample5() {
		// line 2
		long[] line = { 77061, 17552253, 28094349, 6899651, 9250759, 31663883 };
		for (int i = 1; i <= 6; i++) {
			assertThat(solveTask1(5, i), is(line[i - 1]));
		}
	}

	@Test
	public void runExample6() {
		// line 2
		long[] line = { 33071741, 6796745, 25397450, 24659492, 1534922, 27995004 };
		for (int i = 1; i <= 6; i++) {
			assertThat(solveTask1(6, i), is(line[i - 1]));
		}
	}

	private long solveTask1(int row, int col) {
		long value = 20151125;

		int idx = getIndex(row, col);
		while (idx-- > 1) {
			long mul = (value * 252533);
			long rem = mul % 33554393;
			value = rem;
		}
		return value;
	}

	private int getIndex(int row, int col) {
		// position of the "x" value in row 1
		int x1 = col + (row - 1);
		// the index of the value @ x1, i.e. in row 1 at (1,x1)
		int idx1 = sumUpTo(x1);

		int indexInLastDiagonal = (row - 1);
		return idx1 - indexInLastDiagonal;
	}

	private int sumUpTo(int n) {
		return (n * (n + 1)) / 2;
	}

}

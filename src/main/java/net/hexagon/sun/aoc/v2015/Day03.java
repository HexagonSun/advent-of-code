package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/3
public class Day03 extends AdventOfCode {

	private static final int DIMENSION= 600;

	@Test
	@Override
	public void runTask1 () {
		int total= solveTask1(getInputArray());
		System.out.println(total);

		// solution
		assertThat(total, is(2592));
	}

	@Test
	@Override
	public void runTask2 () {
		int total= solveTask2(getInputArray());
		System.out.println(total);

		// solution
		assertThat(total, is(2360));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1(">"), is(2));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask1("^>v<"), is(4));
	}

	@Test
	public void runExample3() {
		assertThat(solveTask1("^v^v^v^v^v"), is(2));
	}

	@Test
	public void runTask2Example1() {
		assertThat(solveTask2("^v"), is(3));
	}

	@Test
	public void runTask2Example2() {
		assertThat(solveTask2("^>v<"), is(3));
	}

	@Test
	public void runTask2Example3() {
		assertThat(solveTask2("^v^v^v^v^v"), is(11));
	}

	private int solveTask1(String line) {
		return solveTask1(line.toCharArray());
	}

	private int solveTask1(char[] input) {
		int[][] grid= new int[DIMENSION][DIMENSION];
		int x= 500;
		int y= 500;

		// starting pos
		grid[x][y]+= 1;
		int houses= 1;

		for (int idx= 0; idx < input.length; idx++) {
			char c= input[idx];

			if (c == '^') {
				y+= 1;
			} else if (c == '>') {
				x+= 1;
			} else if (c == 'v') {
				y-= 1;
			} else {
				x-= 1;
			}

			// add add location
			grid[x][y]+= 1;
			if (grid[x][y] == 1) {
				houses++;
			}
		}
		return houses;
	}

	private int solveTask2(String line) {
		return solveTask2(line.toCharArray());
	}

	private int solveTask2(char[] input) {
		int[][] grid= new int[DIMENSION][DIMENSION];
		int pos_x1= 500;
		int pos_y1= 500;
		int pos_x2= 500;
		int pos_y2= 500;

		// starting pos
		grid[pos_x1][pos_x1]+= 2;
		int houses= 1;

		for (int idx= 0; idx < input.length; idx++) {
			char c= input[idx];

			int turn= idx & 0x1;
			if (turn == 0) {
				// santa
				if (c == '^') {
					pos_y1+= 1;
				} else if (c == '>') {
					pos_x1+= 1;
				} else if (c == 'v') {
					pos_y1-= 1;
				} else {
					pos_x1-= 1;
				}

				// add add location
				grid[pos_x1][pos_y1]+= 1;
				if (grid[pos_x1][pos_y1] == 1) {
					houses++;
				}
			} else {
				// robot
				// santa
				if (c == '^') {
					pos_y2+= 1;
				} else if (c == '>') {
					pos_x2+= 1;
				} else if (c == 'v') {
					pos_y2-= 1;
				} else {
					pos_x2-= 1;
				}

				// add add location
				grid[pos_x2][pos_y2]+= 1;
				if (grid[pos_x2][pos_y2] == 1) {
					houses++;
				}

			}

		}
		return houses;
	}

}

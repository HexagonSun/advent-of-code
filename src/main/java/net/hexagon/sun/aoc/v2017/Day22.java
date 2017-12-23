package net.hexagon.sun.aoc.v2017;

import javafx.geometry.Point2D;
import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day22 extends AdventOfCode {

	enum State {
		CLEAN ('.'), WEAKENED ('W'), INFECTED('#'), FLAGGED('F');

		private final char symbol;

		State (char symbol) {
			this.symbol= symbol;
		}

		@Override
		public String toString () {
			return "" + symbol;
		}
	}

	static class Carrier {
		Point2D direction= new Point2D(0, -1);
		Point2D pos;

		int x() {
			return (int)pos.getX();
		}
		int y() {
			return (int)pos.getY();
		}

		void right() {
			if (direction.getX() == 0 && direction.getY() == -1) {
				direction= new Point2D(1, 0);
			} else if (direction.getX() == 1 && direction.getY() == 0) {
				direction= new Point2D(0, 1);
			} else if (direction.getX() == 0 && direction.getY() == 1) {
				direction= new Point2D(-1, 0);
			} else if (direction.getX() == -1 && direction.getY() == 0) {
				direction= new Point2D(0, -1);
			}
		}

		void left() {
			if (direction.getX() == 0 && direction.getY() == -1) {
				direction= new Point2D(-1, 0);
			} else if (direction.getX() == 1 && direction.getY() == 0) {
				direction= new Point2D(0, -1);
			} else if (direction.getX() == 0 && direction.getY() == 1) {
				direction= new Point2D(1, 0);
			} else if (direction.getX() == -1 && direction.getY() == 0) {
				direction= new Point2D(0, 1);
			}
		}

		void move() {
			pos= pos.add(direction);
		}
	}

	@Test
	@Override
	public void runTask1() {
		assertThat(solveTask1(getInputLines(), 10000), is(5552));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solveTask2(getInputLines(), 10_000_000), is(2_511_527));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1(Arrays.asList("..#",
											"#..",
											"..."), 7), is(5));
	}

	@Test
	public void runExample1_2() {
		assertThat(solveTask1(Arrays.asList("..#",
				"#..",
				"..."), 70), is(41));
	}
	@Test
	public void runExample1_3() {
		assertThat(solveTask1(Arrays.asList("..#",
				"#..",
				"..."), 10000), is(5587));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask2(Arrays.asList("..#",
				"#..",
				"..."), 7), is(1));
	}

	@Test
	public void runExample2_2() {
		assertThat(solveTask2(Arrays.asList("..#",
											"#..",
											"..."), 100), is(26));
	}

	@Test
	public void runExample2_3() {
		assertThat(solveTask2(Arrays.asList("..#",
				"#..",
				"..."), 10_000_000), is(2_511_944));
	}

	private int solveTask1 (List<String> input, int iterations) {
		return solve(input, false, 515, iterations);
	}

	private int solveTask2 (List<String> input, int iterations) {
		return solve(input, true, 515, iterations);
	}

	private int solve (List<String> input, boolean part2, int gridSize, int iterations) {
		State[][] grid= parse(input, gridSize);
		Carrier c= new Carrier();
		c.pos= new Point2D(grid.length/2, grid.length/2);

		int cnt= 0;
		for (int i = 0; i < iterations; i++) {
			boolean wasInfected= part2 ? tick2(grid, c) : tick(grid, c);
			if (wasInfected) {
				cnt++;
			}
		}
		// print(grid);
		return cnt;
	}

	private boolean tick2 (State[][] grid, Carrier c) {
		State current = grid[c.y()][c.x()];
		boolean newInfection= false;
		State newState;

		switch (current) {
			case CLEAN:
				c.left();
				newState= State.WEAKENED;
				break;
			case WEAKENED:
				newState= State.INFECTED;
				newInfection= true;
				break;
			case INFECTED:
				c.right();
				newState= State.FLAGGED;
				break;
			default:
				c.left(); c.left();
				newState= State.CLEAN;
				break;
		}

		grid[c.y()][c.x()]= newState;
		c.move();
		return newInfection;
	}

	private boolean tick (State[][] grid, Carrier c) {
		State current = grid[c.y()][c.x()];
		boolean newInfection= false;
		State newState= State.CLEAN;
		if (current == State.INFECTED) {
			c.right();
		} else {
			c.left();
			newInfection= true;
			newState= State.INFECTED;
		}
		grid[c.y()][c.x()]= newState;
		c.move();
		return newInfection;
	}

	private State[][] parse (List<String> input, int gridSize) {
		int offset= gridSize;
		int len= offset;
		offset= (offset / 2) - (input.size()/2);

		State[][] grid= new State[len][];
		for (int i = 0; i < grid.length; i++) {
			State[] row= new State[len];
			Arrays.fill(row, State.CLEAN);
			grid[i]= row;
		}

		for (int i = 0; i < input.size(); i++) {
			String row = input.get(i);
			int j= offset;
			for (char c : row.toCharArray()) {
				State s= c == '#' ? State.INFECTED : State.CLEAN;
				grid[i + offset][j++]= s;
			}
		}

		return grid;
	}

	private void print (State[][] grid) {
		System.out.println("");
		for (State[] row : grid) {
			for (State x : row) {
				System.out.print(x);
			}
			System.out.println("");
		}
	}

}

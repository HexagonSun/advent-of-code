package net.hexagon.sun.aoc;

import javafx.geometry.Point2D;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/1
public class Day01 extends AdventOfCode {

	private static class Position {
		Point2D position= Point2D.ZERO;
		int direction;

		private int getDistanceFromOrigin() {
			int distX= Math.abs((int) position.getX());
			int distY= Math.abs((int) position.getY());
			return distX + distY;
		}

		private void moveBy(String step) {
			boolean right= 'R' == step.charAt(0);
			int distance= Integer.parseInt(step.substring(1));

			Point2D oneStep;
			switch (direction) {
				case 0:
					// facing north
					if (right) {
						oneStep = new Point2D(distance, 0);
					} else {
						oneStep = new Point2D(-distance, 0);
					}
					break;
				case 1:
					// facing east
					if (right) {
						oneStep = new Point2D(0, -distance);
					} else {
						oneStep = new Point2D(0, distance);
					}
					break;
				case 2:
					// facing south
					if (right) {
						oneStep = new Point2D(-distance, 0);
					} else {
						oneStep = new Point2D(distance, 0);
					}
					break;
				case 3:
				default:
					// facing west
					if (right) {
						oneStep = new Point2D(0, distance);
					} else {
						oneStep = new Point2D(0, -distance);
					}
					break;
			}

			// update new direction
			if (right) {
				direction= Math.floorMod(direction + 1, 4);
			} else {
				direction= Math.floorMod(direction -1, 4);
			}

			// do the step
			position= position.add(oneStep);
		}
	}

	@Test
	@Override
	public void runTask1 () {
		String input = getInputAsString();
		int distance= solveTask1(input);

		// solution
		assertThat(distance, is(181));
	}

	@Test
	@Override
	public void runTask2 () {
		String input = getInputAsString();
		solveTask2(input);
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1("R2, L3"), is(5));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask1("R2, R2, R2"), is(2));
	}

	@Test
	public void runExample3() {
		assertThat(solveTask1("R5, L5, R5, R3"), is(12));
	}

	@Test
	public void runExample4() {
		assertThat(solveTask1("R23"), is(23));
	}

	@Test
	public void runExample5() {
		assertThat(solveTask1("R2, R2, R2, R2"), is(0));
	}

	@Test
	public void runExample6() {
		assertThat(solveTask1("R2, L1"), is(3));
	}

	private int solveTask1(String input) {
		return solveTask1(input.split(","));
	}

	private int solveTask1(String[] input) {
		Position pos= new Position();
		Arrays.stream(input).forEach(step -> pos.moveBy(step.trim()));
		return pos.getDistanceFromOrigin();
	}

	private int solveTask2(String input) {
		return solveTask2(input.split(","));
	}

	private int solveTask2(String[] input) {
		// TODO: implement
		return 1;
	}

}

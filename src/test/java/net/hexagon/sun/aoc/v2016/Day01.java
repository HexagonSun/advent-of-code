package net.hexagon.sun.aoc.v2016;

import javafx.geometry.Point2D;
import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/1
public class Day01 extends AdventOfCode {

	private static class Position {
		Point2D position= Point2D.ZERO;
		int direction;

		private int getDistanceFromOrigin() {
			return Math.abs((int) position.getX())
			     + Math.abs((int) position.getY());
		}

		private Point2D moveBy(String step) {
			boolean right = 'R' == step.charAt(0);
			int distance = Integer.parseInt(step.substring(1));

			Point2D oneStep = calculateStep(right, distance);
			updateDirection(right);

			// do the step
			position = position.add(oneStep);
			return position;
		}

		private Point2D calculateStep(boolean right) {
			return calculateStep(right, 1);
		}

		private Point2D calculateStep(boolean right, int distance) {
			Point2D step;
			switch (direction) {
				case 0:
					// facing north
					step = new Point2D((right ? 1 : -1) * distance, 0);
					break;
				case 1:
					// facing east
					step = new Point2D(0, (right ? -1 : 1) * distance);
					break;
				case 2:
					// facing south
					step = new Point2D((right ? -1 : 1) * distance, 0);
					break;
				case 3:
				default:
					// facing west
					step = new Point2D(0, (right ? 1 : -1) * distance);
					break;
			}
			return step;
		}

		private void updateDirection(boolean right) {
			direction = Math.floorMod(direction + (right ? 1 : -1), 4);
		}

		private boolean stepBy(Set<Point2D> visited, String step) {
			boolean right = 'R' == step.charAt(0);
			int distance = Integer.parseInt(step.substring(1));

			while (distance-- > 0) {
				// simulate steps of distance 1
				Point2D oneStep = calculateStep(right);
				position= position.add(oneStep);
				if (!visited.add(position)) {
					return false;
				}
			}
			updateDirection(right);
			return true;
		}
	}

	@Test
	@Override
	public void runTask1 () {
		String input = getInputAsString();
		int distance= solveTask1(input);

		int solution= 181;
		assertThat(distance, is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
		String input = getInputAsString();
		int distance= solveTask2(input);

		int solution= 140;
		assertThat(distance, is(solution));
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

	@Test
	public void runExample7() {
		assertThat(solveTask1("R2, R0, L0, L1"), is(3));
	}

	@Test
	public void runTask2Example1() {
		assertThat(solveTask2("R8, R4, R4, R8"), is(4));
	}

	@Test
	public void runTask2Example2() {
		assertThat(solveTask2("R8, L0, R0, R4, R4, R8"), is(4));
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
		Set<Point2D> visited= new HashSet<>();
		visited.add(Point2D.ZERO);

		Position pos= new Position();
		for (String step : input) {
			boolean walkOn= pos.stepBy(visited, step.trim());
			if (!walkOn) {
				break;
			}
		}
		return pos.getDistanceFromOrigin();
	}

}

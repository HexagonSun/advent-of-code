package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day03 extends AdventOfCode {

	@Test
	@Override
	public void runTask1() {
		assertThat(solveTask1(325489), is(552));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solveTask2(new int[64][64], 325489), is(330785));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1(1), is(0));
		assertThat(solveTask1(12), is(3));
		assertThat(solveTask1(23), is(2));
		assertThat(solveTask1(1024), is(31));
	}

	@Test
	public void runTask2Example1() {
		//		147  142  133  122   59
		//		304    5    4    2   57
		//		330   10    1    1   54
		//		351   11   23   25   26
		//		362  747  806--->   ...
		assertThat(solveTask2(new int[8][8], 1), is(2));
		assertThat(solveTask2(new int[8][8], 2), is(4));
		assertThat(solveTask2(new int[8][8], 4), is(5));
		assertThat(solveTask2(new int[8][8], 10), is(11));
		assertThat(solveTask2(new int[8][8], 11), is(23));
		assertThat(solveTask2(new int[8][8], 26), is(54));
		assertThat(solveTask2(new int[8][8], 59), is(122));
		assertThat(solveTask2(new int[8][8], 147), is(304));
		assertThat(solveTask2(new int[8][8], 362), is(747));
		assertThat(solveTask2(new int[8][8], 747), is(806));
	}

	private int solveTask1(int target) {
		return distance(spiral(target));
	}

	private int solveTask2(int[][] grid, int threshold) {
		Point origin= new Point(grid.length / 2, grid.length / 2);
		store(grid, origin, 1);
		int maxSteps= 1000;
		for (int i = 2; i < maxSteps; i++) {
			Point spiralPoint= spiral(i);
			Point target= withOrigin(origin, spiralPoint);
			int value= sumOfNeighbours(grid, target);
			if (value > threshold) {
//				System.out.println("Found first value larger than " + threshold + " to be " + value);
//				System.out.println("\tCoordinates: " + target);
//				System.out.println("\tSpiral point: " + spiralPoint);
				return value;
			}
			store(grid, target, value);
		}
		return -1;
	}

	private void store(int[][] grid, Point target, int value) {
		grid[target.x][target.y]= value;
	}

	private int sumOfNeighbours(int[][] grid, Point target) {
		int sum= 0;
		sum+= grid[target.x + 1][target.y];
		sum+= grid[target.x + 1][target.y + 1];
		sum+= grid[target.x][target.y + 1];
		sum+= grid[target.x - 1][target.y + 1];
		sum+= grid[target.x - 1][target.y];
		sum+= grid[target.x - 1][target.y - 1];
		sum+= grid[target.x][target.y - 1];
		sum+= grid[target.x + 1][target.y - 1];
		return sum;
	}

	// https://math.stackexchange.com/a/163101
	private Point spiral(int target) {
		double k= Math.ceil((Math.sqrt(target)-1)/2);
		double t= 2*k+1;
		double m= t * t;
		t=t-1;

		if (target >= m-t) {
			return point(k-(m-target), -k);
		} else {
			m=m-t;
		}

		if (target >= m-t) {
			return point(-k, -k+(m-target));
		} else  {
			m=m-t;
		}

		if (target >= m-t) {
			return point(-k+(m-target), k);
		}  else  {
			return point(k, k-(m-target-t));
		}
	}

	private Point point(double x, double y) {
		return new Point((int)x, (int)y);
	}

	// taxicab distance
	private int distance(Point p) {
		return Math.abs(p.x) + Math.abs(p.y);
	}

	private Point withOrigin(Point origin, Point target) {
		return new Point(origin.x + target.x, origin.y + target.y);
	}

}

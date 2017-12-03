package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

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

		assertThat(solveTask2(new int[100][100], 1), is(2));
	}

	private int solveTask1(int target) {
		return distance(spiral(target));
	}

	private int solveTask2(int[][] grid, int threshold) {
		return -1;
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

}

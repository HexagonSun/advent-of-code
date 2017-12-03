package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

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
	}

	@Test
	public void runExample2() {
		assertThat(solveTask1(12), is(3));
	}

	@Test
	public void runExample3() {
		assertThat(solveTask1(23), is(2));
	}

	@Test
	public void runExample4() {
		assertThat(solveTask1(1024), is(31));
	}

	@Test
	public void runTask2Example1() {

	}


	private int solveTask1(int target) {
		return spiral(target);
	}

	// https://math.stackexchange.com/a/163101
	private int spiral(int target) {
		double k= Math.ceil((Math.sqrt(target)-1)/2);
		double t= 2*k+1;
		double m= t * t;
		t=t-1;

		if (target >= m-t) {
			return distance(k-(m-target), -k);
		} else {
			m=m-t;
		}

		if (target >= m-t) {
			return distance(-k, -k+(m-target));
		} else  {
			m=m-t;
		}

		if (target >= m-t) {
			return distance(-k+(m-target), k);
		}  else  {
			return distance(k, k-(m-target-t));
		}
	}

	// taxicab distance
	private int distance(double x, double y) {
		return Math.abs((int)x) + Math.abs((int)y);
	}

}

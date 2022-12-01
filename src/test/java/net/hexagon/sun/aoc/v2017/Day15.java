package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day15 extends AdventOfCode {

	@Test
	@Override
	public void runTask1() {
		assertThat(solveTask1(289, 629), is(638));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solveTask2(289, 629), is(343));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1(65, 8921), is(588));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask2(65, 8921), is(309));
	}

	private int solveTask1(int genA, int genB) {
		return solve(genA, genB, false);
	}

	private int solveTask2(int genA, int genB) {
		return solve(genA, genB, true);
	}

	private int solve(int valA, int valB, boolean partTwo) {
		int matches= 0;
		int limit= partTwo ? 5_000_000 : 40_000_000;
		for (long i = 0; i < limit; i++) {
			valA= next(valA, partTwo, 16807, 4);
			valB= next(valB, partTwo, 48271, 8);
			if ((valA & 0xffff) == (valB & 0xffff)) {
				matches++;
			}
		}
		return matches;
	}

	private int next (int val, boolean partTwo, int factor, int mod) {
		long res= val;
		if (!partTwo) {
			res*= factor;
			return (int)(res % Integer.MAX_VALUE);
		}

		do  {
			res*= factor;
			res= (int)(res % Integer.MAX_VALUE);
		} while (res % mod != 0);
		return (int)res;
	}
}

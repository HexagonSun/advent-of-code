package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day15 extends AdventOfCode {

	private static final BigInteger MAX_VALUE= BigInteger.valueOf(Integer.MAX_VALUE);
	private static final BigInteger FACTOR_GEN_A= BigInteger.valueOf(16807);
	private static final BigInteger FACTOR_GEN_B= BigInteger.valueOf(48271);

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

	private int solve(int genA, int genB, boolean partTwo) {
		BigInteger valA= BigInteger.valueOf(genA);
		BigInteger valB= BigInteger.valueOf(genB);
		int matches= 0;
		int limit= partTwo ? 5_000_000 : 40_000_000;
		for (long i = 0; i < limit; i++) {
			valA= next(valA, partTwo, FACTOR_GEN_A, 4);
			valB= next(valB, partTwo, FACTOR_GEN_B, 8);
			if ((valA.intValue() & 0xffff) == (valB.intValue() & 0xffff)) {
				matches++;
			}
		}
		return matches;
	}

	private BigInteger next (BigInteger val, boolean partTwo, BigInteger factor, int mod) {
		if (!partTwo) {
			return val.multiply(factor).mod(MAX_VALUE);
		}
		do  {
			val= val.multiply(factor).mod(MAX_VALUE);
		} while (val.intValue() % mod != 0);
		return val;
	}
}

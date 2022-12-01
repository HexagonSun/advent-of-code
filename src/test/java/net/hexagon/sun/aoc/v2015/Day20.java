package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

// http://adventofcode.com/2015/day/20
public class Day20 extends AdventOfCode {

	@Test
	@Override
	public void runTask1() {
		int solution = 665_280;
		assertThat(solveTask1(29_000_000), is(solution));
		System.out.println("Check: sum(" + solution + ") == " + sumOfDivisors(solution));
	}

	@Test
	@Override
	public void runTask2() {
		int solution = 705600;
		assertThat(solveTask2(29_000_000), is(solution));
		System.out.println("Check: sum(" + solution + ") == " + sumOfDivisors(solution, 11));
	}

	private int solveTask1(int target) {
		for (int i = 1; i <= 1_000_000; i++) {
			int sum = sumOfDivisors(i);
			if (sum >= target) {
				return i;
			}
		}
		return -1;
	}

	private int solveTask2(int target) {
		return simulate(target);
	}

	private int simulate(int target) {
		int nbElves = 1_000_000;
		int[] houses = new int[nbElves];
		for (int i = 1; i < nbElves; i++) {
			// simulate elf #i
			int j = i;
			int housesVisited= 1;
			while (j < nbElves && housesVisited <= 50) {
				houses[j] += i * 11;
				j += i;
				housesVisited++;
			}
		}

		for (int i = 0; i < nbElves; i++) {
			if (houses[i] >= target) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * OEIS A000203
	 * a(n) = sigma(n)
	 */
	public int sumOfDivisors(int n) {
		return sumOfDivisors(n, 10);
	}

	public int sumOfDivisors(int n, int payout) {
		int prod = 1;
		for (int k = 2; k * k <= n; k++) {
			int p = 1;
			while (n % k == 0) {
				p = p * k + 1;
				n /= k;
			}
			prod *= p;
		}
		if (n > 1) {
			prod *= 1 + n;
		}
		// Multiply with the parcel-payout
		return prod * payout;
	}
}

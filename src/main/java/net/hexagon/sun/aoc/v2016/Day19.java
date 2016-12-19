package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/19
public class Day19 extends AdventOfCode {

	@Test
	@Override
	public void runTask1 () {
		int solution= 1834471;
		assertThat(solve(3014387), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
//		int solution= 1;
//		assertThat(solve(3014387, 400000), is(solution));
	}

	@Test
	public void runExample1() {
		assertThat(solve(5), is(3));
	}
	@Test
	public void runExample2() {
		assertThat(solve(7), is(7));
	}

	private int solve(int nbElves) {
		boolean[] elves= new boolean[nbElves];
		Arrays.fill(elves, true);

		while (true) {
			boolean presentsStolen= turn(elves);
			if (!presentsStolen) {
				break;
			}
		}
		// search winner
		int winner= -1;
		for (int i = 0; i < elves.length; i++) {
			if (elves[i]) {
				System.out.println("Elf " + (i+1) + " still has presents.");
				winner= (i+1);
			}
		}
		return winner;
	}

	private boolean turn(boolean[] elves) {
		int len = elves.length;
		boolean presentsStolen= false;
		for (int i = 0; i < len; i++) {
			if (!elves[i]) {
				continue;
			}

			int j= (i+1) % len;
			while (j != i) {
//				System.out.println("\tj is: " + j);
				if (elves[j]) {
					elves[i]= true;
					elves[j]= false;
//					System.out.println((i+1) +" took " + (j+1) + "'s presents.");
					presentsStolen= true;
					break;
				}
				j= (j + 1) % len;
			}
		}
		return presentsStolen;
	}

}

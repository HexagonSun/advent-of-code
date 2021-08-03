package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/19
public class Day19 extends AdventOfCode {

	@Test
	@Override
	public void runTask1 () {
		int solution= 1834471;
		assertThat(solveTask1(3014387), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
		// brute force!
		int solution= 1420064;
		assertThat(solveTask2(3014387), is(solution));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1(5), is(3));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask1(7), is(7));
	}

	@Test
	public void runTask2Example1() {
		assertThat(solveTask2(5), is(2));
	}

	@Test
	public void runTask2Example2() {
		assertThat(solveTask2(4), is(1));
	}

	@Test
	public void runTask2Example3() {
		assertThat(solveTask2(3), is(3));
	}

	@Test
	public void runTask2Example4() {
		assertThat(solveTask2(6), is(3));
	}

	private int solveTask1(int nbElves) {
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

	private int solveTask2(int nbElves) {
		List<Integer> elves= new ArrayList<>();
		for (int i = 1; i <= nbElves; i++) {
			elves.add(i);
		}

		int turns= 0;
		int index= 0;
		while (true) {
			int len = elves.size();
			turns++;
			if (turns > 1000) {
				System.out.println("len is : " + len);
				turns= 0;
			}
			if (elves.size() == 1) {
				break;
			}

			// do the turn
			int indexToRemove= (index + (len / 2)) % len;
			elves.remove(indexToRemove);

			if (indexToRemove < index) {
				// removed elf before this node -> adjust index
				index--;
			}
			index++;
			index= index % elves.size();
		}
		return elves.get(0);
	}

}

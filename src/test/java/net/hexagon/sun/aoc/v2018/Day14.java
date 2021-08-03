package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day14 extends AdventOfCode {

	private String INITIAL_RECIPES = "37";

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(INITIAL_RECIPES, 77201), is("9211134315"));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(INITIAL_RECIPES, "077201"), is(20357548));
	}


	@Test
	public void runExample1 () {
		assertThat(solveTask1(INITIAL_RECIPES, 9), is("5158916779"));
	}

	@Test
	public void runExample2 () {
		assertThat(solveTask1(INITIAL_RECIPES, 5), is("0124515891"));
	}

	@Test
	public void runExample3 () {
		assertThat(solveTask1(INITIAL_RECIPES, 18), is("9251071085"));
	}

	@Test
	public void runExample4 () {
		assertThat(solveTask1(INITIAL_RECIPES, 2018), is("5941429882"));
	}

	@Test
	public void runTask2Example1 () {
		assertThat(solveTask2(INITIAL_RECIPES, "51589"), is(9));
	}

	@Test
	public void runTask2Example2 () {
		assertThat(solveTask2(INITIAL_RECIPES, "01245"), is(5));
	}

	@Test
	public void runTask2Example3 () {
		assertThat(solveTask2(INITIAL_RECIPES, "92510"), is(18));
	}

	@Test
	public void runTask2Example4 () {
		assertThat(solveTask2(INITIAL_RECIPES, "59414"), is(2018));
	}

	private String solveTask1 (String input, int nbReceipies) {
		System.out.println("Initial: " + input);

		input = run(input, nbReceipies);

		String score = score(input, nbReceipies);
		System.out.println("score is: " + score);
		return score;
	}

	private String run (String input, int nbReceipies) {
		int index0 = 0;
		int index1 = 1;

		StringBuilder sb = new StringBuilder(input);
		int nbRounds = nbReceipies + 10;
		for (int i = 0; i < nbRounds; i++) {
			tick(sb, index0, index1);
			index0 = nextPos(sb, index0);
			index1 = nextPos(sb, index1);
		}
		return sb.toString();
	}

	private String score (String input, int nbRounds) {
		return input.substring(nbRounds, nbRounds + 10);
	}

	private int nextPos (StringBuilder input, int index0) {
		int next0 = input.charAt(index0) - '0';
		next0 = index0 + 1 + next0;
		index0 = (next0 % input.length());
		return index0;
	}

	private void tick (StringBuilder input, int index0, int index1) {
		char c1 = input.charAt(index0);
		char c2 = input.charAt(index1);

		int x1 = c1 - '0';
		int x2 = c2 - '0';
		int res = x1 + x2;
		String recepie = "" + res;

		input.append(recepie);
	}

	private int solveTask2 (String in, String nbReceipies) {
		// run it a couple of times.
		// The value was found by increasing it a bit after no match was found
		char[] output = run(in, 21_000_000).toCharArray();
		char[] needle = nbReceipies.toCharArray();

		for (int i = 0; i < output.length - 6; i++) {
			boolean match= true;
			for (int j = 0; j < needle.length; j++) {
				match &= output[i + j] == needle[j];
				if (!match) {
					break;
				}
			}
			if (match) {
				System.out.println("Found needle " + nbReceipies + " at index " + i);
				return i;
			}
		}
		return -1;
	}

}

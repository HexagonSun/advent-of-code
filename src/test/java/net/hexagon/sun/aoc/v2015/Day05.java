package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/5
public class Day05 extends AdventOfCode {

	private static final List<String> BANNED= Arrays.asList("ab", "cd", "pq", "xy");

	@Test
	@Override
	public void runTask1 () {
		int total= solveTask1(getInputLines());
		System.out.println(total);

		// solution
		assertThat(total, is(236));
	}

	@Test
	@Override
	public void runTask2 () {
		int total= solveTask2(getInputLines());
		System.out.println(total);

		// solution
		assertThat(total, is(51));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1("ugknbfddgicrmopn"), is(true));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask1("aaa"), is(true));
	}

	@Test
	public void runExample3() {
		assertThat(solveTask1("jchzalrnumimnmhp"), is(false));
	}

	@Test
	public void runExample4() {
		assertThat(solveTask1("haegwjzuvuyypxyu"), is(false));
	}

	@Test
	public void runExample5() {
		assertThat(solveTask1("dvszwmarrgswjxmb"), is(false));
	}


	@Test
	public void runTask2Example1() {
		assertThat(solveTask2("qjhvhtzxzqqjkmpb"), is(true));
	}

	@Test
	public void runTask2Example2() {
		assertThat(solveTask2("xxyxx"), is(true));
	}

	@Test
	public void runTask2Example3() {
		assertThat(solveTask2("uurcxstgmygtbstg"), is(false));
	}

	@Test
	public void runTask2Example4() {
		assertThat(solveTask2("ieodomkazucvgmuy"), is(false));
	}


	private int solveTask1(List<String> lines) {
		return lines
				   .stream()
				   .mapToInt(line -> solveTask1(line) ? 1 : 0)
				   .sum();
	}

	private boolean solveTask1(final String input) {
		boolean threeVowels= checkAtLeastThreeVowels(input);
		if (!threeVowels) {
			return false;
		}

		boolean doubleLetters= checkDoubleLetters(input);
		if (!doubleLetters) {
			return false;
		}

		boolean containsBanned= checkContainsBanned(input);
		if (containsBanned) {
			return false;
		}

		return true;
	}

	private boolean checkAtLeastThreeVowels(String input) {
		int count = input.replaceAll("[^aeiouAEIOU]", "").length();
		return count >= 3;
	}

	private boolean checkDoubleLetters(String input) {
		if (input.length() < 2) {
			return false;
		}

		char[] allChars= input.toCharArray();
		char last= allChars[0];
		for (int i = 1; i < allChars.length; i++) {
			char current= allChars[i];
			if (last == current) {
				return true;
			}
			last= current;
		}
		return false;
	}

	private boolean checkContainsBanned(String input) {
		for (String s : BANNED) {
			if (input.contains(s)) {
				return true;
			}
		}
		return false;
	}

	private int solveTask2(List<String> lines) {
		return lines
					   .stream()
					   .mapToInt(line -> solveTask2(line) ? 1 : 0)
					   .sum();
	}

	private boolean solveTask2(String input) {
		boolean triple= hasTriple(input);
		if (!triple) {
			return false;
		}

		boolean repeatingPair= hasRepeatingPair(input);
		if (!repeatingPair) {
			return false;
		}

		return true;
	}

	private boolean hasRepeatingPair(String input) {
		if (input.length() < 4) {
			return false;
		}

		char[] allChars= input.toCharArray();
		char letter0= allChars[0];
		char letter1= allChars[1];

		for (int i = 2; i < allChars.length; i++) {
			boolean hasOtherPair= checkTail(letter0, letter1, allChars, i);
			if (hasOtherPair) {
				return true;
			}
			letter0= letter1;
			letter1= allChars[i];
		}
		return false;
	}

	// check if "input" has a pair letter0/letter1 after "position"
	private boolean checkTail(char letter0, char letter1, char[] allChars, int position) {
		if (allChars.length < (position + 2)) {
			return false;
		}

		char pair0= allChars[position];
		for(int i= position + 1; i < allChars.length; i++) {
			char pair1= allChars[i];
			if (pair0 == letter0 && pair1 == letter1) {
				return true;
			}
			pair0= pair1;
		}
		return false;
	}

	private boolean hasTriple(String input) {
		if (input.length() < 3) {
			return false;
		}

		char[] allChars= input.toCharArray();
		char n0= allChars[0];
		char n1= allChars[1];

		for (int i = 2; i < allChars.length; i++) {
			char n2= allChars[i];
			if (n0 == n2) {
				return true;
			}
			n0= n1;
			n1= n2;
		}
		return false;
	}
}

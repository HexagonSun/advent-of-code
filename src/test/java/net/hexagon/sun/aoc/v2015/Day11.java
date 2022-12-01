package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/11
public class Day11 extends AdventOfCode {

	@Test
	@Override
	public void runTask1() {
		String next= solve("hxbxwxba");
		assertThat(next, is("hxbxxyzz"));
	}

	@Test
	public void runExample1() {
		assertThat(checkIncreasingStraight("hijklmmn"), is(true));
		assertThat(checkForbiddenChars("hijklmmn"), is(false));
		assertThat(checkTwoPairs("hijklmmn"), is(false));

		assertThat(check("hijklmmn"), is(false));
	}

	@Test
	public void runExample2() {
		assertThat(checkIncreasingStraight("abbceffg"), is(false));
		assertThat(checkForbiddenChars("abbceffg"), is(true));
		assertThat(checkTwoPairs("abbceffg"), is(true));

		assertThat(check("abbceffg"), is(false));
	}

	@Test
	public void runExample3() {
		assertThat(checkIncreasingStraight("abbcegjk"), is(false));
		assertThat(checkForbiddenChars("abbcegjk"), is(true));
		assertThat(checkTwoPairs("abbcegjk"), is(false));

		assertThat(check("abbcegjk"), is(false));
	}

	@Test
	public void runExample4() {
		assertValidPassword("abcdffaa");
		assertThat(solve("abcdefgh"), is("abcdffaa"));
	}

	@Test
	public void runExample5() {
		assertValidPassword("ghjaabcc");
		assertThat(solve("ghijklmn"), is("ghjaabcc"));
	}

	private void assertValidPassword(String password) {
		assertThat(checkIncreasingStraight(password), is(true));
		assertThat(checkForbiddenChars(password), is(true));
		assertThat(checkTwoPairs(password), is(true));
	}

	@Test
	@Override
	public void runTask2() {
		String next= solve("hxbxxyzz");
		assertThat(next, is("hxcaabcc"));
	}

	private String solve(String input) {
		char[] digits= input.toCharArray();
		do {
			nextPassword(digits);
		} while(!check(digits));

		return new String(digits);
	}

	private void nextPassword(char[] digits) {
		for (int i= digits.length - 1; i >= 0; i--) {
			digits[i]+= 1;
			char newChar= digits[i];
			if ('i' == newChar || 'l' == newChar || 'o' == newChar) {
				// heuristic: with the letter there can't be a valid pass. Skip it.
				digits[i]+= 1;
				break;
			} else if (newChar > 'z') {
				digits[i]= 'a';
			} else {
				break;
			}
		}
	}

	private boolean check(char[] digits) {
		return checkIncreasingStraight(digits) && checkForbiddenChars(digits) && checkTwoPairs(digits);
	}

	private boolean check(String input) {
		return checkIncreasingStraight(input) && checkForbiddenChars(input) && checkTwoPairs(input);
	}

	private static boolean checkIncreasingStraight(String input) {
		return checkIncreasingStraight(input.toCharArray());
	}

	private static boolean checkIncreasingStraight(char[] input) {
		char first= input[0];
		char second= input[1];

		for (int i = 2; i < input.length; i++) {
			char third= input[i];
			if ((third == second + 1) && (second == first + 1)) {
				return true;
			}
			first= second;
			second= third;
		}
		return false;
	}

	private static boolean checkForbiddenChars(String input) {
		return checkForbiddenChars(input.toCharArray());
	}

	private static boolean checkForbiddenChars(char[] digits) {
		for (char c : digits) {
			if ('i' == c || 'o' == c || 'l' == c) {
				return false;
			}
		}
		return true;
	}

	private static boolean checkTwoPairs(String input) {
		return checkTwoPairs(input.toCharArray());
	}

	private static boolean checkTwoPairs(char[] digits) {
		int nbPairs= 0;
		char last= digits[0];
		for (int i = 1; i < digits.length; i++) {
			char current= digits[i];
			if (last == current) {
				nbPairs++;
				// dummy value that won't match any valid char
				last= 0;
			} else {
				last= current;
			}

			if (nbPairs > 1) {
				return true;
			}
		}
		return false;
	}

}

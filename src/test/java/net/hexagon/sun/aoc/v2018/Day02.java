package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day02 extends AdventOfCode {

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(getInputLines()), is(7936));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(getInputLines()), is("lnfqdscwjyteorambzuchrgpx"));
	}

	@Test
	public void runExample1 () {
		List<String> input = Arrays.asList(
				"abcdef",
				"bababc",
				"abbcde",
				"abcccd",
				"aabcdd",
				"abcdee",
				"ababab"
		);
		assertThat(solveTask1(input), is(12));
	}

	@Test
	public void runExample2 () {
		List<String> input = Arrays.asList(
				"abcde",
				"fghij",
				"klmno",
				"pqrst",
				"fguij",
				"axcye",
				"wvxyz"
		);
		assertThat(solveTask2(input), is("fgij"));
	}

	private int solveTask1 (List<String> input) {
		Set<String> twoSameLetters = twoSameLetters(input);
		Set<String> threeSameLetters = threeSameLetters(input);

		return twoSameLetters.size() * threeSameLetters.size();
	}

	private String solveTask2 (List<String> input) {
		if (input == null || input.isEmpty()) {
			return "";
		}
		if (input.size() == 1) {
			return input.get(0);
		}

		int smallestDist = Integer.MAX_VALUE;
		String smallestLeft = "";
		String smallestRight = "";

		for (int i = 0; i < input.size(); i++) {
			String left = input.get(i);
			for (int j = i + 1; j < input.size(); j++) {
				String right = input.get(j);
				int lDist = levenshteinDistance(left, right);
				if (lDist < smallestDist) {
					smallestDist = lDist;
					smallestLeft = left;
					smallestRight = right;
				}
			}
		}

		return smallestCommon(smallestLeft, smallestRight);
	}

	private Set<String> twoSameLetters (List<String> input) {
		return sameLetters(2, input);
	}

	private Set<String> threeSameLetters (List<String> input) {
		return sameLetters(3, input);
	}

	private Set<String> sameLetters (int n, List<String> input) {
		return input.stream().filter(s -> hasSameLetters(n, s)).collect(Collectors.toSet());
	}

	private boolean hasSameLetters (int n, String input) {
		if (input == null || input.isEmpty()) {
			return false;
		}

		boolean exactlyN = false;
		char[] arr = input.toCharArray();
		Arrays.sort(arr);
		int cnt = 0;
		int lastChar = arr[0];
		for (char c : arr) {
			if (lastChar != c) {
				// reset
				cnt = 0;
				if (exactlyN) {
					return true;
				}
			}

			cnt++;

			if (cnt == n) {
				exactlyN = true;
			} else if (cnt > n) {
				exactlyN = false;
			}
			lastChar = c;
		}
		return exactlyN;
	}


	private String smallestCommon (String smallestLeft, String smallestRight) {
		char[] left = smallestLeft.toCharArray();
		char[] right = smallestRight.toCharArray();

		String common = "";
		for (int i = 0; i < left.length; i++) {
			if (left[i] == right[i]) {
				common += left[i];
			}
		}
		return common;
	}

	// from https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
	private static int levenshteinDistance (CharSequence lhs, CharSequence rhs) {
		int len0 = lhs.length() + 1;
		int len1 = rhs.length() + 1;

		// the array of distances
		int[] cost = new int[len0];
		int[] newcost = new int[len0];

		// initial cost of skipping prefix in String s0
		for (int i = 0; i < len0; i++) cost[i] = i;

		// dynamically computing the array of distances

		// transformation cost for each letter in s1
		for (int j = 1; j < len1; j++) {
			// initial cost of skipping prefix in String s1
			newcost[0] = j;

			// transformation cost for each letter in s0
			for (int i = 1; i < len0; i++) {
				// matching current letters in both strings
				int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

				// computing cost for each transformation
				int cost_replace = cost[i - 1] + match;
				int cost_insert = cost[i] + 1;
				int cost_delete = newcost[i - 1] + 1;

				// keep minimum cost
				newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
			}

			// swap cost/newcost arrays
			int[] swap = cost;
			cost = newcost;
			newcost = swap;
		}

		// the distance is the cost for transforming all letters in both strings
		return cost[len0 - 1];
	}

}

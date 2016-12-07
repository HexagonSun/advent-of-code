package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/6
public class Day06 extends AdventOfCode {

	private class Frequency {
		int[] counter= new int[26];

		void add(char c) {
			counter[c - 'a'] += 1;
		}

		char getMostFrequent() {
			int maxCount= 0;
			int maxIdx= 0;
			for (int i = 0; i < 26; i++) {
				if (counter[i] > maxCount) {
					maxCount= counter[i];
					maxIdx= i;
				}
			}
			return (char) ('a' + maxIdx);
		}

		char getLeastFrequent() {
			int minCount= Integer.MAX_VALUE;
			int minIdx= 0;
			for (int i = 0; i < 26; i++) {
				int value= counter[i];
				if (value > 0 && value < minCount) {
					minCount= value;
					minIdx= i;
				}
			}
			return (char) ('a' + minIdx);
		}
	}

	@Test
	@Override
	public void runTask1 () {
		String solution= "qtbjqiuq";
		assertThat(solveTask1(getInputLines()), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
		String solution= "akothqli";
		assertThat(solveTask2(getInputLines()), is(solution));
	}

	@Test
	public void runExample1() {
		List<String> input= Arrays.asList("eedadn",
				"drvtee",
				"eandsr",
				"raavrd",
				"atevrs",
				"tsrnev",
				"sdttsa",
				"rasrtv",
				"nssdts",
				"ntnada",
				"svetve",
				"tesnvt",
				"vntsnd",
				"vrdear",
				"dvrsen",
				"enarar");
		assertThat(solveTask1(input), is("easter"));
	}

	@Test
	public void runTask2Example1() {
		List<String> input= Arrays.asList("eedadn",
				"drvtee",
				"eandsr",
				"raavrd",
				"atevrs",
				"tsrnev",
				"sdttsa",
				"rasrtv",
				"nssdts",
				"ntnada",
				"svetve",
				"tesnvt",
				"vntsnd",
				"vrdear",
				"dvrsen",
				"enarar");
		assertThat(solveTask2(input), is("advent"));
	}

	private String solveTask1 (List<String> input) {
		return solve(input, true);
	}

	private String solveTask2 (List<String> input) {
		return solve(input, false);
	}

	private String solve (List<String> input, boolean useMostLikely) {
		int wordLength= input.get(0).length();
		Frequency[] frequencies= new Frequency[wordLength];
		for (int i = 0; i < wordLength; i++) {
			frequencies[i]= new Frequency();
		}

		for (String line : input) {
			char[] charArray = line.toCharArray();
			for (int i = 0; i < wordLength; i++) {
				char c = charArray[i];
				frequencies[i].add(c);
			}
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < wordLength; i++) {
			if (useMostLikely) {
				sb.append(frequencies[i].getMostFrequent());
			} else {
				sb.append(frequencies[i].getLeastFrequent());
			}
		}
		return sb.toString();
	}
}

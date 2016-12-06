package net.hexagon.sun.aoc;

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
	}

	private String solveTask1 (List<String> input) {

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
			sb.append(frequencies[i].getMostFrequent());
		}
		return sb.toString();
	}

	private String solveTask2 (List<String> input) {
		return "";
	}
}

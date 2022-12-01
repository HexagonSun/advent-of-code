package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day04 extends AdventOfCode {

	@Test
	@Override
	public void runTask1() {
		assertThat(solveTask1(getInputLines()), is(455L));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solveTask2(getInputLines()), is(186L));
	}

	@Test
	public void runExample1() {
		List<String> input= Arrays.asList("aa bb cc dd ee", "aa bb cc dd aa", "aa bb cc dd aaa");
		assertThat(solveTask1(input), is(2L));
	}

	@Test
	public void runTask2Example1() {
		List<String> input= Arrays.asList("abcde fghij",
				"abcde xyz ecdab",
				"a ab abc abd abf abj",
				"iiii oiii ooii oooi oooo",
				"oiii ioii iioi iiio");

		assertThat(isValid(input.get(0)), is(true));
		assertThat(isNoAnagram(input.get(0)), is(true));
		assertThat(isValid(input.get(1)), is(true));
		assertThat(isNoAnagram(input.get(1)), is(false));
		assertThat(isValid(input.get(2)), is(true));
		assertThat(isNoAnagram(input.get(2)), is(true));
		assertThat(isValid(input.get(3)), is(true));
		assertThat(isNoAnagram(input.get(3)), is(true));
		assertThat(isValid(input.get(4)), is(true));
		assertThat(isNoAnagram(input.get(4)), is(false));

		assertThat(solveTask2(input), is(3L));
	}

	private long solveTask1(List<String> input) {
		return input.stream().map(this::isValid).filter(b -> b).count();
	}

	private long solveTask2(List<String> input) {
		return input.stream()
					   .map(pwd -> isValid(pwd) && isNoAnagram(pwd))
					   .filter(b -> b)
					   .count();
	}

	private boolean isValid(String passphrase) {
		String[] split = passphrase.split(" ");
		int uniqueCount= new HashSet<>(Arrays.asList(split)).size();
		return split.length == uniqueCount;
	}

	private boolean isNoAnagram(String passphrase) {
		String[] split = passphrase.split(" ");
		long uniqueCount= Arrays.stream(split)
								  .map(this::sortedString)
								  .distinct()
								  .count();
		return split.length == uniqueCount;
	}

	private List<Character> sortedString(String password) {
		return password.chars()
					   .mapToObj(c -> (char)c)
					   .sorted()
					   .collect(Collectors.toList());
	}

}

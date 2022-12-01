package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/7
public class Day07 extends AdventOfCode {

	private static Pattern IN_BRACKETS= Pattern.compile("\\[(.*?)]");

	@Test
	@Override
	public void runTask1 () {
		int solution= 110;
		assertThat((int)solveTask1(getInputLines()), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
		int solution= 242;
		assertThat((int)solveTask2(getInputLines()), is(solution));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1("abba[mnop]qrst"), is(1));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask1("abcd[bddb]xyyx"), is(0));
	}

	@Test
	public void runExample3() {
		assertThat(solveTask1("aaaa[qwer]tyui"), is(0));
	}

	@Test
	public void runExample4() {
		assertThat(solveTask1("ioxxoj[asdfgh]zxcvbn"), is(1));
	}

	@Test
	public void runExample5() {
		assertThat(solveTask1("ioxxoj[asdfgh]ioxxoj[asdfgh]ioxxoj[asdfgh]"), is(1));
	}

	@Test
	public void runExample6() {
		assertThat(solveTask1("ioxxoj[asdfgh]ioxxoj[aabb]asdfgh[ioxxoj]"), is(0));
	}

	@Test
	public void runExample7() {
		assertThat(solveTask1("nojldommqhxo]oxbcsksyguwkkdugg"), is(0));
	}

	@Test
	public void runTask2Example1() {
		assertThat(solveTask2("aba[bab]xyz"), is(1));
	}
	@Test
	public void runTask2Example2() {
		assertThat(solveTask2("abaxyx[xyx]xyx"), is(0));
	}
	@Test
	public void runTask2Example3() {
		assertThat(solveTask2("aaa[kek]eke"), is(1));
	}
	@Test
	public void runTask2Example4() {
		assertThat(solveTask2("zazbz[bzb]cdb"), is(1));
	}
	@Test
	public void runTask2Example5() {
		assertThat(solveTask2("[xxx]zazbz[bzb]cdb"), is(1));
	}
	@Test
	public void runTask2Example6() {
		assertThat(solveTask2("[ababccc]zazbz[bzb]cdb"), is(1));
	}

	private long solveTask1 (List<String> input) {
		return input.stream()
					   .map(this::solveTask1)
					   .filter(i -> i > 0)
					   .count();
	}

	private int solveTask1 (String input) {
		int inBrackets= 0;
		int matchFound= 0;

		char a= 0;
		char b= 0;
		char b2= 0;
		char a2= 0;
		for (char c : input.toCharArray()) {
			if ('[' == c) {
				inBrackets++;
				a=b=b2=a2=0;
				continue;
			}
			if (']' == c) {
				inBrackets--;
				a=b=b2=a2=0;
				continue;
			}

			a= b;
			b= b2;
			b2= a2;
			a2= c;

//			System.out.println("a=" + a + " | b=" + b + " | b2=" + b2 + " | a2=" + a2);

			if (b2 == 0) {
				// sliding window not complete yet
				continue;
			}

			if (a == a2 && b == b2 && a != b) {
				if (inBrackets > 0) {
					// in-bracket match -> abort
					return 0;
				}
//				System.out.println("a=" + a + " | b=" + b + " | b2=" + b2 + " | a2=" + a2);
//				System.out.println("\tmatch found! \"" + a+b+b2+a2 + "\"");
//				System.out.println("\tinput was:\n\t" + input);
				matchFound= 1;
			}
		}

		return matchFound;
	}

	private long solveTask2 (List<String> input) {

		return input.stream()
					   .map(this::solveTask2)
					   .filter(i -> i > 0)
					   .count();
	}

	private int solveTask2 (String input) {

		Set<String> bracketParts = getBracketed(input);
//		System.out.println("got bracket part: " + bracketParts);

		Set<String> bracketMatches= getAba(bracketParts);
//		System.out.println("got bracket matches: " + bracketMatches);


		Set<String> regularParts= getRegularParts(input);
		Set<String> regularMatches= getAba(regularParts);
//		System.out.println("got regular matches: " + regularMatches);

		for (String match : regularMatches) {
			String inverse= inverse(match);
			if (bracketMatches.contains(inverse)) {
				return 1;
			}
		}
		return 0;
	}

	private String inverse(String match) {
		return "" + match.charAt(1) + match.charAt(0) + match.charAt(1);
	}

	private Set<String> getRegularParts(String input) {
		String stripped = IN_BRACKETS.matcher(input).replaceAll("|");
		String[] split = stripped.split("\\|");
		Set<String> parts= new HashSet<>();
		for (String s : split) {
			if (!s.isEmpty()) {
				parts.add(s);
			}
		}
		return parts;
	}

	private Set<String> getAba(Set<String> parts) {
		Set<String> matches = new HashSet<>();
		for (String text : parts) {
			matches.addAll(getAba(text));
		}
		return matches;
	}

	private Set<String> getAba(String text) {
		Set<String> aba= new HashSet<>();

		char a= 0, b= 0, a2= 0;
		for (char c : text.toCharArray()) {
			a= b;
			b= a2;
			a2= c;
			if (a == a2 && a != b) {
				aba.add("" + a + b + a2);
			}
		}
//		System.out.println("got all matches: " + aba);
		return aba;
	}

	private Set<String> getBracketed(String input) {
		Set<String> content= new HashSet<>();
		Matcher m= IN_BRACKETS.matcher(input);
		while(m.find()) {
			content.add(m.group(1));
		}
		return content;
	}
}

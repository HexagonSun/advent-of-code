package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day05 extends AdventOfCode {

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(getInputAsString()), is(9562));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(getInputAsString()), is(4934));
	}

	@Test
	public void runExample1 () {
		assertThat(solveTask1("dabAcCaCBAcCcaDA"), is(10));
	}

	@Test
	public void runTask2Example1 () {
		assertThat(solveTask2("dabAcCaCBAcCcaDA"), is(4));
	}

	private int solveTask1 (String input) {
		String last;
		do {
			last = input;
			input = input.replace("aA", "")
							.replace("Aa", "")
							.replace("bB", "")
							.replace("Bb", "")
							.replace("cC", "")
							.replace("Cc", "")
							.replace("dD", "")
							.replace("Dd", "")
							.replace("eE", "")
							.replace("Ee", "")
							.replace("fF", "")
							.replace("Ff", "")
							.replace("gG", "")
							.replace("Gg", "")
							.replace("hH", "")
							.replace("Hh", "")
							.replace("iI", "")
							.replace("Ii", "")
							.replace("jJ", "")
							.replace("Jj", "")
							.replace("kK", "")
							.replace("Kk", "")
							.replace("lL", "")
							.replace("Ll", "")
							.replace("mM", "")
							.replace("Mm", "")
							.replace("nN", "")
							.replace("Nn", "")
							.replace("oO", "")
							.replace("Oo", "")
							.replace("pP", "")
							.replace("Pp", "")
							.replace("qQ", "")
							.replace("Qq", "")
							.replace("rR", "")
							.replace("Rr", "")
							.replace("sS", "")
							.replace("Ss", "")
							.replace("tT", "")
							.replace("Tt", "")
							.replace("uU", "")
							.replace("Uu", "")
							.replace("vV", "")
							.replace("Vv", "")
							.replace("wW", "")
							.replace("Ww", "")
							.replace("xX", "")
							.replace("Xx", "")
							.replace("yY", "")
							.replace("Yy", "")
							.replace("zZ", "")
							.replace("Zz", "");

		} while (last.length() != input.length());
		return input.length();
	}

	private int solveTask2 (String input) {
		String minUnit = null;
		int min = Integer.MAX_VALUE;
		for (char unit = 'A'; unit <= 'Z'; unit++) {
			String unitString = String.valueOf(unit);
			String oppCase = unitString.toLowerCase();
			String shortened = input.replaceAll(unitString, "")
									   .replaceAll(oppCase, "");
			int len = solveTask1(shortened);
			if (len < min) {
				min= len;
				minUnit= unitString;
			}
		}
		System.out.println("Minimum length: " + min + " with removed unit " + minUnit);
		return min;
	}

}

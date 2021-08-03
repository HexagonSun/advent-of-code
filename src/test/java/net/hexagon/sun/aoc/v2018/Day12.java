package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day12 extends AdventOfCode {

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(getInputLines()), is(2040L));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(getInputLines()), is(-1L));
	}

	private List<String> sampleInput= Arrays.asList(
			"initial state: #..#.#..##......###...###",
			"",
			"...## => #",
			"..#.. => #",
			".#... => #",
			".#.#. => #",
			".#.## => #",
			".##.. => #",
			".#### => #",
			"#.#.# => #",
			"#.### => #",
			"##.#. => #",
			"##.## => #",
			"###.. => #",
			"###.# => #",
			"####. => #");

	@Test
	public void runExample1 () {
		assertThat(solveTask1(sampleInput), is(325L));
	}

	@Test
	public void runTask2Example1 () {
		assertThat(solveTask2(sampleInput), is(-1L));
//		assertThat(solveTask2(sampleInput), is(66));
	}

	private static class Rule {
		String left;
		String output;
		boolean nextPlant;

		String apply(String input) {
			if (left.equals(input)) {
				return input.substring(0, 2) + output + input.substring(3);
			}
			return ".....";
		}

		@Override
		public String toString () {
			return left + " --> " + output;
		}

		public boolean matches (String input) {
			return left.equals(input);
		}
	}

	private long solveTask1 (List<String> input) {
		String state= pad(input.get(0).substring("initial state: ".length()));
		List<Rule> rules= parse(input.subList(2, input.size()));

		System.out.println(String.format("[%02d] %s", 0, state));

		int generations= 20;
		return run(state, rules, generations);
	}

	private long solveTask2 (List<String> input) {
		String state= pad(input.get(0).substring("initial state: ".length()));
		List<Rule> rules= parse(input.subList(2, input.size()));

		System.out.println(String.format("[%02d] %s", 0, state));

		long generations= 50_000_000_000L;
		return run(state, rules, generations);
	}


	private long run (String state, List<Rule> rules, long nbGenerations) {
//		System.out.println(String.format("[%02d] %s", 0, state));
		for (long i = 1; i <= nbGenerations; i++) {
			state= tick(state, rules);
//			System.out.println(String.format("[%02d] %s | %d", i, state, nbPlants(state)));
		}

		long sum= 0;
		long idx= -1 * LEFT_PAD.length();
		for (char c : state.toCharArray()) {
			if ('#' == c) {
				sum+= idx;
			}
			idx++;
		}
		return sum;
	}

	private int nbPlants (String state) {
		return state.replaceAll("\\.", "").length();
	}

	private String tick (String state, List<Rule> rules) {
		StringBuilder nextState= new StringBuilder("..");
		for (int i = 0; i < state.length() - 5; i++) {
			// apply
			String nextFive= state.substring(i, i + 5);
			boolean matched= false;
			for (Rule r : rules) {
				if (r.matches(nextFive)) {
					nextFive= r.apply(nextFive);
					matched= true;
					break;
				}
			}
			if (!matched) {
				nextState.append(".");
			} else {
				nextState.append(nextFive.charAt(2));
			}
		}

		nextState.append("...");
		return nextState.toString();
	}

	private List<Rule> parse (List<String> input) {
		return input.stream().map(this::parse).collect(Collectors.toList());
	}

	private Rule parse (String line) {
		Rule r= new Rule();
		r.left= line.substring(0, 5);
		r.output= line.substring(9, 10);
		r.nextPlant= r.output.equals("#");
		return r;
	}

	private static final String LEFT_PAD= "....................................";
	private static final String RIGHT_PAD= "....................................";

	private String pad (String line) {
		return LEFT_PAD + line + RIGHT_PAD;
	}

}

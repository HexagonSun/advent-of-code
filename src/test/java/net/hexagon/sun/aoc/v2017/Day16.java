package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day16 extends AdventOfCode {

	private static class Move {
		private final Step step;
		private final int places;
		private final String first;
		private final String second;

		Move(Step step, int nbPlaces) {
			this.step= step;
			this.places= nbPlaces;
			this.first= "";
			this.second= "";
		}

		Move(Step step, String first, String second) {
			this.step= step;
			this.places= 0;
			this.first= first;
			this.second= second;
		}

		public void apply(List<String> programs) {
			step.function.apply(programs, this);
		}
	}

	private enum Step {
		SWAP (Step::swap),
		EXCHANGE (Step::exchange),
		PARTNER (Step::partner);

		private final BiFunction<List<String>, Move, Void> function;

		Step (BiFunction<List<String>, Move, Void> function) {
			this.function= function;
		}

		private static Void swap(List<String> programs, Move move) {
			Collections.rotate(programs, move.places);
			return null;
		}

		private static Void exchange(List<String> programs, Move move) {
			int firstIndex= Integer.valueOf(move.first);
			int secondIndex= Integer.valueOf(move.second);
			Collections.swap(programs, firstIndex, secondIndex);
			return null;
		}

		private static Void partner(List<String> programs, Move move) {
			int firstIndex= programs.indexOf(move.first);
			int secondIndex= programs.indexOf(move.second);
			Collections.swap(programs, firstIndex, secondIndex);
			return null;
		}
	}

	@Test
	@Override
	public void runTask1() {
		List<String> input= Arrays.asList(getInputAsString().split(","));
		assertThat(solveTask1(16, input), is("kpbodeajhlicngmf"));
	}

	@Test
	@Override
	public void runTask2() {
		List<String> input= Arrays.asList(getInputAsString().split(","));
		assertThat(solveTask2(16, input), is("ahgpjdkcbfmneloi"));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1(5, Arrays.asList("s1", "x3/4", "pe/b")), is("baedc"));
	}

	private String solveTask1(int length, List<String> input) {
		return solve(length, input, 1);
	}

	private String solveTask2(int length, List<String> input) {
		return solve(length, input, 1_000_000_000);
	}

	private String solve(int length, List<String> input, int repetitions) {
		List<String> programs= init(length);
		List<Move> moves= parse(input);

		Set<List<String>> previous= new HashSet<>();
		List<List<String>> performedInOrder= new ArrayList<>();

		while (repetitions-- > 0) {
			for (Move m : moves) {
				m.apply(programs);
			}

			List<String> copy = new ArrayList<>(programs);
			if (!previous.add(copy)) {
//				System.out.println("repeated state, aborting early, reps: " + repetitions);
//				// print all steps
//				for (List<String> s : performedInOrder) {
//					System.out.println("\t" + s);
//				}

				List<String> endState= performedInOrder.get(repetitions % performedInOrder.size());
				return print(endState);
			}
			performedInOrder.add(copy);
		}
		return print(programs);
	}

	private List<Move> parse(List<String> input) {
		List<Move> parsed= new ArrayList<>(input.size());
		for (String move : input) {
			if (move.charAt(0) == 's') {
				int nbPlaces= Integer.parseInt(move.substring(1, move.length()));
				parsed.add(new Move(Step.SWAP, nbPlaces));
			} else if (move.charAt(0) == 'x') {
				String[] tokens= move.substring(1, move.length()).split("/");
				parsed.add(new Move(Step.EXCHANGE, tokens[0], tokens[1]));
			} else {
				String[] tokens= move.substring(1, move.length()).split("/");
				parsed.add(new Move(Step.PARTNER, tokens[0], tokens[1]));
			}
		}
		return parsed;
	}

	private String print(List<String> programs) {
		StringBuilder sb = new StringBuilder();
		for (String move : programs) {
			sb.append(move);
		}
		return sb.toString();
	}

	private List<String> init(int length) {
		List<String> programs= new ArrayList<>();
		for (char i = 'a'; i < 'a' + length; i++) {
			programs.add("" + i);
		}
		return programs;
	}

}

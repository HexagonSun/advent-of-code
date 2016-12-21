package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/21
public class Day21 extends AdventOfCode {

	private enum Instruction implements BiFunction<Instruction, Instruction, Instruction> {
		SWAP_POSITION 		((String s, Param param) -> swapAtPos(s, param.x, param.y)),
		SWAP_LETTER 		((String s, Param param) -> {
			int idx1= s.indexOf(param.a);
			int idx2= s.indexOf(param.b);
			return swapAtPos(s, idx1, idx2);
		}),
		ROTATE_LEFT 		((String s, Param param) -> rotateLeft(s, param.x)),
		ROTATE_RIGHT 		((String s, Param param) -> rotateRight(s, param.x)),
		ROTATE_LETTER_BASED ((String s, Param param) -> {
			// TODO
			int idx= s.indexOf(param.a);
			if (idx >= 4) {
				// one additional if at least index 4
				idx++;
			}
			// initial rotation
			idx++;
			return rotateRight(s, idx);
		}),
		REVERSE 			((String s, Param param) -> {
			String prefix= s.substring(0, param.x);
			String middle= s.substring(param.x, param.y + 1);
			String suffix= s.substring(param.y + 1);
			return prefix + new StringBuilder(middle).reverse().toString() + suffix;
		}),
		MOVE 				((String s, Param param) -> {
			int x= param.x;
			char atIndex= s.charAt(x);
			String removed= s.substring(0, x) + s.substring(x+1);
			// insert
			int y= param.y;
			return removed.substring(0, y) + atIndex + removed.substring(y);
		});

		private static String swapAtPos(String s, int x, int y) {
			char[] c= s.toCharArray();
			char tmp= c[x];
			c[x]= c[y];
			c[y]= tmp;
			return String.valueOf(c);
		}

		static String rotateRight(String s, int offset) {
			int i= offset % s.length();
			int splitIndex = s.length() - i;
			String part1 = s.substring(splitIndex);
			String part2 = s.substring(0, splitIndex);
			return part1 + part2;
		}

		static String rotateLeft(String s, int offset) {
			int i= offset % s.length();
			return s.substring(i) + s.substring(0, i);
		}

		private static class Param {
			int x;
			int y;
			char a;
			char b;

			Param(Instruction instruction) {
				this.x= instruction.x;
				this.y= instruction.y;
				this.a= instruction.a;
				this.b= instruction.b;
			}
		}

		private final BiFunction<String, Param, String> function;
		public String input;
		int x;
		int y;
		char a;
		char b;

		Instruction(BiFunction<String, Param, String> function) {
			this.function= function;
		}

		@Override
		public Instruction apply(Instruction partial, Instruction next) {
//			System.out.print("applying, partial=" + partial + " || input: " + partial.input + " || next: " + next);
			partial.input= next.function.apply(partial.input, new Param(this));
//			System.out.println(" || --> outcome: " + partial.input);
			return partial;
		}
	}

	@Test
	@Override
	public void runTask1 () {
		String solution= "gbhafcde";
		assertThat(solveTask1("abcdefgh", getInputLines()), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
		String solution= "";
		assertThat(solveTask2("abcdefgh", getInputLines()), is(solution));
	}

	@Test
	public void runExample1() {
		List<String> input= Arrays.asList(
				"swap position 4 with position 0",
				"swap letter d with letter b",
				"reverse positions 0 through 4",
				"rotate left 1 step",
				"move position 1 to position 4",
				"move position 3 to position 0",
				"rotate based on position of letter b",
				"rotate based on position of letter d");
		assertThat(solveTask1("abcde", input), is("decab"));
	}

	@Test
	public void runTask2Example1() {
		List<String> input= Arrays.asList(
				"5-8",
				"0-2",
				"4-7");
		assertThat(solveTask2("abcde", input), is(""));
	}


	private String solveTask1(String start, List<String> input) {
		Instruction initial = Instruction.REVERSE;
		initial.input= start;
		Instruction result= input.stream()
								   .map(this::parse)
								   .reduce(initial, (partial, next) -> next.apply(partial, next));

		System.out.println("finished, final state: : " + result.input);
		return result.input;
	}

	private Instruction parse(String line) {
		Instruction instruction;
		String[] tokens= line.split(" ");
		if ("swap".equals(tokens[0])) {
			if ("position".equals(tokens[1])) {
				instruction= Instruction.SWAP_POSITION;
				instruction.x= Integer.parseInt(tokens[2]);
				instruction.y= Integer.parseInt(tokens[5]);
			} else {
				instruction= Instruction.SWAP_LETTER;
				instruction.a = tokens[2].charAt(0);
				instruction.b = tokens[5].charAt(0);
			}
		} else if ("rotate".equals(tokens[0])) {
			if ("left".equals(tokens[1])) {
				instruction= Instruction.ROTATE_LEFT;
				instruction.x= Integer.parseInt(tokens[2]);
			} else if ("right".equals(tokens[1])) {
				instruction= Instruction.ROTATE_RIGHT;
				instruction.x= Integer.parseInt(tokens[2]);
			} else {
				instruction= Instruction.ROTATE_LETTER_BASED;
				instruction.a = tokens[6].charAt(0);
			}
		} else if ("reverse".equals(tokens[0])) {
			instruction= Instruction.REVERSE;
			instruction.x= Integer.parseInt(tokens[2]);
			instruction.y= Integer.parseInt(tokens[4]);
		} else {
			instruction= Instruction.MOVE;
			instruction.x= Integer.parseInt(tokens[2]);
			instruction.y= Integer.parseInt(tokens[5]);
		}
		return instruction;
	}

	private String solveTask2(String start, List<String> input) {

		return "";
	}
}

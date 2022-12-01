package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/21
public class Day21 extends AdventOfCode {

	private enum Instruction implements BiFunction<Instruction, Instruction, Instruction> {
		SWAP_POSITION 		(Instruction::swapAtPos),
		SWAP_LETTER 		(Instruction::swapLetter),
		ROTATE_LEFT 		(Instruction::rotateLeft),
		ROTATE_RIGHT 		(Instruction::rotateRight),
		ROTATE_LETTER_BASED (Instruction::rotateLetterBased),
		REVERSE 			(Instruction::reverse),
		MOVE 				(Instruction::move);

		private static String swapAtPos(String s, Instruction instruction) {
			return swapAtPos(s, instruction.x, instruction.y);
		}

		private static String swapAtPos(String s, int x, int y) {
			char[] c= s.toCharArray();
			char tmp= c[x];
			c[x]= c[y];
			c[y]= tmp;
			return String.valueOf(c);
		}

		private static String swapLetter(String s, Instruction instruction) {
			int idx1= s.indexOf(instruction.a);
			int idx2= s.indexOf(instruction.b);
			return swapAtPos(s, idx1, idx2);
		}

		static String rotateLeft(String s, Instruction instruction) {
			if (instruction.reversed) {
				return rotateRight(s, instruction.x);
			}
			return rotateLeft(s, instruction.x);
		}

		private static String rotateLeft(String s, int offset) {
			int i= offset % s.length();
			return s.substring(i) + s.substring(0, i);
		}

		static String rotateRight(String s, Instruction instruction) {
			if (instruction.reversed) {
				return rotateLeft(s, instruction.x);
			}
			return rotateRight(s, instruction.x);
		}

		static String rotateRight(String s, int offset) {
			int i= offset % s.length();
			int splitIndex = s.length() - i;
			String part1 = s.substring(splitIndex);
			String part2 = s.substring(0, splitIndex);
			return part1 + part2;
		}

		final static int[] reverseLookup = { 1, 1, 6, 2, 7, 3, 0, 4 };

		private static String rotateLetterBased(String s, Instruction instruction) {
			int idx= s.indexOf(instruction.a);
			if (instruction.reversed) {
				idx= reverseLookup[idx];
				return rotateLeft(s, idx);
			}

			if (idx >= 4) {
				// one additional if at least index 4
				idx++;
			}
			// initial rotation
			idx++;
			return rotateRight(s, idx);
		}

		private static String reverse(String s, Instruction instruction) {
			String prefix= s.substring(0, instruction.x);
			String middle= s.substring(instruction.x, instruction.y + 1);
			String suffix= s.substring(instruction.y + 1);
			return prefix + new StringBuilder(middle).reverse().toString() + suffix;
		}

		private static String move(String s, Instruction instruction) {
			int x= instruction.reversed ? instruction.y : instruction.x;
			char atIndex= s.charAt(x);
			String removed= s.substring(0, x) + s.substring(x+1);
			// insert
			int y= instruction.reversed ? instruction.x : instruction.y;
			return removed.substring(0, y) + atIndex + removed.substring(y);
		}

		private final BiFunction<String, Instruction, String> function;
		public String input;
		boolean reversed;
		int x;
		int y;
		char a;
		char b;

		Instruction(BiFunction<String, Instruction, String> function) {
			this.function= function;
		}

		@Override
		public Instruction apply(Instruction partial, Instruction next) {
			this.reversed= partial.reversed;
			System.out.print("applying, partial=" + partial + " || input: " + partial.input + " || next: " + next + " || reversed: " + partial.reversed);
			partial.input= next.function.apply(partial.input, this);
			System.out.println(" || --> outcome: " + partial.input);
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
		String solution= "bcfaegdh";
		assertThat(solveTask2("fbgdceah", getInputLines()), is(solution));
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
				"swap position 4 with position 0",
				"swap letter d with letter b",
				"reverse positions 0 through 4",
				"rotate left 1 step",
				"move position 1 to position 4",
				"move position 3 to position 0",
				"rotate based on position of letter b",
				"rotate based on position of letter d");
		assertThat(solveTask2("decab", input), is("abcde"));
	}

	@Test
	public void runTask2Example2() {
		List<String> input= Collections.singletonList("rotate based on position of letter d");
		assertThat(solveTask2("decab", input), is("ecabd"));
	}

	@Test
	public void runTask2Example3() {
		List<String> input= Collections.singletonList("rotate based on position of letter b");
		assertThat(solveTask2("ecabd", input), is("abdec"));
	}

	@Test
	public void runTask2Example4() {
		List<String> input= Collections.singletonList("move position 3 to position 0");
		assertThat(solveTask2("abdec", input), is("bdeac"));
	}

	@Test
	public void runTask2Example5() {
		List<String> input= Collections.singletonList("move position 1 to position 4");
		assertThat(solveTask2("bdeac", input), is("bcdea"));
	}

	@Test
	public void runTask2Example6() {
		List<String> input= Collections.singletonList("rotate left 1 step");
		assertThat(solveTask2("bcdea", input), is("abcde"));
	}

	@Test
	public void runTask2Example7() {
		List<String> input= Collections.singletonList("reverse positions 0 through 4");
		assertThat(solveTask2("abcde", input), is("edcba"));
	}

	@Test
	public void runTask2Example8() {
		List<String> input= Collections.singletonList("swap letter d with letter b");
		assertThat(solveTask2("edcba", input), is("ebcda"));
	}

	@Test
	public void runTask2Example9() {
		List<String> input= Collections.singletonList("swap position 4 with position 0");
		assertThat(solveTask2("ebcda", input), is("abcde"));
	}


	private String solveTask1(String start, List<String> input) {
		Instruction initial = Instruction.REVERSE;
		initial.input= start;
		initial.reversed= false;
		Instruction result= input.stream()
								   .map(this::parse)
								   .reduce(initial, (partial, next) -> next.apply(partial, next));

		System.out.println("finished, final state: : " + result.input);
		return result.input;
	}

	private String solveTask2(String start, List<String> input) {
		Collections.reverse(input);

		Instruction initial = Instruction.REVERSE;
		initial.input= start;
		initial.reversed= true;
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

}

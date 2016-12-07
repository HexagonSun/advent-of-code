package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/6
public class Day06 extends AdventOfCode {

	private enum Command { ON, OFF, TOGGLE;

		public int apply(int currentValue) {
			if (TOGGLE == this) {
				return currentValue == 0 ? 1 : 0;
			}
			return ON == this ? 1 : 0;
		}

		public int applyTask2(int currentValue) {
			if (TOGGLE == this) {
				return currentValue + 2;
			} else if (ON == this) {
				return currentValue + 1;
			}
			// off
			return currentValue > 0 ? currentValue - 1 : 0;
		}
	}

	private static final class Instruction {

		private final Command command;
		private final int fromX;
		private final int fromY;
		private final int toX;
		private final int toY;

		public static Instruction of(String[] data) {
			Command cmd;
			int coordinateStartIndex= 0;
			if (data.length == 4) {
				// toggle
				// toggle 352,432 through 628,550
				cmd= Command.TOGGLE;
				coordinateStartIndex= 1;
			} else {
				// on/off
				cmd = parseOnOff(data);
				coordinateStartIndex= 2;
			}

			int[] from= parseCoordinates(data[coordinateStartIndex]);
			int[] to= parseCoordinates(data[coordinateStartIndex + 2]);
			return new Instruction(cmd, from, to);
		}

		private static Command parseOnOff(String[] data) {
			return "on".equals(data[1]) ? Command.ON : Command.OFF;
		}

		private static int[] parseCoordinates(String input) {
			String[] tokens= input.split(",");
			return new int[] {
					Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1])
			};
		}

		private Instruction(Command command, int[] from, int[] to) {
			this.command= command;
			this.fromX= from[0];
			this.fromY= from[1];
			this.toX= to[0];
			this.toY= to[1];
		}

		@Override
		public String toString() {
			return command + " [" + fromX + '/' + fromY + "] / [" + toX + '/' + toY + ']';
		}
	}

	private static final int DIMENSION= 1000;
	private int[][] grid;

	@Before
	public void setUp() {
		grid= new int[DIMENSION][DIMENSION];
	}

	@Test
	@Override
	public void runTask1 () {
		solveTask1(getInputLines());
		int total= sum(grid);
		System.out.println(total);

		// solution
		assertThat(total, is(400410));
	}

	@Test
	@Override
	public void runTask2 () {
		solveTask2(getInputLines());
		int total= sum(grid);
		System.out.println(total);

		// solution
		assertThat(total, is(15343601));
	}

	@Test
	public void runExample1() {
		solveTask1("turn on 0,0 through 999,999");
				
		assertThat(sum(grid), is(DIMENSION * DIMENSION));
	}

	@Test
	public void runExample2() {
		solveTask1("toggle 0,0 through 999,0");
		assertThat(sum(grid), is(1000));
		// same input -> turns them off again
		solveTask1("toggle 0,0 through 999,0");
		assertThat(sum(grid), is(0));
	}

	@Test
	public void runExample3() {
		solveTask1("turn off 499,499 through 500,500");
		assertThat(sum(grid), is(0));
	}

	@Test
	public void runSimpleExample() {
		solveTask1("toggle 0,0 through 2,2");
		assertThat(sum(grid), is(9));
	}


	@Test
	public void runTask2Example1() {
		solveTask2("turn on 0,0 through 0,0");
		assertThat(sum(grid), is(1));
	}

	@Test
	public void runTask2Example2() {
		solveTask2("toggle 0,0 through 999,999");
		assertThat(sum(grid), is(2000000));
	}


	private int sum(int[][] grid) {
		int sum= 0;
		for (int i = 0; i < DIMENSION; i++) {
			int[] line = grid[i];
			for (int j = 0; j < DIMENSION; j++) {
				sum+= line[j];
			}
		}
		return sum;
	}
	
	private void solveTask1(List<String> lines) {
		for (String line : lines) {
			solveTask1(line);
		}
	}

	private void solveTask1(final String input) {
		Instruction instructions= parseLine(input);
//		System.out.println("got instr: " + instructions);
		run(grid, instructions);
	}

	private void run(int[][] grid, Instruction instructions) {
		run(grid, instructions, false);
	}

	private void run(int[][] grid, Instruction instructions, boolean taskTwo) {
		for (int i = instructions.fromX; i <= instructions.toX; i++) {
			int[] line = grid[i];
			for (int j = instructions.fromY; j <= instructions.toY; j++) {
				if (taskTwo) {
					line[j] = instructions.command.applyTask2(line[j]);
				} else {
					line[j] = instructions.command.apply(line[j]);
				}
			}
		}
	}

	private Instruction parseLine(String input) {
		String[] tokens = input.split(" ");
		return Instruction.of(tokens);
	}

	private void solveTask2(List<String> lines) {
		for (String line : lines) {
			solveTask2(line);
		}
	}

	private void solveTask2(final String input) {
		Instruction instructions= parseLine(input);
//		System.out.println("got instr: " + instructions);
		run(grid, instructions, true);
	}
}

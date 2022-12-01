package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/12
public class Day12 extends AdventOfCode {

	private static class Computer {
		Map<String, Integer> registers= new HashMap<>();

		int apply(Command cmd) {
			Integer value;
			switch (cmd.instruction) {
				case CPY:
					Integer source;
					if (cmd.valueX == null) {
						// copy from register
						source= registers.getOrDefault(cmd.registerX, 0);
					} else {
						source= cmd.valueX;
					}
					String targetRegister= cmd.registerY;
					registers.put(targetRegister, source);
					break;
				case INC:
					value= registers.getOrDefault(cmd.registerX, 0) + 1;
					registers.put(cmd.registerX, value);
					break;
				case DEC:
					value= registers.getOrDefault(cmd.registerX, 0) - 1;
					registers.put(cmd.registerX, value);
					break;
				case JNZ:
				default:
					Integer x;
					if (cmd.valueX == null) {
						// copy from register
						x= registers.getOrDefault(cmd.registerX, 0);
					} else {
						x= cmd.valueX;
					}

					if (x != 0) {
						return cmd.valueY;
					}
			}
			// Default: step to next instruction
			return 1;
		}
	}

	private enum Instruction {
		CPY, INC, DEC, JNZ
	}

	private static class Command {
		final Instruction instruction;
		final String registerX;
		final String registerY;
		final Integer valueX;
		final Integer valueY;

		Command(Instruction instruction, String regX, String regY, Integer valX, Integer valY) {
			this.instruction= instruction;
			this.registerX= regX;
			this.registerY= regY;
			this.valueX= valX;
			this.valueY= valY;
		}
	}

	@Test
	@Override
	public void runTask1 () {
		int solution= 318083;
		assertThat(solveTask1(getInputLines()), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
		int solution= 9227737;
		assertThat(solveTask2(getInputLines()), is(solution));
	}

	@Test
	public void runExample1() {
		List<String> input= Arrays.asList(
				"cpy 41 a",
				"inc a",
				"inc a",
				"dec a",
				"jnz a 2",
				"dec a");
		assertThat(solveTask1(input), is(42));
	}

	private int solveTask1(List<String> inputLines) {
		Computer computer= new Computer();
		return solve(inputLines, computer);
	}

	private int solveTask2(List<String> inputLines) {
		Computer computer= new Computer();
		computer.registers.put("c", 1);
		return solve(inputLines, computer);
	}

	private int solve(List<String> inputLines, Computer computer) {
		List<Command> commands = inputLines.stream().map(this::parse).collect(Collectors.toList());
		for (int i = 0; i < commands.size();) {
			if (i >= commands.size()) {
				// we're past all instructions: halt
				break;
			}
			int offset= computer.apply(commands.get(i));
			i+= offset;
		}
		return computer.registers.getOrDefault("a", 0);
	}

	private Command parse(String line) {
		String[] tokens= line.split(" ");
		Integer a, b;
		switch(tokens[0]) {
			case "cpy":
				a= asInt(tokens[1]);
				b= asInt(tokens[2]);
				return new Command(Instruction.CPY, tokens[1], tokens[2], a, b);
			case "inc":
				a= asInt(tokens[1]);
				return new Command(Instruction.INC, tokens[1], null, a, null);
			case "dec":
				a= asInt(tokens[1]);
				return new Command(Instruction.DEC, tokens[1], null, a, null);
			case "jnz":
			default:
				a= asInt(tokens[1]);
				b= asInt(tokens[2]);
				return new Command(Instruction.JNZ, tokens[1], tokens[2], a, b);
		}
	}

	private Integer asInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
		}
		return null;
	}

}

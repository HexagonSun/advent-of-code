package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/25
public class Day25 extends AdventOfCode {

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
					if (targetRegister == null) {
						// just skip
						break;
					}
					registers.put(targetRegister, source);
					break;
				case OUT:
					value= registers.getOrDefault(cmd.valueX, -1);
					if (cmd.valueX == null) {
						value= registers.getOrDefault(cmd.registerX, -1);
					}
					System.out.print(value + " ");
					System.out.flush();
					break;
				case INC:
					value= registers.getOrDefault(cmd.registerX, 0) + 1;
					if (cmd.registerX == null) {
						// just skip
						break;
					}
					registers.put(cmd.registerX, value);
					break;
				case DEC:
					value= registers.getOrDefault(cmd.registerX, 0) - 1;
					if (cmd.registerX == null) {
						// just skip
						break;
					}
					registers.put(cmd.registerX, value);
					break;
				case TGL:
					// handled outside
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
						int y;
						if (cmd.valueY == null) {
							// copy from register
							y= registers.getOrDefault(cmd.registerY, 0);
						} else {
							y= cmd.valueY;
						}
						return y;
					}
			}
			// Default: step to next instruction
			return 1;
		}
	}

	private enum Instruction {
		CPY, INC, DEC, TGL, JNZ, OUT
	}

	private static class Command {
		final String registerX;
		final String registerY;
		final Integer valueX;
		final Integer valueY;

		Instruction instruction;

		Command(Instruction instruction, String regX, String regY, Integer valX, Integer valY) {
			this.instruction= instruction;
			this.registerX= regX;
			this.registerY= regY;
			this.valueX= valX;
			this.valueY= valY;
		}

		@Override
		public String toString() {
			return this.instruction + " regX=" + registerX + " regY=" + registerY + " valX=" + valueX + " valY=" + valueY;
		}
	}

	@Test
	@Override
	public void runTask1 () {
		int solutionInput= 189;

		Computer computer= new Computer();
		computer.registers.put("a", solutionInput);
		assertThat(solve(getInputLines(), computer), is(341));
	}

	@Test
	@Override
	public void runTask2 () {
		// there is no task 2 on day 25
	}

	private int solve(List<String> inputLines, Computer computer) {
		List<Command> commands = inputLines.stream()
										 .map(this::parse)
										 .collect(Collectors.toList());


		int outCount= 0;
		for (int i = 0; i < commands.size();) {
			if (i >= commands.size()) {
				// we're past all instructions: halt
				break;
			}
			Command cmd = commands.get(i);
			int offset= 1;
			if (cmd.instruction == Instruction.TGL) {
				toggle(computer, commands, cmd, i);
			} else if (cmd.instruction == Instruction.OUT) {
				outCount++;
				if (outCount > 50) {
					System.out.flush();
					break;
				}
				offset = computer.apply(cmd);
			} else {
				offset = computer.apply(cmd);
			}
			i += offset;
		}
		return computer.registers.getOrDefault("a", 0);
	}

	private void toggle(Computer computer, List<Command> commands, Command cmd, int index) {
		Integer toggleOffset= computer.registers.getOrDefault(cmd.registerX, 0);
		int target= index + toggleOffset;
		if (target < 0 || target >= commands.size()) {
			return;
		}

		Command targetCommand = commands.get(target);
		if (Instruction.JNZ == targetCommand.instruction) {
			targetCommand.instruction= Instruction.CPY;
		} else if (Instruction.CPY == targetCommand.instruction) {
			targetCommand.instruction= Instruction.JNZ;
		} else if (Instruction.INC == targetCommand.instruction) {
			targetCommand.instruction= Instruction.DEC;
		} else {
			targetCommand.instruction= Instruction.INC;
		}

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
			case "out":
				a= asInt(tokens[1]);
				return new Command(Instruction.OUT, tokens[1], null, a, null);
			case "dec":
				a= asInt(tokens[1]);
				return new Command(Instruction.DEC, tokens[1], null, a, null);
			case "tgl":
				a= asInt(tokens[1]);
				return new Command(Instruction.TGL, tokens[1], null, a, null);
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

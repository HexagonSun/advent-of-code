package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/23
public class Day23 extends AdventOfCode {

	private enum Instruction {
		HLF, TPL, INC, JMP, JIE, JIO
	}

	private enum Register {
		A, B;
		public long value= 0;
	}

	private static class Statement {
		final Instruction instruction;
		final Register register;
		final int offset;
		public Statement(Instruction instruction, Register register, int offset) {
			this.instruction= instruction;
			this.register= register;
			this.offset= offset;
		}

		@Override
		public String toString() {
			return instruction + " " + register + " " + offset;
		}
	}

	private static class Cpu {
		public Cpu simulate(List<Statement> statements) {
			for (int i = 0; i>= 0 && i < statements.size(); ) {
				Statement stmt = statements.get(i);
				int offset = simulate(stmt);
				i += offset;

//				System.out.println("Executing @" + i + " || stmt = " + stmt
//							   + " | REGS: " + Register.A.value + " | " + Register.B.value);
			}
			return this;
		}

		private int simulate(Statement stmt) {
			switch (stmt.instruction) {
				case HLF:
					stmt.register.value /= 2;
					break;
				case TPL:
					stmt.register.value *= 3;
					break;
				case INC:
					stmt.register.value += 1;
					break;
				case JMP:
					return stmt.offset;
				case JIE:
					if ((stmt.register.value % 2) == 0) {
						return stmt.offset;
					}
					break;
				case JIO:
					if (stmt.register.value == 1) {
						return stmt.offset;
					}
					break;
				default:
					break;
			}
			return 1;
		}
	}

	@Test
	@Override
	public void runTask1() {
		List<String> input = getInputLines();
		assertThat(solveTask1(input), is(170L));
	}

	@Test
	@Override
	public void runTask2() {
		List<String> input = getInputLines();
		assertThat(solveTask2(input), is(247L));
	}

	@Test
	public void runExample1() {
		List<String> input = Arrays.asList(
				"inc a",
				"jio a, +2",
				"tpl a",
				"inc a");

		assertThat(solveTask1(input), is(0L));
		assertThat(Register.A.value, is(2L));
	}

	private long solveTask1(List<String> input) {
		initRegisters();
		return solve(input);
	}

	private long solveTask2(List<String> input) {
		initRegisters(1, 0);
		return solve(input);
	}

	private long solve(List<String> input) {
		List<Statement> statements= parse(input);
		new Cpu().simulate(statements);
		return Register.B.value;
	}

	private void initRegisters() {
		initRegisters(0, 0);
	}

	private void initRegisters(int a, int b) {
		Register.A.value= a;
		Register.B.value= b;
	}

	private List<Statement> parse(List<String> input) {
		List<Statement> statements= new ArrayList<>(input.size());
		for (String line : input) {
			statements.add(parse(line));
		}
		return statements;
	}

	private Statement parse(String line) {
		String[] tokens= line.split("[ ,]");
		Instruction i= Instruction.valueOf(tokens[0].trim().toUpperCase());
		Register r = null;
		int o;
		if (Instruction.JMP == i) {
			o= Integer.parseInt(tokens[1]);
		} else {
			r = Register.valueOf(tokens[1].trim().toUpperCase());
			o= tokens.length > 3 ? Integer.parseInt(tokens[3]) : 0;
		}
		return new Statement(i, r, o);
	}
}

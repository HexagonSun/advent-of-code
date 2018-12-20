package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day19 extends AdventOfCode {

	private static class Registers {

		private final Map<Integer, Integer> map = new HashMap<>();

		Integer get (Integer n) {
			return map.getOrDefault(n, 0);
		}

		Void set (Instruction instr, int value) {
			map.put(instr.registerC, value);
			return null;
		}

		@Override
		public String toString () {
			return "[" + this.map.toString() + "]";
		}
	}

	private enum Opcode {
		ADDR((regs, instr) -> regs.set(instr, regs.get(instr.inputA) + regs.get(instr.inputB))),
		ADDI((regs, instr) -> regs.set(instr, regs.get(instr.inputA) + instr.inputB)),

		MULR((regs, instr) -> regs.set(instr, regs.get(instr.inputA) * regs.get(instr.inputB))),
		MULI((regs, instr) -> regs.set(instr, regs.get(instr.inputA) * instr.inputB)),

		BANR((regs, instr) -> regs.set(instr, regs.get(instr.inputA) & regs.get(instr.inputB))),
		BANI((regs, instr) -> regs.set(instr, regs.get(instr.inputA) & instr.inputB)),

		BORR((regs, instr) -> regs.set(instr, regs.get(instr.inputA) | regs.get(instr.inputB))),
		BORI((regs, instr) -> regs.set(instr, regs.get(instr.inputA) | instr.inputB)),

		SETR((regs, instr) -> regs.set(instr, regs.get(instr.inputA))),
		SETI((regs, instr) -> regs.set(instr, instr.inputA)),

		GTIR((regs, instr) -> regs.set(instr, instr.inputA > regs.get(instr.inputB) ? 1 : 0)),
		GTRI((regs, instr) -> regs.set(instr, regs.get(instr.inputA) > instr.inputB ? 1 : 0)),
		GTRR((regs, instr) -> regs.set(instr, regs.get(instr.inputA) > regs.get(instr.inputB) ? 1 : 0)),

		EQIR((regs, instr) -> regs.set(instr, instr.inputA.equals(regs.get(instr.inputB)) ? 1 : 0)),
		EQRI((regs, instr) -> regs.set(instr, regs.get(instr.inputA).equals(instr.inputB) ? 1 : 0)),
		EQRR((regs, instr) -> regs.set(instr, regs.get(instr.inputA).equals(regs.get(instr.inputB)) ? 1 : 0));

		private final BiFunction<Registers, Instruction, Void> op;

		Opcode (BiFunction<Registers, Instruction, Void> op) {
			this.op = op;
		}

		void apply (Registers regs, Instruction instr) {
			op.apply(regs, instr);
		}
	}

	private static class Instruction {
		Opcode opcode;
		Integer inputA;
		Integer inputB;
		Integer registerC;

		@Override
		public String toString () {
			return opcode.name() + " " + inputA + " " + inputB + " " + registerC;
		}
	}

	private static class Program {
		int ipBoundTo;
		Registers registers = new Registers();
		List<Instruction> instructions;
	}

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(getInputLines()), is(1056));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(getInputLines()), is(573));
	}

	private List<String> sampleInput = Arrays.asList(
			"#ip 0",
			"seti 5 0 1",
			"seti 6 0 2",
			"addi 0 1 0",
			"addr 1 2 3",
			"setr 1 0 0",
			"seti 8 0 4",
			"seti 9 0 5");

	@Test
	public void runExample1 () {
		assertThat(solveTask1(sampleInput), is(6));
	}

	private int solveTask1 (List<String> input) {
		return solve(input, 0);
	}

	private int solveTask2 (List<String> input) {
		// not 0 ?
		// not 1
		return solve(input, 1);
	}

	private int solve (List<String> input, int initialRegister0) {
		Program program = parseProgram(input);
		program.registers.map.put(0, initialRegister0);
		int ip = 0;

		// 200:
		// after: [{0=0, 1=0, 2=23, 3=5, 4=10551389, 5=1}]

		// 20001
//		regs after: [{0=0, 1=0, 2=2498, 3=7, 4=10551389, 5=1}]

//		program.registers.map.put(2, 10551380);
//		program.registers.map.put(3, 5);
//		program.registers.map.put(4, 10551389);
//		program.registers.map.put(5, 10551388);
//		ip= 6;


//		for (int i = 0; i < 200; i++) {
		while (true) {
			program.registers.map.put(program.ipBoundTo, ip);
			if (ip < 0 || ip >= program.instructions.size()) {
				System.out.println("ip OOB");
				break;
			}

			Instruction instr = program.instructions.get(ip);
			instr.opcode.apply(program.registers, instr);

//			System.out.println("[" + ip + "] regs after: " + r);
			ip = program.registers.get(program.ipBoundTo);

//			if (program.registers.get(4) == 10551389 && program.registers.get(2) == 1 && program.registers.get(5) == 2) {
//				System.out.println("ip is: " + ip);
//				break;
//			}

			if (ip < 0 || ip >= program.instructions.size() - 1) {
				System.out.println("ip OOB");
				break;
			}

			ip++;
		}
		return program.registers.get(0);
	}

	private Program parseProgram (List<String> input) {
		Program p = new Program();
		p.instructions = new ArrayList<>();
		for (String line : input) {
			if (line.startsWith("#")) {
				p.ipBoundTo = Integer.parseInt(line.substring(line.length() - 1));
			} else {
				p.instructions.add(parseInstruction(line));
			}
		}
		return p;
	}

	private Instruction parseInstruction (String line) {
		String[] tokens = line.split(" ");
		Instruction instr = new Instruction();
		instr.opcode = Opcode.valueOf(tokens[0].trim().toUpperCase());
		instr.inputA = Integer.parseInt(tokens[1].trim());
		instr.inputB = Integer.parseInt(tokens[2].trim());
		instr.registerC = Integer.parseInt(tokens[3].trim());
		return instr;
	}

}

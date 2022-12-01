package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day16 extends AdventOfCode {

	private static class Registers {

		static Registers of (Registers other) {
			Registers regs = new Registers();
			regs.map.putAll(other.map);
			return regs;
		}

		private final Map<Integer, Integer> map = new HashMap<>();

		Integer get (Integer n) {
			return map.getOrDefault(n, 0);
		}

		Void set (Instruction instr, int value) {
			map.put(instr.registerC, value);
			return null;
		}

		@Override
		public boolean equals (Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof Registers)) {
				return false;
			}
			return this.map.equals(((Registers) obj).map);
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
		private int number = -1;

		Opcode (BiFunction<Registers, Instruction, Void> op) {
			this.op = op;
		}

		void apply (Registers regs, Instruction instr) {
			op.apply(regs, instr);
		}
	}

	private static class Instruction {
		Integer opcode;
		Integer inputA;
		Integer inputB;
		Integer registerC;
	}

	private static class Sample {
		Registers before;
		Instruction instr;
		Registers after;
	}

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(getInputLines()), is(529));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(getInputLines()), is(573));
	}

	@Test
	public void runExample1 () {
		assertThat(solveTask1(Arrays.asList(
				"Before: [3, 2, 1, 1]",
				"9 2 1 2",
				"After:  [3, 2, 2, 1]")), is(1));
	}

	private int solveTask1 (List<String> input) {
		List<Sample> samples = parseSamples(input);
		int moreThanTwo = 0;
		for (Sample s : samples) {
			List<Opcode> matching = findMatchingOpcodes(s);
			if (matching.size() >= 3) {
				moreThanTwo++;
			}
		}
		return moreThanTwo;
	}

	private int solveTask2 (List<String> input) {
		List<Sample> samples = parseSamples(input);
		buildOpcodes(samples);

		List<Instruction> program = parseProgram(input);

		Registers state = new Registers();
		Map<Integer, Opcode> opcodes = Arrays.stream(Opcode.values()).collect(Collectors.toMap(
				op -> op.number,
				Function.identity()
		));
		for (Instruction instr : program) {
			Opcode op = opcodes.get(instr.opcode);
			op.apply(state, instr);
		}

		return state.get(0);
	}

	private List<Opcode> findMatchingOpcodes (Sample s) {
		List<Opcode> matching = new ArrayList<>();
		for (Opcode o : Opcode.values()) {
			Registers before = Registers.of(s.before);
			o.apply(before, s.instr);

			if (before.equals(s.after)) {
//				System.out.println("match at opCode " + o + " | name = " + o.name());
				matching.add(o);
			}
		}
		return matching;
	}

	private Instruction parseInstruction (String line) {
		String[] tokens = line.split(" ");
		Instruction instr = new Instruction();
		instr.opcode = Integer.parseInt(tokens[0].trim());
		instr.inputA = Integer.parseInt(tokens[1].trim());
		instr.inputB = Integer.parseInt(tokens[2].trim());
		instr.registerC = Integer.parseInt(tokens[3].trim());
		return instr;
	}

	private Registers parseRegisters (String line) {
		Registers regs = new Registers();
		String sub = line.substring(9, line.length() - 1);
		String[] tokens = sub.split(",");

		regs.map.put(0, Integer.parseInt(tokens[0].trim()));
		regs.map.put(1, Integer.parseInt(tokens[1].trim()));
		regs.map.put(2, Integer.parseInt(tokens[2].trim()));
		regs.map.put(3, Integer.parseInt(tokens[3].trim()));

		return regs;
	}

	private List<Instruction> parseProgram (List<String> input) {
		List<Instruction> instructions = new ArrayList<>();
		boolean lastEmpty = false;
		boolean startFound = false;
		for (String line : input) {
			if (line.trim().isEmpty()) {
				if (lastEmpty) {
					startFound = true;
				}
				lastEmpty = true;
				continue;
			} else {
				lastEmpty = false;
			}
			if (!startFound) {
				continue;
			}

			instructions.add(parseInstruction(line));
		}
		return instructions;
	}

	private void buildOpcodes (List<Sample> samples) {
		while (sizeUnnumbered(Arrays.asList(Opcode.values())) > 0) {
			for (Sample s : samples) {
				List<Opcode> matching = unnumbered(findMatchingOpcodes(s));
				if (matching.size() == 1) {
					matching.get(0).number = s.instr.opcode;
				}
			}
		}
	}

	private List<Opcode> unnumbered (List<Opcode> input) {
		return input.stream().filter(opcode -> opcode.number < 0).collect(Collectors.toList());
	}

	private int sizeUnnumbered (List<Opcode> input) {
		return unnumbered(input).size();
	}

	private List<Sample> parseSamples (List<String> input) {
		Iterator<String> it = input.iterator();
		List<Sample> samples = new ArrayList<>();
		while (it.hasNext()) {
			String line = it.next();
			if (!line.startsWith("Before: ")) {
				continue;
			}

			Sample s = new Sample();
			s.before = parseRegisters(line);
			s.instr = parseInstruction(it.next());
			s.after = parseRegisters(it.next());
			samples.add(s);
		}
		return samples;
	}

}

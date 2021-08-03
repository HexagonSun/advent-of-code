package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.IntBinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day08 extends AdventOfCode {

	private static class Instruction {

		private final String register;
		private final Operation operation;
		private final int opAmount;
		private final String conditionRegister;
		private final ConditionOperation conditionOperation;
		private final int conditionAmount;

		private Instruction(String register, Operation operation, int opAmount,
						   String conditionRegister, ConditionOperation conditionOperation, int conditionAmount) {
			this.register= register;
			this.operation= operation;
			this.opAmount= opAmount;
			this.conditionRegister= conditionRegister;
			this.conditionOperation= conditionOperation;
			this.conditionAmount= conditionAmount;
		}

		private int apply(Map<String, Integer> state) {
			if (conditionApplies(state)) {
				return performOperation(state);
			}
			return 0;
		}

		private boolean conditionApplies(Map<String, Integer> state) {
			int value= state.getOrDefault(conditionRegister, 0);
			return conditionOperation.operation.test(value, conditionAmount);
		}

		private int performOperation(Map<String, Integer> state) {
			int value= state.getOrDefault(register, 0);
			int result= operation.function.applyAsInt(value, opAmount);
			state.put(register, result);
			return result;
		}
	}

	private enum Operation {
		INC((x, y) -> x+y), DEC ((x, y) -> x-y);

		private final IntBinaryOperator function;

		Operation(IntBinaryOperator function) {
			this.function= function;
		}
	}

	private enum ConditionOperation {
		GREATER_THAN((l, r) -> (int)l > (int)r),
		GREATER_OR_EQUAL_THAN((l, r) -> (int)l >= (int)r),
		LESS_THAN((l, r) -> (int)l < (int)r),
		LESS_OR_EQUAL_THAN((l, r) -> (int)l <= (int)r),
		EQUAL((l, r) -> (int)l == (int)r),
		NOT_EQUAL((l, r) -> (int)l != (int)r);

		private final BiPredicate operation;

		ConditionOperation(BiPredicate op) {
			this.operation= op;
		}

		public static ConditionOperation of(String input) {
			switch (input) {
				case ">": return GREATER_THAN;
				case ">=": return GREATER_OR_EQUAL_THAN;
				case "<": return LESS_THAN;
				case "<=": return LESS_OR_EQUAL_THAN;
				case "==": return EQUAL;
				case "!=": return NOT_EQUAL;
				default: throw new IllegalArgumentException("Can't parse input " + input);
			}
		}
	}

	private static final Pattern REGEX_LINE= Pattern.compile("([a-z]+) (inc|dec) (-?\\d+) if ([a-z]+) (>|<|>=|==|<=|!=) (-?\\d+)");

	@Test
	@Override
	public void runTask1() {
		assertThat(solveTask1(getInputLines()), is(3880));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solveTask2(getInputLines()), is(5035));
	}

	@Test
	public void runExample1() {
		List<String> input= Arrays.asList("b inc 5 if a > 1",
				"a inc 1 if b < 5",
				"c dec -10 if a >= 1",
				"c inc -20 if c == 10");
		assertThat(solveTask1(input), is(1));
	}

	@Test
	public void runTask2Example1() {
		List<String> input= Arrays.asList("b inc 5 if a > 1",
				"a inc 1 if b < 5",
				"c dec -10 if a >= 1",
				"c inc -20 if c == 10");
		assertThat(solveTask2(input), is(10));
	}

	private int solveTask1(List<String> input) {
		Map<String, Integer> state= new HashMap<>();
		List<Instruction> instructions= parse(input);
		for (Instruction instr : instructions) {
			instr.apply(state);
		}
		return state.values().stream().mapToInt(i->i).max().orElse(-1);
	}

	private int solveTask2(List<String> input) {
		int max= 0;
		Map<String, Integer> state= new HashMap<>();
		List<Instruction> instructions= parse(input);
		for (Instruction instr : instructions) {
			int result= instr.apply(state);
			max= result > max ? result : max;
		}
		return max;
	}

	private List<Instruction> parse(List<String> input) {
		return input.stream()
					   .map(this::parse)
					   .collect(Collectors.toList());
	}

	private Instruction parse(String line) {
		Matcher m= REGEX_LINE.matcher(line);
		if (m.matches()) {
			String register= m.group(1);
			Operation operation= Operation.valueOf(m.group(2).toUpperCase());
			int opAmount= Integer.valueOf(m.group(3));
			String conditionRegister= m.group(4);
			ConditionOperation conditionOperation= ConditionOperation.of(m.group(5));
			int conditionAmount= Integer.valueOf(m.group(6));
			return new Instruction(register, operation, opAmount, conditionRegister, conditionOperation, conditionAmount);
		} else {
			throw new IllegalArgumentException("Couldn't parse input line " + line);
		}
	}

}

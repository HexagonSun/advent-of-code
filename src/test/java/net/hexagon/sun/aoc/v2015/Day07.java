package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/7
public class Day07 extends AdventOfCode {

	private enum Operator { ASSIGNMENT, AND, LSHIFT, NOT, OR, RSHIFT }
	private enum State { INITIAL, INPUT_WAIT, FILLED }

	private class Operation {

		private final Operator operator;
		private final String x;
		private final String y;
		private final String target;
		private State visitState;
		
		public Operation (Operator op, String inputX, String inputY, String target) {
			this.operator= op;
			this.x= inputX;
			this.y= inputY;
			this.target= target;
			this.visitState = State.INITIAL;
		}

		/** for unary operations */
		public Operation (Operator op, String input, String target) {
			this(op, input, input, target);
		}

		/** apply the operator of this element to the inputs held by "visitState" */
		public Set<String> apply(final Map<String, Integer> graph) {
			Integer graphX = getX(graph);
			Integer graphY = getY(graph);
			boolean hasInput = graphX != null && graphY != null;
			if (!hasInput) {
				// we're waiting one one or more inputs
				this.visitState = State.INPUT_WAIT;
				return Collections.emptySet();
			}

			final Integer result;
			switch (operator) {
				case ASSIGNMENT:
					// direct wire connection or literal integer value
					result= graphX;
					break;
				case AND:
					result = graphX & graphY;
					break;
				case LSHIFT:
					result = graphX << graphY;
					break;
				case NOT:
					result = ~graphX;
					break;
				case OR:
					result = graphX | graphY;
					break;
				case RSHIFT:
					// fall through
				default:
					result = graphX >> graphY;
					break;
			}
			return processResult(graph, result);
		}

		private Set<String> processResult(final Map<String, Integer> graph, Integer result) {
			graph.put(this.target, clamp(result));
			this.visitState = State.FILLED;

			Set<String> s= new HashSet<>();
			s.add(this.target);
			return s;
		}

		private Integer clamp(Integer value) {
			// TODO: do we really need to clamp?
			if (value < 0 || value > 65535) {
//				System.out.println("********* CLAMP NEEDED ********");
			}
			return value;
		}

		private Integer getX(Map<String, Integer> graph) {
			return get(graph, this.x);
		}
		private Integer getY(Map<String, Integer> graph) {
			return get(graph, this.y);
		}
		private Integer get(Map<String, Integer> graph, String wire) {
			try {
				Integer number= Integer.valueOf(wire);
				if (number != null) {
					return number;
				}
			} catch (NumberFormatException nfe) {
				// ignore
			}
			return graph.get(wire);
		}

		@Override
		public String toString() {
			if (Operator.ASSIGNMENT == operator) {
				return "[ " + x + " -> " + target + " ]" ;
			} else if (Operator.NOT == operator) {
				return "[ " + operator + " " + x + " -> " + target + " ]" ;
			}
			return "[ " + x + " " + operator + " " + y + " -> " + target + " ]";
		}
	}

	@Test
	@Override
	public void runTask1 () {
		Map<String, Integer> graph= new HashMap<>();

		solveTask1(getInputLines(), graph);
		printState(graph);

		// solution
		assertThat(graph.get("a"), is(16076));
	}

	@Test
	public void runTask1_A () {
		Map<String, Integer> graph= new HashMap<>();
		solveTask1(getInputLines("./input/day07.a"), graph);
		printState(graph);

		// solution
		assertThat(graph.get("a"), is(956));
	}

	@Test
	@Override
	public void runTask2 () {
		Map<String, Integer> graph= new HashMap<>();
		solveTask1(getInputLines("./input/day07-task2"), graph);
		printState(graph);

		// solution
		assertThat(graph.get("a"), is(2797));
	}

	@Test
	public void runTask2_A () {
		Map<String, Integer> graph= new HashMap<>();
		solveTask1(getInputLines("./input/day07.a2"), graph);
		printState(graph);

		// solution
		assertThat(graph.get("a"), is(40149));
	}

	@Test
	public void runExample1() {
		Map<String, Integer> graph= new HashMap<>();
		solveTask1("123 -> x", graph);

		assertThat(graph.get("x"), is(123));
	}

	@Test
	public void runExample2() {
		Map<String, Integer> graph= new HashMap<>();
		List<String> input = Arrays.asList(
				"7 -> x",
				"4 -> y",
				"x AND y -> z"
		);
		solveTask1(input, graph);

		assertThat(graph.get("z"), is(4));

	}

	@Test
	public void runExample2Reversed() {
		// holds true in another order as well
		Map<String, Integer> graph= new HashMap<>();
		List<String> input = Arrays.asList(
				"x AND y -> z",
				"7 -> x",
				"4 -> y"
		);
		solveTask1(input, graph);

		assertThat(graph.get("z"), is(4));
	}

	@Test
	public void runExample3() {
		Map<String, Integer> graph= new HashMap<>();
		List<String> input = Arrays.asList(
				"1 -> p",
				"p LSHIFT 2 -> q"
		);
		solveTask1(input, graph);

		assertThat(graph.get("q"), is(1 << 2));
		assertThat(graph.get("q"), is(4));
	}

	@Test
	public void runExample4() {
		Map<String, Integer> graph= new HashMap<>();
		List<String> input = Arrays.asList(
				"5 -> e",
				"NOT e -> f"
		);
		solveTask1(input, graph);

		Integer value= ~5;
		assertThat(graph.get("f"), is(value));

		graph= new HashMap<>();
		input = Arrays.asList(
				"NOT e -> f",
				"5 -> e"
		);
		solveTask1(input, graph);

		assertThat(graph.get("f"), is(value));

	}

	private void printState(Map<String, Integer> graph) {
		System.out.println("State of graph:");
		for (Map.Entry<String, Integer> entry : graph.entrySet()) {
			System.out.println("Wire \"" + entry.getKey() + "\":\t\t" + entry.getValue());
		}
		System.out.println("-------------------------");
	}

	private void solveTask1(List<String> lines, Map<String, Integer> graph) {

		Set<Operation> operations= parse(lines);
		fillGraph(graph, operations);

	}

	private void fillGraph(Map<String, Integer> graph, Set<Operation> operations) {
		// TODO: proper partial fill
		// But: if it's ugly and works it's not ugly...
		for (int i = 0; i < 100; i++) {
			// Just iterate many many times & hope that all input is processed
			for (Operation op : operations) {
				op.apply(graph);
			}
		}
	}

	private Set<Operation> parse(List<String> lines) {
		Set<Operation> operations= new HashSet<>();
		for (String line : lines) {
			Operation operation= parse(line);
			operations.add(operation);
		}
		return operations;
	}

	private Operation parse(String line) {
		String[] tokens= line.split(" -> ");
		String lhs= tokens[0].trim();
		String target= tokens[1].trim();

		String[] inputs= lhs.split(" ");
		if (inputs.length == 1) {
			// assignment
			return new Operation(Operator.ASSIGNMENT, inputs[0], target);
		} else if (inputs.length == 2) {
			// unary operator
			if (Operator.NOT.name().equals(inputs[0].trim())) {
				String x= inputs[1].trim();
				return new Operation(Operator.NOT, x, target);
			} else {
				throw new UnsupportedOperationException("Parse error, NOT expected. Line is " + line + "\"");
			}
		} else {
			String x= inputs[0].trim();
			String op= inputs[1].trim();
			String y= inputs[2].trim();

			return new Operation(Operator.valueOf(op), x, y, target);
		}
	}

	private void solveTask1(final String input, Map<String, Integer> graph) {
		solveTask1(Arrays.asList(input), graph);
	}

}

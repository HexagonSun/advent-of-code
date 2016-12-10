package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/10
public class Day10 extends AdventOfCode {

	private static class Bot {

		private final int no;
		private Integer low;
		private Integer high;

		Bot(int no) {
			this.no= no;
		}

		void process(Instruction instruction) {
			if (instruction.type == Type.INITIAL) {
				addValue(instruction.value);
			}
		}

		boolean addValue(Integer value) {
			if (value == null) {
				return false;
			}

			if (low == null) {
				low= value;
				return true;
			}

			if (value < low) {
				high= low;
				low= value;
			} else {
				high= value;
			}
			return true;
		}

		Integer takeLow() {
			Integer value= low;
			low= null;
			return value;
		}

		Integer takeHigh() {
			Integer value= high;
			high= null;
			return value;
		}

		@Override
		public String toString() {
			return "Bot " + no + " | low: " + low + " | high: " + high;
		}
	}

	private enum Type {
		INITIAL, TASK
	}

	private class Instruction implements Comparable {
		final Type type;
		final int botNo;

		int value;
		boolean lowToOutput;
		int lowTargetNo;
		boolean highToOutput;
		int highTargetNo;

		Instruction(Type type, int value, int botNo) {
			this.type= type;
			this.value= value;
			this.botNo= botNo;
		}

		Instruction(Type type, int botNo, int lowTarget, int highTarget) {
			this.type= type;
			this.botNo= botNo;
			this.lowTargetNo= lowTarget;
			this.highTargetNo= highTarget;
		}

		void setOutput(int output) {
			if (output == 0) {
				lowToOutput= true;
			} else {
				highToOutput= true;
			}
		}

		@Override
		public int compareTo(Object o) {
			if (o == null) {
				return 1;
			}
			Instruction other= (Instruction)o;
			if (this.type != other.type) {
				return this.type == Type.INITIAL ? -1 : 1;
			}
			return Integer.compare(this.botNo, other.botNo);
		}

		@Override
		public String toString() {
			return "Type: " + type +
						   " | botNo " + botNo +
						   " | value " + value +
						   " | lowTarget " + lowTargetNo +
						   " | highTarget " + highTargetNo;
		}
	}

	@Test
	@Override
	public void runTask1 () {
		int solution= 73;
		assertThat(solveTask1(getInputLines(), 17, 61), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
	}

	@Test
	public void runExample1() {
		List<String> input= Arrays.asList(
				"value 5 goes to bot 2",
				"bot 2 gives low to bot 1 and high to bot 0",
				"value 3 goes to bot 1",
				"bot 1 gives low to output 1 and high to bot 0",
				"bot 0 gives low to output 2 and high to output 0",
				"value 2 goes to bot 2");
		assertThat(solveTask1(input, 2, 5), is(2));
	}


	@Test
	public void runTask2Example1() {

	}

	private int solveTask1(List<String> input, int needleLow, int needleHigh) {
		List<Instruction> instructions = input.stream()
												 .map(this::parse)
												 .sorted()
												 .collect(Collectors.toList());
//		System.out.println("got instructions: ");
//		for (Instruction i : instructions) {
//			System.out.println("\t" + i);
//		}

		// initial value assignment
		Map<Integer, Bot> bots= new HashMap<>();
		for (Instruction instruction : instructions) {
			if (instruction.type == Type.TASK) {
				continue;
			}
			Bot b= getBot(bots, instruction.botNo);
			b.process(instruction);
		}

//		System.out.println("After initial:");
//		for (Bot b : bots.values()) {
//			System.out.println("\t" + b);
//		}

		// replay tasks
		Bot responsible= null;
		Map<Integer, Integer> outputs= new HashMap<>();
		boolean somethingChanged= true;
		while (somethingChanged) {
			boolean changeInLoop= false;
			for (Instruction instruction : instructions) {
				if (instruction.type == Type.INITIAL) {
					continue;
				}

				Bot b= getBot(bots, instruction.botNo);
				if (b.low == null || b.high == null) {
					// not two values: skip
					continue;
				}

				if (b.low == needleLow && b.high == needleHigh) {
					responsible= b;
				}

				if (instruction.lowToOutput) {
					Integer lowOutput= outputs.getOrDefault(instruction.lowTargetNo, 0);
					Integer lowValue=  b.takeLow();
					if (lowValue != null) {
						lowOutput+= lowValue;
						changeInLoop|= true;
					}
					outputs.put(instruction.lowTargetNo, lowOutput);
				} else {
					Bot lowTarget = getBot(bots, instruction.lowTargetNo);
					changeInLoop |= lowTarget.addValue(b.takeLow());
				}


				if (instruction.highToOutput) {
					Integer highOutput= outputs.getOrDefault(instruction.highTargetNo, 0);
					Integer highValue=  b.takeHigh();
					if (highValue != null) {
						highOutput+= highValue;
						changeInLoop|= true;
					}
					outputs.put(instruction.highTargetNo, highOutput);
				} else {
					Bot highTarget = getBot(bots, instruction.highTargetNo);
					changeInLoop|= highTarget.addValue(b.takeHigh());
				}
			}

			somethingChanged&= changeInLoop;
		}

//		System.out.println("all done");
//		for (Map.Entry<Integer, Integer> entry : outputs.entrySet()) {
//			System.out.println("\tOutput " + entry.getKey() + ": " + entry.getValue());
//		}
//		for (Bot b : bots.values()) {
//			System.out.println("\t" + b);
//		}
		System.out.println("Responsible Bot for needle value was: " + responsible);
		return responsible == null ? -1 : responsible.no;
	}

	private int solveTask2(List<String> input) {
		return -1;
	}

	private Bot getBot(Map<Integer, Bot> bots, int no) {
		Bot bot= bots.getOrDefault(no, new Bot(no));
		bots.put(no, bot);
		return bot;
	}

	private Instruction parse(String line) {
		String[] tokens = line.split(" ");
		if ("value".equals(tokens[0])) {
			return new Instruction(Type.INITIAL, asInt(tokens[1]), asInt(tokens[5]));
		} else {
//				1			    5      6             10  11
//			bot 87 gives low to output 3 and high to bot 163
			Instruction instr = new Instruction(Type.TASK, asInt(tokens[1]), asInt(tokens[6]), asInt(tokens[11]));
			if ("output".equals(tokens[5])) {
				instr.setOutput(0);
			}
			if ("output".equals(tokens[10])) {
				instr.setOutput(1);
			}
			return instr;
		}
	}

	private int asInt(String s) {
		return Integer.parseInt(s);
	}

}

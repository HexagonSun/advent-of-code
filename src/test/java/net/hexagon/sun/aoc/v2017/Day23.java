package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day23 extends AdventOfCode {

	private enum Instruction {
		set (Instruction::set),
		sub (Instruction::sub),
		mul (Instruction::mul),
		jnz (Instruction::jnz);

		private final BiFunction<Map<String, Long>, Command, Long> function;

		Instruction (BiFunction<Map<String, Long>, Command, Long> function) {
			this.function= function;
		}

		public Long apply (Map<String, Long> registers, Command command) {
			return this.function.apply(registers, command);
		}

		private static Long set (Map<String, Long> registers, Command command) {
			Long value= asInt(registers, command.valueX, command);
			registers.put(command.registerX, value);
			return null;
		}

		private static Long sub (Map<String, Long> registers, Command command) {
			Long existing= asInt(registers, command.registerX, command);
			Long other= asInt(registers, command.valueX, command);
			registers.put(command.registerX, existing - other);
			return null;
		}

		private static Long mul (Map<String, Long> registers, Command command) {
			Long existing= asInt(registers, command.registerX, command);
			Long other= asInt(registers, command.valueX, command);
			registers.put(command.registerX, existing * other);
			return null;
		}

		private static Long jnz (Map<String, Long> registers, Command command) {
			if (asInt(registers, command.registerX, command) != 0) {
				return asInt(registers, command.valueX, command);
			}
			return null;
		}

		public static Long asInt (Map<String, Long> registers, String key, Command command) {
			try {
				return Long.parseLong(key);
			} catch (NumberFormatException nfe) {
				return registers.getOrDefault(key, command.defaultValue);
			}
		}
	}

	private static class Command {
		final String registerX;
		final String valueX;
		final Instruction instruction;
		private Long defaultValue;

		Command (Instruction instruction, String regX, String valX) {
			this.instruction= instruction;
			this.registerX= regX;
			this.valueX= valX;
		}

		@Override
		public String toString() {
			return this.instruction + " regX=" + registerX + " valX=" + valueX;
		}

		public Long apply (Map<String, Long> registers, int defaultValue) {
			this.defaultValue= (long)defaultValue;
			return this.instruction.apply(registers, this);
		}
	}

	@Test
	@Override
	public void runTask1() {
		assertThat(solveTask1(getInputLines()), is(6241));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(simulatePartTwo(), is(909));
	}

	private int solveTask1 (List<String> input) {
		List<Command> commands = input.stream()
										 .map(this::parse)
										 .collect(Collectors.toList());
		Map<String, Long> registers= new HashMap<>();
		int mulCount= 0;
		for (int i = 0; i < commands.size(); i++) {
			Command c= commands.get(i);
			Long result= c.apply(registers, 0);

			if (Instruction.mul == c.instruction) {
				mulCount++;
			} else if (Instruction.jnz == c.instruction && result != null) {
				int nextI= i - 1 + result.intValue();
				if (nextI < 0 || nextI >= commands.size()) {
					break;
				}
				i= nextI;
			}
		}
		return mulCount;
	}

	private Command parse(String line) {
		String[] tokens= line.split(" ");
		Instruction instruction= Instruction.valueOf(tokens[0]);
		return new Command(instruction, tokens[1], tokens[2]);
	}

	private int simulatePartTwo() {
		int b= (81 * 100) + 100000;
		int c= b + 17000;
		int g;
		int h= 0;

		do {
			for (int d= 2; d < b; d++) {
				if (b % d == 0) {
					h++;
					break;
				}
			}
			g= b - c;
			b= b + 17;
		} while (g != 0);

		return h;
	}

}



//	f= 1;
//	d= 2;
//
//	// each number d between 2 and b
//	do {
//	e= 2;
//	// each number e between 2 and b
//	do {
//	g= (d * e) - b;
//	if (g == 0) {
//	f = 0;
//	}
//
//	e++;
//	} while (e - b != 0);
//
//	d++;
//	} while (d - b != 0);




// -----------------------------------------------------------------
// -----------------------------------------------------------------



//			after:
//			// each number d between 2 and b
//			do {
//				e= 2;
//				// each number e between 2 and b
//				do {
////					b % (d*e) == 0;
////					d * e - b == 0
////					->
////						d * e == b;
////					-> if there is a divisor, increase h!
//
//					g= (d * e) - b;
//					if (g == 0) {
//						f = 0;
//
//						// set d, so we will skip the outer do-while loop
//						d= b;
//						// we can break, because once f is set to 0, h will be increased.
//						break after;
//					}
//
//					e++;
//				} while (e - b != 0);	// e < b
//
//				d++;
//			} while (d - b != 0);	// d < b
//

//
//			if (f == 0) {
//				h++;
//			}

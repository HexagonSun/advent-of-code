package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day13 extends AdventOfCode {

	private static class Layer {
		int range;
		int scannerIndex;
		boolean isUp;

		Layer(int range) {
			this.range= range;
		}

		Layer(Layer layer) {
			this(layer.range);
			scannerIndex= layer.scannerIndex;
			isUp= layer.isUp;
		}

		int getSeverity(int depth, boolean partTwo) {
			if (scannerIndex == 0) {
				if (partTwo) {
					// non-zero cost for getting caught
					return (depth + 1) * (range + 1);
				} else {
					return depth * range;
				}
			}
			return 0;
		}

		void tickScanner() {
			if (scannerIndex == (range - 1)) {
				isUp= true;
			} else if (scannerIndex == 0) {
				isUp= false;
			}

			if (isUp) {
				scannerIndex--;
			} else {
				scannerIndex++;
			}
		}
	}

	@Test
	@Override
	public void runTask1() {
		assertThat(solveTask1(getInputLines()), is(1624));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solveTask2(getInputLines()), is(3923436));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1(Arrays.asList("0: 3",
											"1: 2",
											"4: 4",
											"6: 4")), is(24));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask2(Arrays.asList("0: 3",
				"1: 2",
				"4: 4",
				"6: 4")), is(10));
	}

	private int solveTask1(List<String> input) {
		Layer[] firewall= buildFirewall(input);
		return runFirewall(firewall, false);
	}

	private int solveTask2(List<String> input) {
		Layer[] firewall= buildFirewall(input);
		int delay= 1;
		while(true) {
			tickScanners(firewall);
			Layer[] runningCopy= copy(firewall);

			if (runFirewall(runningCopy, true) == 0) {
				break;
			}
			delay++;
		}
		return delay;
	}

	private int runFirewall(Layer[] firewall, boolean partTwo) {
		int severity= 0;
		for (int i= 0; i < firewall.length; i++) {
			// packet @ idx i
			severity+= getSeverity(firewall, i, partTwo);

			tickScanners(firewall);
		}
		return severity;
	}

	private int getSeverity(Layer[] fw, int depth, boolean partTwo) {
		if (fw[depth] != null) {
			return fw[depth].getSeverity(depth, partTwo);
		}
		return 0;
	}

	private void tickScanners(Layer[] firewall) {
		for (Layer l : firewall) {
			if (l != null) {
				l.tickScanner();
			}
		}
	}

	private Layer[] buildFirewall(List<String> input) {
		Map<Integer, Integer> definition= parse(input);
		int max= definition.keySet().stream().mapToInt(i -> i).max().orElse(0);
		Layer[] fw= new Layer[max + 1];
		for(Map.Entry<Integer, Integer> entry : definition.entrySet()) {
			fw[entry.getKey()]= new Layer(entry.getValue());
		}
		return fw;
	}

	private Map<Integer, Integer> parse(List<String> input) {
		Map<Integer, Integer> map= new HashMap<>();
		for (String line : input) {
			String[] tokens= line.split(": ");
			map.put(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
		}
		return map;
	}

	private Layer[] copy(Layer[] firewall) {
		Layer[] copy= new Layer[firewall.length];
		for (int i = 0; i < firewall.length; i++) {
			Layer l= firewall[i];
			if (l != null) {
				copy[i]= new Layer(firewall[i]);
			}
		}
		return copy;
	}

}

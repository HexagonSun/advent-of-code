package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day12 extends AdventOfCode {

	@Test
	@Override
	public void runTask1() {
		assertThat(solveTask1(getInputLines()), is(239));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solveTask2(getInputLines()), is(215));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1(Arrays.asList("0 <-> 2",
									  "1 <-> 1",
									  "2 <-> 0, 3, 4",
									  "3 <-> 2, 4",
									  "4 <-> 2, 3, 6",
									  "5 <-> 6",
									  "6 <-> 4, 5")), is(6));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask2(Arrays.asList("0 <-> 2",
				"1 <-> 1",
				"2 <-> 0, 3, 4",
				"3 <-> 2, 4",
				"4 <-> 2, 3, 6",
				"5 <-> 6",
				"6 <-> 4, 5")), is(2));
	}

	private int solveTask1(List<String> input) {
		Map<Integer, List<Integer>> connections= parse(input);
		Set<Integer> visited= new HashSet<>();
		processReachable(visited, connections, 0);
		return visited.size();
	}

	private void processReachable(Set<Integer> visited, Map<Integer, List<Integer>> connections, Integer id) {
		boolean newlyAdded= visited.add(id);
		if (!newlyAdded) {
			return;
		}

		List<Integer> reachable= connections.get(id);
		for (Integer i : reachable) {
			processReachable(visited, connections, i);
		}
	}


	private Map<Integer, List<Integer>> parse(List<String> input) {
		Map<Integer, List<Integer>> connections= new HashMap<>();

		for (String line : input) {
			String[] tokens= line.split("<->");
			Integer from= Integer.parseInt(tokens[0].trim());

			List<Integer> to= new ArrayList<>();
			String[] toTokens= tokens[1].trim().split(",");
			for (String j : toTokens) {
				to.add(Integer.parseInt(j.trim()));
			}

			connections.put(from, to);
		}
		return connections;
	}

	private int solveTask2(List<String> input) {
		Map<Integer, List<Integer>> connections= parse(input);
		int nbGroups= 0;
		Set<Integer> total= new HashSet<>(connections.keySet());
		Integer currentID= 0;

		while (true) {
			Set<Integer> visited= new HashSet<>();
			processReachable(visited, connections, currentID);

			total.removeAll(visited);
			nbGroups++;
			if (total.isEmpty()) {
				break;
			}
			currentID= total.iterator().next();
		}
		return nbGroups;
	}

}

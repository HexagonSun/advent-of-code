package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day06 extends AdventOfCode {

	@Test
	@Override
	public void runTask1() {
		assertThat(solve(getInputAsString(), false), is(3156));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solve(getInputAsString(), true), is(1610));
	}

	@Test
	public void runExample1() {
		String input= "0\t2\t7\t0";
		assertThat(solve(input, false), is(5));
	}

	@Test
	public void runTask2Example1() {
		String input= "0\t2\t7\t0";
		assertThat(solve(input, true), is(4));
	}

	private int solve(String input, boolean task2) {
		int[] banks= toIntArray(input);
		Map<List<Integer>, Integer> states= new HashMap<>();
		states.put(asList(banks), 0);

		int steps= 0;
		while(true) {
			steps++;
			banks= redistribute(banks);
			if (states.keySet().contains(asList(banks)) || steps > 10000) {
				if (task2) {
					return steps - states.get(asList(banks));
				} else {
					return steps;
				}
			}
			states.put(asList(banks), steps);
		}
	}

	private List<Integer> asList(int[] banks) {
		List<Integer> l= new ArrayList<>(banks.length);
		for (int bank : banks) {
			l.add(bank);
		}
		return l;
	}

	private int[] toIntArray(String input) {
		return Arrays.stream(input.split("\t"))
					   .mapToInt(Integer::valueOf)
					   .toArray();
	}

	private int[] redistribute(int[] previous) {
		// len is 16 for input data
		int len= previous.length;
		int[] banks= Arrays.copyOf(previous, len);

		int maxIndex= maxIndex(previous);
		int value= previous[maxIndex];

//		System.out.println("\tgot value: " + value + " @ idx " + maxIndex);
		banks[maxIndex]= 0;
		while (value > 0) {
			int idxToAdd= (maxIndex + 1) % len;
			banks[idxToAdd]++;
			value--;
			maxIndex++;
		}
		return banks;
	}

	private int maxIndex(int[] previous) {
		int idx= 0;
		int max= Integer.MIN_VALUE;
		for (int i = 0; i < previous.length; i++) {
			if (previous[i] > max) {
				max= previous[i];
				idx= i;
			}
		}
		return idx;
	}

}

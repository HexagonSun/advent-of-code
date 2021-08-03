package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/2
public class Day02 extends AdventOfCode {

	@Test
	@Override
	public void runTask1 () {
		List<String> lines = getInputLines();
		int total= solveTask1(lines);
		System.out.println(total);

		// solution
		assertThat(total, is(1586300));
	}

	@Test
	@Override
	public void runTask2 () {
		List<String> lines = getInputLines();
		int total= solveTask2(lines);
		System.out.println(total);

		// solution
		assertThat(total, is(3737498));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1("2x3x4"), is(58));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask1("1x1x10"), is(43));
	}

	@Test
	public void runTask2Example1() {
		assertThat(solveTask2("2x3x4"), is(34));
	}

	@Test
	public void runTask2Example2() {
		assertThat(solveTask2("1x1x10"), is(14));
	}


	private int solveTask1(List<String> lines) {
		return lines
				   .stream()
				   .mapToInt(line -> solveTask1(line)).sum();
	}

	private int solveTask2(List<String> lines) {
		return lines
					   .stream()
					   .mapToInt(line -> solveTask2(line)).sum();
	}

	private int solveTask1(String line) {
		int[] lwh= parse(line);
		int l= lwh[0];
		int w= lwh[1];
		int h= lwh[2];

		int size= 2*l*w + 2*w*h + 2*h*l;
		int slack= Integer.min(l*w, Integer.min(w*h, h*l));

		return size + slack;
	}

	private int solveTask2(String line) {
		List<Integer> lwh= parseAsList(line);
		PriorityQueue<Integer> pq= new PriorityQueue<>(lwh);

		int smallest= pq.poll();
		int nextLargest= pq.poll();

		int sides = 2 * smallest + 2 * nextLargest;
		int bow = lwh.get(0) * lwh.get(1) * lwh.get(2);

		return sides + bow;
	}

	private List<Integer> parseAsList(String line) {
		List<Integer> dimensions= new ArrayList<>();
		String[] tokens = line.split("x");
		for (String i : tokens) {
			dimensions.add(Integer.valueOf(i));
		}
		return dimensions;
	}

	private int[] parse(String line) {
		String[] tokens = line.split("x");
		return new int[] {
				Integer.parseInt(tokens[0]),
				Integer.parseInt(tokens[1]),
				Integer.parseInt(tokens[2]),
		};
	}

}

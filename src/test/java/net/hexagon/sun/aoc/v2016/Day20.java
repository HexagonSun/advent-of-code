package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/20
public class Day20 extends AdventOfCode {

	private static class Interval implements Comparable<Interval> {
		final long from;
		final long to;

		Interval(long from, long to) {
			this.from= from;
			this.to= to;
		}

		@Override
		public int compareTo(Interval other) {
			if (this == other) {
				return 0;
			}
			if (this.from != other.from) {
				return Long.compare(this.from, other.from);
			}
			return Long.compare(this.to, other.to);
		}

		@Override
		public String toString() {
			return "[ " + from + " , " + to + " ]";
		}
	}

	@Test
	@Override
	public void runTask1 () {
		long solution= 19449262L;
		assertThat(solveTask1(getInputLines(), 4_294_967_295L), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
		long solution= 119L;
		assertThat(solveTask2(getInputLines(), 4_294_967_295L), is(solution));
	}

	@Test
	public void runExample1() {
		List<String> input= Arrays.asList(
				"5-8",
				"0-2",
				"4-7");
		assertThat(solveTask1(input, 9), is(3L));
	}

	@Test
	public void runExample2() {
		List<String> input= Arrays.asList(
				"3-6",
				"0-5",
				"8-9");
		assertThat(solveTask1(input, 9), is(7L));
	}

	@Test
	public void runTask2Example1() {
		List<String> input= Arrays.asList(
				"5-8",
				"0-2",
				"4-7");
		assertThat(solveTask2(input, 9), is(2L));
	}

	@Test
	public void runTask2Example2() {
		List<String> input= Arrays.asList(
				"3-6",
				"0-5",
				"8-9");
		assertThat(solveTask2(input, 9), is(1L));
	}

	private long solveTask1(List<String> input, long maxIp) {
		List<Interval> data = parse(input);
		for (long ip = 0L; ip < maxIp; ip++) {
			for (Interval iv : data) {
				if (ip < iv.from) {
					return ip;
				} else {
					ip = iv.to + 1;
				}
			}

		}
		return -1L;
	}

	private long solveTask2(List<String> input, long maxIp) {
		List<Interval> data = parse(input);
		long cnt= 0;
		for (long ip = 0L; ip <= maxIp; ip++) {
			boolean counted= false;
			for (Interval iv : data) {
				if (ip < iv.from) {
					cnt++;
					counted = true;
					break;
				} else if (ip >= iv.from && ip <= iv.to) {
					ip = iv.to + 1;
				}
			}
			if (!counted && ip <= maxIp) {
				cnt++;
			}
		}
		return cnt;
	}

	private List<Interval> parse(List<String> input) {
		List<Interval> parsed = input.stream().map(line -> {
			String[] tokens = line.split("-");
			return new Interval(Long.parseLong(tokens[0]), Long.parseLong(tokens[1]));
		}).collect(Collectors.toList());
		Collections.sort(parsed);
		return parsed;
	}

}

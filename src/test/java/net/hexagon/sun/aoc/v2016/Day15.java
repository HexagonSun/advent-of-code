package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/15
public class Day15 extends AdventOfCode {

	private static class Disc {
		final int nbPositions;
		final int initialPosition;

		public Disc(int nbPositions, int initialPosition) {
			this.nbPositions= nbPositions;
			this.initialPosition= initialPosition;
		}

		@Override
		public String toString() {
			return "[" + nbPositions + "] initial pos: " + initialPosition;
		}

		public int getPosition(int time) {
			return (initialPosition + time) % nbPositions;
		}
	}

	@Test
	@Override
	public void runTask1 () {
		int solution= 122318;
		assertThat(solve(getInputLines()), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
		int solution= 3208583;
		List<String> input = getInputLines();
		input.add("Disc #7 has 11 positions; at time=0, it is at position 0.");
		assertThat(solve(input), is(solution));
	}

	@Test
	public void runExample1() {
		List<String> input = Arrays.asList(
				"Disc #1 has 5 positions; at time=0, it is at position 4.",
				"Disc #2 has 2 positions; at time=0, it is at position 1.");
		int solution= 5;
		assertThat(solve(input), is(solution));
	}

	private int solve(List<String> input) {
		List<Disc> discs= parse(input);
		System.out.println("got discs: ");
		print(discs);

		int time = 0;
		while(true) {
			boolean atZero= true;

			for (int i = 0; i < discs.size(); i++) {
				int atPos= discs.get(i).getPosition(time + i);
				atZero&= atPos == 0;
			}

			if (atZero) {
				System.out.println("Match @ time " + time);
				for (int i = 0; i < discs.size(); i++) {
					int t= time + i;
					int atPos= discs.get(i).getPosition(t);
					int size = discs.get(i).nbPositions;
					System.out.println("["+ t +"] Disc ["+i+"] of size "+ size +" is at position " + atPos);
				}

				// button press ist at t-1
				return time - 1;
			}
			time++;
		}
	}

	private void print(List<Disc> discs) {
		for (Disc d : discs) {
			System.out.println("\t" + d);
		}
	}

	private List<Disc> parse(List<String> input) {
		return input.stream().map(this::parse).collect(Collectors.toList());
	}

	private Disc parse(String line) {
		String[] tokens = line.split(" ");
		return new Disc(
				Integer.parseInt(tokens[3]),
			    Integer.parseInt(tokens[11].substring(0, tokens[11].length() - 1)));
	}

}

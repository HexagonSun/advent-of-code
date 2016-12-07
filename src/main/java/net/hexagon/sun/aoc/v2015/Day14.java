package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/14
public class Day14 extends AdventOfCode {

	private static class Reindeer {
		private int speed;
		private int runDuration;
		private int restDuration;

		private int totalDistance= 0;
		private int points= 0;

		private void raceFor(int seconds) {
			int quotient= seconds / (runDuration + restDuration);
			totalDistance+= quotient * runDuration * speed;

			int remainder= seconds % (runDuration + restDuration);
			if (remainder < runDuration) {
				totalDistance+= remainder * speed;
			} else {
				totalDistance+= runDuration * speed;
			}
		}

		private void step(int raceSeconds) {
			int remainder= raceSeconds % (runDuration + restDuration);
			if (remainder < runDuration) {
				// run Rudolph, run!
				totalDistance+= speed;
			}
		}
	}

	private static final Pattern PATTERN= Pattern.compile("(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.");

	@Test
	@Override
	public void runTask1() {
		List<String> input= getInputLines();
		assertThat(solveTask1(input, 2503), is(2696));
	}

	@Test
	public void runTask1ByStepping() {
		// re-tests task 1, but by stepping through
		List<String> input= getInputLines();
		List<Reindeer> deers= parse(input);
		assertThat(simulateStepsMax(deers, 2503), is(2696));
	}

	@Test
	public void runExample1() {
		List<String> input= Arrays.asList(
				"Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.",
				"Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.");
		assertThat(solveTask1(input, 1000), is(1120));
	}

	@Test
	@Override
	public void runTask2() {
		List<String> input= getInputLines();
		assertThat(solveTask2(input, 2503), is(1084));
	}

	@Test
	public void runTask2Example1() {
		List<String> input= Arrays.asList(
				"Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.",
				"Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.");
		assertThat(solveTask2(input, 1000), is(689));
	}

	@Test
	public void runTask2Example2() {
		List<String> input= Arrays.asList(
				"Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.",
				"Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.");
		assertThat(solveTask2(input, 140), is(139));
	}

	private int solveTask1(List<String> input, int raceDuration) {
		List<Reindeer> deers= parse(input);
		return simulate(deers, raceDuration);
	}

	private int solveTask2(List<String> input, int raceDuration) {
		List<Reindeer> deers= parse(input);
		return simulateStepsMax(deers, raceDuration);
	}

	private int simulate(List<Reindeer> deers, int raceDuration) {
		for(Reindeer r : deers) {
			r.raceFor(raceDuration);
		}
		return getMaxDistance(deers);
	}

	private int simulateStepsMax(List<Reindeer> deers, int raceDuration) {
		return simulateSteps(deers, raceDuration, true);
	}

	private int simulateSteps(List<Reindeer> deers, int raceDuration, boolean awardPoints) {
		for (int i = 0; i < raceDuration; i++) {
			for(Reindeer r : deers) {
				r.step(i);
			}
			if (awardPoints) {
				getMaxDeer(deers).points++;
			}
		}

		if (awardPoints) {
			return getMaxPoints(deers);
		} else {
			return getMaxDistance(deers);
		}
	}

	private Reindeer getMaxDeer(List<Reindeer> deers) {
		Reindeer maxDeer= deers.get(0);
		for(Reindeer r : deers) {
			if (r.totalDistance > maxDeer.totalDistance) {
				maxDeer= r;
			}
		}
		return maxDeer;
	}

	private int getMaxDistance(List<Reindeer> deers) {
		return max(deers, true);
	}

	private int getMaxPoints(List<Reindeer> deers) {
		return max(deers, false);
	}

	private int max(List<Reindeer> deers, boolean checkDistance) {
		int max= Integer.MIN_VALUE;
		for (Reindeer r : deers) {
			if (checkDistance) {
				max = Math.max(max, r.totalDistance);
			} else {
				// points
				max = Math.max(max, r.points);
			}
		}
		return max;
	}

	private List<Reindeer> parse(List<String> lines) {
		List<Reindeer> l= new ArrayList<>(lines.size());
		for (String line : lines) {
			l.add(parse(line));
		}
		return l;
	}

	private Reindeer parse(String line) {
		Matcher m= PATTERN.matcher(line);
		if (m.matches()) {
			Reindeer r= new Reindeer();
			r.speed= Integer.parseInt(m.group(2));
			r.runDuration = Integer.parseInt(m.group(3));
			r.restDuration = Integer.parseInt(m.group(4));
			return r;
		}
		return null;
	}
}

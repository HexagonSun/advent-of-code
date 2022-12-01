package net.hexagon.sun.aoc.v2018;

import javafx.geometry.Point3D;
import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day23 extends AdventOfCode {

	private static class Bot {
		Point3D location;
		int radius;

		@Override
		public String toString () {
			return location + " | " + radius;
		}
	}

	private static final Pattern PATTERN = Pattern.compile("pos=<(-?\\d+),(-?\\d+),(-?\\d+)>, r=(\\d+)");

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(getInputLines()), is(640));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(getInputLines()), is(-1));
	}

	private List<String> sampleInput = Arrays.asList(
			"pos=<0,0,0>, r=4",
			"pos=<1,0,0>, r=1",
			"pos=<4,0,0>, r=3",
			"pos=<0,2,0>, r=1",
			"pos=<0,5,0>, r=3",
			"pos=<0,0,3>, r=1",
			"pos=<1,1,1>, r=1",
			"pos=<1,1,2>, r=1",
			"pos=<1,3,1>, r=1");

	@Test
	public void runExample1 () {
		assertThat(solveTask1(sampleInput), is(7));
	}


	private int solveTask2 (List<String> input) {
		return -1;
	}

	private int solveTask1 (List<String> input) {
		List<Bot> bots = parse(input);
		Bot b = strongest(bots);
//		System.out.println("maxBot is: " + b);
		List<Bot> inRange = getInRange(b, bots);

//		System.out.println("InRange: ");
//		for (Bot b2 : inRange) {
//			int dist = manhattanDistance(b, b2);
//			System.out.println("\t" + b2 + " --> " + dist);
//		}
		return inRange.size();
	}

	private Bot strongest (List<Bot> bots) {
		Bot maxBot = null;
		int maxRadius = Integer.MIN_VALUE;
		for (Bot b : bots) {
			if (b.radius > maxRadius) {
				maxRadius = b.radius;
				maxBot = b;
			}
		}
		return maxBot;
	}

	private List<Bot> getInRange (Bot source, List<Bot> bots) {
		List<Bot> inRange = new ArrayList<>();
		for (Bot b : bots) {
			int dist = manhattanDistance(source, b);
			if (dist <= source.radius) {
				inRange.add(b);
			}
		}
		return inRange;
	}

	private int manhattanDistance (Bot a, Bot b) {
		return  Math.abs((int)a.location.getX() - (int)b.location.getX())
			  + Math.abs((int)a.location.getY() - (int)b.location.getY())
			  + Math.abs((int)a.location.getZ() - (int)b.location.getZ());
	}

	private List<Bot> parse (List<String> input) {
		return input.stream().map(this::parse).collect(Collectors.toList());
	}

	private Bot parse (String line) {
		Matcher m = PATTERN.matcher(line);
		if (m.matches()) {
			int x = Integer.parseInt(m.group(1));
			int y = Integer.parseInt(m.group(2));
			int z = Integer.parseInt(m.group(3));
			int r = Integer.parseInt(m.group(4));
			Bot b = new Bot();
			b.location = new Point3D(x, y, z);
			b.radius = r;
			return b;
		}
		throw new IllegalStateException("Failed to parse line " + line);
	}

}

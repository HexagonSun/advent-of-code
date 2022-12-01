package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day06 extends AdventOfCode {

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(getInputLines()), is(4166));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(getInputLines(), 10000), is(42250));
	}

	@Test
	public void runExample1 () {
		assertThat(solveTask1(Arrays.asList("1, 1",
									  "1, 6",
									  "8, 3",
									  "3, 4",
									  "5, 5",
									  "8, 9")), is(17));
	}

	@Test
	public void runTask2Example1 () {
		assertThat(solveTask2(Arrays.asList("1, 1",
				"1, 6",
				"8, 3",
				"3, 4",
				"5, 5",
				"8, 9"), 32), is(16));
	}

	private int solveTask1 (List<String> input) {
		List<Point> coords = input.stream().map(this::parse).collect(Collectors.toList());

		translateAll(coords, 500);

		Map<Point, Integer> byCoord900= runWithGridSize(coords, 1000, 1000);

		translateAll(coords, 600);
		Map<Point, Integer> byCoord1500= runWithGridSize(coords, 2000, 2000);
		translateAll(byCoord1500.keySet(), -600);

		// calc delta
		Map<Point, Integer> delta = new HashMap<>();
		for (Map.Entry<Point, Integer> entry : byCoord900.entrySet()) {
			Point p = entry.getKey();
			Integer sizeA = entry.getValue();

			// Point equality does not seem to work after translating them :(
			Map.Entry<Point, Integer> entry1500 = byCoord1500.entrySet().stream()
								  .filter(p2 -> p2.getKey().x == p.x && p2.getKey().y == p.y).findFirst().orElse(null);

			if (entry1500  != null && entry1500.getValue().equals(sizeA)) {
				delta.put(p, entry.getValue());
			}
		}

		int max= delta.values().stream().mapToInt(n -> n).max().orElse(-1);
		return max;
	}

	private void translateAll (Collection<Point> coords, int delta) {
		coords.forEach(p -> p.translate(delta, delta));
	}

	private Map<Point, Integer> runWithGridSize (List<Point> coords, int height, int width) {
		Map<Point, Integer> byCoord= new HashMap<>();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Point here = new Point(i, j);
				Point closest = closest(here, coords);
				if (closest != null) {
					Integer value = byCoord.getOrDefault(closest, 0);
					byCoord.put(closest, value + 1);
				}
			}
		}
//		print(grid, coords);
		return byCoord;
	}

	private void print (Point[][] grid, List<Point> coords) {
		for (int i = 0; i < grid.length; i++) {
			Point[] line = grid[i];
			for (int j = 0; j < line.length; j++) {
				Point here = line[j];
				int cIdx= coords.indexOf(here);

				char c = (char) ('a' + cIdx);
				if (cIdx < 0) {
					c= '.';
				}
				System.out.print(c);
			}
			System.out.println();
		}
	}

	private Point closest (Point here, List<Point> coords) {
		int minDist= Integer.MAX_VALUE;
		List<Point> minPoint= new ArrayList<>();
		for (Point c : coords) {
			int delta= Math.abs(here.x - c.x) + Math.abs(here.y - c.y);
			if (delta == minDist) {
				minPoint.add(c);
			} else  if (delta < minDist) {
				minDist= delta;
				minPoint= new ArrayList<>();
				minPoint.add(c);
			}
		}
		return minPoint.size() > 1 ? null : minPoint.get(0);
	}

	private Point parse (String line) {
		String[] tokens= line.split(", ");
		return new Point(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[0]));
	}

	private int solveTask2 (List<String> input, int limit) {
		List<Point> coords = input.stream().map(this::parse).collect(Collectors.toList());

		translateAll(coords, 500);
		return runTwo(coords, 1000, 1000, limit);
	}

	private int runTwo (List<Point> coords, int height, int width, int limit) {
		int regionSize = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Point here = new Point(i, j);
				int dist= distanceTo(here, coords, limit);
				if (dist > 0) {
					regionSize+= 1;
				}
			}
		}
		return regionSize;
	}

	private int distanceTo (Point here, List<Point> coords, int limit) {
		int dist= 0;
		for (Point c : coords) {
			dist += Math.abs(here.x - c.x) + Math.abs(here.y - c.y);
			if (dist >= limit) {
				return -1;
			}
		}
		return dist;
	}

}

package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day25 extends AdventOfCode {

	private static class Point4D {

		static int manhattanDistance (Point4D a, Point4D b) {
			return Math.abs(a.w - b.w) +
						   Math.abs(a.x - b.x) +
						   Math.abs(a.y - b.y) +
						   Math.abs(a.z - b.z);
		}

		int w, x, y, z;
		Constellation constellation = null;

		int manhattanDistance (Point4D other) {
			return manhattanDistance(this, other);
		}

		@Override
		public boolean equals (Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Point4D other = (Point4D) o;
			return w == other.w &&
						   x == other.x &&
						   y == other.y &&
						   z == other.z;
		}

		@Override
		public int hashCode () {
			return Objects.hash(w, x, y, z);
		}

		@Override
		public String toString () {
			return w + ", " + x + ", " + y + ", " + z;
		}
	}

	private static class Constellation {
		Set<Point4D> points = new HashSet<>();

		boolean isPartOf (Point4D p) {
			if (this == p.constellation) {
				System.out.println("already in same constellation");
				return true;
			}
			if (points.isEmpty()) {
				return true;
			}
			return points.stream().anyMatch(x -> x.manhattanDistance(p) <= 3);
		}
	}

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(getInputLines()), is(420));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(getInputLines()), is(-1));
	}

	@Test
	public void runExample1 () {
		List<String> input = Arrays.asList(
				"0,0,0,0",
				" 3,0,0,0",
				" 0,3,0,0",
				" 0,0,3,0",
				" 0,0,0,3",
				" 0,0,0,6",
				" 9,0,0,0",
				"12,0,0,0");
		assertThat(solveTask1(input), is(2));
	}

	@Test
	public void runExample1b () {
		List<String> input = Arrays.asList(
				"0,0,0,0",
				" 3,0,0,0",
				" 0,3,0,0",
				" 0,0,3,0",
				" 0,0,0,3",
				" 0,0,0,6",
				" 9,0,0,0",
				"12,0,0,0",
				"6,0,0,0");
		assertThat(solveTask1(input), is(1));
	}

	@Test
	public void runExample2 () {
		List<String> input = Arrays.asList(
				"-1,2,2,0",
				"0,0,2,-2",
				"0,0,0,-2",
				"-1,2,0,0",
				"-2,-2,-2,2",
				"3,0,2,-1",
				"-1,3,2,2",
				"-1,0,-1,0",
				"0,2,1,-2",
				"3,0,0,0");
		assertThat(solveTask1(input), is(4));
	}

	@Test
	public void runExample3 () {
		List<String> input = Arrays.asList(
				"1,-1,0,1",
				"2,0,-1,0",
				"3,2,-1,0",
				"0,0,3,1",
				"0,0,-1,-1",
				"2,3,-2,0",
				"-2,2,0,0",
				"2,-2,0,-1",
				"1,-1,0,-1",
				"3,2,0,2");
		assertThat(solveTask1(input), is(3));
	}

	@Test
	public void runExample4 () {
		List<String> input = Arrays.asList(
				"1,-1,-1,-2",
				"-2,-2,0,1",
				"0,2,1,3",
				"-2,3,-2,1",
				"0,2,3,-2",
				"-1,-1,1,-2",
				"0,-2,-1,0",
				"-2,2,3,-1",
				"1,2,2,0",
				"-1,-2,0,-2");
		assertThat(solveTask1(input), is(8));
	}

	@Test
	public void runTask2Example1 () {
//		List<String> input = Arrays.asList();
//		assertThat(solveTask2(input), is(-1));
	}

	private int solveTask1 (List<String> input) {
		List<Point4D> points = parse(input);
		List<Constellation> constellations = new ArrayList<>();
		for (Point4D p : points) {
			boolean found = false;
			Iterator<Constellation> it = constellations.iterator();
			while (it.hasNext()) {
				Constellation c = it.next();

				if (c.points.contains(p)) {
					// already present
					System.out.println("~~ already presentt");
					found = true;
					continue;
				}

				if (c.isPartOf(p)) {
					found = true;
					c.points.add(p);

					if (c != p.constellation && p.constellation != null) {
						// point already belongs to another constellation -> join them
						p.constellation.points.addAll(c.points);
						it.remove();
					} else {
						p.constellation = c;
					}
				}
			}

			if (!found) {
				// start a new constellation
				Constellation c = new Constellation();
				c.points.add(p);
				constellations.add(c);
			}
		}
		return constellations.size();
	}

	private int solveTask2 (List<String> input) {
		return -1;
	}

	private List<Point4D> parse (List<String> input) {
		return input.stream().map(this::parse).collect(Collectors.toList());
	}

	private Point4D parse (String line) {
		String[] tokens = line.split(",");
		Point4D p = new Point4D();
		p.w = Integer.parseInt(tokens[0].trim());
		p.x = Integer.parseInt(tokens[1].trim());
		p.y = Integer.parseInt(tokens[2].trim());
		p.z = Integer.parseInt(tokens[3].trim());
		return p;
	}

}

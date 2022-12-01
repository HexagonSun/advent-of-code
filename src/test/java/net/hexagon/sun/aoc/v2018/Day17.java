package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day17 extends AdventOfCode {

	private static class GroundScan {
		final List<Point> points;
		final char[][] grid;

		final int minX;
		final int maxX;
		final int minY;
		final int maxY;

		Point source;
		Set<Point> queue = new HashSet<>();

		GroundScan (List<Point> points) {
			this.points = points;

			minX = points.stream().mapToInt(p -> p.x).min().orElse(-1) - 4;
			maxX = points.stream().mapToInt(p -> p.x).max().orElse(-1) + 4;
			minY = points.stream().mapToInt(p -> p.y).min().orElse(-1);
			maxY = points.stream().mapToInt(p -> p.y).max().orElse(-1);
			int deltaX = maxX - minX + 1;
			int deltaY = maxY - minY + 1;

			grid = new char[deltaY][deltaX];
			for (int i = 0; i < grid.length; i++) {
				char[] row = new char[deltaX];
				Arrays.fill(row, '.');
				grid[i] = row;
			}

			for (Point p : points) {
				set(p, '#');
			}
		}

		void tick () {
			Set<Point> nextOrigins = new HashSet<>();
			for (Point p : queue) {
				try {
					Set<Point> nextFromTick = tick(p);
					nextOrigins.addAll(nextFromTick);
				} catch (ArrayIndexOutOfBoundsException oobe) {
					System.out.println("edges reached for this stream");
				}
			}
			queue = nextOrigins;
		}

		Set<Point> tick (Point origin) {
			Point next = getNext(origin);

			if (isAir(next)) {
				// free fall, regular fill
				set(next, '|');
				return Collections.singleton(next);
			} else if (isWall(next) || isWater(next)) {
				// below is wall or (already) water: fill left & right, but check diagonals below as well
				System.out.println("below is " + get(next));

				if (isFlowingWater(next)) {
					// some source is already filling this row, abort
					System.out.println("flowing wataaa below");
					return Collections.emptySet();
				}
				next = up(next);
				Point leftmost = fillHorizontal(next, new Point(-1, 0));
				Point rightmost = fillHorizontal(next, new Point(1, 0));

				Set<Point> newOrigins = new HashSet<>();
				if (!isWall(leftmost)) {
					// continue freefall here
					newOrigins.add(leftmost);
				}
				if (!isWall(rightmost)) {
					// continue freefall here
					newOrigins.add(rightmost);
				}
				if (newOrigins.isEmpty()) {
					// settle this row
					fillHorizontal(next, new Point(-1, 0), '~');
					fillHorizontal(next, new Point(1, 0), '~');

					// continue above
					newOrigins.add(up(next));
				}
				return newOrigins;
			}
			return Collections.emptySet();
		}

		private boolean isFlowingWater (Point p) {
			return '|' == get(p);
		}

		private Point fillHorizontal (Point p, Point directionDelta) {
			return fillHorizontal(p, directionDelta, '|');
		}

		private Point fillHorizontal (Point p, Point directionDelta, char fill) {
			Point current = new Point(p);
			do {
				set(current, fill);
				current.translate(directionDelta.x, directionDelta.y);
			} while (!isWall(current) && !isAirBelow(current) && !isWaterEdge(current, directionDelta));

			if (!isWall(current) && isAirBelow(current)) {
				// this is the cell that overhangs the edge
				set(current, fill);
			}
			return current;
		}

		private boolean isWaterEdge (Point point, Point directionDelta) {
			// is below water, but 1 step in "direction" is air?
			Point diagonalDown = down(point);
			diagonalDown.translate(directionDelta.x, directionDelta.y);
			return isWater(down(point)) && isAir(diagonalDown);
		}

		private boolean isAirBelow (Point current) {
			// check one down and one to "directionDelta"
			Point below = down(current);
			return isAir(below);
		}

		private Point up (Point p) {
			Point next = new Point(p);
			next.translate(0, -1);
			return next;
		}

		private Point down (Point p) {
			Point next = new Point(p);
			next.translate(0, 1);
			return next;
		}

		private boolean isAir (Point p) {
			return isAir(get(p));
		}

		private boolean isAir (char c) {
			return '.' == c;
		}

		private boolean isWater (Point p) {
			return isWater(get(p));
		}

		private boolean isWater (char c) {
			return '~' == c || '|' == c;
		}

		private boolean isWall (Point p) {
			return isWall(get(p));
		}

		private boolean isWall (char c) {
			return '#' == c;
		}

		private Point getNext (Point p) {
			return down(p);
		}

		private char get (Point p) {
			return grid[p.y - minY][p.x - minX];
		}

		private void set (Point p, char c) {
			grid[p.y - minY][p.x - minX] = c;
		}

		void setSource (Point point) {
			source = new Point(point.x, minY);
			set(source, '~');
			queue = Collections.singleton(source);
		}

		void print () {
			System.out.println("--------------------------");
			for (char[] row : grid) {
				for (char c : row) {
					System.out.print(c);
				}
				System.out.println();
			}
		}
	}

	//	x=447, y=1727..1744
	private static final Pattern INPUT_PATTERN_X = Pattern.compile("x=(\\d+), y=(\\d+)..(\\d+)");
	private static final Pattern INPUT_PATTERN_Y = Pattern.compile("y=(\\d+), x=(\\d+)..(\\d+)");

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(getInputLines()), is(31883));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(getInputLines()), is(24927));
	}

	private List<String> sampleInput = Arrays.asList(
			"x=495, y=2..7",
			"y=7, x=495..501",
			"x=501, y=3..7",
			"x=498, y=2..4",
			"x=506, y=1..2",
			"x=498, y=10..13",
			"x=504, y=10..13",
			"y=13, x=498..504");

	@Test
	public void runExample1 () {
		assertThat(solveTask1(sampleInput), is(57));
	}

	@Test
	public void runTask2Example1 () {
		int waterOnSourceLine = 1;
		assertThat(solveTask2(sampleInput) - waterOnSourceLine, is(29));
	}

	private int solveTask1 (List<String> input) {
		return solve(input, this::countWater);
	}

	private int solveTask2 (List<String> input) {
		return solve(input, this::countSettledWater);
	}

	private int solve (List<String> input, Function<GroundScan, Integer> counter) {

		GroundScan groundScan = parse(input);
		groundScan.setSource(new Point(500, 0));
		// uncomment to print initial state
		groundScan.print();

		try {
			while (!groundScan.queue.isEmpty()) {
				groundScan.tick();
				// uncomment to print each step
//				groundScan.print();
			}
		} catch (ArrayIndexOutOfBoundsException oobe) {
			System.out.println("edges reached");
		}

		// uncomment to print final state
//		groundScan.print();
		return counter.apply(groundScan);
	}

	private Integer countSettledWater (GroundScan groundScan) {
		return countWater(groundScan, true);
	}

	private int countWater (GroundScan groundScan) {
		return countWater(groundScan, false);
	}

	private int countWater (GroundScan groundScan, boolean onlySettled) {
		int sum = 0;
		for (char[] row : groundScan.grid) {
			for (char c : row) {
				if (onlySettled) {
					if (c == '~') {
						sum++;
					}
				} else {
					if (c == '~' || c == '|') {
						sum++;
					}
				}
			}
		}
		return sum;
	}

	GroundScan parse (List<String> input) {
		List<Point> points = input.stream()
									 .map(this::parse)
									 .flatMap(Collection::stream)
									 .collect(Collectors.toList());
		return new GroundScan(points);
	}

	private List<Point> parse (String line) {
		List<Point> points = new ArrayList<>();
		points.addAll(parse(line, INPUT_PATTERN_X, (i, firstNumber) -> new Point(firstNumber, i)));
		points.addAll(parse(line, INPUT_PATTERN_Y, (i, firstNumber) -> new Point(i, firstNumber)));
		return points;
	}

	private List<Point> parse (String line, Pattern pattern, BiFunction<Integer, Integer, Point> creator) {
		Matcher m = pattern.matcher(line);
		if (m.matches()) {
			int firstNumber = Integer.parseInt(m.group(1));
			int y1 = Integer.parseInt(m.group(2));
			int y2 = Integer.parseInt(m.group(3));
			return IntStream.rangeClosed(y1, y2)
						   .mapToObj(i -> creator.apply(i, firstNumber))
						   .collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

}

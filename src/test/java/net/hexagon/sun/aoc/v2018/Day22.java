package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day22 extends AdventOfCode {

	private enum Type {
		ROCKY, NARROW, WET
	}

	private static class Cave {
		private final Point target;
		private final int depth;
		private final Type[][] cave;

		private final Map<Point, Integer> geoIndex= new HashMap<>();
		private final Map<Point, Integer> erosionLevel= new HashMap<>();

		Cave (Point target, int depth) {
			this.target= new Point(target.x + 1, target.y + 1);
			this.depth= depth;
			cave= buildEmpty();
		}

		private Type getType (Point p) {
			int mod3= getErosionLevel(p) % 3;
			if (mod3 == 0) {
				return Type.ROCKY;
			} else if (mod3 == 1) {
				return Type.WET;
			}
			return Type.NARROW;
		}

		private Integer getErosionLevel (Point p) {
			return erosionLevel.computeIfAbsent(p, this::calculateErosionLevel);
		}

		private Integer calculateErosionLevel (Point p) {
			int idx= getGeologicIndex(p);
			int el = idx + this.depth;
			return el % 20183;
		}

		private Integer getGeologicIndex(Point p) {
			return geoIndex.computeIfAbsent(p, this::calculateGeoIndex);
		}

		private Integer calculateGeoIndex (Point p) {
			if (p.x == 0 && p.y == 0 || p.x == target.x -1 && p.y == target.y - 1) {
				return 0;
			} else if (p.y == 0) {
				return p.x * 16807;
			} else if (p.x == 0) {
				return p.y * 48271;
			} else {
				int idx1= getErosionLevel(new Point(p.x - 1, p.y));
				int idx2= getErosionLevel(new Point(p.x, p.y - 1));
				return idx1 * idx2;
			}
		}

		private void calculateType () {
			for (int i = 0; i < target.y; i++) {
				for (int j = 0; j < target.x; j++) {
					cave[i][j] = getType(new Point(j, i));
				}
			}
		}

		private int calculateRiskLevel () {
			int sum= 0;
			for (int i = 0; i < target.y; i++) {
				Type[] row= cave[i];
				for (int j = 0; j < target.x; j++) {
					Type t= row[j];
					if (t == Type.WET) {
						sum+= 1;
					} else if (t == Type.NARROW) {
						sum+= 2;
					}
				}
			}
			return sum;
		}

		private Type[][] buildEmpty () {
			Type[][] cave= new Type[target.y][target.x];
			for (int i = 0; i < target.y; i++) {
				cave[i]= new Type[target.x];
			}
			return cave;
		}

		private void print () {
			for (int i = 0; i < cave.length; i++) {
				Type[] row = cave[i];
				for (int j = 0; j < row.length; j++) {
					Type t = row[j];
					char c = t == Type.ROCKY ? '.' : (t == Type.NARROW ? '|' : '=');
					if (i == 0 && j == 0) {
						c= 'M';
					}
					if (i == target.y - 1 && j == target.x - 1) {
						c= 'T';
					}
					System.out.print(c);
				}
				System.out.println();
			}
		}
	}

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(7305, new Point(13,734)), is(10204));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(7305, new Point(13,734)), is(-1));
	}

	@Test
	public void runExample1 () {
		assertThat(solveTask1(510, new Point(10, 10)), is(114));
	}

	private int solveTask1 (int depth, Point target) {
		Cave c= new Cave(target, depth);
		c.calculateType();
		c.print();
		return c.calculateRiskLevel();
	}

	private int solveTask2 (int depth, Point target) {
		return -1;
	}

}

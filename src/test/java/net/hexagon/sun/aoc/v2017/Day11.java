package net.hexagon.sun.aoc.v2017;

import javafx.geometry.Point3D;
import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// heavily features https://www.redblobgames.com/grids/hexagons/
public class Day11 extends AdventOfCode {

	enum Direction {
		se, ne, n, nw, sw, s;

		Point[][] oddq_directions = {
				{
						new Point(+1, 0), new Point(+1, -1), new Point(0, -1),
						new Point(-1, -1), new Point(-1, 0), new Point(0, +1)
				},
				{
						new Point(+1, +1), new Point(+1, 0), new Point(0, -1),
						new Point(-1, 0), new Point(-1, +1), new Point(0, +1)
				}
		};

		public Point stepTo(Point pos) {
			int parity= pos.x & 1;
			Point dir= oddq_directions[parity][this.ordinal()];
			return new Point(pos.x + dir.x, pos.y + dir.y);
		}

	}

	@Test
	@Override
	public void runTask1() {
		assertThat(solveTask1(getInputAsString()), is(743));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solveTask2(getInputAsString()), is(1493));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1("ne,ne,ne"), is(3));
		assertThat(solveTask1("ne,ne,sw,sw"), is(0));
		assertThat(solveTask1("ne,ne,s,s"), is(2));
		assertThat(solveTask1("se,sw,se,sw,sw"), is(3));
	}

	private int solveTask1(String input) {
		return solve(input, false);
	}

	private int solveTask2(String input) {
		return solve(input, true);
	}

	private int solve(String input, boolean partTwo) {
		List<Direction> directions= Arrays.stream(input.split(","))
											 .map(Direction::valueOf)
											 .collect(Collectors.toList());

		int maxDist= 0;
		Point origin= new Point();
		Point pos= origin;
		for (Direction d : directions) {
			pos= d.stepTo(pos);
			int intermediateDist= offset_distance(origin, pos);
			maxDist= Math.max(maxDist, intermediateDist);
		}

		System.out.println("end pos: " + pos);
		System.out.println("max Dist: " + maxDist);
		int distance = offset_distance(origin, pos);
		return partTwo ? maxDist : distance;
	}

	// https://www.redblobgames.com/grids/hexagons/#distances
	private int offset_distance(Point a, Point b) {
		Point3D ac = oddq_to_cube(a);
		Point3D bc = oddq_to_cube(b);
		return cube_distance(ac, bc);
	}

	private int cube_distance(Point3D a, Point3D b) {
		return (int)((Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY()) + Math.abs(a.getZ() - b.getZ())) / 2);
	}

	private Point3D oddq_to_cube(Point hex) {
		int cubeX = hex.x;
		int cubeZ = hex.y - (hex.x - (hex.x&1)) / 2;
		int cubeY = -cubeX-cubeZ;
		return new Point3D(cubeX, cubeY, cubeZ);
	}

}

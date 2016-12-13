package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/13
public class Day13 extends AdventOfCode {

	@Test
	@Override
	public void runTask1() {
		int solution = 82;
		assertThat(solveTask1(1362, new Point(31, 39), 100), is(solution));
	}

	@Test
	@Override
	public void runTask2() {
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1(10, new Point(7, 4), 10), is(11));
	}

	private int solveTask1(int offset, Point target, int maxSize) {
		Point start = new Point(1, 1);

		Boolean[][] maze= new Boolean[maxSize][maxSize];
		LinkedList<Point> path = findShortestPath(maze, start, target, offset);
		return path == null ? -1 : path.size();
	}

	// http://stackoverflow.com/a/30552530
	private LinkedList<Point> findShortestPath(Boolean[][] mazeArray, Point startLocation, Point targetLocation, int offset) {
		// This double array keeps track of the "level" of each node.
		// The level increments, starting at the startLocation to represent the path
		int[][] levelArray = new int[mazeArray.length][mazeArray[0].length];

		// Assign every free space as 0, every wall as -1
		for (int i = 0; i < mazeArray.length; i++)
			for (int j = 0; j < mazeArray[0].length; j++) {
				Boolean location= mazeArray[i][j];
				if (location == null) {
					// calculate & add
					location= isWall(i, j, offset);
				}
				if (!location || (i== targetLocation.x && j==targetLocation.y)) {
					levelArray[i][j] = 0;
				} else {
					levelArray[i][j] = -1;
				}
			}

		// Keep track of the traversal in a queue
		LinkedList<Point> queue = new LinkedList<>();
		queue.add(startLocation);

		// Mark starting point as 1
		levelArray[startLocation.x][startLocation.y] = 1;

		// Mark every adjacent open node with a numerical level value
		while (!queue.isEmpty()) {
			Point point = queue.poll();
			// Reached the end
			if (point.equals(targetLocation))
				break;

			int level = levelArray[point.x][point.y];
			ArrayList<Point> possibleMoves = new ArrayList<>();
			// Move Up
			possibleMoves.add(new Point(point.x, point.y + 1));
			// Move Left
			possibleMoves.add(new Point(point.x - 1, point.y));
			// Down Move
			possibleMoves.add(new Point(point.x, point.y - 1));
			// Move Right
			possibleMoves.add(new Point(point.x + 1, point.y));

			for (Point potentialMove : possibleMoves) {
				if (spaceIsValid(potentialMove)) {
					// Able to move here if it is labeled as 0
					if (levelArray[potentialMove.x][potentialMove.y] == 0) {
						queue.add(potentialMove);
						// Set this adjacent node as level + 1
						levelArray[potentialMove.x][potentialMove.y] = level + 1;
					}
				}
			}
		}
		// Couldn't find solution
		if (levelArray[targetLocation.x][targetLocation.y] == 0)
			return null;

		LinkedList<Point> shortestPath = new LinkedList<>();
		Point pointToAdd = targetLocation;

		while (!pointToAdd.equals(startLocation)) {
			shortestPath.push(pointToAdd);
			int level = levelArray[pointToAdd.x][pointToAdd.y];
			ArrayList<Point> possibleMoves = new ArrayList<>();
			// Move Right
			possibleMoves.add(new Point(pointToAdd.x + 1, pointToAdd.y));
			// Down Move
			possibleMoves.add(new Point(pointToAdd.x, pointToAdd.y - 1));
			// Move Left
			possibleMoves.add(new Point(pointToAdd.x - 1, pointToAdd.y));
			// Move Up
			possibleMoves.add(new Point(pointToAdd.x, pointToAdd.y + 1));

			for (Point potentialMove : possibleMoves) {
				if (spaceIsValid(potentialMove)) {
					// The shortest level will always be level - 1, from this current node.
					// Longer paths will have higher levels.
					if (levelArray[potentialMove.x][potentialMove.y] == level - 1) {
						pointToAdd = potentialMove;
						break;
					}
				}
			}
		}

		return shortestPath;
	}

	private boolean isWall(int x, int y, int offset) {
		int equation = x*x + 3*x + 2*x*y + y + y*y + offset;
		return (Integer.bitCount(equation) % 2) == 1;
	}

	private boolean spaceIsValid(Point potentialMove) {
		return potentialMove.x > 0 && potentialMove.y > 0;
	}

}

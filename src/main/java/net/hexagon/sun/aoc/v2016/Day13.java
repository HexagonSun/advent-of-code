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
		int solution = 138;
		// 130: too low
		assertThat(solveTask2(50, 1362, 50), is(solution));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1(10, new Point(7, 4), 10), is(11));
	}

	@Test
	public void runTask2Example1() {
		int solution= 11;
		assertThat(solveTask2(5, 10, 10), is(solution));
	}

	@Test
	public void testSteps0() {
		assertThat(solveTask2(0, 1362, 10), is(1));
	}
	@Test
	public void testSteps1() {
		assertThat(solveTask2(1, 1362, 10), is(3));
	}
	@Test
	public void testSteps2() {
		assertThat(solveTask2(2, 1362, 10), is(5));
	}
	@Test
	public void testSteps3() {
		assertThat(solveTask2(3, 1362, 10), is(6));
	}

	private int solveTask1(int offset, Point target, int maxSize) {
		Point start = new Point(1, 1);

		Boolean[][] maze= new Boolean[maxSize][maxSize];
		LinkedList<Point> path = findShortestPath(maze, start, target, offset);
		return path == null ? -1 : path.size();
	}

	private int solveTask2(int maxSteps, int offset, int maxSize) {
		Boolean[][] maze= new Boolean[maxSize+1][maxSize+1];
		int[][] levelArray = calculateMaze(maze, null, offset);

		Point start = new Point(1, 1);
		levelArray[1][1]= 1;
		System.out.println("-------- before --------");
		printMaze(levelArray);
		int reachableTargets= floodFill(levelArray, start, maxSteps);
		System.out.println("-------- " + reachableTargets + " --------");
		printMaze(levelArray);

		return reachableTargets;
	}

	private void printMaze(int[][] levelArray) {
		// print mirrored, to show the same picture as in the puzzle description
		int len= levelArray.length;
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				int cell= levelArray[j][i];
				String dot= "";
				switch (cell) {
					case -1: dot+= '█'; break;
					case 0: dot+= ' '; break;
					case 1: dot+= 'O'; break;
					case 2: dot+= 'X'; break;
					default: dot+= '*';
				}
				System.out.print(dot);
			}
			System.out.println("");
		}
	}

	private int floodFill(int[][] levelArray, Point location, int maxSteps) {
		int visitedCells= 0;
		LinkedList<Point> queue = new LinkedList<>();
		queue.add(location);
		visitedCells++;

		// Mark starting point as 1
		levelArray[location.x][location.y] += 1;

		while (maxSteps > 0) {
			LinkedList<Point> nextQueue = new LinkedList<>();

			// Mark every adjacent open node with a numerical level value
			while (!queue.isEmpty() && maxSteps > 0) {
				Point point = queue.poll();
				ArrayList<Point> possibleMoves = new ArrayList<>();
				possibleMoves.add(new Point(point.x, point.y + 1));
				possibleMoves.add(new Point(point.x - 1, point.y));
				possibleMoves.add(new Point(point.x, point.y - 1));
				possibleMoves.add(new Point(point.x + 1, point.y));

				for (Point potentialMove : possibleMoves) {
					if (spaceIsValid(potentialMove, levelArray.length)) {
						// Able to move here if it is labeled as 0
						if (levelArray[potentialMove.x][potentialMove.y] == 0) {
							nextQueue.add(potentialMove);
							visitedCells++;
							levelArray[potentialMove.x][potentialMove.y] += 1;
						}
					}
				}
			}

			// all nodes in the current step were visited.
			maxSteps--;
			queue.addAll(nextQueue);
		}
		return visitedCells;
	}

	// http://stackoverflow.com/a/30552530
	private LinkedList<Point> findShortestPath(Boolean[][] mazeArray, Point startLocation, Point targetLocation, int offset) {
		int[][] levelArray = calculateMaze(mazeArray, targetLocation, offset);

		printMaze(levelArray);
		return findShortestPathImpl(levelArray, startLocation, targetLocation);
	}

	// http://stackoverflow.com/a/30552530
	private LinkedList<Point> findShortestPathImpl(int[][] levelArray, Point startLocation, Point targetLocation) {
		// Keep track of the traversal in a queue
		LinkedList<Point> queue = new LinkedList<>();
		queue.add(startLocation);

		// Mark starting point as 1
		levelArray[startLocation.x][startLocation.y] = 1;

		// Mark every adjacent open node with a numerical level value
		while (!queue.isEmpty()) {
			Point point = queue.poll();
			// Reached the end
			if (point.equals(targetLocation)) {
				break;
			}

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
				if (spaceIsValid(potentialMove, levelArray.length)) {
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
		if (levelArray[targetLocation.x][targetLocation.y] == 0) {
			return null;
		}

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
				if (spaceIsValid(potentialMove, levelArray.length)) {
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

	private int[][] calculateMaze(Boolean[][] mazeArray, Point targetLocation, int offset) {
		// This double array keeps track of the "level" of each node.
		// The level increments, starting at the startLocation to represent the path
		int[][] levelArray = new int[mazeArray.length][mazeArray[0].length];

		// Assign every free space as 0, every wall as -1
		for (int i = 0; i < mazeArray.length; i++) {
			for (int j = 0; j < mazeArray[0].length; j++) {
				Boolean location = mazeArray[i][j];
				if (location == null) {
					// calculate & add
					location = isWall(i, j, offset);
				}
				if (!location || (targetLocation != null && i == targetLocation.x && j == targetLocation.y)) {
					levelArray[i][j] = 0;
				} else {
					levelArray[i][j] = -1;
				}
			}
		}
		return levelArray;
	}

	private boolean isWall(int x, int y, int offset) {
		int equation = x*x + 3*x + 2*x*y + y + y*y + offset;
		return (Integer.bitCount(equation) % 2) == 1;
	}

	private boolean spaceIsValid(Point potentialMove, int length) {
		return potentialMove.x < length && potentialMove.x >= 0
					   && potentialMove.y < length && potentialMove.y >= 0;
	}

}

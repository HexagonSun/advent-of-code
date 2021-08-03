package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/24
public class Day24 extends AdventOfCode {

	static class Vertex implements Comparable<Vertex> {
		final int number;
		Set<Edge> adjacencies= new HashSet<>();
		int minDistance;
		Vertex previous;
		Map<Vertex, Integer> neighborWeights;

		Vertex(int number) {
			this.number= number;
			reset();
		}

		void reset() {
			minDistance= Integer.MAX_VALUE;
			previous= null;
		}

		@Override
		public int compareTo(Vertex other) {
			return Double.compare(minDistance, other.minDistance);
		}

		@Override
		public String toString() {
			return String.valueOf(number);
		}
	}

	static class Edge {
		final Vertex to;
		final int weight;

		Edge (Vertex to, int weight) {
			this.to= to;
			this.weight= weight;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Edge edge = (Edge) o;
			return weight == edge.weight && to.equals(edge.to);
		}

		@Override
		public int hashCode() {
			int result = to.hashCode();
			result = 31 * result + weight;
			return result;
		}
	}

	@Test
	@Override
	public void runTask1() {
		int solution = 442;
		assertThat(solveTask1(getInputLines()), is(solution));
	}

	@Test
	@Override
	public void runTask2() {
		int solution = 660;
		assertThat(solveTask2(getInputLines()), is(solution));
	}

	@Test
	public void runExample1() {
		List<String> input = Arrays.asList(
				"###########",
				"#0.1.....2#",
				"#.#######.#",
				"#4.......3#",
				"###########");
		assertThat(solveTask1(input), is(14));
	}

	private int solveTask1(List<String> inputLines) {
		return solve(inputLines, false);
	}

	private int solveTask2(List<String> inputLines) {
		return solve(inputLines, true);
	}

	private int solve(List<String> inputLines, boolean part2) {
		Map<Integer, Point> locations = new HashMap<>();
		int[][] maze = parseMaze(inputLines, locations);

//		printMaze(maze);

		Map<Integer, Vertex> vertices = buildGraph(locations, maze);
		Vertex start= vertices.get(0);

		// do the Dijkstra's between all pairs
		for (int i = 0; i < locations.size(); i++) {
			Vertex vertexFrom= vertices.get(i);
			// we need to reset the vertices for the next iteration of Dijkstra
			clearNodes(vertices);
			vertexFrom.neighborWeights= doDijkstra(vertices, vertexFrom);
		}

		List<Vertex> others= new ArrayList<>(vertices.values());
		if (part2) {
			// part two: visit 0 twice
			others.add(start);
		}

		// Brute force: calculate all permutations of edges, i.e. all possible paths.
		// We'll prune paths that we're not interested in within the loop below
		List<List<Vertex>> list= generatePermutations(others);

		int minDist= Integer.MAX_VALUE;
		List<Vertex> minPath= null;
		for (List<Vertex> path : list) {
			if (!path.get(0).equals(start)) {
				// only consider permutations that start at start-node
				continue;
			}
			// part two: only consider paths that #end# at 0, too
			if (part2 && !path.get(path.size()-1).equals(start)) {
				// only consider permutations that start at start-node
				continue;
			}

			int dist= 0;
			for (int i = 0; i < path.size() -1; i++) {
				Vertex v = path.get(i);
				Vertex next= path.get(i+1);
				int distToNext = v.neighborWeights.get(next);
				dist += distToNext;
			}

			if (dist < minDist) {
				minDist= dist;
				minPath= path;
			}
		}

		System.out.println("Min dist: " + minDist);
		System.out.println("Min path: " + minPath);
		return minDist;
	}

	private void calculateDijkstra(Vertex start) {
		start.minDistance= 0;
		PriorityQueue<Vertex> queue= new PriorityQueue<>();
		queue.add(start);

		while (!queue.isEmpty()) {
			Vertex from= queue.poll();

			// Visit each edge exiting "next"
			for (Edge e : from.adjacencies) {
				Vertex to= e.to;
				int weight= e.weight;
				int distanceThroughU= from.minDistance + weight;

				if (distanceThroughU < to.minDistance) {
					queue.remove(to);

					to.minDistance= distanceThroughU ;
					to.previous= from;
					queue.add(to);
				}
			}
		}
	}

	private Map<Integer, Vertex> buildGraph(Map<Integer, Point> locations, int[][] maze) {
		Map<Integer, Vertex> vertices = createVertices(locations);

		// find pairwise shortest distances, create Edge & associate with corresponding Vertex
		for (int i = 0; i < locations.size() - 1; i++) {
			Point pointFrom= locations.get(i);
			Vertex vertexFrom= vertices.get(i);

			for (int j = 1; j < locations.size(); j++) {
				if (i == j) {
					continue;
				}

				Point pointTo= locations.get(j);
				Vertex vertexTo= vertices.get(j);

				// copy maze so we don't modifiy it for the next iteration
				int[][] mazeCopy= clone(maze);
				LinkedList<Point> path= findShortestPath(mazeCopy, pointFrom, pointTo);

//				System.out.println("From (" + i + ")->[" + pointFrom.x + "/" + pointFrom.y + "] pointTo (" + j +
//										   ")->[" + pointTo.x + "/" + pointTo.y + "] path length is: " + path.size());

				int pathLength= path == null ? Integer.MAX_VALUE : path.size();
				Edge e= new Edge(vertexTo, pathLength);
				vertexFrom.adjacencies.add(e);
				// reverse Edge
				vertexTo.adjacencies.add(new Edge(vertexFrom, pathLength));
			}
		}
		return vertices;
	}

	private Map<Integer, Vertex> createVertices(Map<Integer, Point> locations) {
		Map<Integer, Vertex> vertices = new HashMap<>();
		for (Integer i : locations.keySet()) {
			Vertex v= new Vertex(i);
			vertices.put(i, v);
		}
		return vertices;
	}

	private void clearNodes(Map<Integer, Vertex> vertices) {
		for (Vertex v : vertices.values()) {
			v.reset();
		}
	}

	private <T> List<List<T>> generatePermutations(List<T> original) {
		if (original.size() == 0) {
			List<List<T>> result = new ArrayList<>();
			result.add(new ArrayList<>());
			return result;
		}
		T firstElement = original.remove(0);
		List<List<T>> allPermutations = new ArrayList<>();
		List<List<T>> permutations = generatePermutations(original);
		for (List<T> smallerPermutated : permutations) {
			for (int index = 0; index <= smallerPermutated.size(); index++) {
				List<T> tmp = new ArrayList<>(smallerPermutated);
				tmp.add(index, firstElement);
				allPermutations.add(tmp);
			}
		}
		return allPermutations;
	}


	private Map<Vertex, Integer> doDijkstra(Map<Integer, Vertex> vertices, Vertex start) {
		Map<Vertex, Integer> weights= new HashMap<>();

		Set<Vertex> others= new HashSet<>(vertices.values());
		others.remove(start);

		calculateDijkstra(start);
		for (Vertex target : others) {
//			System.out.println("Distance from " + start + " to " + to + ": " + to.minDistance);
			int dist= (int)Math.floor(target.minDistance);
			weights.put(target, dist);
		}
		return weights;
	}

	// http://stackoverflow.com/a/30552530
	private LinkedList<Point> findShortestPath(int[][] maze, Point startLocation, Point targetLocation) {
		// Keep track of the traversal in a queue
		LinkedList<Point> queue = new LinkedList<>();
		queue.add(startLocation);

		// Mark starting point as 1
		maze[startLocation.x][startLocation.y] = 1;

		// Mark every adjacent open node with a numerical level value
		while (!queue.isEmpty()) {
			Point point = queue.poll();
			// Reached the end
			if (point.equals(targetLocation)) {
				break;
			}

			int level = maze[point.x][point.y];
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
				if (inBounds(potentialMove, maze.length)) {
					// Able to move here if it is labeled as 0
					if (maze[potentialMove.x][potentialMove.y] == 0) {
						queue.add(potentialMove);
						// Set this adjacent node as level + 1
						maze[potentialMove.x][potentialMove.y] = level + 1;
					}
				}
			}
		}
		// Couldn't find solution
		if (maze[targetLocation.x][targetLocation.y] == 0) {
			return null;
		}

		LinkedList<Point> shortestPath = new LinkedList<>();
		Point pointToAdd = targetLocation;

		while (!pointToAdd.equals(startLocation)) {
			shortestPath.push(pointToAdd);
			int level = maze[pointToAdd.x][pointToAdd.y];
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
				if (inBounds(potentialMove, maze.length)) {
					// The shortest level will always be level - 1, from this current node.
					// Longer paths will have higher levels.
					if (maze[potentialMove.x][potentialMove.y] == level - 1) {
						pointToAdd = potentialMove;
						break;
					}
				}
			}
		}

		return shortestPath;
	}

	private boolean inBounds(Point potentialMove, int length) {
		return potentialMove.x < length && potentialMove.x >= 0
					   && potentialMove.y < length && potentialMove.y >= 0;
	}

	private void printMaze(int[][] maze) {
		int height = maze[0].length;

		for (int i = 0; i < height; i++) {
			for (int[] aMaze : maze) {
				int cell = aMaze[i];
				String dot = "";
				switch (cell) {
					case -1:
						dot += '█';
						break;
					case 0:
						dot += '_';
						break;
					case 1:
						dot += 'O';
						break;
					case 2:
						dot += 'X';
						break;
					default:
						dot += '*';
				}
				System.out.print(dot);
			}
			System.out.println("");
		}
	}

	private int[][] parseMaze(List<String> inputLines, Map<Integer, Point> locations) {
		int[][] maze = new int[inputLines.get(0).length()][inputLines.size()];

		for (int x = 0; x < inputLines.size(); x++) {
			String line = inputLines.get(x);
			char[] charArray = line.toCharArray();
			for (int y = 0; y < charArray.length; y++) {
				char cell = charArray[y];

				int value;
				if (cell == '#') {
					value = -1;
				} else if (cell == '.') {
					value = 0;
				} else {
					// a location!
					int nb = Integer.parseInt("" + cell);
					locations.put(nb, new Point(y, x));
					value = 0;
				}
				maze[y][x] = value;
			}
		}

		return maze;
	}

	private static int[][] clone(int[][] src) {
		int length = src.length;
		int[][] target = new int[length][src[0].length];
		for (int i = 0; i < length; i++) {
			System.arraycopy(src[i], 0, target[i], 0, src[i].length);
		}
		return target;
	}
}

package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/9
public class Day09 extends AdventOfCode {

	private enum Mode {
		MINIMUM, MAXIMUM
	}

	private static final class Vertex {
		private final Map<String, Integer> distanceToNeighbour= new HashMap<>();
		private String name;
	}

	private static final class Edge {
		String from;
		String to;
		int weigth;
		@Override
		public String toString() {
			return "[ " + from + " |--> " + to + " ]";
		}
	}

	private static final Pattern LINE_PATTERN = Pattern.compile("(\\b.*\\b) to (\\b.*\\b) = (\\d+)");

	@Test
	@Override
	public void runTask1() {
		List<String> input= getInputLines();
		assertThat(solveTask1(input), is(207));
	}

	@Test
	public void runExample1() {
		List<String> input= Arrays.asList(
				"London to Dublin = 464",
				"London to Belfast = 518",
				"Dublin to Belfast = 141");
		assertThat(solveTask1(input), is(605));
	}

	@Test
	@Override
	public void runTask2() {
		List<String> input= getInputLines();
		assertThat(solveTask2(input), is(804));
	}

	@Test
	public void runTask2Example1() {
		List<String> input= Arrays.asList(
				"London to Dublin = 464",
				"London to Belfast = 518",
				"Dublin to Belfast = 141");
		assertThat(solveTask2(input), is(982));
	}

	private int solveTask1(List<String> lines) {
		return solveTask(lines, Mode.MINIMUM);
	}
	private int solveTask2(List<String> lines) {
		return solveTask(lines, Mode.MAXIMUM);
	}

	private int solveTask(List<String> lines, Mode mode) {
		Map<String, Vertex> vertices= new HashMap<>();
		for (String line : lines) {
			Edge edge = parse(line);
			processEdgeDistances(vertices, edge);
		}

		// TODO: Floyd-Warshall instead
		// bruteforce: test all combinations
		int total= Mode.MINIMUM == mode ? Integer.MAX_VALUE : Integer.MIN_VALUE;
		List<List<Vertex>> perms = generatePermutations(new ArrayList<>(vertices.values()));
		for (List<Vertex> list : perms) {
			int score= score(list);
			total= Mode.MINIMUM == mode ? Math.min(total, score) : Math.max(total, score);
		}
		return total;
	}

	private int score(List<Vertex> list) {
		int score= 0;
		int size= list.size();
		for (int i = 0; i < size; i++) {
			Vertex v= list.get(i);
			if (i < size - 1) {
				Vertex next= list.get(i+1);
				Integer distance = v.distanceToNeighbour.get(next.name);
				if (distance != null) {
					score+= distance;
				}
			}
		}
		return score;
	}

	private void processEdgeDistances(Map<String, Vertex> vertices, Edge edge) {
		processVertex(vertices, edge.from, edge);
		processVertex(vertices, edge.to, edge);
	}

	private void processVertex(Map<String, Vertex> vertices, String name, Edge edge) {
		Vertex v= vertices.get(name);
		if (v == null) {
			v= new Vertex();
			v.name= name;
			vertices.put(name, v);
		}
		if (v.name.equals(edge.from)) {
			v.distanceToNeighbour.put(edge.to, edge.weigth);
		} else {
			v.distanceToNeighbour.put(edge.from, edge.weigth);
		}
	}

	private Edge parse(String line) {
		Matcher m = LINE_PATTERN.matcher(line);
		if (!m.matches()) {
			return null;
		}
		Edge e= new Edge();
		e.from= m.group(1);
		e.to= m.group(2);
		e.weigth= Integer.parseInt(m.group(3));
		return e;
	}



	public <T> List<List<T>> generatePermutations(List<T> original) {
		if (original.size() == 0) {
			List<List<T>> result = new ArrayList<List<T>>();
			result.add(new ArrayList<T>());
			return result;
		}
		T firstElement = original.remove(0);
		List<List<T>> allPermutations = new ArrayList<List<T>>();
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

}

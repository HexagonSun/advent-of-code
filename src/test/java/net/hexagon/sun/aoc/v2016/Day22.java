package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/22
public class Day22 extends AdventOfCode {

	private static class Node {
		int x;
		int y;

		int size;
		int used;
		int available;
		int percentage;

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Node node = (Node) o;

			if (x != node.x) return false;
			if (y != node.y) return false;
			return true;
		}

		@Override
		public int hashCode() {
			int result = x;
			result = 31 * result + y;
			return result;
		}
	}

	private static final Pattern PATTERN= Pattern.compile("/dev/grid/node-x(\\d+)-y(\\d+)\\s+(\\d+)T\\s+(\\d+)T\\s+(\\d+)T\\s+(\\d+)%");

	@Test
	@Override
	public void runTask1 () {
		int solution= 1045;
		assertThat(solveTask1(getInputLines()), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
		String solution= "";
		assertThat(solveTask2(getInputLines()), is(solution));
	}

	private int solveTask1(List<String> inputLines) {
		List<Node> nodes= inputLines.stream()
								  .filter(line -> line.startsWith("/dev/"))
								  .map(this::parse)
								  .collect(Collectors.toList());

		int viablePairs= 0;
		for (int i = 0; i < nodes.size(); i++) {
			Node a= nodes.get(i);
			if (a.used == 0) {
				continue;
			}

			for (int j = 0; j < nodes.size(); j++) {
				Node b= nodes.get(j);
				if (viable(a, b)) {
					viablePairs++;
				}
			}
		}
		return viablePairs;
	}

	private String solveTask2(List<String> inputLines) {
		List<Node> nodes= inputLines.stream()
								  .filter(line -> line.startsWith("/dev/"))
								  .map(this::parse)
								  .collect(Collectors.toList());
		print(nodes);
		return "";
	}

	private void print(List<Node> nodes) {
		for (Node n : nodes) {
			if (n.percentage < 10) {
				System.out.print(" ");
			}
			System.out.print(" " + n.percentage + "%");

			if (n.y == 29) {
				System.out.println("");
			}
		}
	}

	private boolean viable(Node a, Node b) {
		if (a.used == 0 || a.equals(b)) {
			return false;
		}
		return b.available >= a.used;
	}

	private Node parse(String line) {
		Matcher m= PATTERN.matcher(line);
		if (m.find()) {
			int idx= 1;
			Node n= new Node();
			n.x= Integer.parseInt(m.group(idx++));
			n.y= Integer.parseInt(m.group(idx++));
			n.size= Integer.parseInt(m.group(idx++));
			n.used= Integer.parseInt(m.group(idx++));
			n.available= Integer.parseInt(m.group(idx++));
			n.percentage= Integer.parseInt(m.group(idx));
			return n;
		}
		return null;
	}

}

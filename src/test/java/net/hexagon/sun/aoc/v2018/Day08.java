package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PrimitiveIterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day08 extends AdventOfCode {

	private static class Node {
		List<Node> children= new ArrayList<>();
		List<Integer> meta= new ArrayList<>();

		int getValue() {
			if (children.isEmpty()) {
				return sumMeta();
			}
			return meta.stream()
				    .map(n -> n - 1) // 0-based
					.filter(n -> n < children.size())
					.map(i -> children.get(i))
					.mapToInt(Node::getValue)
					.sum();
		}

		int sumMeta () {
			return sum(meta) + children.stream().mapToInt(Node::sumMeta).sum();
		}

		static int sum(List<Integer> elements) {
			return elements.stream().mapToInt(n -> n).sum();
		}
	}

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(getInputAsString()), is(45868));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(getInputAsString()), is(19724));
	}

	private String sampleInput= "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2";

	@Test
	public void runExample1 () {
		assertThat(solveTask1(sampleInput), is(138));
	}

	@Test
	public void runTask2Example1 () {
		assertThat(solveTask2(sampleInput), is(66));
	}

	private int solveTask1 (String input) {
		return parse(input).sumMeta();
	}

	private int solveTask2 (String input) {
		return parse(input).getValue();
	}

	private Node parse (String input) {
		PrimitiveIterator.OfInt it= Arrays.stream(input.split(" "))
											.mapToInt(Integer::valueOf)
											.iterator();
		return parseNode(it);
	}

	private Node parseNode (PrimitiveIterator.OfInt it) {
		if (!it.hasNext()) {
			return null;
		}

		Node n= new Node();
		int nbChildren= it.nextInt();
		int nbMeta= it.next();

		// children
		while(nbChildren-- > 0) {
			Node child = parseNode(it);
			n.children.add(child);
		}
		// meta
		while(nbMeta-- > 0) {
			n.meta.add(it.next());
		}
		return n;
	}

}

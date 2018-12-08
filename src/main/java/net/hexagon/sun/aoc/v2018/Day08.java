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
		int nbChildren;
		int nbMeta;
		List<Node> children= new ArrayList<>();
		List<Integer> meta= new ArrayList<>();

		int getValue() {
			if (children.isEmpty()) {
				return meta.stream().mapToInt(n -> n).sum();
			} else {
				int sum= 0;
				for (int n : meta) {
					int index = n - 1; // make it 0 based
					if (index >= children.size()) {
						continue;
					}
					Node c= children.get(index);
					sum+= c.getValue();
				}
				return sum;
			}
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

	String sampleInput= "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2";

	@Test
	public void runExample1 () {
		assertThat(solveTask1(sampleInput), is(138));
	}

	@Test
	public void runTask2Example1 () {
		assertThat(solveTask2(sampleInput), is(66));
	}

	private int solveTask1 (String input) {
		Node root= parse(input);
		return sumMeta(root);
	}

	private int solveTask2 (String input) {
		Node root= parse(input);
		return root.getValue();
	}

	private int sumMeta (Node root) {
		return root.meta.stream().mapToInt(n -> n).sum() +
			   root.children.stream().mapToInt(this::sumMeta).sum();
	}

	private Node parse (String input) {
		String[] tokens= input.split(" ");
		PrimitiveIterator.OfInt it= Arrays.stream(tokens).map(Integer::valueOf).mapToInt(n -> n).iterator();

		return parseNode(it);
	}

	private Node parseNode (PrimitiveIterator.OfInt it) {
		if (!it.hasNext()) {
			return null;
		}

		Node n= new Node();
		n.nbChildren= it.nextInt();
		n.nbMeta= it.next();

		// children
		for (int i = 0; i < n.nbChildren; i++) {
			Node child = parseNode(it);
			n.children.add(child);
		}
		// meta
		for (int i = 0; i < n.nbMeta; i++) {
			n.meta.add(it.next());
		}
		return n;
	}


}

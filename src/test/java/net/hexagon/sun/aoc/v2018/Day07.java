package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day07 extends AdventOfCode {

	private static class Node {

		static Set<Node> set () {
			return set(Collections.emptyList());
		}

		static Set<Node> set (List<Node> nodes) {
			Set<Node> set = new TreeSet<>(Comparator.comparing(n -> n.id));
			set.addAll(nodes);
			return set;
		}

		String id;
		Set<Node> parents = set();
		Set<Node> preconditions = new HashSet<>();
		boolean done = false;

		Node (String id, Node parent) {
			this.id = id;
			if (parent != null) {
				this.parents.add(parent);
			}
		}

		boolean preconditionsDone () {
			return preconditions.stream().allMatch(p -> p.done);
		}

		@Override
		public String toString () {
			return id + "(" + parents + ")";
		}
	}

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(getInputLines()), is("GJFMDHNBCIVTUWEQYALSPXZORK"));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(getInputLines(), 5, 60), is(1050));
	}

	List<String> sampleInput= Arrays.asList(
				"Step C must be finished before step A can begin.",
				"Step C must be finished before step F can begin.",
				"Step A must be finished before step B can begin.",
				"Step A must be finished before step D can begin.",
				"Step B must be finished before step E can begin.",
				"Step D must be finished before step E can begin.",
				"Step F must be finished before step E can begin.");

	@Test
	public void runExample1 () {
		assertThat(solveTask1(sampleInput), is("CABDFE"));
	}

	@Test
	public void runTask2Example1 () {
		assertThat(solveTask2(sampleInput, 2, 0), is(15));
	}

	private String solveTask1 (List<String> input) {
		String path = buildPath(findRoots(parse(input)));
		System.out.println("path: " + path);
		return path;
	}

	private String buildPath (List<Node> roots) {
		Set<Node> available = Node.set(roots);
		StringBuilder sb = new StringBuilder();
		buildPath(available, sb);
		return sb.toString();
	}

	private void buildPath (Set<Node> available, StringBuilder sb) {
		if (available.isEmpty()) {
			return;
		}

		Node next = getNextAvailable(available);
		if (next == null) {
			return;
		}
		available.remove(next);

		available.addAll(next.parents);
		if (!sb.toString().contains(next.id)) {
			sb.append(next.id);
			next.done = true;
		}

		buildPath(available, sb);
	}

	private int solveTask2 (List<String> input, int nbWorkers, int delta) {
		Set<Node> available = Node.set(findRoots(parse(input)));
		int time = buildParallel(available, new int[nbWorkers], delta);
		System.out.println("time: " + time);
		return time;
	}

	private int buildParallel (Set<Node> available, int[] workers, int delta) {
		if (available.isEmpty()) {
			return 0;
		}
		Node[] inWork = new Node[workers.length];

		int time = 0;
		while (true) {
			if (available.isEmpty() && Arrays.stream(inWork).filter(Objects::nonNull).collect(Collectors.toList()).isEmpty()) {
				return time - 1;
			}

			for (int i = 0; i < workers.length; i++) {
				int wi = workers[i];
				if (wi <= 0) {
					Node finished = inWork[i];
					if (finished != null) {
						// we finished this item
						finished.done = true;
						available.addAll(finished.parents);
						inWork[i] = null;
					}

					Node next = getNextAvailable(available);
					if (next == null) {
						continue;
					}
					available.remove(next);
					inWork[i] = next;
					workers[i] = getItemDuration(next, delta);
				}
				workers[i]--;
			}
			time++;
		}
	}

	private int getItemDuration (Node node, int delta) {
		return (node.id.toCharArray()[0] - 'A' + 1) + delta;
	}

	private List<Node> findRoots (Map<String, Node> nodes) {
		return nodes.values().stream()
					   .filter(n -> !isParent(nodes, n))
					   .collect(Collectors.toList());
	}

	private boolean isParent (Map<String, Node> nodes, Node search) {
		for (Node n : nodes.values()) {
			if (n.parents.contains(search)) {
				return true;
			}
		}
		return false;
	}

	private Node getNextAvailable (Set<Node> available) {
		return available.stream()
					   .filter(Node::preconditionsDone)
					   .findFirst()
					   .orElse(null);
	}

	private Map<String, Node> parse (Iterable<String> input) {
		Map<String, Node> nodes = new HashMap<>();
		for (String line : input) {
			String[] t = line.split(" ");

			String id = t[1];
			String parentId = t[7];

			Node parent = nodes.getOrDefault(parentId, new Node(parentId, null));
			nodes.put(parentId, parent);

			Node n = nodes.getOrDefault(id, new Node(id, parent));
			n.parents.add(parent);
			parent.preconditions.add(n);
			nodes.put(id, n);
		}
		return nodes;
	}

}

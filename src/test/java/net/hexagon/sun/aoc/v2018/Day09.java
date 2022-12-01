package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day09 extends AdventOfCode {

	private static class Node {
		private final int value;
		private Node next;
		private Node prev;

		Node(int value) {
			this.value= value;
		}

		@Override
		public String toString () {
			return "[" + value + "]";
		}
	}

	private static class MarbleCircle {

		private Node first;
		private Node current;

		MarbleCircle() {
			first= new Node(0);
			first.next= first;
			first.prev= first;
			current= first;
		}

		Node addAfter(Node node, int value) {
			Node n= new Node(value);

			n.next= node.next;
			node.next.prev= n;

			n.prev= node;
			node.next= n;

			return n;
		}

		int remove(Node node) {
			Node prev= node.prev;
			Node next= node.next;
			prev.next= next;
			next.prev= prev;
			if (current == node) {
				current= next;
			}
			return node.value;
		}

		int addMarble (Integer marbleValue) {
			if (marbleValue % 23 == 0) {
				return add23(marbleValue);
			}
			Node next= current.next;
			current= addAfter(next, marbleValue);
			return 0;
		}

		private int add23 (Integer marbleValue) {
			current= current.prev.prev.prev.prev.prev.prev.prev;
			int removedValue= remove(current);
			return marbleValue + removedValue;
		}
	}

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(476, 71657), is(386018L));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(476, 71657), is(3085518618L));
	}

	@Test
	public void runExample1 () {
		assertThat(solveTask1(9, 25), is(32L));
	}

	@Test
	public void runExample2 () {
		assertThat(solveTask1(10, 1618), is(8317L));
	}

	@Test
	public void runExample3 () {
		assertThat(solveTask1(13, 7999), is(146373L));
	}

	@Test
	public void runExample4 () {
		assertThat(solveTask1(17, 1104), is(2764L));
	}

	@Test
	public void runExample5 () {
		assertThat(solveTask1(21, 6111), is(54718L));
	}

	@Test
	public void runExample6 () {
		assertThat(solveTask1(30, 5807), is(37305L));
	}

	private long solveTask1 (int nbPlayers, int nbMarbles) {
		MarbleCircle circle = new MarbleCircle();

		Iterator<Integer> marbles = IntStream.rangeClosed(1, nbMarbles)
											.boxed()
											.iterator();

		Map<Integer, Long> scoresByPlayer= new HashMap<>();
		int player= 1;
		while (marbles.hasNext()) {
			if (player > nbPlayers) {
				player = 1;
			}

			int score= circle.addMarble(marbles.next());
			scoresByPlayer.putIfAbsent(player, 0L);
			scoresByPlayer.computeIfPresent(player, (key, value) -> value + score);
			player++;
		}
		return scoresByPlayer.values().stream().mapToLong(n -> n).max().orElse(-1L);
	}

	private long solveTask2 (int nbPlayers, int nbMarbles) {
		return solveTask1(nbPlayers, nbMarbles * 100);
	}

}

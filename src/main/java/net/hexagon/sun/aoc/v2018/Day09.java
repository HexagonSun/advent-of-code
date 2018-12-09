package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day09 extends AdventOfCode {

	private static class CircularList<E> extends ArrayList<E> {

		@Override
		public void add (int index, E element) {
			super.add(index % size(), element);
		}

		@Override
		public E get (int index) {
			return super.get(index % size());
		}

		@Override
		public E remove (int index) {
			return super.remove(index % size());
		}
	}

	private static class MarbleCircle extends CircularList<Integer> {

		int currentMarble= 0;

		MarbleCircle() {
			super();
			add(0);
		}

		int addMarble (Integer marbleValue) {
			if (marbleValue % 23 == 0) {
				return add23(marbleValue);
			}
			if (size() == 1) {
				// add at end
				add(marbleValue);
				currentMarble= 1;
				return 0;
			}

			int next1= (currentMarble + 1) % size();
			int next2 = (currentMarble + 2) % size();
//			System.out.println("adding marble " + marbleValue + " between " + next1 + " and " + next2);

			if (next2 == 0) {
				// add at end
				add(marbleValue);
				currentMarble= size() - 1;
				return 0;
			}
			add(next2, marbleValue);
			currentMarble= next2;
			return 0;
		}

		private int add23 (Integer marbleValue) {
//			System.out.println("Special case");

			currentMarble= (currentMarble - 7) % size();
			if (currentMarble < 0) {
				currentMarble += size();
			}
//			System.out.println("\tnext currentMarble " + currentMarble);

			int removedValue= remove(currentMarble);
//			System.out.println("\tmarbleValue " + marbleValue);
//			System.out.println("\tremoved " + removedValue);
//			System.out.println("\tsum: " + (marbleValue + removedValue));

			return marbleValue + removedValue;
		}

	}

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(476, 71657), is(386018));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(476, 71657), is(-1));
	}

	@Test
	public void runExample1 () {
		assertThat(solveTask1(9, 25), is(32));
	}

	@Test
	public void runExample2 () {
		assertThat(solveTask1(10, 1618), is(8317));
	}

	@Test
	public void runExample3 () {
		assertThat(solveTask1(13, 7999), is(146373));
	}

	@Test
	public void runExample4 () {
		assertThat(solveTask1(17, 1104), is(2764));
	}

	@Test
	public void runExample5 () {
		assertThat(solveTask1(21, 6111), is(54718));
	}

	@Test
	public void runExample6 () {
		assertThat(solveTask1(30, 5807), is(37305));
	}

	private int solveTask1 (int nbPlayers, int nbMarbles) {
		MarbleCircle circle = new MarbleCircle();

		Iterator<Integer> marbles = IntStream.rangeClosed(1, nbMarbles)
											.boxed()
											.iterator();

		Map<Integer, Integer> scoresByPlayer= new HashMap<>();
		int player= 1;
		while (marbles.hasNext()) {
			if (player > nbPlayers) {
				player = 1;
			}

			int score= circle.addMarble(marbles.next());
			scoresByPlayer.putIfAbsent(player, 0);
			scoresByPlayer.computeIfPresent(player, (key, value) -> value + score);
			player++;
		}
		return scoresByPlayer.values().stream().mapToInt(n -> n).max().orElse(-1);
	}

	private int solveTask2 (int nbPlayers, int nbMarbles) {
		return solveTask1(nbPlayers, nbMarbles * 100);
	}

}

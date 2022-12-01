package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;




// http://adventofcode.com/2016/day/11
public class Day11 extends AdventOfCode {

	private static class Item implements Comparable {

		final int element;
		final boolean generator;
		Item other;

		Item(int element, boolean isGenerator) {
			this.element= element;
			this.generator= isGenerator;
		}

		@Override
		public String toString() {
			return element + "-" + (generator ? "G" : "M");
		}

		@Override
		public int compareTo(Object o) {
			if (this == o) {
				return 0;
			}
			Item other= (Item)o;
			if (this.element == other.element) {
				return Boolean.compare(this.generator, other.generator);
			}
			return Integer.compare(this.element, other.element);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			Item other= (Item)obj;
			return this.generator == other.generator && this.element == other.element;
		}

		@Override
		public int hashCode() {
			int result = element;
			result = 31 * result + (generator ? 1 : 0);
			return result;
		}
	}

	private static class Floor extends LinkedList<Item> {

		final int no;

		Floor(int no) {
			super();
			this.no= no;
		}

		Floor(Floor other) {
			super(other);
			this.no= other.no;
		}

		boolean isValid() {
			boolean hasSoleChip= false;
			boolean hasGenerator= false;
			for (int i = 0; i < size(); i++) {
				Item item= get(i);
				if (!item.generator) {
					// it's a module: the next item _has_ to be its corresponding generator
					if (i < size() - 1) {
						Item next= get(i+1);
						if (!(next.generator && next.element == item.element)) {
							hasSoleChip= true;
						}
					} else {
						hasSoleChip= true;
					}
				}
				if (item.generator) {
					hasGenerator= true;
				}
			}
			return !hasGenerator || !hasSoleChip;
		}

		@Override
		public String toString() {
			if (size() == 0) {
				return "-";
			}

			StringBuilder sb = new StringBuilder();
			for (Item item : this) {
				if (sb.length() > 0) {
					sb.append(' ');
				}
				sb.append(item);
			}
			return sb.toString();
		}
	}

	private static class Pair implements Comparable {
		final int x;
		final int y;

		Pair(int x, int y) {
			this.x= x;
			this.y= y;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Pair pair = (Pair) o;

			if (x != pair.x) return false;
			return y == pair.y;
		}

		@Override
		public int hashCode() {
			int result = x;
			result = 31 * result + y;
			return result;
		}

		@Override
		public int compareTo(Object o) {
			if (this == o) {
				return 0;
			}
			Pair other= (Pair)o;
			if (this.x != other.x) {
				return Integer.compare(this.x, other.x);
			} else {
				return Integer.compare(this.y, other.y);
			}
		}
	}

	private static class PairState {
		int floorNo;
		List<Pair> pairs;

		PairState(int floorNo, List<Pair> pairs) {
			this.floorNo= floorNo;
			Collections.sort(pairs);
			this.pairs= pairs;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			PairState pairState = (PairState) o;

			if (floorNo != pairState.floorNo) return false;
			return pairs.equals(pairState.pairs);
		}

		@Override
		public int hashCode() {
			int result = floorNo;
			result = 31 * result + pairs.hashCode();
			return result;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder()
									   .append(floorNo)
									   .append(" [");
			for (Pair p : pairs) {
				sb.append('(').append(p.x).append('/').append(p.y).append(')');
			}
			sb.append("]");
			return sb.toString();
		}
	}

	private static class Factory {
		final List<Floor> floors= new ArrayList<>();
		int moves= 0;
		int elevatorAt= 0;
		private PairState pairState;

		Factory(List<Floor> floors) {
			this.floors.addAll(floors);
		}

		Factory(Factory other) {
			for (Floor f : other.floors) {
				this.floors.add(new Floor(f));
			}
			this.elevatorAt= other.elevatorAt;
			this.moves= other.moves;
		}

		boolean isValid() {
			boolean valid= true;
			for (Floor f : floors) {
				valid&= f.isValid();
			}
			return valid;
		}

		boolean isFinished() {
			boolean finished= true;
			for (int i = 0; i < floors.size() - 1; i++) {
				finished&= floors.get(i).isEmpty();
			}
			finished&= !floors.get(floors.size() - 1).isEmpty();
			return finished;
		}

		Factory moveElevator(int direction) {
			this.elevatorAt+= direction;
			return this;
		}

		private Factory moveItem(int sourceFloorIndex, int itemIndex, int targetFloorIndex) {
			Item itemToMove= floors.get(sourceFloorIndex).remove(itemIndex);
			Floor targetFloor = floors.get(targetFloorIndex);

			this.elevatorAt= targetFloorIndex;
			this.moves++;

			targetFloor.add(itemToMove);
			Collections.sort(targetFloor);
			return this;
		}

		private Factory moveItems(int sourceFloorIndex, int idx1, int idx2, int targetFloorIndex) {
			Floor sourceFloor = floors.get(sourceFloorIndex);
			Item item1= sourceFloor.get(idx1);
			Item item2= sourceFloor.get(idx2);
			sourceFloor.remove(item1);
			sourceFloor.remove(item2);

			this.elevatorAt= targetFloorIndex;
			this.moves++;

			Floor targetFloor = floors.get(targetFloorIndex);
			targetFloor.add(item1);
			targetFloor.add(item2);
			Collections.sort(targetFloor);
			return this;
		}

		void addPossibleMoves(Set<Factory> nextMoves) {
			Set<Factory> possibleMoves= new HashSet<>();
			for (Factory elevatorMove : generateElevatorMoves()) {
				generateSingleItemMoves(possibleMoves, elevatorMove);
				generateDoubleItemMoves(possibleMoves, elevatorMove);
			}

			possibleMoves.parallelStream()
							// purge invalid moves
						   .filter(Factory::isValid)
						   .forEach(nextMoves::add);
		}

		// Helper
		private Set<Factory> generateElevatorMoves() {
			Set<Factory> moves= new HashSet<>();
			if (elevatorAt == 0) {
				// just move up
				moves.add(copyOf().moveElevator(1));
			} else if (elevatorAt == floors.size() - 1) {
				// just move down
				moves.add(copyOf().moveElevator(-1));
			} else {
				// move in both dirs
				moves.add(copyOf().moveElevator(1));
				moves.add(copyOf().moveElevator(-1));
			}
			return moves;
		}

		// move type 2: take 1 item
		private void generateSingleItemMoves(Set<Factory> possibleMoves, Factory f) {
			// "this.elevatorAt" points to the location *before* the elevator moved in the copy
			Floor floorBefore= f.floors.get(this.elevatorAt);
			for (int i = 0; i < floorBefore.size(); i++) {
				possibleMoves.add(f.copyOf().moveItem(this.elevatorAt, i, f.elevatorAt));
			}
		}

		// move type 3: take 2 items
		private void generateDoubleItemMoves(Set<Factory> possibleMoves, Factory f) {
			// "this.elevatorAt" points to the location *before* the elevator moved in the copy
			Floor sourceFloor= f.floors.get(this.elevatorAt);
			for (int i = 0; i < sourceFloor.size() - 1; i++) {
				for (int j = i + 1 ; j < sourceFloor.size(); j++) {
					possibleMoves.add(f.copyOf()
							   .moveItems(this.elevatorAt, i, j, f.elevatorAt));
				}
			}
		}

		private Factory copyOf() {
			return new Factory(this);
		}

		PairState getPairState() {
			if (this.pairState != null) {
				return this.pairState;
			}

			List<Pair> pairs= new ArrayList<>();
			for (Floor f : floors) {
				for (Item item : f) {
					if (item.generator) {
						pairs.add(new Pair(getFloor(item), getFloor(item.other)));
					}
				}
			}
			this.pairState= new PairState(elevatorAt, pairs);
			return this.pairState;
		}

		int getFloor(Item item) {
			for (Floor f : floors) {
				for (Item i : f) {
					if (i.equals(item)) {
						return f.no;
					}
				}
			}
			return -1;
		}

		@Override
		public String toString() {
			StringBuilder sb= new StringBuilder();
			for (int i= 0; i < floors.size(); i++) {
				Floor f= floors.get(i);
				sb.append("Floor ").append(i).append(" : ");
				if (elevatorAt == i) {
					sb.append("[E] ");
				} else {
					sb.append("    ");
				}
				sb.append(f);
				sb.append('\n');
			}
			return sb.toString();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (this == obj) {
				return true;
			}
			Factory other= (Factory)obj;
			return this.elevatorAt == other.elevatorAt && this.floors.equals(other.floors);
		}

		@Override
		public int hashCode() {
			int result = floors.hashCode();
			result = 31 * result + elevatorAt;
			return result;
		}
	}

	private static final Pattern GENERATOR_PATTERN = Pattern.compile("(\\w+) generator");
	private static final Pattern CHIP_PATTERN = Pattern.compile("(\\w+)-compatible");

	@Test
	@Override
	public void runTask1 () {
		int solution= 47;
		assertThat(solve(getInputLines()), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
		List<String> input = getInputLines();
		String extendedFirstFloor= input.get(0) +
										   "An elerium generator, " +
										   "An elerium-compatible microchip, " +
										   "A dilithium generator, " +
										   "A dilithium-compatible microchip.";
		input.set(0, extendedFirstFloor);

		int solution= 71;
		assertThat(solve(input), is(solution));
	}

	@Test
	public void runExample1() {
		List<String> input= Arrays.asList(
				"The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.",
				"The second floor contains a hydrogen generator.",
				"The third floor contains a lithium generator.",
				"The fourth floor contains nothing relevant.");
		assertThat(solve(input), is(11));
	}

	@Test
	public void runTask2Example1 () {
		List<String> input = getInputLines();
		String extendedFirstFloor= input.get(0) +
										   "A dilithium generator, " +
										   "A dilithium-compatible microchip.";
		input.set(0, extendedFirstFloor);
		int solution= 59;
		assertThat(solve(input), is(solution));
	}

	private int solve(List<String> inputLines) {
		List<Floor> floors = createFloors(inputLines);
		Factory factory= new Factory(floors);
		System.out.println("------- initial -------");
		System.out.println(factory);


		Set<Factory> solutionQueue= new HashSet<>();
		solutionQueue.add(factory);
		previousSolutions.addAll(solutionQueue);
		previousPairStates.put(factory.getPairState(), factory);

		Factory winner= solveBFS(solutionQueue, 0);

		System.out.println("Got WINNER factory: ");
		System.out.println(winner);
		System.out.println("\nMoves:");
		return winner != null ? winner.moves : -1;
	}

	private Set<Factory> previousSolutions= new HashSet<>();
	private Map<PairState, Factory> previousPairStates= new HashMap<>();

	private Factory solveBFS(Set<Factory> queue, int depth) {
		System.out.println("Depth is: " + depth + " | queue size: " + queue.size());
		if (depth > 100) {
			System.out.println("MAX depth reached!");
			return null;
		}

		Set<Factory> nextLevelQueue= new HashSet<>();
		for (Factory f : queue) {
			if (f.isFinished()) {
				return f;
			} else {
				f.addPossibleMoves(nextLevelQueue);
			}
		}

		System.out.println("Pruning...");
		int before= nextLevelQueue.size();
		nextLevelQueue.removeAll(previousSolutions);
		System.out.println("\t previous solution states. Pruned " + (nextLevelQueue.size() - before) + " moves");

		before= nextLevelQueue.size();
		pruneSimilarPairs(nextLevelQueue);
		System.out.println("\t similar pairs.            Pruned " + (nextLevelQueue.size() - before) + " moves");

		previousSolutions.addAll(nextLevelQueue);
		return solveBFS(nextLevelQueue, depth + 1);
	}

	private void pruneSimilarPairs(Set<Factory> nextLevelQueue) {
		Set<Factory> toRemove= new HashSet<>();
		for (Factory move : nextLevelQueue) {
			PairState movePairState = move.getPairState();
			if (previousPairStates.containsKey(movePairState)) {
				toRemove.add(move);
			} else {
				previousPairStates.put(movePairState, move);
			}
		}
		nextLevelQueue.removeAll(toRemove);
	}

	private List<Floor> createFloors(List<String> inputLines) {
		List<Floor> floors= new ArrayList<>();
		Map<String, Integer> elementToNumber= new HashMap<>();
		for (int i = 0; i < inputLines.size(); i++) {
			String inputLine = inputLines.get(i);
			Floor f = new Floor(i);
			List<Item> items = parse(inputLine, elementToNumber);
			Collections.sort(items);

			f.addAll(items);
			floors.add(f);
		}

		pairItems(floors);
		return floors;
	}

	private void pairItems(List<Floor> floors) {
		// search generators
		Map<Integer, Item> generators= new HashMap<>();
		for (Floor f : floors) {
			for (Item i : f) {
				if (i.generator) {
					generators.put(i.element, i);
				}
			}
		}
		// get items, map to generators
		for (Floor f : floors) {
			for (Item i : f) {
				if (!i.generator) {
					Item generator= generators.get(i.element);
					i.other= generator;
					generator.other= i;
				}
			}
		}
	}

	private List<Item> parse(String line, Map<String, Integer> elementToNumber) {
		List<Item> items = new ArrayList<>();
		Matcher m = GENERATOR_PATTERN.matcher(line);
		while (m.find()) {
			for (int i = 0; i < m.groupCount(); i++) {
				String element = m.group(i).split(" ")[0];
				int elementNumber= elementToNumber.getOrDefault(element, elementToNumber.size() + 1);
				elementToNumber.put(element, elementNumber);
				items.add(new Item(elementNumber, true));
			}
		}

		m= CHIP_PATTERN.matcher(line);
		while (m.find()) {
			for (int i = 0; i < m.groupCount(); i++) {
				String element = m.group(i).split("-")[0];
				int elementNumber= elementToNumber.getOrDefault(element, elementToNumber.size() + 1);
				elementToNumber.put(element, elementNumber);
				items.add(new Item(elementNumber, false));
			}
		}
		return items;
	}

}

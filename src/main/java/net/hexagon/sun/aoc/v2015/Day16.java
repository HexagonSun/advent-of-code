package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/16
public class Day16 extends AdventOfCode {

	private static final Pattern LINE_PATTERN= Pattern.compile("Sue (?<idx>\\d+):(.*)");
	private static final Pattern POSSESSION_PATTERN= Pattern.compile("(\\w+): (\\d+)");

	private enum Possession {
		CHILDREN,
		CATS,
		SAMOYEDS,
		POMERANIANS,
		AKITAS,
		VIZSLAS,
		GOLDFISH,
		TREES,
		CARS,
		PERFUMES
	}

	private static class Sue {
		private final int idx;
		private final Map<Possession, Integer> possessions= new HashMap<>(Possession.values().length);

		public Sue(int idx) {
			this.idx= idx;
		}

		public boolean matches(Map.Entry<Possession, Integer> entry, boolean exactNumberMatch) {
			return exactNumberMatch ? matches(entry) : matchesPart2(entry);
		}

		public boolean matches(Map.Entry<Possession, Integer> entry) {
			Integer value= possessions.get(entry.getKey());
			if (value == null) {
				// no data, assume match
				return true;
			}
			return entry.getValue().equals(value);
		}


		public boolean matchesPart2(Map.Entry<Possession, Integer> entry) {
			Integer value= possessions.get(entry.getKey());
			if (value == null) {
				// no data, assume match
				return true;
			}
			if (Possession.CATS == entry.getKey() || Possession.TREES == entry.getKey()) {
				return entry.getValue() < value;
			}
			if (Possession.POMERANIANS == entry.getKey() || Possession.GOLDFISH == entry.getKey()) {
				return entry.getValue() > value;
			}
			return entry.getValue().equals(value);
		}


		@Override
		public String toString() {
			return "Sue #" + idx;
		}
	}

	@Test
	@Override
	public void runTask1() {
		List<String> input= getInputLines();

		Map<Possession, Integer> target= new HashMap<>(2);
		target.put(Possession.CHILDREN, 3);
		target.put(Possession.CATS, 7);
		target.put(Possession.SAMOYEDS, 2);
		target.put(Possession.POMERANIANS, 3);
		target.put(Possession.AKITAS, 0);
		target.put(Possession.VIZSLAS, 0);
		target.put(Possession.GOLDFISH, 5);
		target.put(Possession.TREES, 3);
		target.put(Possession.CARS, 2);
		target.put(Possession.PERFUMES, 1);

		assertThat(solveTask1(input, target), is(40));
	}

	@Test
	@Override
	public void runTask2() {
		List<String> input= getInputLines();

		Map<Possession, Integer> target= new HashMap<>(2);
		target.put(Possession.CHILDREN, 3);
		target.put(Possession.CATS, 7);
		target.put(Possession.SAMOYEDS, 2);
		target.put(Possession.POMERANIANS, 3);
		target.put(Possession.AKITAS, 0);
		target.put(Possession.VIZSLAS, 0);
		target.put(Possession.GOLDFISH, 5);
		target.put(Possession.TREES, 3);
		target.put(Possession.CARS, 2);
		target.put(Possession.PERFUMES, 1);

		assertThat(solveTask2(input, target), is(241));

	}

	@Test
	public void runExample1() {
		List<String> input = Arrays.asList(
				"Sue 1: goldfish: 9, cars: 0, samoyeds: 9",
				"Sue 2: perfumes: 5, trees: 8, goldfish: 8",
				"Sue 3: pomeranians: 2, akitas: 1, trees: 5",
				"Sue 4: goldfish: 10, akitas: 2, perfumes: 9");

		Map<Possession, Integer> target= new HashMap<>(2);
		target.put(Possession.TREES, 5);
		target.put(Possession.AKITAS, 1);
		target.put(Possession.CARS, 73);

		assertThat(solveTask1(input, target), is(3));
	}

	private int solveTask1(List<String> input, Map<Possession, Integer> target) {
		return solve(true, input, target);
	}

	private int solveTask2(List<String> input, Map<Possession, Integer> target) {
		return solve(false, input, target);
	}

	private int solve(boolean exactNumberMatch, List<String> input, Map<Possession, Integer> target) {
		List<Sue> aunties= parse(input);

		Sue matchingSue= findMatch(target, aunties, exactNumberMatch);
		return matchingSue.idx;
	}

	private Sue findMatch(Map<Possession, Integer> target, List<Sue> aunties, boolean exactNumberMatch) {
		for (Map.Entry<Possession, Integer> entry : target.entrySet()) {
			filterPossessions(aunties, entry, exactNumberMatch);
		}
		assert(aunties.size() == 1);
		return aunties.get(0);
	}

	private void filterPossessions(List<Sue> aunties, Map.Entry<Possession, Integer> entry, boolean exactNumberMatch) {
		Iterator<Sue> it= aunties.iterator();
		while (it.hasNext()) {
			Sue aunt= it.next();
			if (!aunt.matches(entry, exactNumberMatch)) {
				it.remove();
			}
		}
	}

	private List<Sue> parse(List<String> input) {
		List<Sue> aunties= new ArrayList<>(input.size());
		for (String line : input) {
			aunties.add(parse(line));
		}
		return aunties;
	}


	private Sue parse(String line) {
		Sue aunt= parseIdx(line);
		String possessionParts= getPossessionPart(line);
		addPossessions(aunt, possessionParts);
		return aunt;
	}


	private String getPossessionPart(String line) {
		String[] tokens = line.split(":\\s", 2);
		return tokens[1];
	}

	private Sue parseIdx(String line) {
		Matcher m= LINE_PATTERN.matcher(line);
		if (m.matches()) {
			Integer idx= Integer.valueOf(m.group("idx"));
			return new Sue(idx);
		}
		return null;
	}

	private void addPossessions(Sue aunt, String possessions) {
		String[] tokens = possessions.split(", ");
		for (String token : tokens) {
			Matcher m= POSSESSION_PATTERN.matcher(token);
			if (m.matches()) {
				addPossession(aunt, m);
			}
		}
	}

	private void addPossession(Sue aunt, Matcher m) {
		String name= m.group(1);
		String value= m.group(2);

		Possession p= Possession.valueOf(name.trim().toUpperCase());
		aunt.possessions.put(p, Integer.valueOf(value));
	}

}

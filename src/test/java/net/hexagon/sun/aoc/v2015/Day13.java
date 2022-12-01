package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/13
public class Day13 extends AdventOfCode {

	private static final class Constraint {
		private String name;
		private int value;
		private String targetName;
	}

	private static final class Person {
		private String name;
		Map<String, Integer> neighbours= new HashMap<>();

		public int scoreAgainst(String name) {
			Integer score = neighbours.get(name);
			return score == null ? 0 : score;
		}
	}

	private static final Pattern PATTERN= Pattern.compile("(.+) would (.+) (\\d+) happiness units by sitting next to (\\w+)\\.");

	@Test
	@Override
	public void runTask1() {
		List<String> input= getInputLines();
		assertThat(solveTask1(input), is(664));
	}

	@Test
	public void runExample1() {
		List<String> input= Arrays.asList(
				"Alice would gain 54 happiness units by sitting next to Bob.",
				"Alice would lose 79 happiness units by sitting next to Carol.",
				"Alice would lose 2 happiness units by sitting next to David.",
				"Bob would gain 83 happiness units by sitting next to Alice.",
				"Bob would lose 7 happiness units by sitting next to Carol.",
				"Bob would lose 63 happiness units by sitting next to David.",
				"Carol would lose 62 happiness units by sitting next to Alice.",
				"Carol would gain 60 happiness units by sitting next to Bob.",
				"Carol would gain 55 happiness units by sitting next to David.",
				"David would gain 46 happiness units by sitting next to Alice.",
				"David would lose 7 happiness units by sitting next to Bob.",
				"David would gain 41 happiness units by sitting next to Carol.");
		assertThat(solveTask1(input), is(330));
	}

	@Test
	@Override
	public void runTask2() {
		List<String> input= getInputLines();
		assertThat(solveTask2(input), is(640));
	}

	private int solveTask1(List<String> lines) {
		Map<String, Person> properties= parse(lines);
		return solveTask(properties);
	}

	private int solveTask2(List<String> lines) {
		Map<String, Person> properties= parse(lines);
		addMyself(properties);
		return solveTask(properties);
	}

	private int solveTask(Map<String, Person> properties) {
		// bruteforce: test all combinations
		int max= Integer.MIN_VALUE;
		List<List<Person>> perms = generatePermutations(new ArrayList<>(properties.values()));
		for (List<Person> list : perms) {
			int score= score(list);
			max= Math.max(max, score);
		}
		return max;
	}

	private void addMyself(Map<String, Person> properties) {
		String myName= "Foo";
		Person self= new Person();

		for (Person p : properties.values()) {
			p.neighbours.put(myName, 0);
			self.neighbours.put(p.name, 0);
		}
		properties.put(myName, self);
	}

	private int score(List<Person> list) {
		int score= 0;
		int size= list.size();
		for (int i = 0; i < list.size(); i++) {
			Person p= list.get(i);
			int nextIdx= i + 1 >= size ? 0 : i + 1;
			int prevIdx= i - 1 < 0 ? size - 1 : i - 1;

			int nextScore= p.scoreAgainst(list.get(nextIdx).name);
			int prevScore= p.scoreAgainst(list.get(prevIdx).name);

			score+= nextScore;
			score+= prevScore;
		}
		return score;
	}

	private Map<String, Person> parse(List<String> lines) {
		Map<String, Person> properties= new HashMap<>();
		for (String line : lines) {
			Constraint c = parse(line);
			processNeighbours(properties, c);
		}
		return properties;
	}

	private void processNeighbours(Map<String, Person> properties, Constraint c) {
		Person p= properties.get(c.name);
		if (p == null) {
			p= new Person();
			p.name= c.name;
			properties.put(p.name, p);
		}
		p.neighbours.put(c.targetName, c.value);
	}

	private Constraint parse(String line) {
		Matcher m = PATTERN.matcher(line);
		if (m.matches()) {
			int val = Integer.parseInt(m.group(3));
			if ("lose".equals(m.group(2))) {
				val *= -1;
			}
			Constraint cstr = new Constraint();
			cstr.name = m.group(1);
			cstr.value = val;
			cstr.targetName = m.group(4);
			return cstr;
		}
		return null;
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

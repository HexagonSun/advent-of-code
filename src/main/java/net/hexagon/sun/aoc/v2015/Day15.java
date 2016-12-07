package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/15
public class Day15 extends AdventOfCode {

	private static final class Ingredient {
		private String name;
		private int capacity;
		private int durability;
		private int flavor;
		private int texture;
		private int calories;
	}

	private static final Pattern PATTERN= Pattern.compile("(\\w+): capacity (-?\\d+), durability (-?\\d+), "
														  +"flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)");
	
	@Test
	@Override
	public void runTask1() {

	}

	@Test
	@Override
	public void runTask2() {

	}

	@Test
	public void runExample1() {
		List<String> input = Arrays.asList(
				"Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8",
				"Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3");
		assertThat(solveTask1(input), is(42));
	}

	private int solveTask1(List<String> input) {
		List<Ingredient> ingredients= parse(input);

		return 0;
	}


	private int score(List<Ingredient> ingredients) {

		return 0;
	}

	private List<Ingredient> parse(List<String> lines) {
		List<Ingredient> ingredients= new ArrayList<>(lines.size());
		for (String line : lines) {
			ingredients.add(parse(line));
		}
		return ingredients;
	}

	private Ingredient parse(String line) {
		// TODO
		Matcher m = PATTERN.matcher(line);
		if (m.matches()) {
			Ingredient i= new Ingredient();
			i.name= m.group(1);
			i.capacity= Integer.parseInt(m.group(2));
			i.durability= Integer.parseInt(m.group(3));
			i.flavor= Integer.parseInt(m.group(4));
			i.texture= Integer.parseInt(m.group(5));
			i.calories= Integer.parseInt(m.group(6));
			return i;
		}
		return null;
	}
}

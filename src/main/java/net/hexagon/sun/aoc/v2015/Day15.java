package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

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

		int getValue(int index) {
			switch (index) {
				case 0:
					return capacity;
				case 1:
					return durability;
				case 2:
					return flavor;
				case 3:
					return texture;
				default:
					return durability;
			}
		}
	}

	private static class Partitioner {

		private final int maxTerms;
		private Set<Set<Integer>> partitions = new HashSet<>();

		Partitioner(int maxTerms) {
			this.maxTerms = maxTerms;
		}

		Set<Set<Integer>> partition(int n) {
			partition(n, n, new TreeSet<>());
			return partitions.stream()
						   .filter(t -> t.size() <= maxTerms)
//					.peek(set -> System.out.println("\t" + set))
						   .collect(Collectors.toSet());
		}

		void partition(int n, int max, Set<Integer> terms) {
			if (n == 0) {
				partitions.add(terms);
			}

			int len = Math.min(max, n);
			for (int i = len; i >= 1; i--) {
				Set<Integer> newTerms = new TreeSet<>();
				newTerms.addAll(terms);
				newTerms.add(i);
				if (newTerms.size() > maxTerms) {
					// no need to chase this further
					return;
				}
				partition(n - i, i, newTerms);
			}
		}
	}

	private static class Permutator {

		private Permutator() {
		}

		static long factorial(int n) {
			if (n > 20 || n < 0) throw new IllegalArgumentException(n + " is out of range");
			return LongStream.rangeClosed(2, n).reduce(1, (a, b) -> a * b);
		}

		static <T> List<T> permutation(long no, List<T> items) {
			return permutationHelper(no,
					new LinkedList<>(Objects.requireNonNull(items)),
					new ArrayList<>());
		}

		private static <T> List<T> permutationHelper(long no, LinkedList<T> in, List<T> out) {
			if (in.isEmpty()) return out;
			long subFactorial = factorial(in.size() - 1);
			out.add(in.remove((int) (no / subFactorial)));
			return permutationHelper((int) (no % subFactorial), in, out);
		}

//		@SafeVarargs
//		@SuppressWarnings("varargs") // Creating a List from an array is safe
//		public static <T> Stream<Stream<T>> of(T... items) {
//			List<T> itemList = Arrays.asList(items);
//			return LongStream.range(0, factorial(items.length))
//						   .mapToObj(no -> permutation(no, itemList).stream());
//		}

		public static <T> Stream<Stream<T>> of(List<T> itemList) {
			return LongStream.range(0, factorial(itemList.size()))
						   .mapToObj(no -> permutation(no, itemList).stream());
		}

	}

	private static final Pattern PATTERN = Pattern.compile("(\\w+): capacity (-?\\d+), durability (-?\\d+), "
																   + "flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)");

	@Test
	@Override
	public void runTask1() {
		long solution= 222870L;
		assertThat(solveTask1(getInputLines()), is(solution));
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
		long solution= 62842880L;
		assertThat(solveTask1(input), is(solution));
	}

	private long solveTask1(List<String> input) {
		List<Ingredient> ingredients = parse(input);
		int len = ingredients.size();
		Set<Set<Integer>> partitions = new Partitioner(len).partition(100);
		long maxScore = 0;
		for (Set<Integer> terms : partitions) {
			List<Integer> t = new ArrayList<>(terms);

			Optional<Long> maxOfCombinations= Permutator.of(t)
													  .map(s -> s.collect(Collectors.toList()))
//													  .peek(x -> System.out.println("combination x: " + x))
													  .map(combination -> calculateScore(ingredients, combination))
//													  .peek(s -> System.out.println("score: " + s))
													  .max(Comparator.naturalOrder());
			if (maxOfCombinations.isPresent()) {
				long score= maxOfCombinations.get();
				if (score > maxScore) {
					maxScore= score;
				}
			}
		}
		return maxScore;
	}

	private long calculateScore(List<Ingredient> ingredients, List<Integer> combination) {
		// index 0 -> capacity
		// index 1 -> durability
		// index 2 -> flavor
		// index 3 -> texture
		return getTotal(0, ingredients, combination) *
			   getTotal(1, ingredients, combination) *
			   getTotal(2, ingredients, combination) *
			   getTotal(3, ingredients, combination);
	}

	private long getTotal(int index, List<Ingredient> ingredients, List<Integer> t) {
		long total = 0L;
		for (int i = 0; i < ingredients.size(); i++) {
			int ingredientValue = ingredients.get(i).getValue(index);
			int termValue = t.size() >= i + 1 ? t.get(i) : 0;
			total += ingredientValue * termValue;
		}
		return total > 0 ? total : 0L;
	}

	private List<Ingredient> parse(List<String> lines) {
		List<Ingredient> ingredients = new ArrayList<>(lines.size());
		for (String line : lines) {
			ingredients.add(parse(line));
		}
		return ingredients;
	}

	private Ingredient parse(String line) {
		Matcher m = PATTERN.matcher(line);
		if (m.matches()) {
			Ingredient i = new Ingredient();
			i.name = m.group(1);
			i.capacity = Integer.parseInt(m.group(2));
			i.durability = Integer.parseInt(m.group(3));
			i.flavor = Integer.parseInt(m.group(4));
			i.texture = Integer.parseInt(m.group(5));
			i.calories = Integer.parseInt(m.group(6));
			return i;
		}
		return null;
	}
}
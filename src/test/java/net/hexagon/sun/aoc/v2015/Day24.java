package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;

public class Day24 extends AdventOfCode {

	// https://stackoverflow.com/a/28515742
	private static class Combinations {
		private static <E> Stream<List<E>> combinations(List<E> input, int size) {
			if (size == 0) {
				return Stream.of(Collections.emptyList());
			} else {
				return IntStream.range(0, input.size())
							   .boxed()
							   .flatMap (i -> combinations(input.subList(i + 1, input.size()), size - 1).map(t -> pipe(input.get(i), t)));
			}
		}

		private static <E> List<E> pipe(E head, List<E> tail) {
			List<E> newList = new ArrayList<>(tail);
			newList.add(0, head);
			return newList;
		}
	}

	@Test @Override
	public void runTask1() {
		List<Integer> packages= getInputLines().stream().map(Integer::valueOf).sorted().collect(Collectors.toList());
		long qe = solve(packages, 3);
		assertThat(qe, CoreMatchers.is(10_723_906_903L));
	}

	@Test @Override
	public void runTask2() {
		List<Integer> packages= getInputLines().stream().map(Integer::valueOf).sorted().collect(Collectors.toList());
		long qe = solve(packages, 4);
		assertThat(qe, CoreMatchers.is(74850409L));
	}

	@Test
	public void runExample1() {
		int[] input= {1, 2, 3, 4, 5, 7, 8, 9, 10, 11};
		long qe = solve(input, 3);
		assertThat(qe, CoreMatchers.is(99L));
	}

	@Test
	public void runExample2() {
		int[] input= {1, 2, 3, 4, 5, 7, 8, 9, 10, 11};
		long qe = solve(input, 4);
		assertThat(qe, CoreMatchers.is(44L));
	}

	private long solve(int[] input, int nbPiles) {
		List<Integer> packages= IntStream.of(input).boxed().sorted().collect(Collectors.toList());
		return solve(packages, nbPiles);
	}

	private long solve(List<Integer> packages, int nbPiles) {
		System.out.println("input packages: " + packages);
		int sum= sum(packages);
		System.out.println("\tsum is        : " + sum);
		int equilibrium= sum / nbPiles;
		System.out.println("\tequilibrium is: " + equilibrium);

		List<List<Integer>> validPackages= new ArrayList<>();


		int heuristicsThreshold= 7;
		for (int i = 0; i < packages.size() && i < heuristicsThreshold; i++) {
			Combinations.combinations(packages, i)
					.filter(s -> sum(s) == equilibrium)
					.forEach(validPackages::add);
		}

		Optional<Long> first = validPackages.stream()
									   .sorted(Comparator.comparingInt(List::size))
									   .peek(l -> System.out.println("first l? " + l))
									   .map(Day24::product)
									   .findFirst();

		System.out.println("Got first QE! " + first);
		return first.orElse(-1L);
	}

	private static int sum(List<Integer> packages) {
		return packages.stream().mapToInt(Integer::intValue).sum();
	}

	private static long product(List<Integer> packages) {
		return packages.stream()
					   .mapToLong(i -> i)
					   .reduce(1L, (a,b) -> a*b);
	}

}

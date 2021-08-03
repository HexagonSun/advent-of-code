package net.hexagon.sun.aoc.lib;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Combinatorics {

	public static class Combinations {

		public static <T> List<List<T>> fromList (List<T> input) {
			if (input == null) {
				return Collections.emptyList();
			}
			return input.stream()
						   .flatMap(first -> input.stream().map(second -> Arrays.asList(first, second)))
						   .collect(Collectors.toList());
		}

		public static <T, U> List<U> fromList (List<T> input, BiFunction<T, T, U> processor) {
			return fromList(input, processor, (x) -> true);
		}

		public static <T, U> List<U> fromList (List<T> input, BiFunction<T, T, U> processor, Predicate<U> postFilter) {
			if (input == null) {
				return Collections.emptyList();
			}
			return input.stream()
						   .flatMap(first -> input.stream().map(second -> processor.apply(first, second)))
						   .filter(postFilter)
						   .collect(Collectors.toList());
		}

	}
}

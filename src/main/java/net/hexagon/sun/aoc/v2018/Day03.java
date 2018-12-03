package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import net.hexagon.sun.aoc.lib.Combinatorics;
import org.junit.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day03 extends AdventOfCode {

	private static class Patch {
		private final int id;
		private final Rectangle rect;

		Patch (int id, int left, int top, int width, int height) {
			this.id = id;
			rect = new Rectangle(left, top, width, height);
		}

		boolean intersects (Patch other) {
			return rect.intersects(other.rect);
		}

		Rectangle intersectionSquares (Patch other) {
			return rect.intersection(other.rect);
		}
	}

	// matches input of the form "#1264 @ 923,620: 29x18"
	private static final Pattern PATTERN = Pattern.compile("^#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)$");

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(getInputLines()), is(107663L));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(getInputLines()), is(1166));
	}

	@Test
	public void runExample1 () {
		List<String> input = Arrays.asList(
				"#1 @ 1,3: 4x4",
				"#2 @ 3,1: 4x4",
				"#3 @ 5,5: 2x2"
		);
		assertThat(solveTask1(input), is(4L));
	}

	@Test
	public void runExample2 () {
		List<String> input = Arrays.asList(
				"#1 @ 1,3: 4x4",
				"#2 @ 3,1: 4x4",
				"#3 @ 5,5: 2x2",
				// #4: same patch where #1 & #2 already overlap
				"#4 @ 3,3: 2x2"
		);
		assertThat(solveTask1(input), is(4L));
	}

	@Test
	public void runTask2Example1 () {
		List<String> input = Arrays.asList(
				"#1 @ 1,3: 4x4",
				"#2 @ 3,1: 4x4",
				"#3 @ 5,5: 2x2"
		);
		assertThat(solveTask2(input), is(3));
	}

	@Test
	public void runTask2Example2 () {
		List<String> input = Arrays.asList(
				"#1 @ 1,3: 4x4",
				"#2 @ 3,1: 4x4",
				"#3 @ 5,5: 2x2",
				"#4 @ 3,1: 4x4"
		);
		assertThat(solveTask2(input), is(3));
	}


	private long solveTask1 (List<String> input) {
		List<Patch> patches = input.stream().map(this::parse).collect(Collectors.toList());
		List<Rectangle> intersections = Combinatorics.Combinations.fromList(patches, this::getIntersectingRectangle, Objects::nonNull);

		long overlapCount = 0;
		Map<Integer, Map<Integer, Boolean>> taken = new HashMap<>();
		for (Rectangle intersection : intersections) {
			for (int k = intersection.x; k < intersection.x + intersection.width; k++) {
				Map<Integer, Boolean> row = taken.getOrDefault(k, new HashMap<>());
				taken.putIfAbsent(k, row);

				for (int l = intersection.y; l < intersection.y + intersection.height; l++) {
					Boolean tile = row.getOrDefault(l, false);
					if (!tile) {
						overlapCount++;
					}
					row.put(l, true);
				}

			}
		}

		return overlapCount;
	}

	private Rectangle getIntersectingRectangle (Patch pi, Patch pj) {
		if (pi.id == pj.id) {
			return null;
		}
		if (pi.intersects(pj)) {
			return pi.intersectionSquares(pj);
		}
		return null;
	}

	private int solveTask2 (List<String> input) {
		List<Patch> patches = input.stream().map(this::parse).collect(Collectors.toList());

		Set<Integer> allIds = patches.stream().map(p -> p.id).collect(Collectors.toSet());
		Set<Integer> patchesThatOverlap = Combinatorics.Combinations.fromList(patches, this::getOverlappingPatches)
												  .stream()
												  .flatMap(Collection::stream)
												  .collect(Collectors.toSet());

		allIds.removeAll(patchesThatOverlap);
		System.out.println("all ids: " + allIds);
		if (allIds.size() > 1) {
			throw new IllegalStateException("There can only be one");
		}
		return allIds.iterator().next();
	}

	private List<Integer> getOverlappingPatches (Patch pi, Patch pj) {
		if (pi.id == pj.id) {
			return Collections.emptyList();
		}
		if (pi.intersects(pj)) {
			return Arrays.asList(pi.id, pj.id);
		}
		return Collections.emptyList();
	}

	private Patch parse (String input) {
		Matcher m = PATTERN.matcher(input);
		if (m.matches()) {
			return new Patch(
					Integer.parseInt(m.group(1)),
					Integer.parseInt(m.group(2)),
					Integer.parseInt(m.group(3)),
					Integer.parseInt(m.group(4)),
					Integer.parseInt(m.group(5))
			);
		}
		throw new IllegalStateException("parse error @ " + input);
	}

}

package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day03 extends AdventOfCode {

	private static class Patch {
		Rectangle2D r;
		int id;
		int left;
		int top;
		int width;
		int height;

		private Rectangle rect;

		public Rectangle getRect () {
			if (rect == null) {
				rect = new Rectangle(left, top, width, height);
			}
			return rect;
		}

		public boolean intersects (Patch other) {
			return getRect().intersects(other.getRect());
		}

		public Rectangle intersectionSquares (Patch other) {
			return getRect().intersection(other.getRect());
		}
	}

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

		List<Rectangle> intersections = new ArrayList<>();
		for (int i = 0; i < patches.size(); i++) {
			Patch pi = patches.get(i);
			for (int j = i; j < patches.size(); j++) {
				Patch pj = patches.get(j);
				if (pi.id == pj.id) {
					continue;
				}
				if (pi.intersects(pj)) {
					intersections.add(pi.intersectionSquares(pj));
				}
			}
		}

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

	private Patch parse (String input) {
//		#1264 @ 923,620: 29x18
		Matcher m = PATTERN.matcher(input);
		if (m.matches()) {
			Patch p = new Patch();
			p.id = Integer.parseInt(m.group(1));
			p.left = Integer.parseInt(m.group(2));
			p.top = Integer.parseInt(m.group(3));
			p.width = Integer.parseInt(m.group(4));
			p.height = Integer.parseInt(m.group(5));
			return p;
		}
		throw new IllegalStateException("parse error @ " + input);
	}

	private int solveTask2 (List<String> input) {
		List<Patch> patches = input.stream().map(this::parse).collect(Collectors.toList());

		Set<Integer> allIds = new HashSet<>();
		Set<Integer> patchesThatOverlap = new HashSet<>();

		for (int i = 0; i < patches.size(); i++) {
			Patch pi = patches.get(i);
			allIds.add(pi.id);
			for (int j = i; j < patches.size(); j++) {
				Patch pj = patches.get(j);
				allIds.add(pj.id);
				if (pi.id == pj.id) {
					continue;
				}
				if (pi.intersects(pj)) {
					patchesThatOverlap.add(pi.id);
					patchesThatOverlap.add(pj.id);
				}
			}
		}

		allIds.removeAll(patchesThatOverlap);
		System.out.println("all ids: " + allIds);
		if (allIds.size() > 1) {
			throw new IllegalStateException("There can only be one");
		}
		return allIds.iterator().next();
	}


}

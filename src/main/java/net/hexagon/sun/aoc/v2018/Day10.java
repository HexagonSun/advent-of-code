package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day10 extends AdventOfCode {

	private static class Data {
		Point pos;
		Point v;
	}

	@Test
	@Override
	public void runTask1 () {
		// there is no real return value, the string here is the result that was found empirically
		assertThat(solveTask1(getInputLines()), is("LRCXFXRP"));
	}

	@Test
	@Override
	public void runTask2 () {
		// there is no real return value, the number here is the result that was found empirically
		assertThat(solveTask2(getInputLines()), is(10630));
	}

	private List<String> sampleInput= Arrays.asList(
			"position=< 9,  1> velocity=< 0,  2>",
			"position=< 7,  0> velocity=<-1,  0>",
			"position=< 3, -2> velocity=<-1,  1>",
			"position=< 6, 10> velocity=<-2, -1>",
			"position=< 2, -4> velocity=< 2,  2>",
			"position=<-6, 10> velocity=< 2, -2>",
			"position=< 1,  8> velocity=< 1, -1>",
			"position=< 1,  7> velocity=< 1,  0>",
			"position=<-3, 11> velocity=< 1, -2>",
			"position=< 7,  6> velocity=<-1, -1>",
			"position=<-2,  3> velocity=< 1,  0>",
			"position=<-4,  3> velocity=< 2,  0>",
			"position=<10, -3> velocity=<-1,  1>",
			"position=< 5, 11> velocity=< 1, -2>",
			"position=< 4,  7> velocity=< 0, -1>",
			"position=< 8, -2> velocity=< 0,  1>",
			"position=<15,  0> velocity=<-2,  0>",
			"position=< 1,  6> velocity=< 1,  0>",
			"position=< 8,  9> velocity=< 0, -1>",
			"position=< 3,  3> velocity=<-1,  1>",
			"position=< 0,  5> velocity=< 0, -1>",
			"position=<-2,  2> velocity=< 2,  0>",
			"position=< 5, -2> velocity=< 1,  2>",
			"position=< 1,  4> velocity=< 2,  1>",
			"position=<-2,  7> velocity=< 2, -2>",
			"position=< 3,  6> velocity=<-1, -1>",
			"position=< 5,  0> velocity=< 1,  0>",
			"position=<-6,  0> velocity=< 2,  0>",
			"position=< 5,  9> velocity=< 1, -2>",
			"position=<14,  7> velocity=<-2,  0>",
			"position=<-3,  6> velocity=< 2, -1>");

	@Test
	public void runExample1 () {
		assertThat(solveTask1(sampleInput), is(138));
	}

	private int solveTask1 (List<String> input) {
		List<Data> data = parse(input);
		print(data);
		for (int i = 0; i < 10631; i++) {
			System.out.println("tick #" + (i + 1));
			tick(data);
			print(data);
		}
		return -1;
	}

	private void tick (List<Data> data) {
		for (Data d : data) {
			d.pos.translate(d.v.x, d.v.y);
		}
	}

	private void print(List<Data> data) {
		int minX= Integer.MAX_VALUE;
		int maxX= Integer.MIN_VALUE;
		int minY= Integer.MAX_VALUE;
		int maxY= Integer.MIN_VALUE;

		for (Data d : data) {
			if (d.pos.x < minX) {
				minX = d.pos.x;
			}
			if (d.pos.x > maxX) {
				maxX = d.pos.x;
			}
			if (d.pos.y < minY) {
				minY = d.pos.y;
			}
			if (d.pos.y > maxY) {
				maxY = d.pos.y;
			}
		}

		System.out.println("grid size: " + minX + " | " + maxX + " // " + minY + " | " + maxY);
		int offsetX = minX < 0 ? -minX : 0;
		int offsetY = minY < 0 ? -minY : 0;

		int extra = 200;
		int width= extra + maxX - minX;
		int heigth=extra + maxY - minY;
		System.out.println("height: " + heigth + " width: " + width);

		// heuristic: width/height must not be too large,
		// the points must be near each other to form a message
		//
		// the exact value/threshold is found through trial & error, i.e. observing the printed width/heigth
		// with ever increasing number of ticks.

		if (heigth > 210) {
			System.out.println("diff too large, skipping print");
			return;
		}

		// now we print
		char[][] grid= new char[heigth][width];
		for (int i = 0; i < heigth; i++) {
			Arrays.fill(grid[i], ' ');
		}

		for (Data d : data) {
			grid[d.pos.y + offsetY][d.pos.x + offsetX] = '█';
		}

		for (int i = 0; i < heigth; i++) {
			char[] row= grid[i];
			for (int j = 0; j < width; j++) {
				System.out.print(row[j]);
			}
			System.out.println("");
		}
	}

	private int solveTask2 (List<String> input) {
		return -1;
	}

	private List<Data> parse (List<String> input) {
		return input.stream().map(this::parse).collect(Collectors.toList());
	}


	private static final Pattern PATTERN= Pattern.compile(".*<\\s*(-?\\d+),\\s*(-?\\d+)>.*<\\s*(-?\\d+),\\s*(-?\\d+)>.*");

	private Data parse (String line) {
		Matcher m = PATTERN.matcher(line);
		if (m.matches()) {
			Data d= new Data();
			d.pos= new Point(Integer.valueOf(m.group(1)), Integer.valueOf(m.group(2)));
			d.v= new Point(Integer.valueOf(m.group(3)), Integer.valueOf(m.group(4)));
			return d;
		}
		throw new IllegalStateException("parse error on line " + line);
	}

}

package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.awt.*;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day14 extends AdventOfCode {

	/**
	 * From {@link Day10}
	 */
	public static class CircularList {

		public final List<Integer> data;
		private int position= 0;
		private int skipSize= 0;

		public CircularList(int size) {
			this.data= IntStream.rangeClosed(0, size - 1)
							   .boxed()
							   .collect(Collectors.toList());
		}

		public void reverse(int sublistLength) {
			if (position + sublistLength < data.size()) {
				reverseContiguous(sublistLength);
			} else {
				reverseCircular(sublistLength);
			}
			position= (position + sublistLength + skipSize) % data.size();
			skipSize++;
		}

		private void reverseContiguous(int sublistLength) {
			List<Integer> sub= data.subList(position, position + sublistLength);
			Collections.reverse(sub);
			for (int i= position; i < position + sublistLength; i++) {
				data.set(i, sub.get(i - position));
			}
		}

		private void reverseCircular(int sublistLength) {
			List<Integer> sublistToTail= data.subList(position, data.size());
			List<Integer> sublistFromHead= data.subList(0, sublistLength - sublistToTail.size());
			List<Integer> fullSublist= new ArrayList<>(sublistToTail);
			fullSublist.addAll(sublistFromHead);
			Collections.reverse(fullSublist);

			for (int i= position; i < position + sublistLength; i++) {
				data.set(i % data.size(), fullSublist.get(i - position));
			}

		}

	}

	// https://en.wikipedia.org/wiki/Connected-component_labeling
	// "one component at a time"
	private static class CCL {

		private final char[][] data;

		char currentLabel= 'x';
		int regionCount= 0;

		CCL(char[][] data) {
			this.data= data;
		}

		int process() {
			for (int i = 0; i < data.length; i++) {
				char[] row= data[i];
				for (int j = 0; j < row.length; j++) {
					char pixel= row[j];

					if (isCandidate(pixel)) {
						row[j]= currentLabel;
						markRegion(new Point(i, j));
						// all current regions marked. replace with empty cells, increase counter;
						clearRegion();
						regionCount++;
					}
				}
			}
			return regionCount;
		}

		private void clearRegion() {
			for (char[] row : data) {
				for (int j = 0; j < row.length; j++) {
					if (row[j] == currentLabel) {
						row[j] = '0';
					}
				}
			}
		}

		private void markRegion(Point point) {
			Set<Point> queue = new HashSet<>();
			queue.add(point);
			while (!queue.isEmpty()) {
				Iterator<Point> it= queue.iterator();
				Point pixel= it.next();
				it.remove();
				queue.addAll(getNeighbours(pixel));
			}
		}

		private Collection<? extends Point> getNeighbours(Point pixel) {
			List<Point> neighbourQueue= new ArrayList<>();
			int x= pixel.x;
			int y= pixel.y;

			Point north= new Point(x - 1, y);
			Point east= new Point(x, y+1);
			Point south= new Point(x + 1, y);
			Point west= new Point(x, y - 1);

			addCandidate(neighbourQueue, north);
			addCandidate(neighbourQueue, east);
			addCandidate(neighbourQueue, south);
			addCandidate(neighbourQueue, west);
			return neighbourQueue;
		}

		private void addCandidate(List<Point> neighbourQueue, Point pixel) {
			if (!inBounds(pixel)) {
				return;
			}
			char p= data[pixel.x][pixel.y];
			if (isCandidate(p)) {
				data[pixel.x][pixel.y] = currentLabel;
				neighbourQueue.add(pixel);
			}
		}

		private boolean inBounds(Point pixel) {
			return pixel.x >= 0 && pixel.x < 128 && pixel.y >= 0 && pixel.y < 128;
		}

		private boolean isCandidate(char pixel) {
			return !(pixel == '0' || pixel == currentLabel);
		}
	}

	@Test
	@Override
	public void runTask1() {
		assertThat(solveTask1("jzgqcdpd"), is(8074));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solveTask2("jzgqcdpd"), is(1212));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1("flqrgnkx"), is(8108));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask2("flqrgnkx"), is(1242));
	}

	private int solveTask1(String input) {
		List<String> rows= new ArrayList<>();

		for (int i = 0; i < 128; i++) {
			String rowInput= input + "-" + i;
			rows.add(toBinary(knotHash(rowInput)));
		}

		return rows.stream()
					   .map(s -> s.replaceAll("0", ""))
					   .map(String::length)
					   .mapToInt(i -> i)
					   .sum();
	}

	private int solveTask2(String input) {
		char[][] rows= new char[128][128];
		for (int i = 0; i < 128; i++) {
			String rowInput= input + "-" + i;
			String s= knotHash(rowInput);
			String bin= toBinary(s);
			rows[i]= bin.toCharArray();
		}

		return new CCL(rows).process();
	}

	private String toBinary(String input) {
		StringBuilder s= new StringBuilder();
		for (char c : input.toCharArray()) {
			String binaryString= Integer.toString(Integer.parseInt("" + c, 16), 2);
			String padded= String.format("%4s", binaryString).replace(' ', '0');
			s.append(padded);
		}
		return s.toString();
	}

	private String knotHash(String input) {
		List<Integer> lengths= toAsciiCodes(input);
		lengths.addAll(Arrays.asList(17, 31, 73, 47, 23));

		CircularList cl = new CircularList(256);
		for (int i = 0; i < 64; i++) {
			performRound(cl, lengths);
		}

		int[] hash = denseHash(cl.data);
		return toHexString(hash);
	}

	private void performRound(CircularList cl, List<Integer> lengths) {
		for (Integer len : lengths) {
			cl.reverse(len);
		}
	}

	private String toHexString(int[] hash) {
		StringBuilder sb = new StringBuilder(hash.length * 2);
		for (Integer x : hash) {
			sb.append(String.format("%02x", x));
		}
		return sb.toString();
	}

	private int[] denseHash(List<Integer> data) {
		int[] hash= new int[16];
		for (int i = 0; i < 16; i++) {
			int nibble= data.get((i * 16));
			for (int j = 1; j < 16; j++) {
				nibble^= data.get((i * 16) + j);
			}
			hash[i]= nibble;
		}
		return hash;
	}

	private List<Integer> toAsciiCodes(String input) {
		return CharBuffer.wrap(input)
					   .chars()
					   .boxed()
					   .collect(Collectors.toList());
	}
}

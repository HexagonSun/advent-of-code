package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day10 extends AdventOfCode {

	private static class CircularList {

		private final List<Integer> data;
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

	@Test
	@Override
	public void runTask1() {
		assertThat(solveTask1(getInputAsString()), is(13760));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solveTask2(getInputAsString()), is("2da93395f1a6bb3472203252e3b17fe5"));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1(5,"3, 4, 1, 5"), is(12));
	}

	@Test
	public void runExample1Task2() {
		assertThat(solveTask2(""), is("a2582a3a0e66e6e86e3812dcb672a272"));
		assertThat(solveTask2("AoC 2017"), is("33efeb34ea91902bb2f59c9920caa6cd"));
		assertThat(solveTask2("1,2,3"), is("3efbe78a8d82f29979031a4aa0b16a9d"));
		assertThat(solveTask2("1,2,4"), is("63960835bcdc130f0b66d7ff4f6a5a8e"));
	}

	private int solveTask1(String input) {
		return solveTask1(256, input);
	}

	private int solveTask1(int listSize, String input) {
		List<Integer> lengths= parseCSV(input);
		CircularList cl = new CircularList(listSize);
		performRound(cl, lengths);
		System.out.println("CL[0] = " + cl.data.get(0));
		System.out.println("CL[1] = " + cl.data.get(1));
		return cl.data.get(0) * cl.data.get(1);
	}

	private void performRound(CircularList cl, List<Integer> lengths) {
		for (Integer len : lengths) {
			cl.reverse(len);
		}
	}

	private List<Integer> parseCSV(String input) {
		return Arrays.stream(input.split(","))
									  .map(String::trim)
									  .map(Integer::parseInt)
									  .collect(Collectors.toList());
	}


	private String solveTask2(String input) {
		List<Integer> lengths= toAsciiCodes(input);
		lengths.addAll(parseCSV("17, 31, 73, 47, 23"));

		CircularList cl = new CircularList(256);
		for (int i = 0; i < 64; i++) {
			performRound(cl, lengths);
		}

		int[] hash = denseHash(cl.data);
		return toHexString(hash);
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

package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day17 extends AdventOfCode {

	@Test
	@Override
	public void runTask1() {
		assertThat(solveTask1(382), is(1561));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solveTask2(382), is(33454823));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1(3), is(638));
	}

	private static class Buffer {

		private List<Integer> data= new LinkedList<>();
		private int step;

		private int currentLength= 1;
		private int currentPosition= 0;

		private int indexOfZero= 0;
		private int valueAfterZero;

		Buffer(int step) {
			this.step= step;
			data.add(0);
		}

		void spin (int i, boolean partTwo) {
			int nextPos= (currentPosition + step) % currentLength;

			if (indexOfZero == nextPos) {
				valueAfterZero= i;
			}
			if (indexOfZero >= nextPos + 1) {
				indexOfZero++;
			}

			if (!partTwo) {
				data.add(nextPos + 1, i);
			}
			currentLength++;
			currentPosition= nextPos + 1;
		}
	}

	private int solveTask1 (int input) {
		Buffer buffer= new Buffer(input);
		for (int i = 1; i < 2018; i++) {
			buffer.spin(i, false);
		}
		return buffer.data.get(buffer.currentPosition + 1);
	}

	private int solveTask2 (int input) {
		Buffer buffer= new Buffer(input);
		for (int i = 1; i <= 50_000_000; i++) {
			buffer.spin(i, true);
		}
		return buffer.valueAfterZero;
	}
}

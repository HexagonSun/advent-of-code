package net.hexagon.sun.aoc;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/1
public class Day02 extends AdventOfCode {

	enum Pad {
		ONE, TWO, THREE,
		FOUR, FIFE, SIX,
		SEVEN, EIGHT, NINE;

		Pad up;
		Pad right;
		Pad down;
		Pad left;

		static {
			// direction handing could probably replaced by some arithmetic
			ONE.directions(ONE, TWO, FOUR, ONE);
			TWO.directions(TWO, THREE, FIFE, ONE);
			THREE.directions(THREE, THREE, SIX, TWO);
			FOUR.directions(ONE, FIFE, SEVEN, FOUR);
			FIFE.directions(TWO, SIX, EIGHT, FOUR);
			SIX.directions(THREE, SIX, NINE, FIFE);
			SEVEN.directions(FOUR, EIGHT, SEVEN, SEVEN);
			EIGHT.directions(FIFE, NINE, EIGHT, SEVEN);
			NINE.directions(SIX, NINE, NINE, EIGHT);
		}

		private void directions(Pad up, Pad right, Pad down, Pad left) {
			this.up= up;
			this.right= right;
			this.down= down;
			this.left= left;
		}

		public Pad getNext(char step) {
			switch (step) {
				case 'U': return this.up;
				case 'R': return this.right;
				case 'D': return this.down;
				case 'L': return this.left;
				default: return this;
			}
		}
	}

	@Test
	@Override
	public void runTask1 () {
		String solution= "76792";
		assertThat(solveTask1(getInputLines()), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
		solveTask2(getInputLines());
	}

	@Test
	public void runExample1() {
		List<String> input= Arrays.asList("ULL\n", "RRDDD\n", "LURDL\n", "UUUUD");
		assertThat(solveTask1(input), is("1985"));
	}

	private String solveTask1 (List<String> input) {
		Pad button= Pad.FIFE;
		StringBuilder solution= new StringBuilder();
		for (String line : input) {
			for (char step : line.trim().toCharArray()) {
				button= button.getNext(step);
			}
			solution.append(button.ordinal() + 1);
		}
		return solution.toString();
	}

	private String solveTask2 (List<String> input) {
		return "";
	}

}

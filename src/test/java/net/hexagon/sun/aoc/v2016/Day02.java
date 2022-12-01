package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/2
public class Day02 extends AdventOfCode {

	enum Pad {
		ONE, TWO, THREE,
		FOUR, FIFE, SIX,
		SEVEN, EIGHT, NINE,

						ONE_2,
				TWO_2, THREE_2, FOUR_2,
		FIFE_2, SIX_2, SEVEN_2, EIGHT_2, NINE_2,
				A_2, 	B_2, 	C_2,
						D_2;

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


			ONE_2.directions(ONE_2, ONE_2, THREE_2, ONE_2);
			TWO_2.directions(TWO_2, THREE_2, SIX_2, TWO_2);
			THREE_2.directions(ONE_2, FOUR_2, SEVEN_2, TWO_2);
			FOUR_2.directions(FOUR_2, FOUR_2, EIGHT_2, THREE_2);
			FIFE_2.directions(FIFE_2, SIX_2, FIFE_2, FIFE_2);
			SIX_2.directions(TWO_2, SEVEN_2, A_2, FIFE_2);
			SEVEN_2.directions(THREE_2, EIGHT_2, B_2, SIX_2);
			EIGHT_2.directions(FOUR_2, NINE_2, C_2, SEVEN_2);
			NINE_2.directions(NINE_2, NINE_2, NINE_2, EIGHT_2);
			A_2.directions(SIX_2, B_2, A_2, A_2);
			B_2.directions(SEVEN_2, C_2, D_2, A_2);
			C_2.directions(EIGHT_2, C_2, C_2, B_2);
			D_2.directions(B_2, D_2, D_2, D_2);
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
		String solution= "A7AC3";
		assertThat(solveTask2(getInputLines()), is(solution));
	}

	@Test
	public void runExample1() {
		List<String> input= Arrays.asList("ULL\n", "RRDDD\n", "LURDL\n", "UUUUD");
		assertThat(solveTask1(input), is("1985"));
	}

	@Test
	public void runTask2Example1() {
		List<String> input= Arrays.asList("ULL\n", "RRDDD\n", "LURDL\n", "UUUUD");
		assertThat(solveTask2(input), is("5DB3"));
	}

	private String solveTask1 (List<String> input) {
		return solve(input, Pad.FIFE, 1);
	}

	private String solveTask2 (List<String> input) {
		return solve(input, Pad.FIFE_2, -8);
	}

	private String solve(List<String> input, Pad start, int ordinalOffset) {
		Pad button= start;
		StringBuilder solution= new StringBuilder();
		for (String line : input) {
			for (char step : line.trim().toCharArray()) {
				button= button.getNext(step);
			}
			solution.append(Integer.toHexString(button.ordinal() + ordinalOffset).toUpperCase());
		}
		return solution.toString();
	}

}

package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/9
public class Day09 extends AdventOfCode {

	@Test
	@Override
	public void runTask1 () {
		String decompressed= solveTask1(getInputAsString());
		int solution= 74532;
		assertThat(decompressed.length(), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1("ADVENT"), is("ADVENT"));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask1("A(1x5)BC"), is("ABBBBBC"));
	}

	@Test
	public void runExample3() {
		assertThat(solveTask1("(3x3)XYZ"), is("XYZXYZXYZ"));
	}

	@Test
	public void runExample4() {
		assertThat(solveTask1("A(2x2)BCD(2x2)EFG"), is("ABCBCDEFEFG"));
	}

	@Test
	public void runExample5() {
		assertThat(solveTask1("(6x1)(1x3)A"), is("(1x3)A"));
	}

	@Test
	public void runExample6() {
		assertThat(solveTask1("X(8x2)(3x3)ABCY"), is("X(3x3)ABC(3x3)ABCY"));
	}

	private String solveTask1(String input) {
		boolean inMarker= false;
		boolean inRepetition= false;
		StringBuilder markerDefinition= new StringBuilder();
		int nbMarkerChars= 0;
		int nbMarkerRepeat= 0;
		StringBuilder markerRepeatText= new StringBuilder();

		StringBuilder sb= new StringBuilder();
		for (char c : input.toCharArray()) {
			if ('(' == c && !inMarker && !inRepetition) {
				inMarker= true;
				continue;
			}
			if (')' == c && inMarker && !inRepetition) {
				// end of marker text
				String[] tokens= markerDefinition.toString().split("x");
				nbMarkerChars= Integer.valueOf(tokens[0]);
				nbMarkerRepeat= Integer.valueOf(tokens[1]);

				inRepetition= true;

				// reset marker
				inMarker= false;
				continue;
			}

			if (inMarker) {
				markerDefinition.append(c);
				continue;
			}

			// add regular text, or expand marker
			if (nbMarkerChars >= 1) {
				markerRepeatText.append(c);
				nbMarkerChars--;

				if (nbMarkerChars == 0) {
					// this was the last marker char: expand!
					String repetitionText= markerRepeatText.toString();
					for (int i = 0; i < nbMarkerRepeat; i++) {
						sb.append(repetitionText);
					}
					// reset
					inRepetition= false;
					nbMarkerChars= 0;
					nbMarkerRepeat= 0;
					markerDefinition= new StringBuilder();
					markerRepeatText= new StringBuilder();
				}
			} else {
				sb.append(c);
			}


		}
		return sb.toString();
	}

}

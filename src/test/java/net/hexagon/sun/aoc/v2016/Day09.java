package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		long solution= 11558231665L;
		assertThat(solveTask2(getInputAsString()), is(solution));

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



	@Test
	public void runTask2Example1() {
		assertThat(solveTask2("ADVENT"), is(6L));
	}

	@Test
	public void runTask2Example2() {
		assertThat(solveTask2("A(1x5)BC"), is(7L));
	}

	@Test
	public void runTask2Example3() {
		assertThat(solveTask2("(3x3)XYZ"), is(9L));
	}

	@Test
	public void runTask2Example4() {
		assertThat(solveTask2("X(8x2)(3x3)ABCY"), is(20L));
	}

	@Test
	public void runTask2Example5() {
		assertThat(solveTask2("(27x12)(20x12)(13x14)(7x10)(1x12)A"), is(241920L));
	}

	@Test
	public void runTask2Example6() {
		assertThat(solveTask2("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN"), is(445L));
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

	private long solveTask2(String input) {
		return decompressedCount(input);
	}

	private static Pattern MARKER= Pattern.compile("\\((\\d+)x(\\d+)\\)");

	private long decompressedCount(String input) {
		if (input.isEmpty()) {
			return 0L;
		}

		long count= 0L;
		Matcher m= MARKER.matcher(input);
		if (m.find()) {
			int repeatLength= Integer.valueOf(m.group(1));
			int nbRepeats= Integer.valueOf(m.group(2));

			// add character data up to the first marker
			count+= input.indexOf('(');

			// check/evaluate subgroup
			int subgroupStart= input.indexOf(')') + 1;
			String innerText= input.substring(subgroupStart, subgroupStart + repeatLength);
			long subgroupCount= decompressedCount(innerText);
			count+= (subgroupCount * nbRepeats);

			// add character data *after* the first marker: recurse
			int indexAfter= subgroupStart + repeatLength;
			String restOfText= input.substring(indexAfter);
			count+= decompressedCount(restOfText);

		} else {
			// no marker in this substring
			count= input.length();
		}
		return count;
	}

}

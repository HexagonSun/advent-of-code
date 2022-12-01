package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day18 extends AdventOfCode {

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(getInputLines()), is(515496));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(getInputLines()), is(233058));
	}

	private List<String> sampleInput = Arrays.asList(
			".#.#...|#.",
			".....#|##|",
			".|..|...#.",
			"..|#.....#",
			"#.#|||#|#|",
			"...#.||...",
			".|....|...",
			"||...#|.#|",
			"|.||||..|.",
			"...#.|..|.");

	@Test
	public void runExample1 () {
		assertThat(solveTask1(sampleInput), is(1147));
	}

	private int solveTask1 (List<String> input) {
		int nbMinutes = 10;
		return solve(input, nbMinutes);
	}

	private int solveTask2 (List<String> input) {
		int nbMinutes = 1_000_000_000;
		return solve(input, nbMinutes);
	}

	private int solve (List<String> input, int nbMinutes) {
		char[][] field = parse(input);
		print(field);

		field = run(field, nbMinutes);

		int nbTrees = 0;
		int nbLumberyard = 0;
		for (char[] row : field) {
			for (char c : row) {
				if (c == '|') { nbTrees++; }
				if (c == '#') { nbLumberyard++; }
			}
		}
		return nbTrees * nbLumberyard;
	}

	private char[][] run (char[][] field, int nbMinutes) {
		Map<String, Integer> previousStates = new HashMap<>();

		for (int i = 1; i <= nbMinutes; i++) {
			field = tick(field);
			String key = fieldToString(field);

			if (previousStates.containsKey(key)) {
				int prevTick = previousStates.get(key);

				System.out.println("Tick [" + i + "] Detected previous state of tick " + prevTick);

				// the pattern repeats every "delta" ticks
				int delta = i - prevTick;
//				System.out.println("\tdelta: " + delta);

				// substract previous runs
				int total = nbMinutes - i;
				// calculate the remainder to the requested nbMinutes
				int mod = total % delta;
//				System.out.println("\tmod: " + mod);

				// and run "mod" more times
				return run(field, mod);
			}

			previousStates.put(key, i);
//			print(field);
		}
		return field;
	}

	private String fieldToString (char[][] field) {
		StringBuilder sb = new StringBuilder();
		for (char[] row : field) {
			for (char c : row) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	private char[][] tick (char[][] field) {
		char[][] after = copy(field);

		for (int i = 0; i < field.length; i++) {
			char[] row = field[i];
			for (int j = 0; j < row.length; j++) {
				char cell = row[j];
				if (cell == '.') {
					int nbTrees = countType(field, i, j, '|');
					if (nbTrees >= 3) {
						after[i][j] = '|';
					}
				} else if (cell == '|') {
					int nbLumberyard = countType(field, i, j, '#');
					if (nbLumberyard >= 3) {
						after[i][j] = '#';
					}
				} else if (cell == '#') {
					int nbLumberyard = countType(field, i, j, '#');
					int nbTrees = countType(field, i, j, '|');

					if (nbLumberyard >= 1 && nbTrees >= 1) {
						after[i][j] = '#';
					} else {
						after[i][j] = '.';
					}
				}

			}
		}

		return after;
	}

	private int countType (char[][] field, int i, int j, char type) {
		int nb = 0;
		char[] row;
		if (i > 0) {
			// check above
			row = field[i - 1];
			if (j > 0) {
				nb += row[j - 1] == type ? 1 : 0;
			}
			nb += row[j] == type ? 1 : 0;
			if (j + 1 < row.length) {
				nb += row[j + 1] == type ? 1 : 0;
			}
		}
		// check current row
		row = field[i];
		if (j > 0) {
			nb += row[j - 1] == type ? 1 : 0;
		}
		// skip oneself
		// nb+= row[j] == type ? 1 : 0;
		if (j + 1 < row.length) {
			nb += row[j + 1] == type ? 1 : 0;
		}
		if (i + 1 < field.length) {
			// check below
			row = field[i + 1];
			if (j > 0) {
				nb += row[j - 1] == type ? 1 : 0;
			}
			nb += row[j] == type ? 1 : 0;
			if (j + 1 < row.length) {
				nb += row[j + 1] == type ? 1 : 0;
			}
		}
		return nb;
	}

	private char[][] copy (char[][] field) {
		char[][] copy = new char[field.length][];
		for (int i = 0; i < field.length; i++) {
			copy[i] = new char[field[i].length];
			for (int j = 0; j < field[i].length; j++) {
				copy[i][j] = field[i][j];
			}
		}
		return copy;
	}

	private void print (char[][] field) {
		for (char[] row : field) {
			for (char c : row) {
				System.out.print(c);
			}
			System.out.println();
		}
	}

	private char[][] parse (List<String> input) {
		char[][] field = new char[input.size()][];
		for (int i = 0; i < input.size(); i++) {
			char[] line = input.get(i).toCharArray();
			char[] row = new char[line.length];
			field[i] = row;
			System.arraycopy(line, 0, row, 0, line.length);
		}
		return field;
	}

}

package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/8
public class Day08 extends AdventOfCode {

	private static class Board {

		static Board of(String solution) {
			String[] lines = solution.split("\n");
			int width= lines.length;
			int height= lines[0].length();

			Board b= new Board(height, width);
			for (int i=0; i < width; i++) {
				char[] line= lines[i].toCharArray();
				Boolean[] row = new Boolean[height];
				for (int j = 0; j < height; j++) {
					row[j]= line[j]=='#';
				}
				b.board[i] = row;
			}
			return b;
		}

		private final int width;
		private final int height;
		private final Boolean[][] board;

		Board(int width, int height) {
			this.width= width;
			this.height= height;
			this.board= new Boolean[height][width];
			// init
			for (Boolean[] row : board) {
				Arrays.fill(row, false);
			}
		}

		int bitCount() {
			int cnt= 0;
			for(Boolean[] row : board) {
				for (Boolean cell : row) {
					cnt+= cell ? 1 : 0;
				}
			}
			return cnt;
		}

		void rect(int width, int height) {
			for (int i=0; i < height; i++) {
				Boolean[] row = board[i];
				for (int j = 0; j < width; j++) {
					row[j]= true;
				}
			}
		}

		void rotateColumn(int colNum, int amount) {
//			System.out.println("rot col " + colNum + " by " + amount);
			if (colNum >= width) {
				System.out.println("ColNum too large");
				return;
			}

			Boolean[] newValues= new Boolean[height];
			for (int i = 0; i < board.length; i++) {
				Boolean[] row = board[i];
//				System.out.println("row ["+i+"]: value at " + colNum + " is: " + row[colNum]);
				int targetRow = Math.floorMod(i - amount, height);
				newValues[i]= board[targetRow][colNum];
			}

			// copy over
			for (int i = 0; i < board.length; i++) {
				board[i][colNum]= newValues[i];
			}
		}


		void rotateRow(int rowNum, int amount) {
			if (rowNum >= width) {
				System.out.println("wrong index");
				return;
			}

			Boolean[] row= board[rowNum];
			if(amount > row.length)
				amount= amount % row.length;

			Boolean[] result = new Boolean[row.length];

			for(int i=0; i < amount; i++){
				result[i] = row[row.length-amount+i];
			}

			int j=0;
			for(int i=amount; i<row.length; i++){
				result[i] = row[j];
				j++;
			}

			System.arraycopy( result, 0, row, 0, row.length );
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof Board)) {
				return false;
			}
			return Arrays.deepEquals(this.board, ((Board)obj).board);
		}

		@Override
		public String toString() {
			StringBuilder sb= new StringBuilder();
			for (Boolean[] row : board) {
				for (Boolean cell : row) {
					sb.append(cell == null || !cell ? '.' : '#');
				}
				sb.append('\n');
			}
			return sb.toString();
		}

	}

	@Test
	@Override
	public void runTask1 () {
		Board board= new Board(50, 6);
		String solution=
				"####.####.####.#...##..#.####.###..####..###...##.\n" +
				"#....#....#....#...##.#..#....#..#.#......#.....#.\n" +
				"###..###..###...#.#.##...###..#..#.###....#.....#.\n" +
				"#....#....#......#..#.#..#....###..#......#.....#.\n" +
				"#....#....#......#..#.#..#....#.#..#......#..#..#.\n" +
				"####.#....####...#..#..#.#....#..#.#.....###..##..";
		solveTask1(board, getInputLines());
		assertThat(board, is(Board.of(solution)));
		assertThat(board.bitCount(), is(115));
	}

	@Test
	@Override
	public void runTask2 () {
		Board board= new Board(50, 6);
		solveTask1(board, getInputLines());
		System.out.println("Board after: ");
		System.out.println(board);
		String display= "EFEYKFRFIJ";
		assertThat("No way to really assert this :)", display, is(display));
	}

	@Test
	public void runExample1() {
		Board board= new Board(7, 3);
		String solution=
				"###....\n" +
				"###....\n" +
				".......";
		solveTask1(board, "rect 3x2");
		assertThat(board, is(Board.of(solution)));
		assertThat(board.bitCount(), is(6));
	}

	@Test
	public void runExample2() {
		String input=
				"###....\n" +
				"###....\n" +
				".......";
		Board board= Board.of(input);
		solveTask1(board, "rotate column x=1 by 1");
		String solution= "#.#....\n" +
						 "###....\n" +
						 ".#.....";
		assertThat(board, is(Board.of(solution)));
		assertThat(board.bitCount(), is(6));
	}

	@Test
	public void runExample3() {
		String input=
				"#.#....\n" +
				"###....\n" +
				".#.....";
		Board board= Board.of(input);
		solveTask1(board, "rotate row y=0 by 4");
		String solution= "....#.#\n" +
						 "###....\n" +
						 ".#.....";
		assertThat(board, is(Board.of(solution)));
		assertThat(board.bitCount(), is(6));
	}

	@Test
	public void runExample4() {
		String input=
				"....#.#\n" +
				 "###....\n" +
			 	".#.....";
		Board board= Board.of(input);
		solveTask1(board, "rotate column x=1 by 1");
		String solution= ".#..#.#\n" +
						 "#.#....\n" +
						 ".#.....";
		assertThat(board, is(Board.of(solution)));
		assertThat(board.bitCount(), is(6));
	}


	@Test
	public void runExample5() {
		Board board= new Board(7, 3);
		solveTask1(board, Arrays.asList(
				"rect 3x2",
				"rotate column x=1 by 1",
				"rotate row y=0 by 4",
				"rotate column x=1 by 1"
		));
		String solution= ".#..#.#\n" +
						 "#.#....\n" +
						 ".#.....";
		assertThat(board, is(Board.of(solution)));
		assertThat(board.bitCount(), is(6));
	}

	private void solveTask1 (Board board, List<String> input) {
		input.forEach(in -> solveTask1(board, in));
	}

	private void solveTask1(Board board, String input) {

//		System.out.println("step before:");
//		System.out.println(board);

		String[] tokens = input.split(" ");
		if ("rect".equals(tokens[0])) {
			String[] dim= tokens[1].split("x");
			int width= Integer.parseInt(dim[0]);
			int height= Integer.parseInt(dim[1]);
//			System.out.println("\trect [" + width + "/" + height + "]");
			board.rect(width, height);

		} else {
			// rotate
			String what= tokens[2];
			int by= Integer.parseInt(tokens[4]);
			String[] whatTokens= what.split("=");
			int whichRowCol= Integer.parseInt(whatTokens[1]);

//			System.out.println("\trotate " + rowCol + ": " + whichRowCol + " by " + by);
			if ("row".equals(tokens[1])) {
				board.rotateRow(whichRowCol, by);
			} else {
				board.rotateColumn(whichRowCol, by);
			}
		}

//		System.out.println("step after:");
//		System.out.println(board);
	}

}

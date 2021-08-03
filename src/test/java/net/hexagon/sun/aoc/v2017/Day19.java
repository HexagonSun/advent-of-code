package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.awt.*;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day19 extends AdventOfCode {

	private static class Packet {
		Point pos;
		char direction;
		String letters= "";
		public int nbSteps;
	}

	@Test
	@Override
	public void runTask1() {
		Point start= new Point(0, 13);
		assertThat(solveTask1(getInputAsString(), start), is("FEZDNIVJWT"));
	}

	@Test
	@Override
	public void runTask2() {
		Point start= new Point(0, 13);
		assertThat(solveTask2(getInputAsString(), start), is(17200));
	}

	@Test
	public void runExample1() {
		Point start= new Point(0, 5);
		assertThat(solveTask1("     |          \n" +
								  "     |  +--+    \n" +
								  "     A  |  C    \n" +
								  " F---|----E|--+ \n" +
								  "     |  |  |  D \n" +
								  "     +B-+  +--+ ", start), is("ABCDEF"));
	}

	@Test
	public void runExample2() {
		Point start= new Point(0, 5);
		assertThat(solveTask2("     |          \n" +
									  "     |  +--+    \n" +
									  "     A  |  C    \n" +
									  " F---|----E|--+ \n" +
									  "     |  |  |  D \n" +
									  "     +B-+  +--+ ", start), is(38));
	}

	private String solveTask1 (String input, Point start) {
		Packet packet= solve(input, start);
		return packet.letters;
	}

	private int solveTask2 (String input, Point start) {
		Packet packet= solve(input, start);
		return packet.nbSteps;
	}


	private Packet solve (String input, Point start) {
		char[][] maze = createMaze(input);

		Packet p= new Packet();
		p.direction= 'd';
		p.pos= start;

		return runMaze(maze, p);
	}

	private Packet runMaze (char[][] maze, Packet packet) {
		int maxY= maze[0].length;
		char current= maze[packet.pos.x][packet.pos.y];
		checkLetter(packet, current);

		Point nextPos= packet.pos;
		char nextDirection = packet.direction;
		while(true) {
			if (nextDirection == 'd') {
				// move down to the next +
				nextPos= new Point(nextPos.x + 1, nextPos.y);
			} else if (nextDirection == 'u') {
				// move up to the next +
				nextPos= new Point(nextPos.x - 1, nextPos.y);
			} else if (nextDirection == 'l') {
				// move left
				nextPos= new Point(nextPos.x, nextPos.y - 1);
			}else {
				// move right to the next +
				nextPos= new Point(nextPos.x, nextPos.y + 1);
			}
			packet.nbSteps++;

			if (nextPos.x < 0 || nextPos.x >= maze.length || nextPos.y < 0 || nextPos.y >= maxY) {
				return packet;
			}

			current= maze[nextPos.x][nextPos.y];
			if (current == ' ') {
				return packet;
			}
			checkLetter(packet, current);
			if (isPlus(current)) {
				nextDirection= changeDir(maze, nextPos, nextDirection);
			}
		}
	}

	private char changeDir (char[][] maze, Point position, char direction) {
		// checkUp, checkDown, checkLeft, checkRight

		if (direction != 'd' && position.x > 0) {
			// check up (only if not currently moving down (and coming from UP)
			char upChar= maze[position.x - 1][position.y];
			if (!isPlus(upChar) && upChar != ' ') {
				return 'u';
			}
		}


		if (direction != 'u' && position.x < maze.length - 1) {
			// check down (only if not currently moving down
			char upChar= maze[position.x + 1][position.y];
			if (!isPlus(upChar) && upChar != ' ') {
				return 'd';
			}
		}


		if (direction != 'r' && position.y > 0) {
			// check left (only if not currently moving left
			char upChar= maze[position.x][position.y - 1];
			if (!isPlus(upChar) && upChar != ' ') {
				return 'l';
			}
		}


		if (direction != 'l' && position.y < maze[0].length - 1) {
			// check right (only if not currently moving right
			char upChar= maze[position.x][position.y + 1];
			if (!isPlus(upChar) && upChar != ' ') {
				return 'r';
			}
		}
		return ' ';
	}

	private boolean isPlus (char current) {
		return current == '+';
	}

	private void checkLetter (Packet packet, char current) {
		if (Character.isAlphabetic(current)) {
			packet.letters+= current;
		}
	}

	private char[][] createMaze (String input) {
		String[] rows= input.split("\n");
		char[][] maze= new char[rows.length][];
		for (int i = 0; i < rows.length; i++) {
			String line = rows[i];
			char[] lineChar= new char[210];
			Arrays.fill(lineChar, ' ');
			char[] src= line.toCharArray();
			System.arraycopy(src, 0, lineChar, 0, src.length);
			maze[i]= lineChar;
		}
		return maze;
	}

}

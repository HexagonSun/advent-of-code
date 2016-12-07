package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/18
public class Day18 extends AdventOfCode {

	private static final List<String> EXAMPLE_INITIAL_STATE= Arrays.asList(".#.#.#",
																		   "...##.",
																		   "#....#",
																		   "..#...",
																		   "#.#..#",
																		   "####..");
	private static final List<String> EXAMPLE_INITIAL_STATE_TASK_2= Arrays.asList( "##.#.#",
																				   "...##.",
																				   "#....#",
																				   "..#...",
																				   "#.#..#",
																				   "####.#");

	@Test
	@Override
	public void runTask1() {
		String solution= solveTask1(getInputLines(), 100);
		assertThat(countLights(solution), is(768));
	}

	@Test
	@Override
	public void runTask2() {
		String solution= solveTask2(getInputLines(), 100);
		assertThat(countLights(solution), is(781));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1(EXAMPLE_INITIAL_STATE, 1), is("..##..\n" +
															"..##.#\n" +
															"...##.\n" +
															"......\n" +
															"#.....\n" +
															"#.##..\n"));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask1(EXAMPLE_INITIAL_STATE, 2), is("..###.\n" +
															"......\n" +
															"..###.\n" +
															"......\n" +
															".#....\n" +
															".#....\n"));
	}

	@Test
	public void runExample3() {
		assertThat(solveTask1(EXAMPLE_INITIAL_STATE, 3), is("...#..\n" +
															"......\n" +
															"...#..\n" +
															"..##..\n" +
															"......\n" +
															"......\n"));
	}

	@Test
	public void runExample4() {
		String solution= solveTask1(EXAMPLE_INITIAL_STATE, 4);
		assertThat(solution, is("......\n" +
								"......\n" +
								"..##..\n" +
								"..##..\n" +
								"......\n" +
								"......\n"));
		assertThat(countLights(solution), is(4));
	}


	@Test
	public void runExampleIdentity() {
		assertThat(solveTask1(EXAMPLE_INITIAL_STATE, 0), is(".#.#.#\n" +
															"...##.\n" +
															"#....#\n" +
															"..#...\n" +
															"#.#..#\n" +
															"####..\n"));
	}


	@Test
	public void runTask2Example1() {
		assertThat(solveTask2(EXAMPLE_INITIAL_STATE_TASK_2, 1), is( "#.##.#\n" +
																	"####.#\n" +
																	"...##.\n" +
																	"......\n" +
																	"#...#.\n" +
																	"#.####\n"));
	}

	@Test
	public void runTask2Example2() {
		assertThat(solveTask2(EXAMPLE_INITIAL_STATE_TASK_2, 2), is( "#..#.#\n" +
																	"#....#\n" +
																	".#.##.\n" +
																	"...##.\n" +
																	".#..##\n" +
																	"##.###\n"));
	}

	@Test
	public void runTask2Example3() {
		assertThat(solveTask2(EXAMPLE_INITIAL_STATE_TASK_2, 3), is( "#...##\n" +
																	"####.#\n" +
																	"..##.#\n" +
																	"......\n" +
																	"##....\n" +
																	"####.#\n"));
	}


	@Test
	public void runTask2Example4() {
		assertThat(solveTask2(EXAMPLE_INITIAL_STATE_TASK_2, 4), is( "#.####\n" +
																	"#....#\n" +
																	"...#..\n" +
																	".##...\n" +
																	"#.....\n" +
																	"#.#..#\n"));
	}

	@Test
	public void runTask2Example5() {
		String solution = solveTask2(EXAMPLE_INITIAL_STATE_TASK_2, 5);
		assertThat(solution, is("##.###\n" +
								".##..#\n" +
								".##...\n" +
								".##...\n" +
								"#.#...\n" +
								"##...#\n"));
		assertThat(countLights(solution), is(17));
	}

	private String solveTask1(List<String> lines, int steps) {
		return solve(lines, steps, false);
	}

	private String solveTask2(List<String> lines, int steps) {
		return solve(lines, steps, true);
	}

	private String solve(List<String> lines, int steps, boolean withStuckCorners) {
		boolean[][] grid= toGrid(lines);
		int currentStep= steps;
		while(currentStep-- > 0) {
			grid= step(grid, withStuckCorners);
		}
		return asString(grid);
	}

	private boolean[][] step(boolean[][] grid, boolean withStuckCorners) {
		int len= grid.length;
		boolean[][] next= new boolean[len][len];
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				next[i][j]= processNeighbours(grid, len, i, j);
			}
		}
		if (withStuckCorners) {
			next[0][0]= true;
			next[0][len-1]= true;
			next[len-1][0]= true;
			next[len-1][len-1]= true;
		}
		return next;
	}

	private boolean processNeighbours(boolean[][] grid, int len, int i, int j) {
		int cnt= countNeighbours(grid, len, i, j);
		boolean nextState;
		if (grid[i][j]) {
			nextState= (cnt == 2 || cnt == 3);
		} else {
			nextState= cnt==3;
		}
		return nextState;
	}

	private int countNeighbours(boolean[][] grid, int len, int gridX, int gridY) {
		int xFrom= gridX - 1 < 0 ? 0 : gridX - 1;
		int xTo  = gridX + 1 >= len ? gridX : gridX + 1;
		int yFrom= gridY - 1 < 0 ? 0 : gridY - 1;
		int yTo  = gridY + 1 >= len ? gridY : gridY + 1;

//		System.out.println("counting neighbours of [" + gridX + "]["+gridY+"]");
//		printGrid(grid, gridX, gridY);

		int cnt= 0;
		for (int i= xFrom; i <= xTo; i++) {
			for (int j = yFrom; j <= yTo; j++) {
				if (i == gridX && j == gridY) {
					// skip self
					continue;
				}
//				System.out.println("[" + gridX + "]["+gridY+"] : processing neighbour at [" + i + "]["+j+"]");
				if (grid[i][j]) {
					cnt++;
				}
			}
		}
//		System.out.println("count is: " + cnt + "\n");
		return cnt;
	}

	private boolean[][] toGrid(List<String> lines) {
		int size= lines.get(0).length();
		boolean[][] grid= new boolean[size][size];

		for (int i = 0; i < lines.size(); i++) {
			char[] line= lines.get(i).toCharArray();
			for (int j = 0; j < line.length; j++) {
				grid[i][j]= (line[j] == '#');
			}
		}
		return grid;
	}

	private String asString(boolean[][] grid) {
		return asString(grid, -1, -1);
	}

	private String asString(boolean[][] grid, int posX, int posY) {
		StringBuilder sb= new StringBuilder(grid.length * grid.length + grid.length);
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				if (i == posX && j == posY) {
					sb.append("█");
				} else {
					sb.append(grid[i][j] == true ? '#' : '.');
				}
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	private void printGrid(boolean[][] grid, int posX, int posY) {
		System.out.println(asString(grid, posX, posY));
	}

	private void printGrid(boolean[][] grid) {
		System.out.println(asString(grid));
	}

	private int countLights(String solution) {
		// nasty way to count…
		int count = solution.length() - solution.replace("#", "").length();
		return count;
	}

}

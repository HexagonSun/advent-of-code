package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.awt.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day11 extends AdventOfCode {

	private static class Cell {

		int x;
		int y;
		int gridSerial;
		Integer level;

		Cell (int x, int y, int gridSerial) {
			this.x= x;
			this.y= y;
			this.gridSerial= gridSerial;
		}

		int getLevel() {
			if (level == null) {
				level= calculateLevel();
			}
			return level;
		}

		private Integer calculateLevel () {
			int rackId= this.x + 10;
			int powerLevel = rackId * y;
			powerLevel+= gridSerial;
			powerLevel *= rackId;
			int keep = powerLevel < 100 ? 0 : (powerLevel % 1000) / 100;
			int sub = keep - 5;
			return sub;
		}

		@Override
		public String toString () {
			return "[" + x + "/" + y + " @ " + level + "]";
		}
	}

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(Integer.valueOf(getInputAsString())), is(new Point(243, 17)));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(Integer.valueOf(getInputAsString())), is("233,228,12"));
	}

	@Test
	public void runExample1CellValues () {
		assertThat(new Cell(3, 5, 8).getLevel(), is(4));
		assertThat(new Cell(122, 79, 57).getLevel(), is(-5));
		assertThat(new Cell(217,196, 39).getLevel(), is(0));
		assertThat(new Cell(101,153, 71).getLevel(), is(4));
	}

	@Test
	public void runExample1 () {
		assertThat(solveTask1(18), is(new Point(33, 45)));
	}

	@Test
	public void runTask2Example1 () {
		assertThat(solveTask2(18), is("90,269,16"));
	}
	@Test
	public void runTask2Example2 () {
		assertThat(solveTask2(42), is("232,251,12"));
	}

	private Point solveTask1 (int gridSerial) {
		int gridSize= 300;
		Cell[][] grid = buildGrid(gridSerial, gridSize);
		return runGrid(grid, 3);
	}

	private String solveTask2 (int gridSerial) {
		int gridSize= 300;
		Cell[][] grid = buildGrid(gridSerial, gridSize);

		int maxValue= -1;
		Point maxPoint= null;
		int maxSideLength= -1;

		for (int i = 1; i <= 300; i++) {
			Point point= runGrid(grid, i);
			if (point.x < 0 || point.y < 0) {
				// no point found
				continue;
			}
			int sumAtPoint = sumSquare(grid, point.x, point.y, i);

			if (sumAtPoint > maxValue) {
//				System.out.println("new max @ " + point + " | sum = " + sumAtPoint + " | sideLen = " + i);
				maxValue= sumAtPoint;
				maxPoint= point;
				maxSideLength= i;
			}
		}
		return "" + maxPoint.x + "," + maxPoint.y + "," + maxSideLength;
	}

	private Point runGrid (Cell[][] grid, int sideLength) {
		int maxValue= -1;
		int maxX= -1;
		int maxY= -1;
		int xSize = grid.length - sideLength;
		int ySize = grid[0].length - sideLength;

		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				int sum = sumSquare(grid, i, j, sideLength);
				if (sum > maxValue) {
//					System.out.println("new max @ " + i + "/" + j + " | sum = " + sum);
					sum = sumSquare(grid, i, j, sideLength);
					maxValue= sum;
					maxX = i;
					maxY = j;
				}
			}
		}
		return new Point(maxX, maxY);
	}

	private void print (Cell[][] grid, int x, int y) {
		System.out.println("-----");
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				System.out.print("[" + grid[x + i][y+j].getLevel() + "] ");

			}
			System.out.println("");
		}
		System.out.println("-----");
	}

	private int sumSquare (Cell[][] grid, int x, int y, int sideLength) {
		int sum= 0;
		for (int i = 0; i < sideLength; i++) {
			Cell[] row= grid[x + i];
			for (int j = 0; j < sideLength; j++) {
				Cell c= row[y + j];
				sum+= c.getLevel();
			}
		}
		return sum;
	}

	private Cell[][] buildGrid (int gridSerial, int gridSize) {
		Cell[][] grid = new Cell[gridSize][gridSize];

		for (int i = 0; i < gridSize; i++) {
			Cell[] row= new Cell[gridSize];
			grid[i]= row;

			for (int j = 0; j < gridSize; j++) {
				Cell c= new Cell(i, j, gridSerial);
				c.getLevel();
				row[j]= c;
			}
		}
		return grid;
	}

}

package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day13 extends AdventOfCode {


	private static class Cell {
		int x;
		int y;
		char track;

		Cell (int x, int y, char track) {
			this.x = x;
			this.y = y;
			this.track = track;
		}

		@Override
		public String toString () {
			return "" + track;
		}
	}

	private static class Cart {
		int x;
		int y;
		// NESW -> 0 = ^, 1 = >, 2 = v, 3 = <
		int direction;

		int nextAtIntersection = 0;
		boolean movedInTick;

		Cart (int x, int y, int direction) {
			this.x = x;
			this.y = y;
			this.direction = direction;
		}

		void moveTo (Cell cell) {
			this.x = cell.x;
			this.y = cell.y;
			setDirectionFromCell(cell);
		}

		void setDirectionFromCell (Cell cell) {
			switch (cell.track) {
				case '/':
					switch (direction) {
						case 0:
							direction = 1;
							break; // from below
						case 1:
							direction = 0;
							break; // from left
						case 2:
							direction = 3;
							break; // from top
						default:
							direction = 2;        // from right
					}
					break;
				case '\\':
					switch (direction) {
						case 0:
							direction = 3;
							break; // from below
						case 1:
							direction = 2;
							break; // from left
						case 2:
							direction = 1;
							break; // from top
						default:
							direction = 0;        // from right
					}
					break;
				case '+':
					if (nextAtIntersection == 0) {
						// turn left
						switch (direction) {
							case 0: // ^
								direction = 3;
								break;
							case 1: // >
								direction = 0;
								break;
							case 2: // v
								direction = 1;
								break;
							default: // <
								direction = 2;
						}
					} else if (nextAtIntersection == 1) {
						// go straight: do nothing
					} else {
						// turn right
						switch (direction) {
							case 0: // ^
								direction = 1;
								break;
							case 1: // >
								direction = 2;
								break;
							case 2: // v
								direction = 3;
								break;
							default: // <
								direction = 0;
						}
					}
					nextAtIntersection = (nextAtIntersection + 1) % 3;
					break;
				default:
					// do nothing
			}
		}

		Point getNext () {
			int nextX = x;
			int nextY = y;
			switch (direction) {
				case 0: //  ^
					nextX = x;
					nextY = y - 1;
					break;
				case 1: // >
					nextX = x + 1;
					nextY = y;
					break;
				case 2: // v
					nextX = x;
					nextY = y + 1;
					break;
				case 3: // <
					nextX = x - 1;
					nextY = y;
					break;
				default:
					// do nothing
			}
			return new Point(nextX, nextY);
		}

		static int toDirection (char symbol) {
			switch (symbol) {
				case '^':
					return 0;
				case '>':
					return 1;
				case 'v':
					return 2;
				default:
					return 3;
			}
		}

		static char toSymbol (int direction) {
			switch (direction) {
				case 0:
					return '^';
				case 1:
					return '>';
				case 2:
					return 'v';
				default:
					return '<';
			}
		}

		@Override
		public String toString () {
			return "[" + x + "/" + y + "] " + toSymbol(direction);
		}
	}

	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(getInputLines()), is(new Point(14, 42)));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(getInputLines()), is(new Point(8, 7)));
	}

	@Test
	public void runExample1 () {
		List<String> sample1 = Arrays.asList(
				"|",
				"v",
				"|",
				"|",
				"|",
				"^",
				"|"
		);
		assertThat(solveTask1(sample1), is(new Point(0, 3)));
	}

	@Test
	public void runExample2 () {
		List<String> sample2 = Arrays.asList(
				"/->-\\        ",
				"|   |  /----\\",
				"| /-+--+-\\  |",
				"| | |  | v  |",
				"\\-+-/  \\-+--/",
				"  \\------/   ");
		assertThat(solveTask1(sample2), is(new Point(7, 3)));
	}

	@Test
	public void runTask2Example1 () {
		List<String> samplePart2 = Arrays.asList(
				"/>-<\\  ",
				"|   |  ",
				"| /<+-\\",
				"| | | v",
				"\\>+</ |",
				"  |   ^",
				"  \\<->/");
		assertThat(solveTask2(samplePart2), is(new Point(6, 4)));
	}

	private Point solveTask1 (List<String> input) {
		return solve(input,
				false,
				(point, carts) -> point != null,
				(point1, carts1) -> point1);
	}

	private Point solveTask2 (List<String> input) {
		return solve(input,
				true,
				(point, carts) -> countCarts(carts) <= 1,
				(point, carts) -> {
					Cart c = getFirst(carts);
					return new Point(c.x, c.y);
				});
	}

	private Point solve (List<String> input,
						 boolean removeColliding,
						 BiPredicate<Point, Cart[][]> exitCondition,
						 BiFunction<Point, Cart[][], Point> returnValue) {
		char[][] combined = parse(input);
		Cell[][] grid = buildGrid(combined);
		Cart[][] carts = buildCarts(combined);
		print(grid, carts);

		int iterations = 0;
		while (true) {
//			System.out.println("iter " + iterations);
			Point p = tick(grid, carts, removeColliding);
//			print(grid, carts);

			if (exitCondition.test(p, carts)) {
				return returnValue.apply(p, carts);
			}
			iterations++;
		}
	}

	private char[][] parse (List<String> input) {
		int yLen = input.size();
		int xLen = input.stream().mapToInt(String::length).max().orElse(0);

		char[][] combined = new char[xLen][yLen];
		for (int i = 0; i < xLen; i++) {
			combined[i] = new char[yLen];
		}

		for (int i = 0; i < yLen; i++) {
			char[] row = input.get(i).toCharArray();
			for (int j = 0; j < xLen; j++) {
				char target = j < row.length ? row[j] : ' ';
				combined[j][i] = target;
			}
		}
		return combined;
	}

	private Cell[][] buildGrid (char[][] combined) {
		Cell[][] grid = new Cell[combined.length][];

		for (int i = 0; i < combined.length; i++) {
			char[] row = combined[i];

			Cell[] cellRow = new Cell[row.length];
			grid[i] = cellRow;

			for (int j = 0; j < row.length; j++) {
				char current = row[j];
				if (current == '|' || current == '-' || current == '/' || current == '\\' || current == '+') {
					cellRow[j] = new Cell(i, j, current);
				} else if (current == '^' || current == 'v') {
					cellRow[j] = new Cell(i, j, '|');
				} else if (current == '>' || current == '<') {
					cellRow[j] = new Cell(i, j, '-');
				}
			}
		}
		return grid;
	}

	private Cart[][] buildCarts (char[][] combined) {
		Cart[][] carts = new Cart[combined.length][];

		for (int i = 0; i < combined.length; i++) {
			char[] row = combined[i];

			Cart[] cellRow = new Cart[row.length];
			carts[i] = cellRow;

			for (int j = 0; j < row.length; j++) {
				char current = row[j];
				if (current == '^' || current == '>' || current == 'v' || current == '<') {
					cellRow[j] = new Cart(i, j, Cart.toDirection(current));
				}
			}
		}
		return carts;
	}

	private Point tick (Cell[][] grid, Cart[][] carts, boolean removeColliding) {
		for (int i = 0; i < carts.length; i++) {
			Cart[] row = carts[i];
			for (int j = 0; j < row.length; j++) {
				Cart current = row[j];
				if (current == null || current.movedInTick) {
					continue;
				}

//				System.out.println("current cart: " + current);

				Point next = current.getNext();
				Cell nextCell = grid[next.x][next.y];

				current.moveTo(nextCell);

				// check collision
				if (carts[next.x][next.y] != null) {
					if (removeColliding) {
						row[j] = null;
						carts[next.x][next.y] = null;
					} else {
						System.out.println("xxx COLLISION xxx  @ " + next.x + " / " + next.y);
						return new Point(next.x, next.y);
					}
				} else {
					row[j] = null;
					carts[next.x][next.y] = current;
					current.movedInTick = true;
				}

//				System.out.println("\tafter moving: " + current);
			}
		}

		clearMovedFlag(carts);
//		System.out.println("end tick");
		return null;
	}

	private void clearMovedFlag (Cart[][] carts) {
		for (Cart[] row : carts) {
			for (Cart c : row) {
				if (c != null) {
					c.movedInTick = false;
				}
			}
		}
	}

	private void print (Cell[][] grid, Cart[][] carts) {
		System.out.println("---------------------------------");
		Cart[] firstRow = carts[0];

		for (int i = 0; i < firstRow.length; i++) {
			for (int j = 0; j < carts.length; j++) {
				Cart cart = carts[j][i];

				if (cart != null) {
					System.out.print(Cart.toSymbol(cart.direction));
				} else {
					Cell c = grid[j][i];
					System.out.print(c != null ? c.track : ' ');
				}
			}
			System.out.println();
		}
		System.out.println("---------------------------------");
	}

	private Cart getFirst (Cart[][] carts) {
		for (Cart[] row : carts) {
			for (Cart c : row) {
				if (c != null) {
					return c;
				}
			}
		}
		throw new IllegalStateException("no carts left");
	}

	private int countCarts (Cart[][] carts) {
		int sum = 0;
		for (Cart[] row : carts) {
			for (Cart c : row) {
				if (c != null) {
					sum++;
				}
			}
		}
		return sum;
	}

}

package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/17
public class Day17 extends AdventOfCode {

	@Test
	@Override
	public void runTask1() {
		List<String> input = getInputLines();
		assertThat(solveTask1(input, 150), is(1304));
	}

	@Test
	@Override
	public void runTask2() {
		List<String> input = getInputLines();
		assertThat(solveTask2(input, 150), is(18));
	}

	@Test
	public void runExample1() {
		List<String> input = Arrays.asList("20", "15", "10", "5", "5");
		assertThat(solveTask1(input, 25), is(4));
	}

	@Test
	public void runExample2() {
		List<String> input = Arrays.asList("20", "15", "10", "5", "5");
		assertThat(solveTask2(input, 25), is(3));
	}

	private int solveTask1(List<String> input, int targetAmount) {
		int[] containers= asInt(input);

		int[][] ways= calculateWays(containers, targetAmount);
		return calculateNumberOfWays(ways[targetAmount]);


	}

	private int solveTask2(List<String> input, int targetAmount) {
		int[] containers= asInt(input);
		int[][] ways= calculateWays(containers, targetAmount);

		return calculateMinContainers(ways);
	}

	private int[][] calculateWays(int[] coins, int targetAmount) {
		int[][] ways= new int[targetAmount + 1][coins.length];
		ways[0][0]= 1;

		for (int idxCoin = 0; idxCoin < coins.length; idxCoin++) {
			int denomination= coins[idxCoin];

			for (int remainder = targetAmount - denomination; remainder >= 0; remainder--) {

				for (int n = coins.length - 1; n > 0; n--) {
					ways[remainder + denomination][n] += ways[remainder][n - 1];
				}
			}
		}
//		print(ways);
		return ways;
	}

	/** count ways in the last row, the target amount */
	private int calculateNumberOfWays(int[] waysToTargetAmount) {
		int nbWays= 0;
		for (int i = 0; i < waysToTargetAmount.length; i++) {
			nbWays += waysToTargetAmount[i];
		}
		return nbWays;
	}

	private int calculateMinContainers(int[][] ways) {
//		print(ways);

		int minCount= -1;
		int[] lastRow= ways[ways.length - 1];
		for (int i = 0; i < lastRow.length; i++) {
			int value= lastRow[i];
			if (value > 0) {
				minCount= i;
				break;
			}
		}

		// System.out.println("Min containers: " + minCount);
		int nbMinCombo = lastRow[minCount];
//		System.out.println("Min combinations: " + nbMinCombo);
		return nbMinCombo;
	}

	private void print(int[][] matrix) {
		System.out.print("Matrix is now:");
		for (int i = 0; i < matrix.length; i++) {
			System.out.print("\n\t" + i + "\t");
			int[] row= matrix[i];
			for (int j = 0; j < row.length; j++) {
				System.out.print("[" + row[j] + "]");
			}
		}
		System.out.print("\n\n");
	}

	private int[] asInt(List<String> input) {
		int[] containers= new int[input.size()];
		for (int i = 0; i < input.size(); i++) {
			containers[i]= Integer.parseInt(input.get(i));
		}
		return containers;
	}

}

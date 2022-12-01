package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day21 extends AdventOfCode {

	static class Grid {
		final char[][] data;

		Grid() {
			this(".#." + "..#" + "###");
		}

		Grid (String flattenedGrid) {
			int len= (int) Math.sqrt(flattenedGrid.length());
			String[] split= flattenedGrid.split("(?<=\\G.{" + len + "})");
			data= new char[len][len];
			for (int i = 0; i < split.length; i++) {
				data[i]= split[i].toCharArray();
			}
		}

		Grid tick (Collection<Rule> rules) {
			List<String> blocks= toBlocks(data[0].length % 2 == 0 ? 2 : 3);

			List<String> outputBlocks= new ArrayList<>();
			for (String block : blocks) {
				String output = applyRules(block, rules);
				outputBlocks.add(output);
			}

			String collected= blocksToString(outputBlocks);
			return new Grid(collected);
		}

		// convert back
		private String blocksToString (List<String> outputBlocks) {
			int blocksPerLine= (int) Math.sqrt(outputBlocks.size());
			List<List<String>> blockLines= new ArrayList<>();
			for (int i = 0; i < blocksPerLine; i++) {
				List<String> row= new ArrayList<>();
				for (int j = 0; j < blocksPerLine; j++) {
					row.add(outputBlocks.get(i * blocksPerLine + j));
				}
				blockLines.add(row);
			}

			if (blockLines.size() == 1) {
				return blockLines.get(0).stream().collect(Collectors.joining(""));
			}

			StringBuilder output= new StringBuilder();
			for (List<String> blockRow : blockLines) {
				int subLength= (int)Math.sqrt(blockRow.get(0).length());

				for (int i = 0; i < subLength; i++) {
					int startIndex= i * subLength;
					int endIndex= startIndex + subLength;

					for (int j = 0; j < blocksPerLine; j++) {
						output.append(blockRow.get(j).substring(startIndex, endIndex));
					}
				}
			}
			return output.toString();
		}

		private String applyRules (String block, Collection<Rule> rules) {
			for (Rule r : rules) {
				if (r.from.equals(block)) {
					return r.to;
				}
			}
			throw new RuntimeException("failed to match, block = " + block);
		}

		private List<String> toBlocks (int nb) {
			int blockSize= data[0].length / nb;

			String[][] blocks = new String[blockSize][blockSize];
			for (int i = 0; i < data.length; ) {
				String[] blockLine = new String[blockSize];
				Arrays.fill(blockLine, "");
				blocks[i / nb]= blockLine;

				processRow(nb, data[i++], blockLine);
				processRow(nb, data[i++], blockLine);

				if (nb == 3) {
					processRow(nb, data[i++], blockLine);
				}
			}
			return blockToString(blocks);
		}

		private List<String> blockToString (String[][] blocks) {
			List<String> result= new ArrayList<>();
			for (String[] blockRow : blocks) {
				result.addAll(Arrays.asList(blockRow));
			}
			return result;
		}

		private void processRow (int nb, char[] rowOfGrid, String[] blockLine) {
			String[] blocks1= rowToBlock(rowOfGrid, nb);
			addTo(blockLine, blocks1);
		}

		private void addTo (String[] blockLine, String[] blocksOfRow) {
			for (int i = 0; i < blocksOfRow.length; i++) {
				blockLine[i]+= blocksOfRow[i];
			}
		}

		// cut up "fullRow" into segments that are each "nb" long
		private String[] rowToBlock (char[] fullRow, int nb) {
			int blockLength= fullRow.length / nb;
			String[] blockOfThisLine= new String[blockLength];
			Arrays.fill(blockOfThisLine, "");

			for (int i = 0; i < blockLength; i++) {
				for (int j = 0; j < nb; j++) {
					blockOfThisLine[i]+= fullRow[i*nb + j];
				}
			}
			return blockOfThisLine;
		}

		int getPixelCount () {
			int cnt= 0;
			for (char[] row : data) {
				for (char c : row) {
					cnt+= '#' == c ? 1 : 0;
				}
			}
			return cnt;
		}

		@Override
		public String toString () {
			StringBuilder sb= new StringBuilder();
			for (char[] row : data) {
				sb.append("\t").append(row).append("\n");
			}
			return sb.toString();
		}
	}

	static class Rule {
		String from;
		String to;
		static Rule of (String from, String to) {
			Rule r= new Rule();
			r.from= from;
			r.to= to;
			return r;
		}

		@Override
		public String toString () {
			return "Rule{" +
						   "from='" + from + '\'' +
						   ", to='" + to + '\'' +
						   '}';
		}

		@Override
		public boolean equals (Object other) {
			return this.from.equals(((Rule)other).from);
		}

		@Override
		public int hashCode () {
			return from.hashCode();
		}
	}

	@Test
	@Override
	public void runTask1() {
		assertThat(solve(getInputLines(), 5), is(171));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solve(getInputLines(), 18), is(2498142));
	}

	@Test
	public void runExample1() {
		assertThat(solve(Arrays.asList("../.# => ##./#../...",
											".#./..#/### => #..#/..../..../#..#"), 2), is(12));
	}

	private int solve (List<String> input, int iterations) {
		Collection<Rule> rules = parse(input);
		Grid grid= new Grid();

		while(iterations-- > 0) {
			grid= grid.tick(rules);
		}
		return grid.getPixelCount();
	}

	private Collection<Rule> parse (List<String> input) {
		Set<Rule> rules= new HashSet<>();
		for (String line : input) {
			String[] ruleParts= line.split(" => ");
			String[] from= ruleParts[0].split("/");
			String[] to= ruleParts[1].split("/");
			rules.addAll(createRules(from, to));
		}
		return rules;
	}

	private Collection<Rule> createRules (String[] from, String[] to) {
		String toString= String.join("", to);
		Rule original= Rule.of(String.join("", from), toString);

		Rule rot1= Rule.of(rotate90(original.from), toString);
		Rule rot2= Rule.of(rotate90(rot1.from), toString);
		Rule rot3= Rule.of(rotate90(rot2.from), toString);

		Rule flipped1= Rule.of(flip(original.from), toString);
		Rule flipped2= Rule.of(flip(rot1.from), toString);
		Rule flipped3= Rule.of(flip(rot2.from), toString);
		Rule flipped4= Rule.of(flip(rot3.from), toString);

		return new HashSet<>(Arrays.asList(
				original,
				rot1, rot2, rot3,
				flipped1, flipped2, flipped3, flipped4));
	}

	private String rotate90 (String input) {
		// 0 1 		-->		2 0
		// 2 3		--> 	3 1
		//
		// 0 1 2	-->		6 3 0
		// 3 4 5	-->		7 4 1
		// 6 7 8	-->		8 5 2

		if (input.length() == 4) {
			return "" +
				   input.charAt(2) + input.charAt(0) +
				   input.charAt(3) + input.charAt(1);
		}

		return "" +
			   input.charAt(6) + input.charAt(3) + input.charAt(0) +
			   input.charAt(7) + input.charAt(4) + input.charAt(1) +
			   input.charAt(8) + input.charAt(5) + input.charAt(2);
	}

	private String flip (String input) {
		// 0 1 		-->		1 0
		// 2 3		--> 	3 2
		//
		// 0 1 2	-->		2 1 0
		// 3 4 5	-->		5 4 3
		// 6 7 8	-->		8 7 6

		if (input.length() == 4) {
			return "" +
				   input.charAt(1) + input.charAt(0) +
				   input.charAt(3) + input.charAt(2);
		}

		return "" +
			   input.charAt(2) + input.charAt(1) + input.charAt(0) +
			   input.charAt(5) + input.charAt(4) + input.charAt(3) +
			   input.charAt(8) + input.charAt(7) + input.charAt(6);
	}

}

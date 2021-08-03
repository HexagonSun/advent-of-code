package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day07 extends AdventOfCode {

	private static class Program {
		private String name;
		private int weigth;
		private List<Program> children= new ArrayList<>();
		private Program parent;

		private String getName() {
			return name;
		}

		private int getWeigth() {
			int w= weigth;
			for (Program p : children) {
				w+= p.getWeigth();
			}
			return w;
		}

		private boolean hasBalancedChildren() {
			return childWithInbalance() == null;
		}


		private Program childWithInbalance() {
			if (children.size() < 1) {
				// no inbalance with no children or just a single child
				return null;
			}

			int w= -1;
			List<Program> sortedChildren= new ArrayList<>(children);
			sortedChildren.sort(Comparator.comparingInt(other -> other.weigth));
			for (Program p : sortedChildren) {
				if (w < 0) {
					w= p.getWeigth();
				} else {
					if (p.getWeigth() != w) {
						return p;
					}
				}
			}
			return null;
		}

		private String childrenWithWeight() {
			StringBuilder sb= new StringBuilder();
			for (Program p : children) {
				sb.append("\t").append(p.name).append(" (").append(p.getWeigth()).append(")\n");
			}
			return sb.toString();
		}

		@Override
		public String toString() {
			return "Program{" +
						   "name='" + name + '\'' +
						   ", weigth=" + getWeigth() +
						   ", parent.name=" + (parent != null ? parent.name : "<null>") +
						   ", weigth with chilren=\n" + childrenWithWeight();
		}
	}

	@Test
	@Override
	public void runTask1() {
		assertThat(solveTask1(getInputLines()), is("vmpywg"));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solveTask2(getInputLines()), is(1674));
	}

	@Test
	public void runExample1() {
		List<String> input= Arrays.asList("pbga (66)",
							  "xhth (57)",
							  "ebii (61)",
							  "havc (66)",
							  "ktlj (57)",
							  "fwft (72) -> ktlj, cntj, xhth",
							  "qoyq (66)",
							  "padx (45) -> pbga, havc, qoyq",
							  "tknk (41) -> ugml, padx, fwft",
							  "jptl (61)",
							  "ugml (68) -> gyxo, ebii, jptl",
							  "gyxo (61)",
							  "cntj (57)");
		assertThat(solveTask1(input), is("tknk"));
	}

	@Test
	public void runTask2Example1() {
		List<String> input= Arrays.asList("pbga (66)",
				"xhth (57)",
				"ebii (61)",
				"havc (66)",
				"ktlj (57)",
				"fwft (72) -> ktlj, cntj, xhth",
				"qoyq (66)",
				"padx (45) -> pbga, havc, qoyq",
				"tknk (41) -> ugml, padx, fwft",
				"jptl (61)",
				"ugml (68) -> gyxo, ebii, jptl",
				"gyxo (61)",
				"cntj (57)");
		assertThat(solveTask2(input), is(60));
	}

	private String solveTask1(List<String> input) {
		Set<Program> programs= parse(input);
		for (Program p : programs) {
			if (p.parent == null) {
				System.out.println("found root! " + p.name);
				return p.name;
			}
		}
		return "";
	}

	private int solveTask2(List<String> input) {
		Map<String, Program> inbalanced= new HashMap<>();
		Set<Program> programs= parse(input);
		for (Program p : programs) {
			if (!p.hasBalancedChildren()) {
				inbalanced.put(p.name, p);
			}
		}

		Program nodeWithInbalance= findLowestChild(inbalanced);
//		System.out.println("offending node: " + nodeWithInbalance);
		Program childWithInbalance= nodeWithInbalance.childWithInbalance();
//		System.out.println("offending child: " + childWithInbalance);
//		System.out.println("offending child own weight: " + childWithInbalance.weigth);
		int balancedWeight= getBalancedWeigth(nodeWithInbalance, childWithInbalance);
//		System.out.println("balanced weight: "+ balancedWeight);

		int delta= balancedWeight - childWithInbalance.getWeigth();
//		System.out.println("delta is: " + delta);
		if (delta > 0) {
			return childWithInbalance.weigth - delta;
		} else {
			return childWithInbalance.weigth + delta;
		}
	}

	private int getBalancedWeigth(Program nodeWithInbalance, Program childWithInbalance) {
		for (Program c : nodeWithInbalance.children) {
			if (c.name.equals(childWithInbalance.name)) {
				continue;
			}
			return c.getWeigth();
		}
		return 0;
	}

	private Program findLowestChild(Map<String, Program> inbalanced) {
		for (Program p : inbalanced.values()) {
			boolean childInInbalanced= false;
			for (Program c : p.children) {
				if (inbalanced.containsKey(c.name)) {
					childInInbalanced= true;
				}
			}
			if (!childInInbalanced) {
				System.out.println("found node with no children in inbalanced tree: " + p.name);
				return p;
			}
		}
		return null;
	}

	private Set<Program> parse(List<String> input) {
		// first iteration to get all programs
		final Map<String, Program> progs= input.stream()
									   .map(s -> parseLine(s, null))
										  .collect(Collectors.toMap(Program::getName, Function.identity()));
		// first iteration to link up children
		return input.stream()
					  .map(s -> parseLine(s, progs))
					  .collect(Collectors.toSet());

	}

	private Program parseLine(String line, Map<String, Program> allPrograms) {
		String[] tokens= line.split(" ");

		Program p= new Program();
		p.name= tokens[0];
		p.weigth= Integer.parseInt(tokens[1].substring(1, tokens[1].length() - 1));
		if (allPrograms != null) {
			p= allPrograms.get(p.name);
		}
		if (allPrograms != null && tokens.length > 3) {
			for (int i = 3; i < tokens.length; i++) {
				String childName= tokens[i];
				if (childName.endsWith(",")) {
					childName= childName.substring(0, childName.length()-1);
				}
				Program child= allPrograms.get(childName);
				if (child != null) {
					p.children.add(child);
					child.parent= p;
				}
			}
		}
		return p;
	}

}

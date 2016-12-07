package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/19
public class Day19 extends AdventOfCode {

	private static final Pattern RULE_PATTERN= Pattern.compile("([A-Za-z]+) => ([A-Za-z]+)");

	private static class FissionPlant {
		Set<Rule> rules= new HashSet<>();
		String molecule;

		public int runCalibration() {
			Set<String> allNewMolecules= new HashSet<>();

			char[] chars= molecule.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				Set<String> moleculesOfRule = applyRules(molecule, i);
				allNewMolecules.addAll(moleculesOfRule);
			}
			return allNewMolecules.size();
		}

		/** like calibration, but "runs backwards" to get to electrons */
		public int runFabrication() {
			Set<String> molecules= new HashSet<>();
			molecules.add(molecule);

			int maxSteps= 50;
			for (int i = 1; i < maxSteps; i++) {
				Set<String> newMolecules= reduce(rules, molecules);
				// prune
				newMolecules.removeAll(molecules);
				molecules= newMolecules;
				if (molecules.contains("e")) {
					return i;
				}
			}

			return -1;
		}

		private Set<String> reduce(Set<Rule> rules, Set<String> molecules) {
			Set<String> reducedMolecules= new HashSet<>(rules.size());
			for (String molecule : molecules) {
				reducedMolecules.addAll(reduce(rules, molecule));
			}
			return reducedMolecules;
		}

		private Set<String> reduce(Set<Rule> rules, String molecule) {
			Set<String> moleculesOfRule= new HashSet<>(rules.size());
			List<Rule> ruleset= new ArrayList<>(rules);
			Collections.shuffle(ruleset);
			for (Rule r : ruleset) {
				String newMolecule = r.reduce(molecule);
				if (newMolecule != null && !molecule.equals(newMolecule)) {
					moleculesOfRule.add(newMolecule);
				}
			}
			return moleculesOfRule;
		}

		private Set<String> applyRules(String molecule, int position) {
			Set<String> moleculesOfRule= new HashSet<>(rules.size());
			for (Rule r : rules) {
				String newMolecule = r.apply(molecule, position);
				if (newMolecule != null) {
					moleculesOfRule.add(newMolecule);
				}
			}
			return moleculesOfRule;
		}
	}

	private static class Rule {
		String from;
		String to;

		public String apply(String molecule, int position) {
			if (molecule.regionMatches(position, from, 0, from.length())) {
				return molecule.substring(0, position) +
					   to +
					   molecule.substring(position + from.length());
			}
			return null;
		}

		public String reduce(String molecule) {
			return molecule.replaceFirst(to, from);
		}
	}

	@Test
	@Override
	public void runTask1() {
		List<String> input= getInputLines();
		assertThat(solveTask1(input), is(535));

	}

	@Test
	@Override
	public void runTask2() {
		List<String> input= getInputLines();
		assertThat(solveTask2(input), is(-42));

	}

	@Test
	public void runExample1() {
		List<String> input= Arrays.asList("H => HO",
										  "H => OH",
										  "O => HH",
										  "",
										  "HOH");
		assertThat(solveTask1(input), is(4));
	}

	@Test
	public void runExample2() {
		List<String> input= Arrays.asList("H => HO",
				"H => OH",
				"O => HH",
				"",
				"HOHOHO");
		assertThat(solveTask1(input), is(7));
	}

	@Test
	public void runTask2Example1() {
		List<String> input= Arrays.asList("e => H",
											"e => O",
											"H => HO",
											"H => OH",
											"O => HH",
											"",
											"HOH");
		assertThat(solveTask2(input), is(3));
	}

	@Test
	public void runTask2Example2() {
		List<String> input= Arrays.asList("e => H",
											"e => O",
											"H => HO",
											"H => OH",
											"O => HH",
											"",
											"HOHOHO");
		assertThat(solveTask2(input), is(6));
	}

	private int solveTask1(List<String> lines) {
		FissionPlant plant= parse(lines);
		return plant.runCalibration();
	}

	private int solveTask2(List<String> lines) {
		FissionPlant plant= parse(lines);
		return plant.runFabrication();
	}

	private FissionPlant parse(List<String> lines) {
		FissionPlant plant= new FissionPlant();
		boolean readMolecule= false;

		for (String line : lines) {
			if ("".equals(line)) {
				readMolecule = true;
				continue;
			}

			if (!readMolecule) {
				Rule r= parseRule(line);
				plant.rules.add(r);
			} else {
				plant.molecule = line.trim();
			}
		}
		return plant;
	}

	private Rule parseRule(String line) {
		Matcher m = RULE_PATTERN.matcher(line.trim());
		if (m.matches()) {
			Rule r= new Rule();
			r.from= m.group(1);
			r.to= m.group(2);
			return r;
		}
		return null;
	}
}

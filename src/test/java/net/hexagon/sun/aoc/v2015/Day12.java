package net.hexagon.sun.aoc.v2015;

import com.google.gson.Gson;
import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/12
public class Day12 extends AdventOfCode {

	private static final Pattern DIGITS = Pattern.compile("-?\\d+");

	@Test
	@Override
	public void runTask1() {
		String input= getInputAsString();
		assertThat(solveTask1(input), is(191164));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1("[1,2,3]"), is(6));
		assertThat(solveTask1("{\"a\":2,\"b\":4}"), is(6));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask1("[[[3]]]"), is(3));
		assertThat(solveTask1("{\"a\":{\"b\":4},\"c\":-1}"), is(3));
	}

	@Test
	public void runExample3() {
		assertThat(solveTask1("{\"a\":[-1,1]}"), is(0));
		assertThat(solveTask1("[-1,{\"a\":1}]"), is(0));
	}

	@Test
	public void runExample4() {
		assertThat(solveTask1("[]"), is(0));
		assertThat(solveTask1("{}"), is(0));
	}

	@Test
	@Override
	public void runTask2() {
		String input= getInputAsString();
		assertThat(solveTask2(input), is(87842));
	}

	@Test
	public void runTask2Example1() {
		assertThat(solveTask2("[1,2,3]"), is(6));
	}

	@Test
	public void runTask2Example2() {
		assertThat(solveTask2("[1,{\"c\":\"red\",\"b\":2},3]"), is(4));
	}

	@Test
	public void runTask2Example3() {
		assertThat(solveTask2("{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}"), is(0));
	}

	@Test
	public void runTask2Example4() {
		assertThat(solveTask2("[1,\"red\",5]"), is(6));
	}

	private int solveTask1(String input) {
		int sum= 0;
		Matcher m = DIGITS.matcher(input);
		while (m.find()) {
			int i= Integer.parseInt(m.group().trim());
			sum+= i;
//			System.out.println(i);
		}
		return sum;
	}


	private int solveTask2(String input) {
		Object parsed= parseJson(input);
		List processed= process(parsed);
		String jsonString= toJson(processed);
		return solveTask1(jsonString);
	}

	private String toJson(List processed) {
		Gson gson = new Gson();
		StringBuilder sb= new StringBuilder();
		gson.toJson(processed, sb);
		return sb.toString();
	}

	private List process(Object jsonObject) {
		ArrayList<Object> items = new ArrayList<>();
		if (jsonObject instanceof Map) {
			Collection values = ((Map)jsonObject).values();
			if (values.contains("red")) {
				// discard
			} else {
				// recurse
				for (Object val : values) {
					items.addAll(process(val));
				}
			}
		} else if (jsonObject instanceof List) {
			for (Object val : (List)jsonObject) {
				// process list elements
				items.add(process(val));
			}
		} else {
			// primitive
			items.add(jsonObject);
		}
		return items;
	}

	private Object parseJson(String input) {
		Object parsed= new Gson().fromJson(input, Object.class);
		return parsed;
	}
}

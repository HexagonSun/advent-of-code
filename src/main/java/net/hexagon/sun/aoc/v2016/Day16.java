package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/16
public class Day16 extends AdventOfCode {


	@Test
	@Override
	public void runTask1() {
		String solution = "10010101010011101";
		assertThat(solve(272, "10001110011110000"), is(solution));
	}

	@Test
	@Override
	public void runTask2() {
		int solution = -1;
		assertThat(solve(1, ""), is(solution));
	}

	@Test
	public void runDragon1() {
		assertThat(step("1"), is("100"));
	}

	@Test
	public void runDragon2() {
		assertThat(step("0"), is("001"));
	}

	@Test
	public void runDragon3() {
		assertThat(step("11111"), is("11111000000"));
	}

	@Test
	public void runDragon4() {
		assertThat(step("111100001010"), is("1111000010100101011110000"));
	}


	@Test
	public void runChecksum1() {
		assertThat(checksum("110010110100"), is("100"));
	}

	@Test
	public void runExample1() {
		int len = 20;
		String input = "10000";
		String solution = "01100";
		assertThat(solve(len, input), is(solution));
	}

	private String solve(int len, String input) {
		String data = input;
		while (data.length() < len) {
			data = step(data);
		}
//		System.out.println("data that fits disc: \"" + data + "\" (len: " + data.length() + ")");
		data = data.substring(0, len);
//		System.out.println("\tcapped: " + data);
		return checksum(data);
	}

	private String step(String a) {
		StringBuilder b = new StringBuilder(a);
		b.reverse();
		String b2 = b.toString();
		b2 = b2.replaceAll("0", "x");
		b2 = b2.replaceAll("1", "0");
		b2 = b2.replaceAll("x", "1");
		return a + "0" + b2;
	}

	private String checksum(String input) {
		String checksum = input;
		while (true) {
			StringBuilder checksumChars = new StringBuilder();
			char[] chars = checksum.toCharArray();
			for (int i = 0; i < chars.length - 1; i += 2) {
				char a = chars[i];
				char b = chars[i + 1];
				checksumChars.append(a == b ? '1' : '0');
			}
			checksum= checksumChars.toString();
			if (checksum.length() % 2 == 1) {
				return checksum;
			}
		}
	}

}

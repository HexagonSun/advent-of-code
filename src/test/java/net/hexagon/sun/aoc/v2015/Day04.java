package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2015/day/4
public class Day04 extends AdventOfCode {

	private enum Zeros { FIVE, SIX };

	MessageDigest digest;

	@BeforeEach
	public void setUp() throws NoSuchAlgorithmException {
		digest = MessageDigest.getInstance("MD5");
	}

	@Test
	@Override
	public void runTask1 () {
		int total= solveTask1(getInputAsString());
		System.out.println(total);

		// solution
		assertThat(total, is(117946));
	}

	@Test
	@Override
	public void runTask2 () {
		int total= solveTask2(getInputAsString());
		System.out.println(total);

		// solution
		assertThat(total, is(3938038));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1("abcdef"), is(609043));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask1("pqrstuv"), is(1048970));
	}

	private int solveTask1(final String input) {
		return solve(Zeros.FIVE, input);
	}

	private int solveTask2(String input) {
		return solve(Zeros.SIX, input);
	}

	private int solve(Zeros zeros, String input) {
		for (int i = 1; i <= Integer.MAX_VALUE; i++) {
			byte[] hash= hash(input, i);
			if (hasLeadingZeros(zeros, hash)) {
//				printSolution(input, i, hash);
				return i;
			}
		}
		return 0;
	}

	private boolean hasLeadingZeros(Zeros zeros, byte[] hash) {
		if (hash == null || hash.length < 3) {
			return false;
		}
		if (Zeros.FIVE == zeros) {
			return hash[0] == 0 && hash[1] == 0 && (hash[2] & 0xf0) == 0;
		} else {
			return hash[0] == 0 && hash[1] == 0 && hash[2] == 0;
		}
	}

	private void printSolution(String input, int i, byte[] hash) {
		System.out.println("Found solution for input string \"" + input + i + "\".");
		String fullHash= getHashAsString(hash);
		System.out.println("Full hash is " + fullHash);
	}

	private byte[] hash(String prefix, int suffix) {
		String raw= prefix + String.valueOf(suffix);
		return digest.digest(raw.getBytes());
	}

	private String getHashAsString(byte[] hash) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < hash.length; i++) {
			sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

}

package net.hexagon.sun.aoc;

import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/5
public class Day05 extends AdventOfCode {

	private final MessageDigest md5= MessageDigest.getInstance("MD5");

	public Day05() throws NoSuchAlgorithmException {
	}

	@Test
	@Override
	public void runTask1 () {
		String solution= "d4cd2ee1";
		assertThat(solveTask1("ugkcyxxp"), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1("abc"), is("18f47a30"));
	}

	@Test
	public void runTask2Example1() {

	}

	private String solveTask1 (String doorId) {
		int sequence= 0;
		int cnt= 0;
		StringBuilder pwd= new StringBuilder();
		while(true) {
			if (cnt > 7) {
				System.out.println("limit reached");
				break;
			}

			String input= doorId + sequence;
			md5.update(input.getBytes());
			byte[] digest = md5.digest();
//			System.out.println("bytes: " + toHexString(digest));
			if (digest[0] == 0 && digest[1] == 0 && (digest[2] & 0xF0) == 0) {
				pwd.append(Integer.toString(digest[2], 16));
				cnt++;
			}
			sequence++;
		}
		System.out.println(" PASS IS " + pwd);
		return pwd.toString();
	}

	private String toHexString(byte[] data) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	private String solveTask2 (String input) {
		return "-1";
	}

}

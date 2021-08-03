package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
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
		String solution= "f2c730e5";
		assertThat(solveTask2("ugkcyxxp"), is(solution));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1("abc"), is("18f47a30"));
	}

	@Test
	public void runTask2Example1() {
		assertThat(solveTask2("abc"), is("05ace8e3"));
	}

	private String solveTask1 (String doorId) {
		int sequence= 0;
		int cnt= 0;
		StringBuilder pwd= new StringBuilder();
		while(true) {
			if (cnt > 7 || sequence < 0) {
				System.out.println("limit reached, cnt: " + cnt + " | seq: " + sequence);
				break;
			}

			String input= doorId + sequence;
			md5.update(input.getBytes());
			byte[] digest = md5.digest();
			if (digest[0] == 0 && digest[1] == 0 && (digest[2] & 0xF0) == 0) {
				pwd.append(Integer.toString(digest[2], 16));
				cnt++;
			}
			sequence++;
		}
		return pwd.toString();
	}

	private String solveTask2 (String doorId) {
		int sequence= 0;
		int cnt= 0;
		char[] pwd= new char[8];
		while(true) {
			if (cnt > 7 || sequence < 0) {
				System.out.println("limit reached, cnt: " + cnt + " | seq: " + sequence);
				break;
			}

			String input= doorId + sequence;
			md5.update(input.getBytes());
			byte[] digest = md5.digest();
			if (digest[0] == 0 && digest[1] == 0 && (digest[2] & 0xF0) == 0) {
				int position= digest[2];
				if (position > 7 || pwd[position] != 0) {
					sequence++;
					continue;
				}

				int pwdChar= (digest[3] & 0xF0);
				pwd[position]= Integer.toString(pwdChar, 16).toCharArray()[0];
				cnt++;
			}
			sequence++;
		}
		return String.valueOf(pwd);
	}

}

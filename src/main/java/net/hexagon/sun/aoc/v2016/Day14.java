package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/14
public class Day14 extends AdventOfCode {

	private final MessageDigest md5= MessageDigest.getInstance("MD5");

	public Day14() throws NoSuchAlgorithmException {
	}

	@Test
	@Override
	public void runTask1 () {
		String input= "ihaygndm";
		int solution= 15035;
		assertThat(solveTask1(input), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
		String input= "ihaygndm";
		int solution= 19968;
		assertThat(solveTask2(input), is(solution));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1("abc"), is(22728));
	}

	@Test
	public void runTask2InitialHash() {

		md5.update("abc0".getBytes());
		byte[] plus1000digest = md5.digest();
		System.out.println("first hash is " + digestToString(plus1000digest));

		// repeat 2016 times more
		for (int j = 0; j < 2016; j++) {
//			md5.update(digestToString(plus1000digest).getBytes());
			md5.update(digestToNextHash(plus1000digest));
			plus1000digest= md5.digest();
		}

		System.out.println("strechted first hash is " + digestToString(plus1000digest));
		assertThat(digestToString(plus1000digest), is("a107ff634856bb300138cac6568c0f24"));
	}

	@Test
	public void runTask2Example1() {
		assertThat(solveTask2("abc"), is(22551));
	}

	@Test
	public void testDigestToNextHash() {
		md5.update("abc0".getBytes());
		byte[] initialHash = md5.digest();

		System.out.println("initialHash is " + digestToString(initialHash));


		byte[] toStringGetBytes= digestToString(initialHash).getBytes();
		byte[] nextRawBytes= digestToNextHash(initialHash);
		assertThat(nextRawBytes, is(toStringGetBytes));
	}

	private int solveTask1(String salt) {
		return solve(salt, false);
	}

	private int solveTask2 (String salt) {
		return solve(salt, true);
	}

	private int solve(String salt, boolean keyStretching) {
		List<byte[]> hashes= new ArrayList<>();
		prefill(hashes, salt, keyStretching);

		byte[] lastRealHash= null;
		int index= 0;
		int nbRealHashes= 0;
		while (nbRealHashes < 64) {
			md5.update((salt + (index + 1001)).getBytes());
			byte[] plus1000digest = md5.digest();
			if (keyStretching) {
				// repeat 2016 times more
				for (int j = 0; j < 2016; j++) {
					md5.update(digestToNextHash(plus1000digest));
					plus1000digest= md5.digest();
				}
			}
			hashes.add(plus1000digest);

			// now check @index. If a match is found, we can just loop through "hashes" because we know it contains the 1000 next hashes
			byte[] digest= hashes.get(index);
			char c= hasConsecutiveChars(digest);
			if (c > 0) {
				for (int i= index + 1 ; i < index + 1001; i++) {
					byte[] oneOfNext1000 = hashes.get(i);
					boolean has5= hasFiveConsecutiveChars(oneOfNext1000, c);
					if (has5) {
//						System.out.println("[" + nbRealHashes + "] real match "+digestToString(oneOfNext1000)+" @idx "
//												   + index + " || i is: " + i);
//						System.out.println("\t'c' was " + c + ", triple-hash was: " + digestToString(digest));
						lastRealHash= oneOfNext1000;
						nbRealHashes++;
						break;
					}
				}
			}
			index++;
		}

		System.out.println("last real hash is " + digestToString(lastRealHash));
		// -1 because the last execution of the while loop does index++
		return index - 1;
	}

	private void prefill(List<byte[]> hashes, String salt, boolean keyStretching) {
		for (int i = 0; i <= 1000; i++) {
			md5.update((salt + i).getBytes());
			byte[] digest = md5.digest();
			if (keyStretching) {
				// repeat 2016 times more
				for (int j = 0; j < 2016; j++) {
					md5.update(digestToNextHash(digest));
					digest= md5.digest();
				}
			}
			hashes.add(digest);
		}
	}

	private char hasConsecutiveChars(byte[] digest) {
		// scan through each nibble
		int d0= digest[0];
		int last1= (d0 & 0xf0) >> 4;
		int last2= (d0 & 0x0f);

		int d1= digest[1];
		int last3= (d1 & 0xf0) >> 4;
		int last4= (d1 & 0x0f);

		if (last1 == last2 && last2 == last3) {
			return Character.forDigit(last3, 16);
		}
		if (last2 == last3 && last3 == last4) {
			return Character.forDigit(last4, 16);
		}

		// len is 16 for md5 hashes
		for (int i = 2; i < digest.length; i++) {
			int d= digest[i];
			int last5 = (d & 0xf0) >> 4;
			int last6 = (d & 0x0f);

			if (last3 == last4 && last4 == last5) {
				return Character.forDigit(last5, 16);
			}

			if (last4 == last5 && last5 == last6) {
				return Character.forDigit(last6, 16);
			} else {
				last3 = last5;
				last4 = last6;
			}
		}
		return (char)0;
	}

	private boolean hasFiveConsecutiveChars(byte[] digest, char c) {
		int match= Integer.valueOf("" + c, 16);
		// scan through each nibble
		int d0= digest[0];
		int last1= (d0 & 0xf0) >> 4;
		int last2= (d0 & 0x0f);

		int d1= digest[1];
		int last3= (d1 & 0xf0) >> 4;
		int last4= (d1 & 0x0f);

		int d2= digest[2];
		int last5= (d2 & 0xf0) >> 4;
		int last6= (d2 & 0x0f);

		if (match == last1 && last1 == last2 && last2 == last3 && last3 == last4 && last4 == last5) {
			return true;
		}
		if (match == last2 && last2 == last3 && last3 == last4 && last4 == last5 && last5 == last6) {
			return true;
		}

		// len is 16 for md5 hashes
		for (int i = 3; i < digest.length; i++) {
			int d= digest[i];
			int last7 = (d & 0xf0) >> 4;
			int last8 = (d & 0x0f);

			if (match == last3 && last3 == last4 && last4 == last5 && last5 == last6 && last6 == last7) {
				return true;
			}

			if (match == last4 && last4 == last5 && last5 == last6 && last6 == last7 && last7 == last8) {
				return true;
			} else {
				// move nibbles
				last3= last5;
				last4= last6;
				last5= last7;
				last6= last8;
			}
		}
		return false;
	}

	private byte[] digestToNextHash(byte[] digest) {
		byte[] hash= new byte[32];
		for (int i = 0; i < digest.length; i++) {
			int b= digest[i];
			int highNibble= (b & 0xf0) >> 4;
			int lowNibble= (b & 0x0f);

			char highOffset= '0';
			if (highNibble >= 10) {
				highOffset= 'W';
			}
			char lowOffset= '0';
			if (lowNibble >= 10) {
				lowOffset= 'W';
			}
//			System.out.println("\t+'0': highNibble: " + (highOffset + highNibble) + " | lowNibble: " + (lowOffset + lowNibble));
			hash[2*i]    = (byte)(highOffset + highNibble);
			hash[2*i + 1]= (byte)(lowOffset + lowNibble);
		}
		return hash;
	}

	// for debugging
	private static String digestToString(byte[] digest) {
		StringBuilder sb = new StringBuilder();
		for (byte d : digest) {
			sb.append(Integer.toString((d & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

}

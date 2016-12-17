package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.awt.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/17
public class Day17 extends AdventOfCode {

	private static class Path {
		private final Point location;
		private final String path;
		// where we moved from
		private final char direction;

		Path(Point location, String path) {
			this(location, path, ' ');
		}

		private Path(Point location, String path, char direction) {
			this.location= location;
			this.path= path;
			this.direction= direction;
		}


		Path up() {
			Point p= new Point(this.location.x, this.location.y - 1);
			return new Path(p, this.path + "U", 'U');
		}

		Path down() {
			Point p= new Point(this.location.x, this.location.y + 1);
			return new Path(p, this.path + "D", 'D');
		}

		Path left() {
			Point p= new Point(this.location.x - 1, this.location.y);
			return new Path(p, this.path + "L", 'L');
		}

		Path right() {
			Point p= new Point(this.location.x + 1, this.location.y);
			return new Path(p, this.path + "R", 'R');
		}

		boolean isValid(String hash) {
			boolean inBounds= this.location.x > 0 && this.location.x <= 4 &&
							  this.location.y > 0 && this.location.y <= 4;
			if (!inBounds) {
				return false;
			}
			switch (this.direction) {
				case 'U': return hash.charAt(0) > 'a';
				case 'D': return hash.charAt(1) > 'a';
				case 'L': return hash.charAt(2) > 'a';
				case 'R':
				default : return hash.charAt(3) > 'a';
			}
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Path path1 = (Path) o;

			if (!location.equals(path1.location)) return false;
			return path.equals(path1.path);
		}

		@Override
		public int hashCode() {
			int result = location.hashCode();
			result = 31 * result + path.hashCode();
			return result;
		}

		@Override
		public String toString() {
			return "["+location.x + "/"+location.y + "] " + path + " (last dir: " + direction + ")";
		}
	}

	private final MessageDigest md5= MessageDigest.getInstance("MD5");

	public Day17() throws NoSuchAlgorithmException {
	}

	@Test
	@Override
	public void runTask1 () {
		String input= "edjrjqaa";
		String solution= "DUDRDLRRRD";
		assertThat(solveTask1(input), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
		String input= "edjrjqaa";
		int solution= -1;
		assertThat(solveTask2(input), is(solution));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1("hijkl"), is(""));
	}
	@Test
	public void runExample2() {
		assertThat(solveTask1("ihgpwlah"), is("DDRRRD"));
	}
	@Test
	public void runExample3() {
		assertThat(solveTask1("kglvqrro"), is("DDUDRLRRUDRD"));
	}
	@Test
	public void runExample4() {
		// ulqzkmivDRDDRR

		assertThat(solveTask1("ulqzkmiv"), is("DRURDRUDDLLDLUURRDULRLDUUDDDRR"));
	}

	@Test
	public void runTask2Example1() {
		assertThat(solveTask2("abc"), is(22551));
	}

	private String solveTask1(String salt) {
		return solve(salt, new Point(1,1), new Point(4,4));
	}

	private String solveTask2 (String salt) {
		return solve(salt, new Point(1,1), new Point(4,4));
	}

	private String solve(String salt, Point startLocation, Point targetLocation) {
		Path start= new Path(startLocation, salt);

		LinkedList<Path> queue = new LinkedList<>();
		queue.add(start);

		int cnt= 0;
		Path lastPoint= null;
		while (!queue.isEmpty() && cnt < 1000) {
			lastPoint= queue.poll();
			if (lastPoint.location.equals(targetLocation)) {
				break;
			}

			cnt++;
			String hash= calculateHash(lastPoint);

			ArrayList<Path> possibleMoves = new ArrayList<>();
			possibleMoves.add(lastPoint.up());
			possibleMoves.add(lastPoint.down());
			possibleMoves.add(lastPoint.left());
			possibleMoves.add(lastPoint.right());

			for (Path move : possibleMoves) {
				if (move.isValid(hash)) {
//					System.out.println("adding move " + move);
					queue.add(move);
				}
			}
		}
		System.out.println("Finished, lastPoint is " + lastPoint);
		if (lastPoint == null || !lastPoint.location.equals(targetLocation)) {
			return "";
		}
		return lastPoint.path.substring(salt.length());
	}

	private String calculateHash(Path point) {
		md5.update(point.path.getBytes());
		byte[] digest = md5.digest();
		return digestToString(digest);
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

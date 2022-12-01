package net.hexagon.sun.aoc.v2016;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
		int solution= 502;
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
		assertThat(solveTask1("ulqzkmiv"), is("DRURDRUDDLLDLUURRDULRLDUUDDDRR"));
	}

	@Test
	public void runTask2Example1() {
		assertThat(solveTask2("ihgpwlah"), is(370));
	}
	@Test
	public void runTask2Example2() {
		assertThat(solveTask2("kglvqrro"), is(492));
	}
	@Test
	public void runTask2Example3() {
		assertThat(solveTask2("ulqzkmiv"), is(830));
	}

	private String solveTask1(String salt) {
		return solve(salt, new Point(1,1), new Point(4,4), true);
	}

	private int solveTask2 (String salt) {
		String longestPath = solve(salt, new Point(1, 1), new Point(4, 4), false);
		return longestPath.length();
	}

	private String solve(String salt, Point startLocation, Point targetLocation, boolean findShortest) {
		Path start= new Path(startLocation, salt);

		LinkedList<Path> queue = new LinkedList<>();
		queue.add(start);

		List<Path> targetPaths= new ArrayList<>();
		Path lastPoint= null;
		while (!queue.isEmpty()) {
			lastPoint= queue.poll();
			if (lastPoint.location.equals(targetLocation)) {
				targetPaths.add(lastPoint);
				if (findShortest) {
					break;
				} else {
					continue;
				}
			}

			String hash= calculateHash(lastPoint);

			ArrayList<Path> possibleMoves = new ArrayList<>();
			possibleMoves.add(lastPoint.up());
			possibleMoves.add(lastPoint.down());
			possibleMoves.add(lastPoint.left());
			possibleMoves.add(lastPoint.right());

			for (Path move : possibleMoves) {
				if (move.isValid(hash)) {
					queue.add(move);
				}
			}
		}
		System.out.println("Finished, lastPoint is " + lastPoint);
		if (!findShortest) {
			Optional<Path> max = targetPaths.stream().max(Comparator.comparingInt(a -> a.path.length()));
			if (max.isPresent()) {
				System.out.println("Longest: " + max.get());
				return stripSalt(salt, max.get());
			} else {
				System.out.println("No longest path found");
				return "";
			}
		}
		if (lastPoint == null || !lastPoint.location.equals(targetLocation)) {
			return "";
		}
		return stripSalt(salt, lastPoint);
	}

	private String stripSalt(String salt, Path point) {
		return point.path.substring(salt.length());
	}

	private String calculateHash(Path point) {
		md5.update(point.path.getBytes());
		byte[] digest = md5.digest();
		return digestToString(digest);
	}

	private static String digestToString(byte[] digest) {
		StringBuilder sb = new StringBuilder();
		for (byte d : digest) {
			sb.append(Integer.toString((d & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

}

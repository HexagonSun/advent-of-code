package net.hexagon.sun.aoc;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

// http://adventofcode.com/2016/day/4
public class Day04 extends AdventOfCode {

	private class FrequencyElement implements Comparable {
		Character c;
		int count;

		@Override
		public int compareTo(Object o) {
			if (this == o) {
				return 0;
			}
			FrequencyElement other= (FrequencyElement)o;
			if (count != other.count) {
				return Integer.compare(other.count, count);
			}
			return Character.compare(c, other.c);
		}
	}
	private class RoomData {
		private final String stripped;
		private final PriorityQueue<FrequencyElement> frequencies;
		private final String frequencyFive;

		String name;
		int sectorId;
		String checksum;

		RoomData(String name) {
			this.name= name;
			this.stripped= name.replaceAll("-", "");
			this.frequencies= buildFrequency(stripped);
			this.frequencyFive= top5(frequencies);
		}

		private String top5(PriorityQueue<FrequencyElement> frequencies) {
			int cnt= 0;
			StringBuilder sb= new StringBuilder();
			while(cnt++ < 5) {
				FrequencyElement fe = frequencies.poll();
				if (fe != null) {
					sb.append(fe.c);
				}
			}
			return sb.toString();
		}

		private PriorityQueue<FrequencyElement> buildFrequency(String name) {
			PriorityQueue<FrequencyElement> pq= new PriorityQueue<>();

			Map<Character, Integer> freq= new TreeMap<>();
			for (char c : name.toCharArray()) {
				Integer cFreq= freq.get(c);
				cFreq= cFreq == null ? 0 : cFreq;
				freq.put(c, 1 + cFreq);
			}

			for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
				FrequencyElement fe= new FrequencyElement();
				fe.c = entry.getKey();
				fe.count = entry.getValue();
				pq.add(fe);
			}

			return pq;
		}

		boolean isReal() {
			return checksum.equals(frequencyFive);
		}

		String getDecrypted() {
			return rotate(name, sectorId);
		}

		private  String rotate(String encrypted, int offset) {
			StringBuilder decrypted= new StringBuilder();
			for (char c : encrypted.toCharArray()) {
				char newC= ' ';
				if ('-' != c) {
					int x= (c - 'a' + offset) % 26;
					newC= (char)(x + 'a');
				}
				decrypted.append(newC);
			}
			return decrypted.toString();
		}
	}

	private static final Pattern FORMAT = Pattern.compile("([a-z-]*)-(\\d*)\\[(\\w*)]");

	@Test
	@Override
	public void runTask1 () {
		int solution= 185371;
		assertThat(solveTask1(getInputLines()), is(solution));
	}

	@Test
	@Override
	public void runTask2 () {
		int solution= 984;
		assertThat(solveTask2(getInputLines()), is(solution));
	}

	@Test
	public void runExample1() {
		List<String> input= Collections.singletonList("aaaaa-bbb-z-y-x-123[abxyz]");
		assertThat(solveTask1(input), is(123));
	}

	@Test
	public void runExample2() {
		List<String> input= Collections.singletonList("a-b-c-d-e-f-g-h-987[abcde]");
		assertThat(solveTask1(input), is(987));
	}

	@Test
	public void runExample3() {
		List<String> input= Collections.singletonList("not-a-real-room-404[oarel]");
		assertThat(solveTask1(input), is(404));
	}

	@Test
	public void runExample4() {
		List<String> input= Collections.singletonList("totally-real-room-200[decoy]");
		assertThat(solveTask1(input), is(0));
	}

	@Test
	public void runExample5() {
		List<String> input= Arrays.asList(
				"aaaaa-bbb-z-y-x-123[abxyz]",
				"a-b-c-d-e-f-g-h-987[abcde]",
				"not-a-real-room-404[oarel]",
				"totally-real-room-200[decoy]");
		assertThat(solveTask1(input), is(1514));
	}


	@Test
	public void runTask2Example1() {
		List<String> input= Collections.singletonList("qzmt-zixmtkozy-ivhz-343[zimth]");
		assertThat(solveTask2(input), is(0));

	}

	private int solveTask1 (List<String> input) {
		int sectorIdCount = 0;
		for (String line : input) {
			RoomData data= parse(line);
			if (data != null && data.isReal()) {
				sectorIdCount+= data.sectorId;
			}
		}
		return sectorIdCount;
	}

	private RoomData parse (String input) {
		Matcher m= FORMAT.matcher(input);
		if (m.find()) {
			String name= m.group(1);
			int sector= Integer.parseInt(m.group(2));
			String checksum= m.group(3);

			RoomData rd = new RoomData(name);
			rd.sectorId= sector;
			rd.checksum= checksum;

			return rd;
		}
		return null;
	}

	private int solveTask2 (List<String> input) {
		int sectorIdCount = 0;
		for (String line : input) {
			RoomData data= parse(line);
			if (data != null && data.isReal()) {
				String s= data.getDecrypted();
//				System.out.println(s);
				if (s.contains("northpole")) {
					return data.sectorId;
				}
			}
		}
		return sectorIdCount;
	}

}

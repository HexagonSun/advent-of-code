package net.hexagon.sun.aoc.v2018;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day04 extends AdventOfCode {

	private enum Event {
		BEGIN, SLEEP, WAKE
	}

	private static class Data {
		Event event;
		LocalDateTime date;
		Integer guardId;

		@Override
		public String toString () {
			return event + " @ " + date;
		}
	}

	private static class SleepDuration {
		LocalDateTime start;
		LocalDateTime end;
	}

	private static class Shift {
		int guardId;
		List<Data> dataPoints= new ArrayList<>();
		List<SleepDuration> sleepDuring= new ArrayList<>();

		int getTotalSleepMinutes () {
			int sleep= 0;
			LocalDateTime asleepAt= null;
			for (Data d : dataPoints) {
				if (Event.SLEEP == d.event) {
					asleepAt= d.date;
				} else if (Event.WAKE == d.event) {
					Duration duration= Duration.between(asleepAt, d.date);
					SleepDuration sd= new SleepDuration();
					sd.start= asleepAt;
					sd.end= d.date;
					sleepDuring.add(sd);
					sleep+= (duration.get(ChronoUnit.SECONDS) / 60);
				}
			}
			return sleep;
		}

		@Override
		public String toString () {
			return "Guard #" + guardId + " [" + dataPoints + "]";
		}
	}

	private static class MaxMinute {
		int maxMinute= -1;
		int maxSleep= -1;
	}

	private static final DateTimeFormatter DATE_FORMAT = new DateTimeFormatterBuilder()
																 .appendPattern("yyyy-MM-dd HH:mm")
																 .toFormatter()
																 .withZone(ZoneId.of("UTC"));
	// [1518-11-01 00:00] Guard #10 begins shift
	private static final Pattern BEGIN_PATTERN = Pattern.compile("\\[([0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2})\\] Guard #(\\d+) begins shift");
	private static final Pattern DATE_PATTERN = Pattern.compile("\\[([0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2})\\].*");


	@Test
	@Override
	public void runTask1 () {
		assertThat(solveTask1(getInputLines()), is(14346));
	}

	@Test
	@Override
	public void runTask2 () {
		assertThat(solveTask2(getInputLines()), is(5705));
	}

	@Test
	public void runExample1 () {
		List<String> input = Arrays.asList(
			"[1518-11-01 00:00] Guard #10 begins shift",
			"[1518-11-01 00:05] falls asleep",
			"[1518-11-01 00:25] wakes up",
			"[1518-11-01 00:30] falls asleep",
			"[1518-11-01 00:55] wakes up",
			"[1518-11-01 23:58] Guard #99 begins shift",
			"[1518-11-02 00:40] falls asleep",
			"[1518-11-02 00:50] wakes up",
			"[1518-11-03 00:05] Guard #10 begins shift",
			"[1518-11-03 00:24] falls asleep",
			"[1518-11-03 00:29] wakes up",
			"[1518-11-04 00:02] Guard #99 begins shift",
			"[1518-11-04 00:36] falls asleep",
			"[1518-11-04 00:46] wakes up",
			"[1518-11-05 00:03] Guard #99 begins shift",
			"[1518-11-05 00:45] falls asleep",
			"[1518-11-05 00:55] wakes up"
		);
		assertThat(solveTask1(input), is(240));
	}

	@Test
	public void runExample2 () {
		List<String> input = Arrays.asList(
				"[1518-11-01 00:00] Guard #10 begins shift",
				"[1518-11-01 00:05] falls asleep",
				"[1518-11-01 00:25] wakes up",
				"[1518-11-01 00:30] falls asleep",
				"[1518-11-01 00:55] wakes up",
				"[1518-11-01 23:58] Guard #99 begins shift",
				"[1518-11-02 00:40] falls asleep",
				"[1518-11-02 00:50] wakes up",
				"[1518-11-03 00:05] Guard #10 begins shift",
				"[1518-11-03 00:24] falls asleep",
				"[1518-11-03 00:29] wakes up",
				"[1518-11-04 00:02] Guard #99 begins shift",
				"[1518-11-04 00:36] falls asleep",
				"[1518-11-04 00:46] wakes up",
				"[1518-11-05 00:03] Guard #99 begins shift",
				"[1518-11-05 00:45] falls asleep",
				"[1518-11-05 00:55] wakes up"
		);
		assertThat(solveTask2(input), is(4455));
	}

	private int solveTask1 (List<String> input) {
		List<Data> dataPoints = parse(input);
		List<Shift> shifts= toShifts(dataPoints);
//		System.out.println("parsed shifts: " + shifts);

		Map<Integer, Integer> totalSleepByGuard= new HashMap<>();
		for (Shift s : shifts) {
			Integer sleep= totalSleepByGuard.getOrDefault(s.guardId, 0);
			totalSleepByGuard.put(s.guardId, sleep + s.getTotalSleepMinutes());
		}

//		System.out.println("by guard: " + totalSleepByGuard);

		int maxGuard= -1;
		int maxSleep= -1;
		for (Map.Entry<Integer, Integer> entry : totalSleepByGuard.entrySet()) {
			if (entry.getValue() > maxSleep) {
				maxGuard= entry.getKey();
				maxSleep= entry.getValue();
			}
		}

		System.out.println("max sleep (" + maxSleep + ") by guard #" + maxGuard);

		int mostAsleepMinute= calculateMostAsleepMinute(shifts, maxGuard);
		return maxGuard * mostAsleepMinute;
	}

	private int calculateMostAsleepMinute (List<Shift> shifts, int guardId) {
		List<Shift> shiftsOfGuard= shifts.stream().filter(s -> s.guardId == guardId).collect(Collectors.toList());

		Map<Integer, Integer> nbSleepByMinute= new HashMap<>();
		for (Shift s : shiftsOfGuard) {
			for (SleepDuration duration : s.sleepDuring) {
				int startMinute= duration.start.getMinute();
				int endMinute= duration.end.getMinute();
				for (int i = startMinute; i < endMinute; i++) {
					Integer nbSleep= nbSleepByMinute.getOrDefault(i, 0);
					nbSleepByMinute.put(i, nbSleep + 1);
				}
			}
		}
//		System.out.println("nbSleepByMinute: " + nbSleepByMinute);

		int maxMinute= -1;
		int maxSleep= -1;
		for (Map.Entry<Integer, Integer> entry : nbSleepByMinute.entrySet()) {
			if (entry.getValue() > maxSleep) {
				maxMinute= entry.getKey();
				maxSleep= entry.getValue();
			}
		}
		return maxMinute;
	}

	private int solveTask2 (List<String> input) {
		List<Data> dataPoints = parse(input);
		List<Shift> shifts= toShifts(dataPoints);
		System.out.println("parsed shifts: " + shifts);

		Map<Integer, Integer> totalSleepByGuard= new HashMap<>();
		for (Shift s : shifts) {
			Integer sleep= totalSleepByGuard.getOrDefault(s.guardId, 0);
			totalSleepByGuard.put(s.guardId, sleep + s.getTotalSleepMinutes());
		}

		System.out.println("by guiard: " + totalSleepByGuard);


		MaxMinute mm= null;
		int maxSleep= -1;
		int maxGuard= -1;
		for (Map.Entry<Integer, Integer> entry : totalSleepByGuard.entrySet()) {
			MaxMinute mostMinuteOfGuard= calculateMaxMinute(shifts, entry.getKey());
			System.out.println("mostMinuteOfGuard: " + mostMinuteOfGuard);
			if (mostMinuteOfGuard.maxSleep > maxSleep) {
				maxSleep= mostMinuteOfGuard.maxSleep;
				mm= mostMinuteOfGuard;
				maxGuard= entry.getKey();
			}
		}
		return maxGuard * mm.maxMinute;
	}

	private MaxMinute calculateMaxMinute (List<Shift> shifts, int guardId) {
		List<Shift> shiftsOfGuard= shifts.stream().filter(s -> s.guardId == guardId).collect(Collectors.toList());

		Map<Integer, Integer> nbSleepByMinute= new HashMap<>();
		for (Shift s : shiftsOfGuard) {
			for (SleepDuration duration : s.sleepDuring) {
				int startMinute= duration.start.getMinute();
				int endMinute= duration.end.getMinute();
				for (int i = startMinute; i < endMinute; i++) {
					Integer nbSleep= nbSleepByMinute.getOrDefault(i, 0);
					nbSleepByMinute.put(i, nbSleep + 1);
				}
			}
		}
		System.out.println("[MM] nbSleepByMinute: " + nbSleepByMinute);

		int maxMinute= -1;
		int maxSleep= -1;
		for (Map.Entry<Integer, Integer> entry : nbSleepByMinute.entrySet()) {
			if (entry.getValue() > maxSleep) {
				maxMinute= entry.getKey();
				maxSleep= entry.getValue();
			}
		}
		MaxMinute mm = new MaxMinute();
		mm.maxMinute= maxMinute;
		mm.maxSleep= maxSleep;
		return mm;
	}

	private List<Shift> toShifts (List<Data> dataPoints) {
		List<Shift> shifts= new ArrayList<>();
		Shift lastShift= null;
		for (Data data : dataPoints) {
			if (data.guardId != null) {
				lastShift= new Shift();
				lastShift.guardId= data.guardId;
				shifts.add(lastShift);
			}
			lastShift.dataPoints.add(data);
		}
		return shifts;
	}

	private List<Data> parse (List<String> input) {
		return input.stream()
					   .map(this::parse)
					   .sorted(Comparator.comparing(l -> l.date))
					   .collect(Collectors.toList());
	}

	private Data parse (String line) {
		Data d = new Data();
		if (line.endsWith("begins shift")) {
			d.event= Event.BEGIN;
			Matcher m = BEGIN_PATTERN.matcher(line);
			if (m.matches()) {
				d.date= parseDate(m.group(1));
				d.guardId= Integer.parseInt(m.group(2));
				return d;
			}
		} else if (line.endsWith("falls asleep")) {
			d.event= Event.SLEEP;
			Matcher m = DATE_PATTERN.matcher(line);
			if (m.matches()) {
				d.date= parseDate(m.group(1));
				return d;
			}
		} else {
			d.event= Event.WAKE;
			Matcher m = DATE_PATTERN.matcher(line);
			if (m.matches()) {
				d.date= parseDate(m.group(1));
				return d;
			}
		}
		throw new IllegalStateException("couldn't parse input \"" + line + "\"");
	}

	private LocalDateTime parseDate(String dateString) {
		return LocalDateTime.from(Instant.from(DATE_FORMAT.parse(dateString))
										  .atOffset(ZoneOffset.UTC));
	}

}

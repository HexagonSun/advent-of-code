package net.hexagon.sun.aoc.v2017;

import javafx.geometry.Point3D;
import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day20 extends AdventOfCode {

	private static class Particle {
		int id;
		Point3D pos;
		Point3D v;
		Point3D a;
		boolean collided;

		@Override
		public String toString () {
			return "Particle{id=" + id + ": pos=" + pos + ", v=" + v + ", a=" + a + '}';
		}

		void tick () {
			v= v.add(a);
			pos= pos.add(v);
		}

		long dist() {
			return (long) pos.distance(0, 0, 0);
		}
	}

	private static final Pattern INPUT_PATTERN = Pattern.compile("p=<([-\\s]?[0-9]+),([-\\s]?[0-9]+),([-\\s]?[0-9]+)>, v=<([-\\s]?[0-9]+),([-\\s]?[0-9]+),([-\\s]?[0-9]+)>, a=<([-\\s]?[0-9]+),([-\\s]?[0-9]+),([-\\s]?[0-9]+)>");

	@Test
	@Override
	public void runTask1() {
		assertThat(solveTask1(getInputLines()), is(144));
	}

	@Test
	@Override
	public void runTask2() {
		assertThat(solveTask2(getInputLines()), is(477));
	}

	@Test
	public void runExample1() {
		assertThat(solveTask1(Arrays.asList("p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>",
							  "p=< 4,0,0>, v=< 0,0,0>, a=<-2,0,0>")), is(0));
	}

	@Test
	public void runExample2() {
		assertThat(solveTask2(Arrays.asList(
				"p=<-6,0,0>, v=< 3,0,0>, a=< 0,0,0>",
				"p=<-4,0,0>, v=< 2,0,0>, a=< 0,0,0>",
				"p=<-2,0,0>, v=< 1,0,0>, a=< 0,0,0>",
				"p=< 3,0,0>, v=<-1,0,0>, a=< 0,0,0>"
				)), is(1));
	}

	private int solveTask1 (List<String> input) {
		List<Particle> particles = parse(input);
		Particle minParticle= null;

		for (int i = 0; i < 10000; i++) {
			long tickMin= Integer.MAX_VALUE;
			Particle tickMinParticle= null;
			for (Particle p : particles) {
				p.tick();
				long dist= p.dist();
				if (dist < tickMin) {
					tickMin= dist;
					tickMinParticle= p;
				}
			}
			minParticle= tickMinParticle;
		}
		return minParticle.id;
	}

	private int solveTask2  (List<String> input) {
		List<Particle> particles = parse(input);
		for (int i = 0; i < 10000; i++) {
			Map<Point3D, List<Particle>> data= new HashMap<>();
			for (Particle p : particles) {
				if (p.collided) {
					continue;
				}
				p.tick();
				data.computeIfAbsent(p.pos, foo -> new ArrayList<>()).add(p);
			}

			data.values().stream().filter(l -> l.size() > 1).forEach(list -> {
				list.forEach(p -> p.collided= true);
			});
		}
		return (int) particles.stream().filter(p -> !p.collided).count();
	}

	private List<Particle> parse (List<String> input) {
		List<Particle> particles= new ArrayList<>();
		for (int i = 0; i < input.size(); i++) {
			String line = input.get(i);
			Matcher m = INPUT_PATTERN.matcher(line);
			m.matches();

			int groupNo= 1;
			Particle p = new Particle();
			p.id= i;
			p.pos = new Point3D(asInt(m.group(groupNo++)), asInt(m.group(groupNo++)), asInt(m.group(groupNo++)));
			p.v = new Point3D(asInt(m.group(groupNo++)), asInt(m.group(groupNo++)), asInt(m.group(groupNo++)));
			p.a = new Point3D(asInt(m.group(groupNo++)), asInt(m.group(groupNo++)), asInt(m.group(groupNo++)));
			particles.add(p);
		}
		return particles;
	}

	private int asInt(String s) {
		return Integer.parseInt(s.trim());
	}

}

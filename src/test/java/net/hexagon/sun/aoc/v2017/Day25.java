package net.hexagon.sun.aoc.v2017;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day25 extends AdventOfCode {

	static class Action {
		public static final int RIGHT= 1;
		public static final int LEFT= -1;

		String state;
		int[] toWrite= new int[2];
		int[] movement= new int[2];
		String[] nextState= new String[2];

		static Action of (String state,
						  int write0, int movement0, String nextState0,
						  int write1, int movement1, String nextState1) {
			Action a= new Action();
			a.state= state;
			a.toWrite[0]= write0;
			a.movement[0]= movement0;
			a.nextState[0]= nextState0;
			a.toWrite[1]= write1;
			a.movement[1]= movement1;
			a.nextState[1]= nextState1;
			return a;
		}

	}

	static class Blueprint {
		String startState= "A";
		int nbStepsForDiagnostic;
		Map<String, Action> states= new HashMap<>();
	}

	static class TuringMachine {
		private final Blueprint blueprint;
		private final int[] tape;

		private String state;
		private int index;

		TuringMachine (Blueprint blueprint, int tapeSize) {
			this.blueprint= blueprint;
			this.state= blueprint.startState;
			tape= new int[tapeSize];
			Arrays.fill(tape, 0);
			index= tapeSize/2;
		}

		void run () {
			for (int i = 0; i < blueprint.nbStepsForDiagnostic; i++) {
				Action action= blueprint.states.get(state);
				apply(action);
			}
		}

		int countOnes () {
			return Arrays.stream(tape).filter(value -> value == 1).sum();
		}

		private void apply (Action action) {
			int currentValue= tape[index];
			tape[index]= action.toWrite[currentValue];
			index+= action.movement[currentValue];
			state= action.nextState[currentValue];
		}
	}

	@Test
	@Override
	public void runTask1() {
		Blueprint blueprint= new Blueprint();
		blueprint.nbStepsForDiagnostic= 12_919_244;

		blueprint.states.put("A", Action.of("A", 1, Action.RIGHT, "B", 0, Action.LEFT, "C"));
		blueprint.states.put("B", Action.of("B", 1, Action.LEFT, "A", 1, Action.RIGHT, "D"));
		blueprint.states.put("C", Action.of("C", 1, Action.RIGHT, "A", 0, Action.LEFT, "E"));
		blueprint.states.put("D", Action.of("D", 1, Action.RIGHT, "A", 0, Action.RIGHT, "B"));
		blueprint.states.put("E", Action.of("E", 1, Action.LEFT, "F", 1, Action.LEFT, "C"));
		blueprint.states.put("F", Action.of("F", 1, Action.RIGHT, "D", 1, Action.RIGHT, "A"));

		assertThat(solveTask1(blueprint, 100_000), is(4287));
	}

	@Test
	@Override
	public void runTask2() {
		// nop
	}

	@Test
	public void runExample1() {
		Blueprint blueprint= new Blueprint();
		blueprint.nbStepsForDiagnostic= 6;
		Action a= Action.of("A", 1, 1, "B", 0, -1, "B");
		Action b= Action.of("B", 1, -1, "A", 1, 1, "A");
		blueprint.states.put("A", a);
		blueprint.states.put("B", b);

		assertThat(solveTask1(blueprint, 100), is(3));
	}

	private int solveTask1 (Blueprint blueprint, int tapeSize) {
		TuringMachine tm= new TuringMachine(blueprint, tapeSize);
		tm.run();
		return tm.countOnes();
	}

}

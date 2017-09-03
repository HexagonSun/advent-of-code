package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

// http://adventofcode.com/2015/day/22
public class Day22 extends AdventOfCode {

	private enum Spell {
		MAGIC_MISSILE(53, 4, 0, 0, 0, 0),
		DRAIN(73, 2, 2, 0, 0, 0),
		SHIELD(113, 0, 0, 7, 0, 6),
		POISON(173, 3, 0, 0, 0, 6),
		RECHARGE(229, 0, 0, 0, 101, 5);

		final int cost;
		final int damage;
		final int healing;
		final int armor;
		final int mana;
		final int duration;

		Spell(int cost, int damage, int healing, int armor, int mana, int duration) {
			this.cost= cost;
			this.damage= damage;
			this.healing= healing;
			this.armor= armor;
			this.mana= mana;
			this.duration= duration;
		}

		boolean hasEffect() {
			return this.duration > 0;
		}
	}

	private enum Difficulty {
		NORMAL, HARD
	}

	private static class GameState {

		final Difficulty difficulty;
		final Wizard player;
		final Boss boss;
		private List<Spell> spellHistory= new ArrayList<>();

		GameState(Difficulty difficulty, Wizard player, Boss boss) {
			this.difficulty= difficulty;
			this.player= player;
			this.boss= boss;
		}

		GameState(GameState other) {
			this(other.difficulty, new Wizard(other.player), new Boss(other.boss));
			this.spellHistory= new ArrayList<>(other.spellHistory);
		}

		void applyEffect() {
			player.applyEffect();
			boss.applyEffect();
		}

		void cast(Spell spell) {
			spellHistory.add(spell);
			player.cast(spell);
			boss.cast(spell);
		}

		boolean isOver() {
			return boss.hp <= 0 || player.hp <= 0;
		}

		@Override
		public String toString() {
			List<String> spellNames = spellHistory.stream().map(Enum::name).collect(Collectors.toList());
			return "[GS]: " + String.join(" -> ", spellNames);
		}
	}

	private static abstract class Player {
		final int damage;
		int armor;
		int hp;

		Map<Spell, Integer> activeEffects= new HashMap<>();

		Player(int hp, int damage, int armor) {
			this.hp= hp;
			this.damage= damage;
			this.armor= armor;
		}

		Player(Player other) {
			this.damage= other.damage;
			this.armor= other.armor;
			this.hp= other.hp;
			this.activeEffects= new HashMap<>(other.activeEffects);
		}

		protected abstract void applyDurationEffect(Spell spell);

		void applyOnceEffect(Integer timer, Spell spell) {
			// default: do nothing
		}

		protected void cast(Spell spell) {
			if (spell.hasEffect()) {
				activeEffects.put(spell, spell.duration);
			}
		}

		void applyEffect() {
			activeEffects.forEach((spell, timer) -> {
//				System.out.println("Applying effect " + spell);
				applyOnceEffect(timer, spell);
				applyDurationEffect(spell);
			});
			activeEffects= updateDurations(activeEffects);
		}

		HashMap<Spell, Integer> updateDurations(Map<Spell, Integer> current) {
			HashMap<Spell, Integer> updated = new HashMap<>();
			current.forEach((spell, timer) -> {
				if (timer > 1) {
					updated.put(spell, timer - 1);
				} else {
					unapply(spell);
				}
			});
			return updated;
		}

		private void unapply(Spell spell) {
			// DAMAGE is the only spell that need unapplying, and it's the only one affecting "armor"
			this.armor-= spell.armor;
		}
	}

	private static class Wizard extends Player {

		Map<Spell, Integer> castSpells= new HashMap<>();
		int spentMana;
		int mana;

		Wizard(int hp, int damage, int armor, int mana) {
			super(hp, damage, armor);
			this.mana= mana;
		}

		Wizard(Wizard other) {
			super(other);
			this.castSpells = new HashMap<>(other.castSpells);

			this.spentMana= other.spentMana;
			this.mana= other.mana;
		}

		protected void cast(Spell spell) {
			// spell cost
			this.spentMana+= spell.cost;
			this.mana-= spell.cost;

			// apply
//			System.out.println("Player casts " + spell);
			super.cast(spell);
			castSpells.put(spell, spell.duration);
			if (!spell.hasEffect()) {
				// immediate action
				this.hp += spell.healing;
			}
		}

		private List<Spell> getPossibleSpells() {
			List<Spell> all = new ArrayList<>(Arrays.asList(Spell.values()));
			Set<Spell> nonexipring= castSpells.entrySet().stream()
											 .filter(e -> e.getValue() > 1)
											 .map(Map.Entry::getKey)
											 .collect(Collectors.toSet());
			all.removeAll(nonexipring);

			// filter by mana cost
			all.removeIf(spell -> spell.cost > mana);
			return all;
		}

		protected void applyDurationEffect(Spell spell) {
			this.hp+= spell.healing;
			this.mana+= spell.mana;
		}

		protected void applyOnceEffect(Integer timer, Spell spell) {
			if (timer != spell.duration) {
				return;
			}
			this.armor += spell.armor;
		}

		@Override
		void applyEffect() {
			super.applyEffect();
			this.castSpells = new HashMap<>(this.activeEffects);
		}
	}

	private static class Boss extends Player {
		Boss(int hp, int damage, int armor) {
			super(hp, damage, armor);
		}

		Boss(Boss other) {
			super(other);
		}

		protected void cast(Spell spell) {
			super.cast(spell);
			if (!spell.hasEffect()) {
				// immediate action
				applyDurationEffect(spell);
			}
		}

		void attack(Player opponent) {
			opponent.hp-= Math.max(damage - opponent.armor, 1);
		}

		protected void applyDurationEffect(Spell spell) {
			this.hp-= spell.damage;
		}
	}

	@Override
	@Test
	public void runTask1() {
		// from input file:
		//		Hit Points: 51
		//		Damage: 9
		int spentMana= fightMatch(new GameState(Difficulty.NORMAL,
												new Wizard(50, 0, 0, 500),
											    new Boss  (51, 9, 0)));
		// Best game was: [GS]: MAGIC_MISSILE -> POISON -> RECHARGE -> MAGIC_MISSILE -> SHIELD -> POISON -> MAGIC_MISSILE -> MAGIC_MISSILE
		// Total mana spent: 900
		assertThat(spentMana, is(900));
	}

	@Test
	public void runTask1_stepping() {
		// from input file:
		//		Hit Points: 51
		//		Damage: 9

		GameState state = new GameState(Difficulty.NORMAL,
									    new Wizard(50, 0, 0, 500),
									    new Boss  (51, 9, 0));

		processTurn(state, Spell.POISON);
		processTurn(state, Spell.MAGIC_MISSILE);
		processTurn(state, Spell.RECHARGE);
		processTurn(state, Spell.MAGIC_MISSILE);
		processTurn(state, Spell.POISON);
		processTurn(state, Spell.SHIELD);
		processTurn(state, Spell.MAGIC_MISSILE);
		processTurn(state, Spell.MAGIC_MISSILE);

		assertThat(state.isOver(), is(true));
		assertThat(state.player.spentMana, is(900));
	}

	@Override
	@Test
	public void runTask2() {
		// from input file:
		//		Hit Points: 51
		//		Damage: 9
		int spentMana= fightMatch(new GameState(Difficulty.HARD,
											    new Wizard(50, 0, 0, 500),
											    new Boss  (51, 9, 0)));
		// Best game was: [GS]: POISON -> RECHARGE -> SHIELD -> POISON -> RECHARGE -> DRAIN -> POISON -> MAGIC_MISSILE
		// Total mana spent: 1216

		assertThat(spentMana, is(1216));
	}

	@Test
	public void runTask2_stepping() {
		// from input file:
		//		Hit Points: 51
		//		Damage: 9
		GameState state = new GameState(Difficulty.HARD,
									    new Wizard(50, 0, 0, 500),
									    new Boss  (51, 9, 0));

		// Best game was: [GS]: POISON -> RECHARGE -> SHIELD -> POISON -> RECHARGE -> DRAIN -> POISON -> MAGIC_MISSILE


		processTurn(state, Spell.POISON);
		processTurn(state, Spell.RECHARGE);
		processTurn(state, Spell.SHIELD);
		processTurn(state, Spell.POISON);
		processTurn(state, Spell.RECHARGE);
		processTurn(state, Spell.DRAIN);
		processTurn(state, Spell.POISON);
		processTurn(state, Spell.MAGIC_MISSILE);
		
		assertThat(state.isOver(), is(true));
		assertThat(state.player.spentMana, is(1216));
	}

	@Test
	public void runExample1() {
		int spentMana= fightMatch(new GameState(Difficulty.NORMAL,
											    new Wizard(10, 0, 0, 250),
											    new Boss  (13, 8, 0)));
		assertThat(spentMana, is(226));
	}

	@Test
	public void runExample1_stepped() {
		GameState state= new GameState(Difficulty.NORMAL,
									   new Wizard(10, 0, 0, 250),
									   new Boss  (13, 8, 0));
		processTurn(state, Spell.POISON);
		processTurn(state, Spell.MAGIC_MISSILE);

		assertThat(state.isOver(), is(true));
		assertThat(state.player.spentMana, is(226));
	}

	@Test
	public void runExample2() {
		int spentMana= fightMatch(new GameState(Difficulty.NORMAL,
											    new Wizard(10, 0, 0, 250),
											   	new Boss  (14, 8, 0)));

		// RECHARGE -> SHIELD -> DRAIN -> POISON -> MAGIC_MISSILE
		// == 229 + 	113 + 	 73	+	  173 + 	53
		// == 641
		assertThat(spentMana, is(641));
	}

	private int fightMatch(GameState initialState) {
		PriorityQueue<GameState> queue= new PriorityQueue<>((a, b) -> Integer.compare(b.player.spentMana, a.player.spentMana));
		queue.add(initialState);

		GameState bestGame = null;

		while (queue.size() > 0) {
			GameState state = queue.poll();
			if (moreSpentMana(state, bestGame)) {
				// this state has already spent more mana than the currently best one
				continue;
			}

			for (Spell spell : state.player.getPossibleSpells()) {
				GameState nextState= new GameState(state);
				processTurn(nextState, spell);

				boolean more= moreSpentMana(nextState, bestGame);
				if (nextState.isOver() && !more) {
					if (nextState.player.hp > 0) {
						bestGame = nextState;
					}
				} else {
					queue.add(nextState);
				}
			}
		}

		System.out.println("*** FINISHED ***");
		System.out.println("Best game was: " + bestGame);
		if (bestGame == null) {
			return -1;
		}
		if (!bestGame.isOver()) {
			throw new IllegalStateException("Algorithm finished, but game was not over yet!");
		}
		System.out.println("Total mana spent: " + bestGame.player.spentMana);
		return bestGame.player.spentMana;
	}

	private boolean moreSpentMana(GameState state, GameState bestGame) {
		return bestGame != null && state.player.spentMana > bestGame.player.spentMana;
	}

	private void processTurn(GameState state, Spell spell) {
//		System.out.println("Processing turn for state " + state);
		Player player= state.player;
		Boss boss= state.boss;

		// player turn
		processDifficulty(state);
		if (state.isOver()) {
			return;
		}

		state.applyEffect();
		if (state.isOver()) {
			return;
		}
		state.cast(spell);

		// compare HP
		if (state.isOver()) {
			return;
		}

		// boss turn
		state.applyEffect();
		if (state.isOver()) {
			return;
		}
		boss.attack(player);
	}

	private void processDifficulty(GameState state) {
		if (Difficulty.HARD == state.difficulty) {
			state.player.hp-= 1;
		}
	}

}

package net.hexagon.sun.aoc.v2015;

import net.hexagon.sun.aoc.AdventOfCode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Day21 extends AdventOfCode {

	private static class Player {
		private final String name;
		int cost;
		int damage;
		int armor;
		int hp;

		Player(String name, int hp, int damage, int armor) {
			this.name= name;
			this.hp= hp;
			this.damage= damage;
			this.armor= armor;
		}

		Player add(Item item) {
			this.cost+= item.cost;
			this.armor+= item.armor;
			this.damage+= item.damage;
			return this;
		}
	}

	private static class Item {

		final int cost;
		final int damage;
		final int armor;

		Item(int cost, int damage, int armor) {
			this.cost= cost;
			this.damage= damage;
			this.armor= armor;
		}
 	}

	@Override
	@Test
	public void runTask1() {
		Player winner= fight(true);
		assertThat(winner.cost, is(91));
	}

	@Override
	@Test
	public void runTask2() {
		Player winner= fight(false);
		if (winner == null) {
			assert false;
		}
		assertThat(winner.cost, is(158));
	}

	private Player fight(boolean calculateMinPrice) {
		List<Item> weapons = Arrays.asList(
				new Item(8, 4, 0)
				,
				new Item(10,5, 0),
				new Item(25, 6, 0),
				new Item(40, 7, 0),
				new Item(74, 8, 0)
		);
		List<Item> armor = Arrays.asList(
				new Item(0, 0, 0)
				,
				new Item(13, 0, 1),
				new Item(31,0, 2),
				new Item(53, 0, 3),
				new Item(75, 0, 4),
				new Item(102, 0, 5)
		);
		List<Item> rings = Arrays.asList(
				new Item(0, 0, 0),
				new Item(0, 0, 0),
				new Item(25, 1, 0),
				new Item(50,2, 0),
				new Item(100, 3, 0)
				,
				new Item(20, 0, 1),
				new Item(40, 0, 2),
				new Item(80, 0, 3)
		);

		Player outcomePlayer = null;
		int minCost= Integer.MAX_VALUE;
		int maxCost= Integer.MIN_VALUE;
		for (Item weapon : weapons) {
			for(Item a : armor) {
				for (int i = 0; i < rings.size()-1; i++) {
					for (int j = i + 1; j < rings.size(); j++) {
						Player boss= new Player("boss", 100, 8, 2);
						Player player= new Player("player", 100, 0, 0)
											   .add(weapon)
											   .add(a)
											   .add(rings.get(i))
											   .add(rings.get(j));

						boolean playerWon= fightMatch(player, boss);
						if (calculateMinPrice) {
							if (playerWon && player.cost < minCost) {
								minCost = player.cost;
								outcomePlayer = player;
							}
						} else {
							if (!playerWon && player.cost > maxCost) {
								maxCost= player.cost;
								outcomePlayer= player;
							}
						}
					}
				}
			}
		}
		return outcomePlayer;
	}

	@Test
	public void runExample1() {
		Player player= new Player("player", 8, 5, 5);
		Player boss= new Player("boss", 12, 7, 2);
		assertThat(fightMatch(player, boss), is(true));
	}

	private boolean fightMatch(Player player, Player boss) {
		Player attacker= player;
		Player opponent= boss;
		while(true) {
//			System.out.println("Attacker: " + attacker.name);
//			System.out.println("Opponent: " + opponent.name + " | hp: " + opponent.hp);
			opponent.hp-= Math.max(attacker.damage - opponent.armor, 1);
//			System.out.println("\t" + opponent.name + " HP down to " + opponent.hp);
			if (opponent.hp <= 0) {
//				System.out.println("Player " + opponent.name + " loses!");
				return opponent.name.equals(boss.name);
			}

			// swap
			Player tmp= opponent;
			opponent= attacker;
			attacker= tmp;
		}
	}

}

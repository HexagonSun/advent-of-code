package net.hexagon.sun.aoc.v2022

import net.hexagon.sun.aoc.AdventOfKode
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class Day02K : AdventOfKode() {

    @Test
    override fun runTask1() {
        val input = getInputLines()
        Assertions.assertThat(solveTask1(input)).isEqualTo(15422)
    }

    @Test
    override fun runTask2() {
        val input = getInputLines()
        Assertions.assertThat(solveTask2(input)).isEqualTo(15442)
    }

    @Test
    fun runExample1() {
        val input = """
            A Y
            B X
            C Z
        """.trimIndent()
            .split('\n')

        Assertions.assertThat(solveTask1(input)).isEqualTo(15)
    }

    @Test
    fun runTask2Example1() {
        val input = """
            A Y
            B X
            C Z
        """.trimIndent()
            .split('\n')

        Assertions.assertThat(solveTask2(input)).isEqualTo(12)
    }
}

private enum class Symbol(val value: Set<String>, val score: Int) {
    ROCK(setOf("A", "X"), 1),
    PAPER(setOf("B", "Y"), 2),
    SCISSOR(setOf("C", "Z"), 3),
}

private fun Symbol.toShapeThatLoses(): Symbol =
    when(this) {
        Symbol.ROCK -> Symbol.SCISSOR
        Symbol.PAPER -> Symbol.ROCK
        Symbol.SCISSOR -> Symbol.PAPER
    }

private fun Symbol.toShapeThatDraws(): Symbol = this

private fun Symbol.toShapeThatWins(): Symbol =
    when(this) {
        Symbol.ROCK -> Symbol.PAPER
        Symbol.PAPER -> Symbol.SCISSOR
        Symbol.SCISSOR -> Symbol.ROCK
    }

private fun solveTask1(input: List<String>): Int =
    input.sumOf { line ->
        val (opponent, own) = line.split(' ').map { it.toSymbol() }
        score(opponent, own)
    }

private fun solveTask2(input: List<String>): Int =
    input.sumOf { line ->
        val (opponent, target) = line.split(' ').map { it.toSymbol() }
        val own = when (target) {
            Symbol.ROCK -> opponent.toShapeThatLoses()
            Symbol.PAPER -> opponent.toShapeThatDraws()
            Symbol.SCISSOR -> opponent.toShapeThatWins()
        }
        score(opponent, own)
    }

private fun score(opponent: Symbol, own: Symbol): Int =
    outcome(opponent, own) + own.score

private fun outcome(opponent: Symbol, own: Symbol): Int =
    when(opponent) {
        Symbol.ROCK ->
            when (own) {
                Symbol.ROCK -> 3
                Symbol.PAPER -> 6
                Symbol.SCISSOR -> 0
            }
        Symbol.PAPER ->
            when (own) {
                Symbol.ROCK -> 0
                Symbol.PAPER -> 3
                Symbol.SCISSOR -> 6
            }
        Symbol.SCISSOR ->
            when (own) {
                Symbol.ROCK -> 6
                Symbol.PAPER -> 0
                Symbol.SCISSOR -> 3
            }
    }

private fun String.toSymbol(): Symbol =
    trim().let {  input ->
        Symbol.values().first { it.value.contains(input) }
    }

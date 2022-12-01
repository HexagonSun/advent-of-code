package net.hexagon.sun.aoc.v2022

import net.hexagon.sun.aoc.AdventOfKode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day01K : AdventOfKode() {

    @Test
    override fun runTask1() {
        val input = getInputAsString().trim()
        assertThat(solveTask1(input)).isEqualTo(72017)
    }

    @Test
    override fun runTask2() {
        val input = getInputAsString().trim()
        assertThat(solveTask2(input)).isEqualTo(212520)
    }

    @Test
    fun runExample1() {
        val input = """
            1000
            2000
            3000

            4000

            5000
            6000

            7000
            8000
            9000

            10000
        """.trimIndent()

        assertThat(solveTask1(input)).isEqualTo(24000)
    }

    @Test
    fun runTask2Example1() {
        val input = """
            1000
            2000
            3000

            4000

            5000
            6000

            7000
            8000
            9000

            10000
        """.trimIndent()

        assertThat(solveTask2(input)).isEqualTo(45000)
    }
}

private fun solveTask1(input: String): Int =
    sumByElves(input).maxOf { it }

private fun solveTask2(input: String): Int =
    sumByElves(input)
        .sortedDescending()
        .top(3)
        .sum()

private fun sumByElves(input: String) = input.split("\n\n")
    .map { chunk ->
        chunk.split('\n').sumOf { it.toInt() }
    }

private fun List<Int>.top(n: Int): List<Int> =
    dropLast(size - n)

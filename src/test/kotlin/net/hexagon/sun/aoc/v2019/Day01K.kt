package net.hexagon.sun.aoc.v2019

import net.hexagon.sun.aoc.AdventOfKode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day01K : AdventOfKode() {

    @Test
    override fun runTask1() {
        assertThat(solveTask1(toIntList(getInputLines()))).isEqualTo(3331523)
    }

    @Test
    override fun runTask2() {
        assertThat(solveTask2(toIntList(getInputLines()))).isEqualTo(4994396)
    }

    @Test
    fun runExample1() {
        val input = listOf(12)
        assertThat(solveTask1(input)).isEqualTo(2)
    }

    @Test
    fun runExample2() {
        val input = listOf(14)
        assertThat(solveTask1(input)).isEqualTo(2)
    }

    @Test
    fun runExample3() {
        val input = listOf(1969)
        assertThat(solveTask1(input)).isEqualTo(654)
    }

    @Test
    fun runExample4() {
        val input = listOf(100756)
        assertThat(solveTask1(input)).isEqualTo(33583)
    }

    @Test
    fun runTask2Example1() {
        val input = listOf(14)
        assertThat(solveTask2(input)).isEqualTo(2)
    }

    @Test
    fun runTask2Example2() {
        val input = listOf(1969)
        assertThat(solveTask2(input)).isEqualTo(966)
    }

    @Test
    fun runTask2Example3() {
        val input = listOf(100756)
        assertThat(solveTask2(input)).isEqualTo(50346)
    }

    private fun solveTask1(input: List<Int>): Int =
        input.fold(0) { acc, value -> acc + determineFuel(value) }

    private fun determineFuel(mass: Int): Int =
        (mass.floorDiv(3) - 2).takeIf { it > 0 } ?: 0

    private fun solveTask2(input: List<Int>): Int =
        input.fold(0) { acc, value -> acc + determineFuelRecursively(value) }

    private fun determineFuelRecursively(mass: Int): Int =
        when (mass) {
            0 -> 0
            else -> determineFuel(mass).let { it + determineFuelRecursively(it) }
        }

    private fun toIntList(input: List<String>): List<Int> =
        input
            .filterNot { it.isBlank() }
            .map { it.toInt() }
}

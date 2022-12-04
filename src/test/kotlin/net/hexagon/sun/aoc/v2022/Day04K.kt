package net.hexagon.sun.aoc.v2022

import net.hexagon.sun.aoc.AdventOfKode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day04K : AdventOfKode() {

    @Test
    override fun runTask1() {
        val input = getInputLines()
        assertThat(solveTask1(input)).isEqualTo(599)
    }

    @Test
    override fun runTask2() {
        val input = getInputLines()
        assertThat(solveTask2(input)).isEqualTo(928)
    }

    @Test
    fun runExample1() {
        val input = """
            2-4,6-8
            2-3,4-5
            5-7,7-9
            2-8,3-7
            6-6,4-6
            2-6,4-8
        """.trimIndent()
            .split('\n')

        assertThat(solveTask1(input)).isEqualTo(2)
    }

    @Test
    fun runTask2Example1() {
        val input = """
            2-4,6-8
            2-3,4-5
            5-7,7-9
            2-8,3-7
            6-6,4-6
            2-6,4-8
        """.trimIndent()
            .split('\n')

        assertThat(solveTask2(input)).isEqualTo(4)
    }

    fun solveTask1(input: List<String>): Int =
        input.map(String::toPair)
            .filter { (a, b) -> a.containsAll(b) || b.containsAll(a) }
            .size

    fun solveTask2(input: List<String>): Int =
        input.map(String::toPair)
            .filter { (a, b) -> a.intersect(b).isNotEmpty() }
            .size
}

private fun String.toPair() =
    split(',').let {
        it.first().toSection() to it.last().toSection()
    }

private fun String.toSection(): Set<Int> =
    split('-').let {
        it.first().toInt().rangeTo(it.last().toInt())
    }.toSet()

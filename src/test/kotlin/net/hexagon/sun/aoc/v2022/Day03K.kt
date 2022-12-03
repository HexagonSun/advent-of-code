package net.hexagon.sun.aoc.v2022

import net.hexagon.sun.aoc.AdventOfKode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day03K : AdventOfKode() {

    @Test
    override fun runTask1() {
        val input = getInputLines()
        assertThat(solveTask1(input)).isEqualTo(7967)
    }

    @Test
    override fun runTask2() {
        val input = getInputLines()
        assertThat(solveTask2(input)).isEqualTo(2716)
    }

    @Test
    fun runExample1() {
        val input = """
            vJrwpWtwJgWrhcsFMMfFFhFp
            jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
            PmmdzqPrVvPwwTWBwg
            wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
            ttgJtRGJQctTZtZT
            CrZsJsPPZsGzwwsLwLmpwMDw
        """.trimIndent()
            .split('\n')

        assertThat(solveTask1(input)).isEqualTo(157)
    }

    @Test
    fun runTask2Example1() {
        val input = """
            vJrwpWtwJgWrhcsFMMfFFhFp
            jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
            PmmdzqPrVvPwwTWBwg
            wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
            ttgJtRGJQctTZtZT
            CrZsJsPPZsGzwwsLwLmpwMDw
        """.trimIndent()
            .split('\n')

        assertThat(solveTask2(input)).isEqualTo(70)
    }

    fun solveTask1(input: List<String>): Int =
        input.sumOf {
            val middle = it.length / 2
            val first = it.substring(0 until middle)
            val second = it.substring(middle)

            val common = first.toSet().intersect(second.toSet())
            val score = common.sumOf { c -> c.toScore() }
            score
        }

    fun solveTask2(input: List<String>): Int =
        input.chunked(3).map { group ->
            val common = group.drop(1)
                .fold(group.first().toSet()) { acc, value ->
                    acc.intersect(value.toSet())
                }
            val score = common.sumOf { c -> c.toScore() }
            score
        }.sum()
}

fun Char.toScore(): Int =
    when {
        this.isLowerCase() -> this - 'a' + 1
        else -> this - 'A' + 27
    }

package net.hexagon.sun.aoc.v2018

import net.hexagon.sun.aoc.AdventOfKode
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.jupiter.api.Test

class Day01K : AdventOfKode() {

    @Test
    override fun runTask1() {
        assertThat(solveTask1(toIntList(getInputLines())), Is.`is`(538))
    }

    @Test
    override fun runTask2() {
        assertThat(solveTask2(toIntList(getInputLines())), Is.`is`(77271))
    }

    @Test
    fun runExample1() {
        val input = listOf(+1, -2, +3, +1)
        assertThat(solveTask1(input), Is.`is`(3))
    }

    @Test
    fun runExample2() {
        val input = listOf(+1, +1, +1)
        assertThat(solveTask1(input), Is.`is`(3))
    }

    @Test
    fun runExample3() {
        val input = listOf(+1, +1, -2)
        assertThat(solveTask1(input), Is.`is`(0))
    }

    @Test
    fun runExample4() {
        val input = listOf(-1, -2, -3)
        assertThat(solveTask1(input), Is.`is`(-6))
    }

    @Test
    fun runTask2Example1() {
        val input = listOf(+1, -2, +3, +1)
        assertThat(solveTask2(input), Is.`is`(2))
    }

    @Test
    fun runTask2Example2() {
        val input = listOf(+1, -1)
        // Website says expected is 0, but it's actually 1
        assertThat(solveTask2(input), Is.`is`(1))
    }

    @Test
    fun runTask2Example3() {
        val input = listOf(+3, +3, +4, -2, -4)
        assertThat(solveTask2(input), Is.`is`(10))
    }

    @Test
    fun runTask2Example4() {
        val input = listOf(-6, +3, +8, +5, -6)
        assertThat(solveTask2(input), Is.`is`(5))
    }

    @Test
    fun runTask2Example5() {
        val input = listOf(+7, +7, -2, -7, -4)
        assertThat(solveTask2(input), Is.`is`(14))
    }

    private fun solveTask1(input: List<Int>): Int =
        input.fold(0, Integer::sum)

    private fun solveTask2(input: List<Int>): Int {
        val seen: MutableSet<Int> = HashSet()
        var freq = 0
        while (true) {
            for (i in input) {
                freq += i
                if (seen.contains(freq)) {
                    return freq
                }
                seen.add(freq)
            }
        }
    }

    private fun toIntList(input: List<String>): List<Int> =
        input
            .filterNot { it.isBlank() }
            .map { it.toInt() }
}

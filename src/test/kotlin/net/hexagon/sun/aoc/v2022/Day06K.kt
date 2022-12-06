package net.hexagon.sun.aoc.v2022

import net.hexagon.sun.aoc.AdventOfKode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day06K : AdventOfKode() {

    @Test
    override fun runTask1() {
        val input = getInputAsString()
        assertThat(solveTask1(input)).isEqualTo(1582)
    }

    @Test
    override fun runTask2() {
        val input = getInputAsString()
        assertThat(solveTask2(input)).isEqualTo(3588)
    }

    @Test
    fun runExample1() {
        assertThat(solveTask1("mjqjpqmgbljsphdztnvjfqwrcgsmlb")).isEqualTo(7)
        assertThat(solveTask1("bvwbjplbgvbhsrlpgdmjqwftvncz")).isEqualTo(5)
        assertThat(solveTask1("nppdvjthqldpwncqszvftbrmjlhg")).isEqualTo(6)
        assertThat(solveTask1("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")).isEqualTo(10)
        assertThat(solveTask1("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")).isEqualTo(11)
    }

    @Test
    fun runTask2Example1() {
        assertThat(solveTask2("mjqjpqmgbljsphdztnvjfqwrcgsmlb")).isEqualTo(19)
        assertThat(solveTask2("bvwbjplbgvbhsrlpgdmjqwftvncz")).isEqualTo(23)
        assertThat(solveTask2("nppdvjthqldpwncqszvftbrmjlhg")).isEqualTo(23)
        assertThat(solveTask2("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")).isEqualTo(29)
        assertThat(solveTask2("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")).isEqualTo(26)
    }

    fun solveTask1(input: String): Int = solve(input, 4)

    fun solveTask2(input: String): Int = solve(input, 14)

    fun solve(input: String, size: Int): Int =
        input.asSequence()
            .windowed(size)
            .indexOfFirst { it.toSet().size == size }
            .plus(size)
}

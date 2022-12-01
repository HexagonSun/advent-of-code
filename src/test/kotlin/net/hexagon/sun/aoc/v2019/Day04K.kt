package net.hexagon.sun.aoc.v2019

import net.hexagon.sun.aoc.AdventOfKode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private val ThreeOrMoreIdentical = """(\d)(\1){2,}""".toRegex()

class Day04K : AdventOfKode() {

    @Test
    override fun runTask1() {
        val input = getInputAsString()
        assertThat(solveTask1(input)).isEqualTo(1675)
    }

    @Test
    override fun runTask2() {
        val input = getInputAsString()
        assertThat(solveTask2(input)).isEqualTo(1142)
    }

    @Test
    fun runExample1() {
        assertThat(122345.isValid()).isTrue
    }

    @Test
    fun runExample2() {
        assertThat(111123.isValid()).isTrue
    }

    @Test
    fun runExample3() {
        assertThat(135679.isValid()).isFalse
    }

    @Test
    fun runExample4() {
        assertThat(111111.isValid()).isTrue
    }

    @Test
    fun runExample5() {
        assertThat(223450.isValid()).isFalse
    }

    @Test
    fun runExample6() {
        assertThat(123789.isValid()).isFalse
    }

    @Test
    fun runTask2Example1() {
        assertThat(112233.isValid2()).isTrue
    }

    @Test
    fun runTask2Example2() {
        assertThat(123444.isValid2()).isFalse
    }

    @Test
    fun runTask2Example3() {
        assertThat(111122.isValid2()).isTrue
    }

    private fun solveTask1(input: String): Int =
        getRange(input).count(Int::isValid)

    private fun solveTask2(input: String): Int =
        getRange(input).count(Int::isValid2)

    private fun getRange(input: String): IntRange =
        input.split('-')
            .map { it.toInt() }
            .let { it.first().rangeTo(it.last()) }
}

private fun Int.isValid(): Boolean = toString().isValid()
private fun Int.isValid2(): Boolean = toString().isValid2()

private fun String.isValid(): Boolean {
    var adjacentDigits = false
    var monotonicallyIncreasing = true

    toCharArray()
        .asSequence()
        .windowed(2)
        .forEach { (a, b) ->
            if (a == b) { adjacentDigits = true }
            if (b < a) { monotonicallyIncreasing = false }
        }
    return adjacentDigits && monotonicallyIncreasing
}

private fun String.isValid2(): Boolean =
    isValid() && replace(ThreeOrMoreIdentical, "").isValid()

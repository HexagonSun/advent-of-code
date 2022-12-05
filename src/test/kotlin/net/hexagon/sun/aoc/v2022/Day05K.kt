package net.hexagon.sun.aoc.v2022

import net.hexagon.sun.aoc.AdventOfKode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day05K : AdventOfKode() {

    @Test
    override fun runTask1() {
        val input = getInputLines()
        assertThat(solveTask1(input)).isEqualTo("NTWZZWHFV")
    }

    @Test
    override fun runTask2() {
        val input = getInputLines()
        assertThat(solveTask2(input)).isEqualTo("BRZGFVBTJ")
    }

    @Test
    fun runExample1() {
        val input = """
                [D]    
            [N] [C]    
            [Z] [M] [P]
             1   2   3             
            
            move 1 from 2 to 1
            move 3 from 1 to 3
            move 2 from 2 to 1
            move 1 from 1 to 2
        """.trimIndent()
            .split('\n')

        assertThat(solveTask1(input)).isEqualTo("CMZ")
    }

    @Test
    fun runTask2Example1() {
        val input = """
                [D]    
            [N] [C]    
            [Z] [M] [P]
             1   2   3             
            
            move 1 from 2 to 1
            move 3 from 1 to 3
            move 2 from 2 to 1
            move 1 from 1 to 2
        """.trimIndent()
            .split('\n')

        assertThat(solveTask2(input)).isEqualTo("MCD")
    }

    fun solveTask1(input: List<String>): String =
        solve(input, 1)

    fun solveTask2(input: List<String>): String =
        solve(input, 2)

    fun solve(input: List<String>, task: Int = 1): String {
        val idxBlank = input.indexOfFirst { it.isBlank() }
        val rawStacks = input.subList(0, idxBlank - 1)
        val stackNumbers = input[idxBlank - 1]
        val maxStacks = stackNumbers.split(' ').last { it.isNotEmpty() }.toInt()
        val stackIndices = stackNumbers.mapIndexedNotNull { index, c ->
            when {
                c.isDigit() -> index to c.toString().toInt() - 1
                else -> null
            }
        }
        val moves = input.subList(idxBlank + 1, input.size).map(String::toMove)
        val stacks = MutableList(maxStacks) { mutableListOf<Char>() }

        rawStacks.reversed().forEach { line ->
            line.forEachIndexed { index, c ->
                if (c.isUpperCase()) {
                    stackIndices
                        .first { (i, _) -> i == index }
                        .let { (_, stackNumber) ->
                            stacks
                                .getOrElse(stackNumber) { mutableListOf() }
                                .add(c)
                        }
                }
            }
        }

        moves.forEach { m ->
            val from = stacks[m.fromIndex]
            val to = stacks[m.toIndex]

            val elements = from.takeLast(m.size).let {
                when(task) {
                    1 -> it.reversed()
                    else -> it
                }
            }
            to.addAll(elements)
            var size = m.size
            while(size > 0) {
                from.removeLast()
                size--
            }
        }
        return stacks.map { it.last() }.joinToString("")
    }
}

private data class Move(
    val size: Int,
    val fromIndex: Int,
    val toIndex: Int,
)

private fun String.toMove() =
    split(' ').let {
        Move(it[1].toInt(), it[3].toInt() - 1, it[5].toInt() - 1)
    }

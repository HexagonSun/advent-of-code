package net.hexagon.sun.aoc.v2019

import net.hexagon.sun.aoc.AdventOfKode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.math.min

private typealias Coordinate = Pair<Int, Int>
private typealias Board = MutableMap<Coordinate, Location>
private typealias Step = String

class Day03K : AdventOfKode() {

    @Test
    override fun runTask1() {
        val input = getInputLines()
        assertThat(solveTask1(input[0], input[1])).isEqualTo(1285)
    }

    @Test
    override fun runTask2() {
        val input = getInputLines()
        assertThat(solveTask2(input[0], input[1])).isEqualTo(14228)
    }

    @Test
    fun runExample1() {
        val input = listOf(
            "R8,U5,L5,D3",
            "U7,R6,D4,L4",
        )
        assertThat(solveTask1(input[0], input[1])).isEqualTo(6)
    }

    @Test
    fun runExample2() {
        val input = listOf(
            "R75,D30,R83,U83,L12,D49,R71,U7,L72",
            "U62,R66,U55,R34,D71,R55,D58,R83",
        )
        assertThat(solveTask1(input[0], input[1])).isEqualTo(159)
    }

    @Test
    fun runExample3() {
        val input = listOf(
            "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51",
            "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7",
        )
        assertThat(solveTask1(input[0], input[1])).isEqualTo(135)
    }

    @Test
    fun runTask2Example1() {
        val input = listOf(
            "R8,U5,L5,D3",
            "U7,R6,D4,L4",
        )
        assertThat(solveTask2(input[0], input[1])).isEqualTo(30)
    }

    @Test
    fun runTask2Example2() {
        val input = listOf(
            "R75,D30,R83,U83,L12,D49,R71,U7,L72",
            "U62,R66,U55,R34,D71,R55,D58,R83",
        )
        assertThat(solveTask2(input[0], input[1])).isEqualTo(610)
    }

    @Test
    fun runTask2Example3() {
        val input = listOf(
            "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51",
            "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7",
        )
        assertThat(solveTask2(input[0], input[1])).isEqualTo(410)
    }

    private fun calculateBoard(wire1: String, wire2: String): Board {
        val board: Board = mutableMapOf()
        board.draw(wire1) { it, len -> it.setWireLengthA(len); it.hasWireA = true }
        board.draw(wire2) { it, len -> it.setWireLengthB(len); it.hasWireB = true }
        return board
    }

    private fun solveTask1(wire1: String, wire2: String): Int {
        val board = calculateBoard(wire1, wire2)

        return board.filterValues(Location::hasCrossingWires)
            .map { (coordinate, _) -> abs(coordinate.first) + abs(coordinate.second) }
            .minOf { it }
    }

    private fun solveTask2(wire1: String, wire2: String): Int {
        val board = calculateBoard(wire1, wire2)

        return board.filterValues(Location::hasCrossingWires)
            .map { (_, location) -> location.wireLengthA + location.wireLengthB }
            .minOf { it }
    }

    private fun Board.draw(wire: String, setWire: (Location, Int) -> Unit) {
        var coordinate = 0 to 0
        var wireLength = 0

        wire.toSteps().forEach { step ->
            var len = step.length()
            while (len > 0) {
                wireLength++
                coordinate = step.direction().stepTo(coordinate)
                this.computeIfAbsent(coordinate) { Location() }
                    .let {
                        setWire(it, wireLength)
                    }
                len--
            }
        }
    }
}

data class Location(
    var wireLengthA: Int = Int.MAX_VALUE,
    var wireLengthB: Int = Int.MAX_VALUE,
    var hasWireA: Boolean = false,
    var hasWireB: Boolean = false
)

enum class Direction(val stepTo: (Pair<Int, Int>) -> Pair<Int, Int>) {
    U({ (x: Int, y: Int): Pair<Int, Int> -> x to y + 1 }),
    L({ (x: Int, y: Int): Pair<Int, Int> -> x + 1 to y }),
    D({ (x: Int, y: Int): Pair<Int, Int> -> x to y - 1 }),
    R({ (x: Int, y: Int): Pair<Int, Int> -> x - 1 to y }),
}

private fun String.toSteps(): List<Step> = split(',')

private fun Step.direction(): Direction = Direction.valueOf(this[0].toString())
private fun Step.length(): Int = drop(1).toInt()

private fun Location.hasCrossingWires(): Boolean = hasWireA && hasWireB
private fun Location.setWireLengthA(len: Int) {
    wireLengthA = min(wireLengthA, len)
}
private fun Location.setWireLengthB(len: Int) {
    wireLengthB = min(wireLengthB, len)
}
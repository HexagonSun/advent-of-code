package net.hexagon.sun.aoc.v2022

import net.hexagon.sun.aoc.AdventOfKode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private typealias TreeGrid = List<List<Tree>>
private typealias TreeGridPosition = Pair<Int, Int>

class Day08K : AdventOfKode() {

    @Test
    override fun runTask1() {
        val input = getInputLines()
        assertThat(solveTask1(input)).isEqualTo(1733)
    }

    @Test
    override fun runTask2() {
        val input = getInputLines()
        assertThat(solveTask2(input)).isEqualTo(284648)
    }

    @Test
    fun runExample1() {
        val input = """
            30373
            25512
            65332
            33549
            35390
        """.trimIndent()
            .split('\n')

        assertThat(solveTask1(input)).isEqualTo(21)
    }

    @Test
    fun runTask2Example1() {
        val input = """
            30373
            25512
            65332
            33549
            35390
        """.trimIndent()
            .split('\n')

        assertThat(solveTask2(input)).isEqualTo(8)
    }

    private fun solveTask1(input: List<String>): Int =
        input.toGrid().let { grid ->
            grid.flatMapIndexed { row, line ->
                line.mapIndexedNotNull { col, tree ->
                    tree.takeIf {
                        it.isVisible(grid, row to col)
                    }
                }
            }.size
        }

    private fun solveTask2(input: List<String>): Int =
        input.toGrid().let { grid ->
            grid.flatMapIndexed { row, line ->
                line.mapIndexed { col, tree ->
                    tree.determineVisibleTrees(grid, row to col)
                    tree.nbVisible
                }
            }.max()
        }
}

data class Tree(val height: Int, var visible: Boolean = false, var nbVisible: Int = 1)

private enum class GirdDirection(val next: (TreeGrid, TreeGridPosition) -> Tree?) {
    UP({ grid, (row, col) -> if (row > 0) { grid[row-1][col]} else null }),
    LEFT({ grid, (row, col) -> if (col > 0) { grid[row][col-1]} else null }),
    DOWN({ grid, (row, col) -> if (row < grid.size - 1) { grid[row+1][col]} else null }),
    RIGHT({ grid, (row, col) -> if (col < grid.size - 1) { grid[row][col+1]} else null }),
}

private fun TreeGridPosition.next(direction: GirdDirection): TreeGridPosition =
    when(direction) {
        GirdDirection.UP -> first - 1 to second
        GirdDirection.LEFT -> first to second - 1
        GirdDirection.DOWN -> first + 1 to second
        GirdDirection.RIGHT -> first to second + 1
    }

private fun Char.toTree(): Tree = Tree("$this".toInt())

private fun Tree.isShadowedBy(other: Tree): Boolean = other.height >= this.height

private fun List<String>.toGrid(): TreeGrid = map { line -> line.map { it.toTree() } }

private fun Tree.isVisible(grid: TreeGrid, position: TreeGridPosition, direction: GirdDirection): Boolean {
    var cursor = position
    var next = direction.next(grid, cursor)
    var nbVisible = 0
    while(next != null) {
        if (isShadowedBy(next)) {
            nbVisible++
            this.nbVisible *= nbVisible
            return false
        }
        nbVisible++
        cursor = cursor.next(direction)
        next = direction.next(grid, cursor)
    }
    this.nbVisible *= nbVisible
    return true
}

private fun Tree.isVisible(grid: TreeGrid, position: TreeGridPosition): Boolean =
    isVisible(grid, position, GirdDirection.UP) ||
        isVisible(grid, position, GirdDirection.LEFT) ||
        isVisible(grid, position, GirdDirection.DOWN) ||
        isVisible(grid, position, GirdDirection.RIGHT)

private fun Tree.determineVisibleTrees(grid: TreeGrid, position: TreeGridPosition) {
    isVisible(grid, position, GirdDirection.UP)
    isVisible(grid, position, GirdDirection.LEFT)
    isVisible(grid, position, GirdDirection.DOWN)
    isVisible(grid, position, GirdDirection.RIGHT)
}

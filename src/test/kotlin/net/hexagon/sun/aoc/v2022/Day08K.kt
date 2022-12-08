package net.hexagon.sun.aoc.v2022

import net.hexagon.sun.aoc.AdventOfKode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

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

    fun solveTask1(input: List<String>): Int {
        val grid = input.map { line -> line.map { it.toTree() } }

        var nbVisible = 0
        grid.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, col ->
                val visible = col.isVisible(grid, rowIndex, colIndex)
                if (visible) {
                    nbVisible++
                }
            }
        }
        return nbVisible
    }

    fun solveTask2(input: List<String>): Int {
        val grid = input.map { line -> line.map { it.toTree() } }

        var maxVisible = 0
        grid.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, col ->
                col.determineVisibleTrees(grid, rowIndex, colIndex)
                if (col.nbVisible > maxVisible) {
                    maxVisible = col.nbVisible
                }
            }
        }
        return maxVisible
    }

}

data class Tree(val height: Int, var visible: Boolean = false, var nbVisible: Int = 1)

private fun Char.toTree(): Tree = Tree("$this".toInt())

private fun Tree.isVisibleFromTop(grid: List<List<Tree>>, rowIndex: Int, colIndex: Int): Boolean {
    var row = rowIndex - 1
    if (row < 0) {
        this.visible = true
        this.nbVisible = 0
        return true
    }

    var nbVisible = 0
    while (row >= 0) {
        val other = grid[row][colIndex]
        if (other.height >= this.height) {
            // tree is shadowed
            nbVisible++
            this.nbVisible *= nbVisible
            return false
        }
        nbVisible++
        row--
    }
    this.nbVisible *= nbVisible
    return true
}

private fun Tree.isVisibleFromLeft(grid: List<List<Tree>>, rowIndex: Int, colIndex: Int): Boolean {
    var col = colIndex - 1
    if (col < 0) {
        this.visible = true
        this.nbVisible = 0
        return true
    }

    var nbVisible = 0
    while (col >= 0) {
        val other = grid[rowIndex][col]
        if (other.height >= this.height) {
            // tree is shadowed
            nbVisible++
            this.nbVisible *= nbVisible
            return false
        }
        nbVisible++
        col--
    }
    this.nbVisible *= nbVisible
    return true
}

private fun Tree.isVisibleFromBottom(grid: List<List<Tree>>, rowIndex: Int, colIndex: Int): Boolean {
    var row = rowIndex + 1
    if (row >= grid.size) {
        this.visible = true
        this.nbVisible = 0
        return true
    }

    var nbVisible = 0
    while (row < grid.size) {
        val other = grid[row][colIndex]
        if (other.height >= this.height) {
            // tree is shadowed
            nbVisible++
            this.nbVisible *= nbVisible
            return false
        }
        nbVisible++
        row++
    }
    this.nbVisible *= nbVisible
    return true
}

private fun Tree.isVisibleFromRight(grid: List<List<Tree>>, rowIndex: Int, colIndex: Int): Boolean {
    var col = colIndex + 1
    if (col >= grid.size) {
        this.visible = true
        this.nbVisible = 0
        return true
    }

    var nbVisible = 0
    while (col < grid.size) {
        val other = grid[rowIndex][col]
        if (other.height >= this.height) {
            // tree is shadowed
            nbVisible++
            this.nbVisible *= nbVisible
            return false
        }
        nbVisible++
        col++
    }
    this.nbVisible *= nbVisible
    return true
}

private fun Tree.isVisible(grid: List<List<Tree>>, rowIndex: Int, colIndex: Int): Boolean =
    isVisibleFromTop(grid, rowIndex, colIndex) ||
        isVisibleFromLeft(grid, rowIndex, colIndex) ||
        isVisibleFromBottom(grid, rowIndex, colIndex) ||
        isVisibleFromRight(grid, rowIndex, colIndex)

private fun Tree.determineVisibleTrees(grid: List<List<Tree>>, rowIndex: Int, colIndex: Int) {
    isVisibleFromTop(grid, rowIndex, colIndex)
    isVisibleFromLeft(grid, rowIndex, colIndex)
    isVisibleFromBottom(grid, rowIndex, colIndex)
    isVisibleFromRight(grid, rowIndex, colIndex)
}


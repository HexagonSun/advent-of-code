package net.hexagon.sun.aoc.v2022

import net.hexagon.sun.aoc.AdventOfKode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day07K : AdventOfKode() {

    @Test
    override fun runTask1() {
        val input = getInputLines()
        assertThat(solveTask1(input)).isEqualTo(1453349)
    }

    @Test
    override fun runTask2() {
        val input = getInputLines()
        assertThat(solveTask2(input)).isEqualTo(2948823)
    }

    @Test
    fun runExample1() {
        val input = """
            $ cd /
            $ ls
            dir a
            14848514 b.txt
            8504156 c.dat
            dir d
            $ cd a
            $ ls
            dir e
            29116 f
            2557 g
            62596 h.lst
            $ cd e
            $ ls
            584 i
            $ cd ..
            $ cd ..
            $ cd d
            $ ls
            4060174 j
            8033020 d.log
            5626152 d.ext
            7214296 k
        """.trimIndent()
            .split('\n')

        assertThat(solveTask1(input)).isEqualTo(95437)
    }

    @Test
    fun runTask2Example1() {
        val input = """
            $ cd /
            $ ls
            dir a
            14848514 b.txt
            8504156 c.dat
            dir d
            $ cd a
            $ ls
            dir e
            29116 f
            2557 g
            62596 h.lst
            $ cd e
            $ ls
            584 i
            $ cd ..
            $ cd ..
            $ cd d
            $ ls
            4060174 j
            8033020 d.log
            5626152 d.ext
            7214296 k
        """.trimIndent()
            .split('\n')

        assertThat(solveTask2(input)).isEqualTo(24933642)
    }

    fun solveTask1(input: List<String>): Int =
        calculateFileSystem(input)
            .getUnder100k()
            .sumOf { it.size }

    fun solveTask2(input: List<String>): Int {
        val root = calculateFileSystem(input)

        val unused = 70000000 - root.size
        val needed = 30000000 - unused
        return root.getAllDirectories()
            .filter { it.size >= needed }
            .minBy { it.size }
            .size
    }

    private fun calculateFileSystem(input: List<String>): Directory {
        val root = Directory("/")
        var currentDir = root
        input.drop(1).forEach {
            if (it.isCommand()) {
                if (it.startsWith("$ cd")) {
                    val name = it.split(' ')[2]
                    if (name == "..") {
                        currentDir = currentDir.parent!!
                    } else {
                        var childDir = currentDir.getDirectories().firstOrNull { f -> f.name == name }
                        if (childDir == null) {
                            childDir = Directory(name, currentDir)
                            currentDir.files.add(childDir)
                        }
                        currentDir = childDir
                    }
                }
            } else if (it.isDirectory()) {
                val dir = it.toDirectory(currentDir)
                currentDir.files.add(dir)
            } else {
                val file = it.toFile()
                currentDir.files.add(file)
            }
        }
        return root
    }

    private fun Directory.getUnder100k(): List<Directory> =
        getDirectories().flatMap { it.getUnder100k() } +
        listOfNotNull(
            this.takeIf { it.size < 100000 }
        )
}

private fun String.isCommand(): Boolean = startsWith("$")
private fun String.isDirectory(): Boolean = startsWith("dir")
private fun String.toDirectory(parent: Directory): Directory =
    Directory(split(' ')[1], parent)
private fun String.toFile(): File =
    split(' ').let {
        File(it[1], it[0].toInt())
    }

private sealed class AbstractFile {
    abstract val name: String
    abstract val size: Int
}

private data class File(
    override val name: String,
    override val size: Int
) : AbstractFile()

private data class Directory(
    override val name: String,
    val parent: Directory? = null,
    val files: MutableList<AbstractFile> = mutableListOf(),
) : AbstractFile() {

    override val size: Int
        get() = files.sumOf { it.size }

    // prevent recursive calculation in debugger
    override fun toString(): String = this.name
}

private fun Directory.getDirectories(): List<Directory> =
    files.filterIsInstance<Directory>()

private fun Directory.getAllDirectories(): List<Directory> =
    files.filterIsInstance<Directory>().flatMap { it.getAllDirectories() } + listOf(this)

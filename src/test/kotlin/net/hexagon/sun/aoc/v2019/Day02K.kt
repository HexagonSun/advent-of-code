package net.hexagon.sun.aoc.v2019

import net.hexagon.sun.aoc.AdventOfKode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private typealias Program = MutableList<Int>

class Day02K : AdventOfKode() {

    @Test
    override fun runTask1() {
        val input = getInput().with1202()
        assertThat(solveTask1(input)).isEqualTo(4930687)
    }

    @Test
    override fun runTask2() {
        val input = getInput()
        val targetOutput = 19690720
        assertThat(solveTask2(input, targetOutput)).isEqualTo(5335)
    }

    @Test
    fun runExample1() {
        val input = getInput("1,9,10,3,2,3,11,0,99,30,40,50")
        assertThat(solveTask1(input)).isEqualTo(3500)
    }

    @Test
    fun runExample2() {
        val input = getInput("1,0,0,0,99")
        assertThat(solveTask1(input)).isEqualTo(2)
    }

    @Test
    fun runExample3() {
        val input = getInput("2,3,0,3,99")
        assertThat(solveTask1(input)).isEqualTo(2)
    }

    @Test
    fun runExample4() {
        val input = getInput("2,4,4,5,99,0")
        assertThat(solveTask1(input)).isEqualTo(2)
    }

    @Test
    fun runExample5() {
        val input = getInput("1,1,1,4,99,5,6,0,99")
        assertThat(solveTask1(input)).isEqualTo(30)
    }

    private fun solveTask1(program: Program): Int =
        runProgram(program)

    private fun runProgram(program: Program): Int {
        var instructionPointer = 0
        var instruction = Instruction.ADD

        while(instruction.isNotHalt()) {
            instruction = program.getOperation(instructionPointer)
            if (instruction.isHalt()) {
                break
            }

            val address1 = program[instructionPointer+1]
            val address2 = program[instructionPointer+2]
            val parameter1 = program[address1]
            val parameter2 = program[address2]
            val targetAddress = program[instructionPointer+3]

            val result = when(instruction) {
                Instruction.ADD -> parameter1 + parameter2
                Instruction.MUL -> parameter1 * parameter2
                Instruction.HLT -> break
            }
            program[targetAddress] = result

            instructionPointer += instruction.parameterValues
        }

        return program[0]
    }

    private fun Program.getOperation(pc: Int): Instruction =
        Instruction.of(this[pc])

    private fun solveTask2(input: Program, targetOutput: Int): Int {
        (0..99).forEach { noun ->
            (0..99).forEach { verb ->
//                println("Running program with ($noun, $verb)")
                val result = try {
                    runProgram(input.toMutableList().with(noun, verb))
                } catch (e: Exception) {
                    -1
                }

                if (result == targetOutput) {
//                    println("Calculated target output!")
                    return 100 * noun + verb
                }
            }
        }
        return -1
    }

    private fun getInput(raw: String = getInputAsString().trim()): Program =
         raw.split(',')
            .mapTo(mutableListOf()) { it.toInt() }
}

enum class Instruction(val code: Int, val parameterValues: Int) {
    ADD(1, 4),
    MUL(2, 4),
    HLT(99, 1),
    ;

    companion object {
        fun of(code: Int): Instruction =
            Instruction.values().first { it.code == code }
    }
}

private fun Instruction.`is`(instruction: Instruction) = this == instruction
private fun Instruction.isHalt() = `is`(Instruction.HLT)
private fun Instruction.isNotHalt() = !isHalt()

private fun Program.with1202(): Program =
    with(12, 2)

private fun Program.with(noun: Int, verb: Int): Program =
    also {
        set(1, noun)
        set(2, verb)
    }

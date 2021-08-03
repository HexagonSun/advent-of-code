package net.hexagon.sun.aoc

import java.io.IOException

private const val BASE_PATH = "./input/"

abstract class AdventOfKode {

    abstract fun runTask1()
    abstract fun runTask2()

    protected fun getInputArray(): CharArray = getInputAsString().toCharArray()

    protected fun getInputAsString(): String = getInputAsString(getResourceNameFromClass())

    protected fun getInputLines(): List<String> = getInputLines(getResourceNameFromClass())

    protected fun getInputArray(resource: String): CharArray = getInputAsString(resource).toCharArray()

    protected fun getInputAsString(resource: String): String =
        try {
            this.javaClass.getResource(resource)!!.readText()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    protected fun getInputLines(resource: String): List<String> =
        getInputAsString(resource).lines()

    private fun getResourceNameFromClass(): String =
        getResourceName(javaClass.simpleName.removeSuffix("K").lowercase())

    private fun getResourceName(suffix: String): String = BASE_PATH + suffix
}
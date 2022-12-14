package day05

import readInput

fun main() {

    class State(val stacks: Array<MutableList<Char>>, val allAtOnce: Boolean) {
        fun move(num: Int, from: Int, to: Int) {
            val buffer = ArrayDeque<Char>()
            repeat(num) {
                val c = stacks[from].removeLast()
                if (allAtOnce) {
                    buffer.addFirst(c)
                } else {
                    buffer.addLast(c)
                }
            }
            stacks[to].addAll(buffer)
        }
    }

    fun parse(input: List<String>, allAtOnce: Boolean): State {
        val numsIndex = input.indexOfFirst { it.startsWith(" 1 ") }
        val size = input[numsIndex].trim().split("\\s+".toRegex()).size
        val stacks = Array<MutableList<Char>>(size) { mutableListOf() }
        (0 until numsIndex)
            .reversed()
            .map { input[it] }
            .forEach { level ->
                for (i in 0 until  size) {
                    val shift = 1 + i * 4
                    if (shift < level.length && level[shift] != ' ') {
                        stacks[i].add(level[shift])
                    }
                }
            }
        return State(stacks, allAtOnce)
    }


    fun solve(input: List<String>, allAtOnce: Boolean): String {
        val state = parse(input, allAtOnce)
        val regex = "move (\\d+) from (\\d+) to (\\d+)".toRegex()
        input.forEach { line ->
            val match = regex.matchEntire(line)
            if (match != null) {
                val num = match.groups[1]!!.value.toInt()
                val from = match.groups[2]!!.value.toInt() - 1
                val to = match.groups[3]!!.value.toInt() - 1
                state.move(num, from, to)
            }
        }
        return state.stacks.joinToString(separator = "") { it.last().toString() }
    }

    fun part1(input: List<String>): String {
        return solve(input, false)
    }

    fun part2(input: List<String>): String {
        return solve(input, true)
    }

    val testInput = readInput("day05", "test")
    val input = readInput("day05", "input")
    check(part1(testInput) == "CMZ")
    println(part1(input))

    check(part2(testInput) == "MCD")
    println(part2(input))
}

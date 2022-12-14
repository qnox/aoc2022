package day06

import readInput

fun main() {

    fun solve(input: List<String>, num: Int): Int {
        val str = input[0]
        return (0 until str.length - num + 1)
            .asSequence()
            .filter {
                str.substring(it, it + num).toSet().size == num
            }
            .first() + num
    }

    fun part1(input: List<String>): Int {
        return solve(input, 4)
    }

    fun part2(input: List<String>): Int {
        return solve(input, 14)
    }

    val testInput = readInput("day06", "test")
    val input = readInput("day06", "input")
    check(part1(testInput) == 7)
    println(part1(input))

    check(part2(testInput) == 19)
    println(part2(input))
}

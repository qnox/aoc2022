package day04

import readInput

typealias Range = List<Int>

fun Range.includes(range: Range): Boolean = this[0] <= range[0] && this[1] >= range[1]
fun Range.overlap(range: Range): Boolean = this[1] >= range[0] && this[0] <= range[1]

fun main() {
    fun part1(input: List<String>): Int {
        return input.count {line ->
            val (r1: Range, r2: Range) = line.split(",").map { it.split("-").map { it.toInt() } }
            r1.includes(r2) || r2.includes(r1)
        }
    }

    fun part2(input: List<String>): Int {
        return input.count {line ->
            val (r1: Range, r2: Range) = line.split(",").map { it.split("-").map { it.toInt() } }
            r1.overlap(r2)
        }
    }

    val testInput = readInput("day04", "test")
    val input = readInput("day04", "input")
    check(part1(testInput) == 2)
    println(part1(input))

    check(part2(testInput) == 4)
    println(part2(input))
}

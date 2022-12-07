package day02

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.fold(0) { acc, line ->
            val p1 = line[0] - 'A' + 1
            val p2 = line[2] - 'X' + 1
            acc + p2 + if (p1 == p2) {
                3
            } else if (p2 - p1 == 1 || (p1 == 3 && p2 == 1)) {
                6
            } else {
                0
            }
        }
    }

    fun part2(input: List<String>): Int {
        return input.fold(0) { acc, line ->
            val p1 = line[0] - 'A'
            val p2 = when (line[2]) {
                'X' -> (p1 + 3 - 1) % 3
                'Y' -> p1
                'Z' -> (p1 + 1) % 3
                else -> error(line[2])
            }
            acc + p2 + 1 + when (line[2]) {
                'X' -> 0
                'Y' -> 3
                'Z' -> 6
                else -> error(line[2])
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02", "test")
    val input = readInput("day02", "input")
    check(part1(testInput) == 22)
    println(part1(input))

    check(part2(testInput) == 14)
    println(part2(input))
}

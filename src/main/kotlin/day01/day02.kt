package day01

import readInput
import java.util.PriorityQueue

fun main() {
    fun part1(input: List<String>): Int {
        return input.fold(0 to 0) { acc, v ->
            if (v.isEmpty()) {
                maxOf(acc.first, acc.second) to 0
            } else {
                acc.first to acc.second + v.toInt()
            }
        }.first
    }

    fun part2(input: List<String>): Int {
        val (q, acc) = input.fold(PriorityQueue<Int>(reverseOrder()) to 0) { (q, acc), v ->
            if (v.isEmpty()) {
                q.add(acc)
                q to 0
            } else {
                q to acc + v.toInt()
            }
        }
        q.add(acc)
        var result = 0
        repeat(3) {
            result += q.remove()
        }
        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day01", "test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("day01", "input")
    println(part1(input))
    println(part2(input))
}

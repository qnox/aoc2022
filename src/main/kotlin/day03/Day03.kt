package day03

import readInput

fun main() {
    fun value(item: Char) = if (item in 'a'..'z') {
        item - 'a' + 1
    } else {
        item - 'A' + 27
    }

    fun part1(input: List<String>): Int {
        return input.sumOf {line ->
            val cmp1 = line.substring(0, line.length / 2)
            val cmp2 = line.substring(line.length / 2)
            val failedItems = cmp1.toSet().intersect(cmp2.toSet())
            check(failedItems.size == 1)
            val item = failedItems.first()
            value(item)
        }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf {group ->
            val badges = group.map { it.toSet() }.reduce { v1, v2 -> v1.intersect(v2) }
            check(badges.size == 1)
            val badge = badges.first()
            value(badge)
        }
    }

    val testInput = readInput("day03", "test")
    val input = readInput("day03", "input")
    check(part1(testInput) == 157)
    println(part1(input))

    check(part2(testInput) == 70)
    println(part2(input))
}

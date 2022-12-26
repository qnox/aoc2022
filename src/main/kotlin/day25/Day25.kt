package day25


import readInput
import java.math.BigDecimal

fun toDec(snafu: String): Long {
    var result = BigDecimal.ZERO
    var multiplier = BigDecimal.ONE
    for (c in snafu.reversed()) {
        val n = when (c) {
            '=' -> 0
            '-' -> 1
            '0' -> 2
            '1' -> 3
            '2' -> 4
            else -> error("Unexpected char $c")
        }
        result += multiplier * BigDecimal.valueOf(n - 2L)

        multiplier *= BigDecimal.valueOf(5L)
    }
    return result.toLong()
}

fun toSnafu(sumOf: Long): String {
    val sb = StringBuilder()
    var rest = sumOf
    while (rest > 0) {
        rest += 2
        val n = rest % 5
        rest /= 5

        val c = when (n.toInt()) {
            0 -> '='
            1 -> '-'
            2 -> '0'
            3 -> '1'
            4 -> '2'
            else -> error("Unexpected num $n")
        }
        sb.append(c)
    }
    return sb.reversed().toString()
}

fun main() {

    fun part1(input: List<String>): String {
        return toSnafu(input.sumOf { toDec(it) })
    }

    fun part2(input: List<String>): Long {
        return 1
    }

    val testInput = readInput("day25", "test")
    val input = readInput("day25", "input")
    val part1 = part1(testInput)
    println(part1)
    check(part1 == "2=-1=0")
    println(part1(input))

    val part2 = part2(testInput)
    println(part2)
    check(part2 == 54L)
    println(part2(input))
}


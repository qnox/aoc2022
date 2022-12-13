package day10


import readInput

fun main() {

    fun toSequence(input: List<String>) = sequence {
        var register = 1
        input.forEach { line ->
            if (line.startsWith("noop")) {
                yield(register)
            } else if (line.startsWith("addx ")) {
                val value = line.substring(5).toInt()
                yield(register)
                yield(register)
                register += value
            } else {
                error("Unknown input $line")
            }
        }
    }

    fun part1(input: List<String>): Long {
        val sequence = toSequence(input)
        return sequence
            .mapIndexed { index, i -> (index + 1).toLong() * i }
            .filterIndexed { index, _ -> index == 19 || (index - 19) % 40 == 0 }
            .sum()
    }

    fun part2(input: List<String>): String {
        val sequence = toSequence(input).iterator()
        return (0..5).joinToString(separator = "\n") { row ->
            (0..39).joinToString(separator = "") { column ->
                val middle = sequence.next()
                if (column in middle - 1..middle + 1) {
                    "#"
                } else {
                    "."
                }
            }
        }
    }

    val testInput = readInput("day10", "test")
    val input = readInput("day10", "input")
    check(part1(testInput) == 13140L)
    println(part1(input))

    println(part2(testInput))
    println()
    println(part2(input))
}

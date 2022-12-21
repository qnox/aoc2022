package day20


import readInput

fun main() {

    fun mix(numbers: IntArray, decryptionKey: Int = 1, repeatCount: Int = 1): List<Long> {
        val actualNumbers = numbers.map { it.toLong() * decryptionKey }.withIndex()
        val mixed = actualNumbers.toMutableList()
        repeat(repeatCount) {
            actualNumbers.forEach { number ->
                val index = mixed.indexOf(number)
                val item = mixed.removeAt(index)
                mixed.add((index + number.value).mod(mixed.size), item)
            }
        }
        return mixed.map { it.value }
    }

    fun part1(input: List<String>): Long {
        val list = input.map { it.toInt() }.toIntArray()
        val result = mix(list)
        val start = result.indexOf(0)
        return listOf(1000, 2000, 3000).sumOf { result[(start + it) % result.size] }
    }

    fun part2(input: List<String>): Long {
        val list = input.map { it.toInt() }.toIntArray()
        val result = mix(list, decryptionKey = 811589153, repeatCount = 10)
        val start = result.indexOf(0)
        return listOf(1000, 2000, 3000).sumOf { result[(start + it) % result.size] }
    }


    val testInput = readInput("day20", "test")
    val input = readInput("day20", "input")
    val part1 = part1(testInput)
    println(part1)
    check(part1 == 3L)
    println(part1(input))

    val part2 = part2(testInput)
    println(part2)
    check(part2 == 1623178306L)
    println(part2(input))
}


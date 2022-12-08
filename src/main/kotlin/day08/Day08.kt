package day08

import readInput
import java.util.BitSet
import java.util.PriorityQueue

fun main() {

    fun part1(input: List<String>): Int {
        val m = input.size
        val n = input[0].length
        val bitSet = BitSet(m * n)

        fun check(startX: Int, startY: Int, deltaX: Int, deltaY: Int) {
            var x = startX
            var y = startY
            var max = -1
            while (x in 0 until m && y in 0 until n) {
                val treeHeight = input[x][y].digitToInt()
                if (treeHeight > max) {
                    bitSet[x * n + y] = true
                }
                max = maxOf(max, treeHeight)
                x += deltaX
                y += deltaY
            }
        }

        for (i in 0 until m) {
            check(i, 0, 0, 1)
            check(i, n - 1, 0, -1)
        }
        for (i in 0 until n) {
            check(0, i, 1, 0)
            check(m - 1, i, -1, 0)
        }
        println((0 until m).joinToString(separator = "\n") { x ->
            (0 until m).joinToString(separator = "") { y -> if (bitSet[x * n + y]) "1" else "0" }
        })
        return bitSet.cardinality()
    }

    fun part2(input: List<String>): Int {
        val m = input.size
        val n = input[0].length
        val result = IntArray(m * n) { 1 }

        fun check(startX: Int, startY: Int, deltaX: Int, deltaY: Int) {
            var x = startX
            var y = startY
            val q = PriorityQueue<Pair<Int, Int>>(compareBy { it.first })
            while (x in 0 until m && y in 0 until n) {
                val treeHeight = input[x][y].digitToInt()
                var counter = 0
                while (q.isNotEmpty() && q.peek().first < treeHeight) {
                    val (_, count) = q.remove()
                    counter += count
                }
                result[x * n + y] *= counter + if (q.isNotEmpty()) 1 else 0
                q.add(treeHeight to counter + 1)
                x += deltaX
                y += deltaY
            }
        }

        for (i in 0 until m) {
            check(i, 0, 0, 1)
            check(i, n - 1, 0, -1)
        }
        for (i in 0 until n) {
            check(0, i, 1, 0)
            check(m - 1, i, -1, 0)
        }
        println((0 until m).joinToString(separator = "\n") { x ->
            (0 until m).joinToString(separator = " ") { y -> result[x * n + y].toString().padStart(2) }
        })
        return result.max()
    }

    val testInput = readInput("day08", "test")
    val input = readInput("day08", "input")
    check(part1(testInput) == 21)
    println(part1(input))

    check(part2(testInput) == 8)
    check(part2(input) == 410400)
    println(part2(input))
}

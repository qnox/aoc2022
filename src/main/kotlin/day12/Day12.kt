package day12


import readInput
import java.util.LinkedList

fun main() {

    data class Location(val x: Int, val y: Int)

    fun findEnd(input: List<String>): Location {
        for (x in input.indices) {
            for (y in input[x].indices) {
                if (input[x][y] == 'E') {
                    return Location(x, y)
                }
            }
        }
        error("No start found")
    }

    fun height(c: Char): Int {
        return when (c) {
            'S' -> 0
            'E' -> 'z' - 'a'
            else -> c - 'a'
        }
    }

    fun solve(input: List<String>, ends: Set<Char>): Int {
        val q = LinkedList<Location>()
        q.add(findEnd(input))
        var steps = 0
        val directions = listOf(
            1 to 0,
            0 to 1,
            -1 to 0,
            0 to -1
        )
        val visited = mutableSetOf<Location>()
        while (q.isNotEmpty()) {
            var rest = q.size
            while (rest > 0) {
                val l = q.removeFirst()
                if (input[l.x][l.y] in ends) {
                    return steps
                }
                directions.forEach { (dx, dy) ->
                    val nx = l.x + dx
                    val ny = l.y + dy
                    if (nx in input.indices
                        && ny in input[nx].indices
                        && height(input[l.x][l.y]) - height(input[nx][ny]) < 2
                        && visited.add(Location(nx, ny))
                    ) {
                        q.addLast(Location(nx, ny))
                    }
                }
                rest -= 1
            }
            steps += 1
        }
        error("No solution")
    }

    fun part1(input: List<String>): Int {
        return solve(input, setOf('S'))
    }

    fun part2(input: List<String>): Int {
        return solve(input, setOf('a', 'S'))
    }

    val testInput = readInput("day12", "test")
    val input = readInput("day12", "input")
    check(part1(testInput) == 31)
    println(part1(input))

    check(part2(testInput) == 29)
    println(part2(input))
}

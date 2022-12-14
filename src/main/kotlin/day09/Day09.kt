package day09


import readInput
import kotlin.math.abs
import kotlin.math.sign

data class Position(val x: Int, val y: Int) {
    operator fun plus(direction: Direction): Position {
        return Position(x + direction.x, y + direction.y)
    }

    fun inTouch(other: Position): Boolean {
        return abs(x - other.x) <= 1 && abs(y - other.y) <= 1
    }
}

enum class Direction(val char: Char, val x: Int, val y: Int) {
    RIGHT('R', 1, 0),
    LEFT('L', -1, 0),
    UP('U', 0, 1),
    DOWN('D', 0, -1);

    companion object {
        fun getByChar(c: Char): Direction {
            return values().first { it.char == c }
        }
    }
}

data class State(val knots: List<Position>) {

    fun move(direction: Direction): State {
        val newRope = mutableListOf(knots.first() + direction)
        for (i in 1 until knots.size) {
            val knot = knots[i]
            val head = newRope.last()
            val tail = if (head.inTouch(knot)) {
                knot
            } else {
                Position(knot.x + (head.x - knot.x).sign, knot.y + (head.y - knot.y).sign)
            }
            newRope.add(tail)
        }
        return State(newRope)
    }

}

fun main() {

    fun simulate(input: List<String>, n: Int): Int {
        var state1 = State((0 until n).map { Position(0, 0) })
        val visited = mutableSetOf<Position>()
        input.forEach {
            val direction = Direction.getByChar(it[0])
            val count = it.substring(2).toInt()
            repeat(count) {
                state1 = state1.move(direction)
                visited.add(state1.knots.last())
                println("${direction.name} $state1")
            }
        }

        return visited.size
    }

    fun part1(input: List<String>): Int {
        return simulate(input, 2)
    }

    fun part2(input: List<String>): Int {
        return simulate(input, 10)
    }

    val testInput = readInput("day09", "test")
    val input = readInput("day09", "input")
    check(part1(testInput) == 13)
    println(part1(input))

    val testInput2 = readInput("day09", "test2")
    println(part2(testInput2))
    check(part2(testInput2) == 36)
    println(part2(input))
}

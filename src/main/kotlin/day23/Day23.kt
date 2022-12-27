package day23


import readInput

data class Position(val col: Int, val row: Int)

enum class Direction(val dx: Int, val dy: Int, val horizontal: Boolean) {

    LEFT(-1, 0, false),
    UP(0, -1, true),
    RIGHT(1, 0, false),
    DOWN(0, 1, true);

}

data class Limits(val minX: Int, val minY: Int, val maxX: Int, val maxY: Int)

data class State(val elves: Set<Position>) {
    fun result(): Int {
        return limits.let {
            (it.maxY - it.minY + 1) * (it.maxX - it.minX + 1) - elves.size
        }
    }

    private val limits = elves.fold(Limits(Int.MAX_VALUE, Int.MAX_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)) { acc, elf ->
        Limits(
            minOf(acc.minX, elf.col),
            minOf(acc.minY, elf.row),
            maxOf(acc.maxX, elf.col),
            maxOf(acc.maxY, elf.row)
        )
    }

    private val directions = listOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)
    fun round(round: Int): State {
        val moves = mutableMapOf<Position, Position>()
        val seen = mutableSetOf<Position>()
        val invalid = mutableSetOf<Position>()
        for (elf in elves) {
            if (!shouldMove(elf)) {
                continue
            }
            for (i in 0..3) {
                val direction = directions[(round + i) % 4]
                val nx = elf.col + direction.dx
                val ny = elf.row + direction.dy
                val horizontal = direction.horizontal
                val position = Position(nx, ny)
                if (isValid(position, horizontal)) {
                    if (!seen.add(position)) {
                        invalid.add(position)
                    }
                    moves[elf] = position
                    break
                }
            }
        }
        return State(
            elves.map {
                moves[it]?.let { pos -> if (!invalid.contains(pos)) pos else it } ?: it
            }.toSet()
        )
    }

    fun shouldMove(position: Position): Boolean {
        return listOf(
            Position(position.col - 1, position.row),
            Position(position.col - 1, position.row - 1),
            Position(position.col, position.row - 1),
            Position(position.col + 1, position.row - 1),
            Position(position.col + 1, position.row),
            Position(position.col + 1, position.row + 1),
            Position(position.col, position.row + 1),
            Position(position.col - 1, position.row + 1)
        ).any { isOccupied(it) }
    }

    fun isValid(position: Position, horizontal: Boolean): Boolean {
        val positionsToCheck = if (horizontal) {
            listOf(position, Position(position.col - 1, position.row), Position(position.col + 1, position.row))
        } else {
            listOf(position, Position(position.col, position.row - 1), Position(position.col, position.row + 1))
        }
        return positionsToCheck.none { isOccupied(it) }
    }

    private fun isOccupied(position: Position): Boolean {
        return elves.contains(position)
    }

    fun print() {
        println("${limits.minX} ${limits.minY} ")
        println((limits.minY..limits.maxY).joinToString(separator = "\n") { y ->
            (limits.minX..limits.maxX).joinToString(separator = "") { x ->
                if (isOccupied(Position(x, y))) {
                    "#"
                } else {
                    "."
                }
            }
        })
    }
}

fun parse(input: List<String>): State {
    val map = input.takeWhile { it.isNotEmpty() }
    val elves = map.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c -> if (c == '#') Position(x, y) else null }
    }.map { it }
    return State(elves.toSet())
}

fun main() {

    fun part1(input: List<String>): Int {
        var state = parse(input)
        state.print()
        repeat(10) {
            state = state.round(it)
            state.print()
        }
        return state.result()
    }

    fun part2(input: List<String>): Int {
        var state = parse(input)
        var round = 0
        while (true) {
            val newState = state.round(round)
            round += 1
            if (newState == state) {
                break
            }
            state = newState
        }
        return round
    }

    val testInput = readInput("day23", "test")
    val input = readInput("day23", "input")
    val part1 = part1(testInput)
    println(part1)
    check(part1 == 110)
    println(part1(input))

    val part2 = part2(testInput)
    println(part2)
    check(part2 == 20)
    println(part2(input))
}


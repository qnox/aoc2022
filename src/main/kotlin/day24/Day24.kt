package day24


import readInput
import java.util.PriorityQueue
import kotlin.math.abs

class Map(
    val width: Int,
    val height: Int,
    val map: List<String>,
    private val horizontal: kotlin.collections.Map<Int, List<Blizzard>>,
    private val vertical: kotlin.collections.Map<Int, List<Blizzard>>
) {

    fun neighbours(position: Position, round: Int): List<Position> {
        return listOf(
            position,
            position + Direction.RIGHT,
            position + Direction.LEFT,
            position + Direction.UP,
            position + Direction.DOWN,
        )
            .filter { itValid(it, round + 1) }

    }

    private fun itValid(it: Position, round: Int): Boolean {
        return it.col in (0 until width)
                && it.row in (0 until height)
                && map[it.row][it.col] != '#'
                && checkHorizontal(it, round)
                && checkVertical(it, round)
    }

    private fun checkHorizontal(it: Position, round: Int): Boolean {
        val innerCol = it.col - 1
        val blizzards = horizontal[it.row] ?: emptyList()
        return blizzards.none { innerCol == currentCol(it, round) }
    }

    private fun currentCol(it: Blizzard, round: Int): Int {
        val innerWidth = width - 2
        return ((it.initPosition.col - 1 + innerWidth + (it.direction.dx * round) % innerWidth) % innerWidth)
    }

    private fun checkVertical(it: Position, round: Int): Boolean {
        val innerRow = it.row - 1
        val blizzards = vertical[it.col] ?: emptyList()
        return blizzards.none { innerRow == currentRow(it, round) }
    }

    private fun currentRow(it: Blizzard, round: Int): Int {
        val innerHeight = height - 2
        return ((it.initPosition.row - 1 + innerHeight + (it.direction.dy * round) % innerHeight) % innerHeight)
    }

    fun println(round: Int) {
        val result: Array<CharArray> = Array(height) { CharArray(width) { '.' } }
        for (blizzard in vertical.values.flatten() + horizontal.values.flatten()) {
            val row = currentRow(blizzard, round) + 1
            val col = currentCol(blizzard, round) + 1
            val char = when (blizzard.direction) {
                Direction.LEFT -> '<'
                Direction.RIGHT -> '>'
                Direction.UP -> '^'
                Direction.DOWN -> 'v'
            }
            result[row][col] = char
        }

        println(result.joinToString(separator = "\n") { String(it) })
    }

}

data class Position(val col: Int, val row: Int) {
    operator fun plus(dir: Direction): Position = Position(col + dir.dx, row + dir.dy)
}

enum class Direction(val dx: Int, val dy: Int, val value: Int) {

    LEFT(-1, 0, 2),
    UP(0, -1, 3),
    RIGHT(1, 0, 0),
    DOWN(0, 1, 1);
}

data class Blizzard(val initPosition: Position, val direction: Direction)

fun parse(input: List<String>): Map {
    val map = input.takeWhile { it.isNotEmpty() }
    val (horizontal, vertical) = map.flatMapIndexed { y, line ->
        line.mapIndexed { x, c ->
            when (c) {
                '>' -> Blizzard(Position(x, y), Direction.RIGHT)
                '<' -> Blizzard(Position(x, y), Direction.LEFT)
                '^' -> Blizzard(Position(x, y), Direction.UP)
                'v' -> Blizzard(Position(x, y), Direction.DOWN)
                '.', '#' -> null
                else -> error("Unexpected char $c")
            }
        }.filterNotNull()
    }
        .partition { it.direction in setOf(Direction.LEFT, Direction.RIGHT) }
        .let { (horizontal, vertical) ->
            horizontal.groupBy { it.initPosition.row } to vertical.groupBy { it.initPosition.col }
        }
    return Map(map[0].length, map.size, map, horizontal, vertical)
}

data class Path(val p: Position, val round: Int) {
    operator fun plus(it: Position): Path = Path(it, round + 1)
}

fun main() {

    fun solve(map: Map, start: Position, end: Position, startRound: Int): Int {
        val q = PriorityQueue(compareBy<Path> { (p, r) -> r + abs(end.col - p.col) + abs(end.row - p.row) })
        val seen = mutableSetOf<Path>()
        q.add(Path(start, startRound))
        while (q.isNotEmpty()) {
            val (p, round) = q.poll()
            if (p == end) {
                return round
            }
            val neighbours = map.neighbours(p, round)
            neighbours.forEach {
                val path = Path(it, round + 1)
                if (seen.add(path)) {
                    q.add(path)
                }
            }
        }

        return -1
    }

    fun part1(input: List<String>): Int {
        val map = parse(input)
        val end = Position(map.width - 2, map.height - 1)
        val start = Position(1, 0)
        return solve(map, start, end, 0)
    }

    fun part2(input: List<String>): Int {
        val map = parse(input)
        val end = Position(map.width - 2, map.height - 1)
        val start = Position(1, 0)
        val trip1 = solve(map, start, end, 0)
        val trip2 = solve(map, end, start, trip1)
        return solve(map, start, end, trip2)
    }

    val testInput = readInput("day24", "test")
    val input = readInput("day24", "input")
    val part1 = part1(testInput)
    println(part1)
    check(part1 == 18)
    println(part1(input))

    val part2 = part2(testInput)
    println(part2)
    check(part2 == 54)
    println(part2(input))
}


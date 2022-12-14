package day14


import readInput

fun main() {

    data class Location(val x: Int, val y: Int)

    class Waterfall(val cond:(Location, Int) -> Boolean) {

        val state = Array(1000) { IntArray(1000) }
        var max = 0
        fun addLine(f: Location, t: Location) {
            for (x in minOf(f.x, t.x)..maxOf(f.x, t.x)) {
                for (y in minOf(f.y, t.y)..maxOf(f.y, t.y)) {
                    state[x][y] = 1
                    max = maxOf(max, y + 1)
                }
            }
        }
        fun simulate(): Boolean {
            var p = Location(500, 0)
            while(p.y < max) {
                if (state[p.x][p.y + 1] == 0) {
                    p = Location(p.x, p.y + 1)
                } else if (state[p.x - 1][p.y + 1] == 0) {
                    p = Location(p.x - 1, p.y + 1)
                } else if (state[p.x + 1][p.y + 1] == 0) {
                    p = Location(p.x + 1, p.y + 1)
                } else {
                    break
                }
            }
            state[p.x][p.y] = 2
            return cond(p, max)
        }
    }

    fun fill(input: List<String>, wf: Waterfall) {
        input.forEach { line ->
            line.split(" -> ")
                .map {
                    it.split(",")
                        .let { (x, y) -> Location(x.toInt(), y.toInt()) }
                }
                .windowed(2)
                .forEach { (f, t) ->
                    wf.addLine(f, t)
                }
        }
    }

    fun part1(input: List<String>): Int {
        val wf = Waterfall { p, max -> p.y == max }
        fill(input, wf)
        var counter = 0
        while (!wf.simulate()) {
            counter += 1
        }
        return counter
    }

    fun part2(input: List<String>): Int {
        val wf = Waterfall { p, max -> p == Location(500, 0) }
        fill(input, wf)
        var counter = 0
        while (!wf.simulate()) {
            counter += 1
        }
        return counter + 1
    }

    val testInput = readInput("day14", "test")
    val input = readInput("day14", "input")
    check(part1(testInput) == 24)
    println(part1(input))

    check(part2(testInput) == 93)
    println(part2(input))
}

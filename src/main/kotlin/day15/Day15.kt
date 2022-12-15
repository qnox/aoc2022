package day15


import readInput
import kotlin.math.abs

fun main() {

    data class Scanner(val sx: Int, val sy: Int, val bx: Int, val by: Int) {
        val dist: Int
            get() = abs(sx - bx) + abs(sy - by)
    }

    val pattern = "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)".toRegex()
    fun parseScanners(input: List<String>) = input.mapNotNull { line ->
        pattern.matchEntire(line)?.let { match ->
            val sx = match.groups[1]!!.value.toInt()
            val sy = match.groups[2]!!.value.toInt()
            val bx = match.groups[3]!!.value.toInt()
            val by = match.groups[4]!!.value.toInt()
            Scanner(sx, sy, bx, by)
        }
    }

    fun calculateRanges(
        scanners: List<Scanner>,
        target: Int
    ) = scanners
        .mapNotNull { scanner ->
            val delta = scanner.dist - abs(scanner.sy - target)
            if (delta >= 0) {
                scanner.sx - delta to scanner.sx + delta
            } else {
                null
            }
        }
        .sortedBy { it.first }
        .fold(mutableListOf<Pair<Int, Int>>()) { ranges, range ->
            if (ranges.isEmpty()) {
                ranges.add(range)
            } else {
                val last = ranges.last()
                if (last.second >= range.first && last.second < range.second) {
                    ranges.removeLast()
                    ranges.add(last.first to range.second)
                } else if (last.second < range.first) {
                    ranges.add(range.first to range.second)
                }
            }
            ranges
        }

    fun part1(input: List<String>, target: Int): Int {
        val scanners = parseScanners(input)
        val ranges = calculateRanges(scanners, target)
        val beacons = scanners.filter { it.by == target }.map { it.bx }.toSortedSet()
        var count = 0
        var last = Int.MIN_VALUE
        for ((f, t) in ranges) {
            val start = maxOf(last + 1, f)
            val end = t
            val length = maxOf(0, end - start + 1)
            count += length - beacons.count { it in start..end }
            last = maxOf(last, end)
        }
        return count
    }

    fun part2(input: List<String>, limit: Int): Long {
        val scanners = parseScanners(input)
        for (y in 0 until  limit) {
            val ranges = calculateRanges(scanners, y)
            if (ranges.size > 1) {
                val x = ranges[0].second + 1
                return x.toLong() * 4000000 + y
            }
        }
        return -1
    }

    val testInput = readInput("day15", "test")
    val input = readInput("day15", "input")
    check(part1(testInput, 10) == 26)
    println(part1(input, 2000000))

    check(part2(testInput, 20) == 56000011L)
    println(part2(input, 4000000))
}

package day18


import readInput
import java.util.BitSet

fun main() {

    data class Cube(val x: Int, val y: Int, val z: Int)

    data class Limits(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int, val minZ: Int, val maxZ: Int) {

        val numX = maxX - minX + 1
        val numY = maxY - minY + 1
        val numZ = maxZ - minZ + 1

        val size: Int = numX * numY * numZ

        fun index(x: Int, y: Int, z: Int): Int = if (isValid(x, y, z)) {
            (x - minX) * numY * numZ + (y - minY) * numZ + (z - minZ)
        } else {
            -1
        }

        private fun isValid(x: Int, y: Int, z: Int) = x in minX..maxX && y in minY..maxY && z in minZ..maxZ

        fun neighbours(cube: Cube): List<Cube> {
            val x = cube.x
            val y = cube.y
            val z = cube.z
            val result = listOf<Cube>(
                Cube(x + 1, y, z),
                Cube(x - 1, y, z),
                Cube(x, y + 1, z),
                Cube(x, y - 1, z),
                Cube(x, y, z + 1),
                Cube(x, y, z - 1)
            ).filter {
                isValid(it.x, it.y, it.z)
            }
            return result
        }

        fun neighbours(x: Int, y: Int, z: Int): List<Int> {
            val result = mutableListOf<Int>()
            index(x + 1, y, z).takeIf { it >= 0 }?.let { result.add(it) }
            index(x - 1, y, z).takeIf { it >= 0 }?.let { result.add(it) }
            index(x, y + 1, z).takeIf { it >= 0 }?.let { result.add(it) }
            index(x, y - 1, z).takeIf { it >= 0 }?.let { result.add(it) }
            index(x, y, z + 1).takeIf { it >= 0 }?.let { result.add(it) }
            index(x, y, z - 1).takeIf { it >= 0 }?.let { result.add(it) }
            return result
        }

    }

    fun parseInput(input: List<String>): Pair<List<Cube>, Limits> {
        val cubes = input.map { it.split(",").let { (x, y, z) -> Cube(x.toInt(), y.toInt(), z.toInt()) } }
        val limits = cubes.fold(
            Limits(
                Int.MAX_VALUE,
                Int.MIN_VALUE,
                Int.MAX_VALUE,
                Int.MIN_VALUE,
                Int.MAX_VALUE,
                Int.MIN_VALUE
            )
        ) { limits, cube ->
            Limits(
                minOf(limits.minX, cube.x - 1),
                maxOf(limits.maxX, cube.x + 1),
                minOf(limits.minY, cube.y - 1),
                maxOf(limits.maxY, cube.y + 1),
                minOf(limits.minZ, cube.z - 1),
                maxOf(limits.maxZ, cube.z + 1),
            )
        }
        return Pair(cubes, limits)
    }

    fun part1(input: List<String>): Int {
        val (cubes, limits) = parseInput(input)
        var result = 0
        val space = BitSet(limits.size)
        cubes.forEach { cube ->
            space[limits.index(cube.x, cube.y, cube.z)] = true
            result += 6
            val neighbours = limits.neighbours(cube.x, cube.y, cube.z).count { space[it] }
            result -= 2 * neighbours
        }

        return result
    }


    fun part2(input: List<String>): Int {
        val (cubes, limits) = parseInput(input)
        val space = BitSet(limits.size)
        cubes.forEach { cube ->
            space[limits.index(cube.x, cube.y, cube.z)] = true
        }
        val visited = BitSet(limits.size)
        val q = ArrayDeque<Cube>()
        val start = Cube(limits.minX, limits.minY, limits.minZ)
        q.add(start)
        visited[limits.index(start.x, start.y, start.z)] = true

        var result = 0
        while (q.isNotEmpty()) {
            val cube = q.removeFirst()

            for (n in limits.neighbours(cube)) {
                val index = limits.index(n.x, n.y, n.z)
                if (space[index]) {
                    result += 1
                } else if (!visited[index]) {
                    visited[index] = true
                    q.addLast(n)
                }
            }
        }
        return result
    }


    val testInput = readInput("day18", "test")
    val input = readInput("day18", "input")
    val part1 = part1(testInput)
    println(part1)
    check(part1 == 64)
    println(part1(input))

    val part2 = part2(testInput)
    println(part2)
    check(part2 == 58)
    println(part2(input))
}


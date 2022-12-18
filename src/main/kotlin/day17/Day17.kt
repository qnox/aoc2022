package day17


import readInput

fun main() {

    val rocks = listOf(
        listOf("####"),
        listOf(
            " # ",
            "###",
            " # "
        ),
        listOf(
            "  #",
            "  #",
            "###"
        ),
        listOf(
            "#",
            "#",
            "#",
            "#"
        ),
        listOf(
            "##",
            "##"
        )
    )


    fun isValid(chamber: List<CharArray>, rock: Int, x: Int, y: Int): Boolean {
        val width = rocks[rock][0].length
        val height = rocks[rock].size
        if (y < 0 || x < 0 || x + width > 7) {
            return false
        }
        for (i in 0 until height) {
            for (j in 0 until width) {
                if (rocks[rock][i][j] != ' ' && chamber[y + height - 1 - i][x + j] != ' ') {
                    return false
                }
            }
        }
        return true
    }

    fun apply(chamber: List<CharArray>, rock: Int, x: Int, y: Int): Boolean {
        val width = rocks[rock][0].length
        val height = rocks[rock].size
        for (i in 0 until height) {
            for (j in 0 until width) {
                if (rocks[rock][i][j] != ' ') {
                    chamber[y + height - 1 - i][x + j] = rocks[rock][i][j]
                }
            }
        }
        return true
    }

    fun printChamber(chamber: MutableList<CharArray>) {
        println(
            chamber.withIndex().reversed()
                .joinToString(separator = "\n") {
                    it.value.joinToString(separator = "", prefix = "${it.index.toString().padStart(3)} #", postfix = "#")
                }
        )
        println("    #########")
    }

    fun isEqual(chamber: List<CharArray>, from1: Int, from2: Int, len: Int): Boolean {
        for (i in 0 until len) {
            if (!chamber[from1 + i].contentEquals(chamber[from2 + i])) {
                return false
            }
        }
        return true
    }

    data class Result(val top: Int, val rount: Int, val rock: Int, val jet: Int, val cycle: Int, val cycleTop: Int)

    data class RoundKey(val rock: Int, val jet: Int)
    data class RoundResult(val n: Int, val top: Int)

    fun simulate(input: List<String>, rounds: Int, initRock: Int = 0, initJet: Int = 0): Result {
        var top = -1
        var jet = initJet
        var rock = initRock
        val chamber = mutableListOf<CharArray>()
        var cycle = -1
        var cycleTop = -1
        val seen = mutableMapOf<RoundKey,  RoundResult>()
        var round = 0
        while(round < rounds) {
            val stateKey = RoundKey(rock, jet)
            if (seen.contains(stateKey)) {
                val roundResult = seen[stateKey]!!
                val len = top - roundResult.top
                if (roundResult.top >= len) {
                    if (isEqual(chamber, roundResult.top - len + 1, top - len + 1, len)) {
                        cycle = round - roundResult.n
                        cycleTop = len
                        break
                    }
                }
            }
            seen[stateKey] = RoundResult(round, top)

            var x = 2
            var y = top + 4
            val height = rocks[rock].size

            repeat(y + height + 1 - chamber.size) {
                chamber.add(CharArray(7) { ' ' })
            }
            var stop = false
            while (!stop) {
                val shift = if (input[0][jet] == '>') {
                    1
                } else {
                    -1
                }
                jet = (jet + 1) % input[0].length
                if (isValid(chamber, rock, x + shift, y)) {
                    x += shift
                }
                if (isValid(chamber, rock, x, y - 1)) {
                    y -= 1
                } else {
                    stop = true
                }
            }
            apply(chamber, rock, x, y)
            top = maxOf(top, y + height - 1)
            rock = (rock + 1) % 5
            round += 1
        }
        printChamber(chamber)

        return Result(top + 1, round, rock, jet, cycle, cycleTop)
    }

    fun solve(input: List<String>, count: Long): Long {
        val (top, round, rock, jet, cycleSize, cycleTop) = simulate(input, 10000)
        val initHeight = top - cycleTop * (round / cycleSize)
        val initRounds = round % cycleSize
        val cycles = (count - initRounds) / cycleSize
        val restRounds = (count - initRounds) % cycleSize
        val restHeight = simulate(input, restRounds.toInt(), rock, jet).top
        return initHeight + cycleTop * cycles + restHeight
    }

    fun part1(input: List<String>): Long {
        return solve(input, 2022L)
    }

    fun part2(input: List<String>): Long {
        return solve(input, 1000000000000L)
    }

    val testInput = readInput("day17", "test")
    val input = readInput("day17", "input")
    val part1 = part1(testInput)
    println(part1)
    check(part1 == 3068L)
    val part1_2 = part1(input)
    println(part1_2)
    check(part1_2 == 3191L)

    val part2 = part2(testInput)
    println(part2)
    check(part2 == 1514285714288L)
    println(part2(input))
}


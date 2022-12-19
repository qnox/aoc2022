package day19


import readInput

@JvmInline
value class Resources(val resources: IntArray) {
    operator fun plus(other: Resources): Resources {
        return Resources(IntArray(resources.size) { resources[it] + other.resources[it] })
    }

    operator fun minus(other: Resources): Resources {
        return Resources(IntArray(resources.size) { resources[it] - other.resources[it] })
    }

    operator fun times(times: Int): Resources {
        return Resources(IntArray(resources.size) { resources[it] * times })
    }

    operator fun get(index: Int): Int = resources[index]
}

@JvmInline
value class Robots(val robots: IntArray) {
    fun produce(): Resources = Resources(robots)
    operator fun get(robot: Int): Int = robots[robot]

}

fun main() {

    data class Blueprint(
        val id: Int,
        val costs: List<Resources>
    ) {

        val maxConsumption = costs.reduce { acc, resources ->
            Resources(acc.resources.mapIndexed { index, v -> maxOf(v, resources[index]) }.toIntArray())
        }

    }

    data class State(val round: Int, val resources: Resources, val robots: Robots) {
        fun build(cost: Resources, robot: Int): State =
            State(
                round + 1,
                resources - cost + robots.produce(),
                Robots(robots.robots.copyOf().also {
                    it[robot] += 1
                })
            )

        fun after(rounds: Int): State = if (rounds == 0) {
            this
        } else {
            copy(round = round + rounds, resources = resources + (robots.produce() * rounds))
        }

    }

    val pattern =
        "Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.".toRegex()

    fun parseBlueprints(input: List<String>) = input.map { line ->
        pattern.matchEntire(line)?.let { match ->
            Blueprint(
                match.groups[1]!!.value.toInt(),
                listOf(
                    Resources(intArrayOf(match.groups[2]!!.value.toInt(), 0, 0, 0)),
                    Resources(intArrayOf(match.groups[3]!!.value.toInt(), 0, 0, 0)),
                    Resources(intArrayOf(match.groups[4]!!.value.toInt(), match.groups[5]!!.value.toInt(), 0, 0)),
                    Resources(intArrayOf(match.groups[6]!!.value.toInt(), 0, match.groups[7]!!.value.toInt(), 0))
                )
            )
        } ?: error("No match: $line")
    }

    fun roundsForResources(blueprint: Blueprint, robot: Int, state: State): Int {
        var max = 0
        for (index in 0..3) {
            val cost = blueprint.costs[robot][index]
            val v = if (cost == 0) {
                0
            } else if (state.robots[index] == 0) {
                9999
            } else {
                (cost - state.resources[index] + state.robots[index] - 1) / state.robots[index]
            }
            max = maxOf(max, v)
        }
        return max
    }

    fun round(state: State, blueprint: Blueprint, minutes: Int): State {
        return if (state.round == minutes) {
            state
        } else {
            val rest = minutes - state.round
            var max = state.after(rest)
            for (robot in 0 until 4) {
                if (robot == 3 || state.robots[robot] < blueprint.maxConsumption[robot]) {
                    val roundsForResources = roundsForResources(blueprint, robot, state)
                    if (roundsForResources < rest - 1) {
                        val nextState = state.after(roundsForResources).build(blueprint.costs[robot], robot)
                        val result = round(nextState, blueprint, minutes)
                        if (result.resources[3] > max.resources[3]) {
                            max = result
                        }
                    }
                }
            }
            max
        }
    }

    fun solve(blueprint: Blueprint, minutes: Int): Int {
        val resources = Resources(intArrayOf(0, 0, 0, 0))
        val robots = Robots(intArrayOf(1, 0, 0, 0))
        val initState = State(0, resources, robots)
        val result = round(initState, blueprint, minutes)
        println("$blueprint $result")
        return result.resources[3]
    }

    fun part1(input: List<String>): Int {
        return parseBlueprints(input).sumOf { it.id * solve(it, 24) }
    }


    fun part2(input: List<String>): Long {
        return parseBlueprints(input).take(3).map { solve(it, 32) }.fold(1L) { acc, v -> acc * v }
    }


    val testInput = readInput("day19", "test")
    val input = readInput("day19", "input")
    val part1 = part1(testInput)
    println(part1)
    check(part1 == 33)
    println(part1(input))

    println(part2(input))
}


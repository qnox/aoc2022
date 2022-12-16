package day16


import readInput

fun main() {


    data class Valve(val id: String, val rate: Int, val linkedWith: Set<String>)
    data class Valves(val valves: Map<String, Valve>) {
        val dist: Map<String, Map<String, Int>> = valves.mapValues { (id, _) ->
            dist(valves, id)
        }

        fun dist(valves: Map<String, Valve>, from: String): Map<String, Int> {
            val q = ArrayDeque<String>()
            q.addLast(from)
            val visited = mutableSetOf(from)
            val result = mutableMapOf<String, Int>()
            var dist = 0
            while (q.isNotEmpty()) {
                var rest = q.size
                while (rest > 0) {
                    val c = q.removeFirst()
                    result[c] = dist
                    q.addAll(valves[c]!!.linkedWith.filter { visited.add(it) })
                    rest -= 1
                }
                dist += 1
            }
            return result
        }

        operator fun get(current: String): Valve = valves[current] ?: error("Unknown valve $current")

    }

    val pattern = "Valve (.+) has flow rate=(\\d+); tunnels? leads? to valves? (.+)".toRegex()
    fun parseValves(input: List<String>) = Valves(input.associate { line ->
        pattern.matchEntire(line)?.let { match ->
            val id = match.groups[1]!!.value
            val rate = match.groups[2]!!.value.toInt()
            val linkedWith = match.groups[3]!!.value.split(", ")
            id to Valve(id, rate, linkedWith.toSet())
        } ?: error("No match: $line")
    })

    fun solve(valves: Valves, current: List<String>, notVisited: Set<String>, rest: List<Int>): Int {
        val (currentActor, actorRest) = rest.withIndex().maxBy { it.value }
        val valveId = current[currentActor]
        val dist = valves.dist[valveId]!!
        return notVisited.maxOfOrNull {
            val newRest = actorRest - dist[it]!! - 1
            if (newRest > 0) {
                val valve = valves[it]
                val newNotVisited = notVisited - it
                (newRest * valve.rate) + solve(valves, buildList {
                    addAll(current)
                    set(currentActor, it)
                }, newNotVisited, buildList {
                    addAll(rest)
                    set(currentActor, newRest)
                })
            } else {
                0
            }
        } ?: 0
    }

    fun part1(input: List<String>): Int {
        val valves = parseValves(input)
        val notVisited = valves.valves.filter { (_, valve) -> valve.rate > 0 }.keys.toSet()
        return solve(valves, listOf("AA"), notVisited, listOf(30))
    }


    fun part2(input: List<String>): Int {
        val valves = parseValves(input)
        val notVisited = valves.valves.filter { (_, valve) -> valve.rate > 0 }.keys.toSet()
        return solve(valves, listOf("AA", "AA"), notVisited, listOf(26, 26))
    }

    val testInput = readInput("day16", "test")
    val input = readInput("day16", "input")
    val part1 = part1(testInput)
    println(part1)
    check(part1 == 1651)
    println(part1(input))

    val part2 = part2(testInput)
    println(part2)
    check(part2 == 1707)
    println(part2(input))
}

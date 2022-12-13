package day11


import readInput

class Monkey(
    items: List<Long>,
    val operation: (Long) -> Long,
    val test: (Long) -> Boolean,
    val ifTrue: Int,
    val ifFalse: Int
) {
    fun inspectFirst(): Boolean {
        _count += 1
        val old = _items[0]
        val worry = operation(old)
        _items[0] = worry
        return test(worry)
    }

    fun addItem(item: Long) {
        _items.add(item)
    }

    fun removeFirst(): Long = _items.removeFirst()
    fun hasItems(): Boolean = _items.isNotEmpty()

    private val _items = items.toMutableList()
    private var _count = 0

    val count: Int
        get() = _count

    val items: List<Long>
        get() = _items


}

fun main() {

    fun match(input: String, regex: Regex): MatchNamedGroupCollection {
        val trimmed = input.trim()
        val match = regex.matchEntire(trimmed) ?: error("Failed to match `$trimmed` `$regex`")
        return match.groups as MatchNamedGroupCollection
    }

    fun parseMonkey(input: Iterator<String>, worryPostOp: (Long) -> Long): Monkey {
        val id = match(input.next(), "Monkey (\\d+):".toRegex())[1]!!.value
        val items = match(input.next(), "Starting items: (.+)".toRegex())[1]!!.value
            .split(", ")
            .map { it.toLong() }
        val (op, value) = match(input.next(), "Operation: new = old (.) (.+)".toRegex()).let {
            it[1]!!.value to it[2]!!.value
        }
        val divisibleBy = match(input.next(), "Test: divisible by (\\d+)".toRegex())[1]!!.value.toInt()
        val ifTrue = match(input.next(), "If true: throw to monkey (\\d+)".toRegex())[1]!!.value.toInt()
        val ifFalse = match(input.next(), "If false: throw to monkey (\\d+)".toRegex())[1]!!.value.toInt()
        if (input.hasNext()) {
            check(input.next() == "")
        }
        val operation: (Long) -> Long = {
            val v = if (value == "old") {
                it
            } else {
                value.toLong()
            }
            when (op) {
                "*" -> it * v
                "+" -> it + v
                else -> error("Unknown op $op")
            }
        }
        return Monkey(items, { worryPostOp(operation(it)) }, { it % divisibleBy == 0L }, ifTrue, ifFalse)
    }


    fun parseMonkeys(input: List<String>, worryPostOp: (Long) -> Long): List<Monkey> {
        val monkeys = mutableListOf<Monkey>()
        val iterator = input.iterator()
        while (iterator.hasNext()) {
            val monkey = parseMonkey(iterator, worryPostOp)
            monkeys.add(monkey)
        }
        return monkeys
    }

    fun round(monkeys: List<Monkey>) {
        for (monkey in monkeys) {
            while (monkey.hasItems()) {
                if (monkey.inspectFirst()) {
                    monkeys[monkey.ifTrue].addItem(monkey.removeFirst())
                } else {
                    monkeys[monkey.ifFalse].addItem(monkey.removeFirst())
                }
            }
        }
    }

    fun part1(input: List<String>): Long {
        val monkeys = parseMonkeys(input) { it / 3 }
        repeat(20) {
            round(monkeys)
            println("Round $it")
            for (i in monkeys.indices) {
                println("Monkey $i: ${monkeys[i].items.joinToString()}")
            }
        }
        return monkeys.sortedByDescending { it.count }.let { (m1, m2) -> m1.count.toLong() * m2.count }
    }

    fun part2(input: List<String>): Long {
        val monkeys = parseMonkeys(input) { it }
        repeat(10_000) {
            round(monkeys)
            println("Round $it")
            for (i in monkeys.indices) {
                println("Monkey $i: ${monkeys[i].items.joinToString()}")
            }
        }
        return monkeys.sortedByDescending { it.count }.let { (m1, m2) -> m1.count.toLong() * m2.count }
    }

    val testInput = readInput("day11", "test")
    val input = readInput("day11", "input")
    check(part1(testInput) == 10605L)
    println(part1(input))

    check(part2(testInput) == 2713310158L)
    println(part2(testInput))
    println()
    println(part2(input))
}

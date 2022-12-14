package day13


import readInput

fun main() {

    fun parseList(str: String, p: Int): Pair<List<Any>, Int> {
        val result = mutableListOf<Any>()
        var cur = p
        check(str[cur] == '[')
        cur += 1
        while (str[cur] != ']') {
            if (str[cur] == ',') {
                cur += 1
            }
            if (str[cur] == '[') {
                val (list, ret) = parseList(str, cur)
                cur = ret
                result.add(list)
            } else {
                val from = cur
                while (str[cur].isDigit()) {
                    cur += 1
                }
                result.add(str.substring(from, cur).toInt())
            }
        }
        cur += 1
        return result to cur
    }

    fun parseList(str: String): List<Any> {
        return parseList(str, 0).first
    }

    fun inorder(v1: Any, v2: Any): Int {
        if (v1 is Int && v2 is Int) {
            print("($v1 $v2)")
            return v1 - v2
        } else {
            val l1 = if (v1 is List<*>) v1 else listOf(v1)
            val l2 = if (v2 is List<*>) v2 else listOf(v2)
            for (i in 0 until minOf(l1.size, l2.size)) {
                val r = inorder(l1[i]!!, l2[i]!!)
                if (r != 0) {
                    return r
                }
            }
            return l1.size - l2.size
        }
    }

    fun inorder(str1: String, str2: String): Boolean {
        val v1 = parseList(str1)
        val v2 = parseList(str2)
        return inorder(v1, v2) < 1
    }

    fun part1(input: List<String>): Int {
        return input.chunked(3)
            .mapIndexed { i, (v1, v2, _) ->
                i + 1 to inorder(v1, v2)
            }
            .filter { it.second }
            .sumOf { it.first }
    }

    fun part2(input: List<String>): Int {
        val sorted = (input.filter { it.isNotEmpty() } + listOf("[[2]]", "[[6]]"))
            .sortedWith { str1, str2 ->
                val v1 = parseList(str1)
                val v2 = parseList(str2)
                inorder(v1, v2)
            }
        val i1 = sorted.indexOf("[[2]]")
        val i2 = sorted.indexOf("[[6]]")
        return (i1 + 1) * (i2 + 1)
    }

    val testInput = readInput("day13", "test")
    val input = readInput("day13", "input")
    check(part1(testInput) == 13)
    println(part1(input))

    check(part2(testInput) == 140)
    println(part2(input))
}

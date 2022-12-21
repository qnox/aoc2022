package day21


import readInput

sealed interface Node {
    val id: String
}

class Expr(override val id: String, val v1: Node, val v2: Node, val op: String) : Node {
    override fun toString(): String = "($v1 $op $v2)"
}

class Num(override val id: String, val v: Long) : Node {
    override fun toString(): String = if (id == "humn") {"$$v$"} else  { v.toString() }
}

val expr = "(.+): (.+) (.) (.+)".toRegex()
val num = "(.+): (\\d+)".toRegex()

fun parse(input: List<String>): Expr {
    val map: Map<String, ((String) -> Node) -> Node> = input.associate { line ->
        expr.matchEntire(line)?.let {
            it.groups[1]!!.value to { n: (String) -> Node ->
                Expr(
                    it.groups[1]!!.value,
                    n(it.groups[2]!!.value),
                    n(it.groups[4]!!.value),
                    it.groups[3]!!.value
                )
            }
        }
            ?: num.matchEntire(line)?.let {
                it.groups[1]!!.value to { n: (String) -> Node ->
                    Num(it.groups[1]!!.value, it.groups[2]!!.value.toLong())
                }
            }
            ?: error("Failed to parse: $line")
    }
    val resolver = object : Function1<String, Node>  {
        override fun invoke(name: String): Node = map[name]!!(this)
    }
    return map["root"]!!(resolver) as Expr
}

fun calculate(node: Node): Long {
    return when (node) {
        is Num -> node.v
        is Expr -> when (node.op) {
            "+" -> calculate(node.v1) + calculate(node.v2)
            "-" -> calculate(node.v1) - calculate(node.v2)
            "*" -> calculate(node.v1) * calculate(node.v2)
            "/" -> calculate(node.v1) / calculate(node.v2)
            else -> error("Unknown op: $node.op")
        }
    }
}

fun containsHumn(node: Node): Boolean {
    return node.id == "humn" || when (node) {
        is Num -> false
        is Expr -> containsHumn(node.v1) || containsHumn(node.v2)
    }
}

fun transform(node: Node, v: Node, id: String= "humn"): Node {
    return if (node.id == "humn") {
        v
    } else {
        if (node is Expr) {
            val isV1 = containsHumn(node.v1)
            val expr = when(node.op) {
                "+" -> if (isV1) {
                    Expr(node.id, v, node.v2, "-")
                } else {
                    Expr(node.id, v, node.v1, "-")
                }
                "-" -> if (isV1) {
                    Expr(node.id, v, node.v2, "+")
                } else {
                    Expr(node.id, node.v1, v, "-")
                }
                "*" -> if (isV1) {
                    Expr(node.id, v, node.v2, "/")
                } else {
                    Expr(node.id, v, node.v1, "/")
                }
                "/" -> if (isV1) {
                    Expr(node.id, v, node.v2, "*")
                } else {
                    Expr(node.id, node.v1, v, "/")
                }
                else -> error("Unknown op: $node.op")
            }
            return if (isV1) {
                transform(node.v1, expr, id)
            } else {
                transform(node.v2, expr, id)
            }
        }
        error("Only expression nodes are expected")
    }
}

fun main() {


    fun part1(input: List<String>): Long {
        val root = parse(input)
        return calculate(root)
    }

    fun part2(input: List<String>): Long {
        val root = parse(input)
        val (v, expr) = if (containsHumn(root.v1)) {
            calculate(root.v2) to root.v1
        } else {
            calculate(root.v1) to root.v2
        }
        val node = transform(expr, Num("root", v))
        return calculate(node)
    }


    val testInput = readInput("day21", "test")
    val input = readInput("day21", "input")
    val part1 = part1(testInput)
    println(part1)
    check(part1 == 152L)
    println(part1(input))

    val part2 = part2(testInput)
    println(part2)
    check(part2 == 301L)
    println(part2(input))
}


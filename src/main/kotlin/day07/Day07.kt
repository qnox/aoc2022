package day07

import readInput

sealed interface FSNode {
    val name: String
}

class Dir(override val name: String, val parent: Dir? = null) : FSNode {

    private val _children: MutableMap<String, FSNode> = mutableMapOf()
    val children: Map<String, FSNode>
        get() = _children

    operator fun get(name: String) = children[name] ?: error("Unknown name $name")
    fun add(fsNode: FSNode) {
        if (_children.contains(fsNode.name)) {
            error("Name ${fsNode.name} already exists!")
        }
        _children[fsNode.name] = fsNode
    }

}

class File(override val name: String, val size: Long) : FSNode {

    override fun toString(): String {
        return "$name($size)"
    }

}


fun main() {

    fun buildTree(input: List<String>): Dir {
        val root = Dir("/")
        var current = root

        input.forEach { line ->
            if (line.startsWith("$ ls")) {
                //skip
            } else if (line.startsWith("$ cd ")) {
                val name = line.substring(5)
                current = if (name == "/") {
                    root
                } else if (name == "..") {
                    current.parent ?: error("No parent")
                } else {
                    current[name] as Dir
                }
            } else if (line.startsWith("dir ")) {
                val name = line.substring(4)
                current.add(Dir(name, current))
            } else {
                val (size, name) = line.split("\\s+".toRegex())
                current.add(File(name, size.toLong()))
            }
        }
        return root
    }

    fun part1(input: List<String>): Long {
        val root = buildTree(input)

        fun postorder(dir: Dir): Pair<Long, Long> {
            var total = 0L
            var result = 0L
            dir.children.values.forEach {
                when (it) {
                    is Dir -> {
                        val (size, sub) = postorder(it)
                        total += size
                        result += sub
                    }

                    is File -> {
                        total += it.size
                    }
                }
            }
            if (total <= 100_000) {
                result += total
            }
            return total to result
        }
        return postorder(root).second
    }

    fun part2(input: List<String>): Long {
        val root = buildTree(input)
        fun total(dir: Dir): Long {
            return dir.children.values.sumOf {
                when (it) {
                    is Dir -> {
                        total(it)
                    }

                    is File -> {
                        it.size
                    }
                }
            }
        }

        val total = total(root)
        val need = 30000000 - (70000000 - total)

        fun postorder(dir: Dir, need: Long): Pair<Long, Long> {
            var total = 0L
            var result = Long.MAX_VALUE
            dir.children.values.forEach {
                when (it) {
                    is Dir -> {
                        val (size, sub) = postorder(it, need)
                        total += size
                        result = minOf(result, sub)
                    }

                    is File -> {
                        total += it.size
                    }
                }
            }
            if (total >= need) {
                result = minOf(result, total)
            }
            return total to result
        }
        return postorder(root, need).second
    }

    val testInput = readInput("day07", "test")
    val input = readInput("day07", "input")
    check(part1(testInput) == 95437L)
    println(part1(input))

    check(part2(testInput) == 24933642L)
    println(part2(input))
}

package year2022.day7

import util.InputLoader

fun main() {
    val input = InputLoader.load("/year2022/day7/input.txt").split("\n")
    val filesystem = buildFilesystem(input)

    println("Part 1: ${partOne(filesystem)}")
    println("Part 2: ${partTwo(filesystem)}")
}

fun partOne(filesystem: TreeNode): Int {
    return getSubTree(filesystem)
        .filter { it.isDirectory }
        .filter { it.size!! <= 100000 }
        .sumOf { it.size!! }
}

fun partTwo(filesystem: TreeNode): Int {
    val spaceToFree = filesystem.size!! - 40000000
    return getSubTree(filesystem)
        .filter { it.isDirectory }
        .filter { it.size!! >= spaceToFree }
        .minOf { it.size!! }
}

fun getSubTree(node: TreeNode): List<TreeNode> {
    val children = node.children.values.flatMap { getSubTree(it) }
        .toMutableList()
    children.add(node)
    return children
}

fun buildFilesystem(input: List<String>): TreeNode {
    val root = TreeNode(true)
    var curr = root
    for (i in 1..input.lastIndex) {
        val parts = input[i].split(" ")
        if (parts[0] == "$") {
            curr = if (parts[1] == "ls") {
                continue
            } else if (parts[2] == "..") {
                curr.parent!!
            } else {
                curr.children[parts[2]]!!
            }
        } else if (parts[0] == "dir") {
            curr.children[parts[1]] = TreeNode(true, null, curr)
        } else {
            curr.children[parts[1]] = TreeNode(false, parts[0].toInt(), curr)
        }
    }

    computeSize(root)
    return root
}

fun computeSize(node: TreeNode): Int {
    if (!node.isDirectory)  {
        return node.size!!
    }

    val size = node.children.values.sumOf { computeSize(it) }
    node.size = size
    return size
}

class TreeNode(
    val isDirectory: Boolean,
    var size: Int? = null,
    var parent: TreeNode? = null,
    var children: MutableMap<String, TreeNode> = mutableMapOf(),
)



package year2024.day9

import util.InputLoader

private const val FREE_SPACE = -1

fun main() {
  val diskMap = InputLoader.load("/year2024/day9/input.txt")
    .toCharArray()
    .map { "$it".toInt() }

  println("Part 1: ${partOne(diskMap)}")
  println("Part 2: ${partTwo(diskMap)}")
}

private fun partOne(diskMap: List<Int>): Long {
  val decompressed = decompress(diskMap)

  var forwardPointer = 0
  var backwardPointer = decompressed.size - 1
  while (forwardPointer < backwardPointer) {
    if (decompressed[forwardPointer] == FREE_SPACE) {
      while (backwardPointer > forwardPointer && decompressed[backwardPointer] == FREE_SPACE) {
        backwardPointer--
      }

      decompressed[forwardPointer] = decompressed[backwardPointer]
      decompressed[backwardPointer--] = FREE_SPACE
    }

    forwardPointer++
  }

  return computeChecksum(decompressed)
}

private fun partTwo(diskMap: List<Int>): Long {
  val decompressed = decompress(diskMap)

  var backwardPointer = decompressed.size - 1
  while (backwardPointer > 0) {
    while (backwardPointer > 0 && decompressed[backwardPointer] == FREE_SPACE) {
      backwardPointer--
    }

    val id = decompressed[backwardPointer]
    var fileSize = 0
    var lookBackPointer = backwardPointer
    while (lookBackPointer > 0 && decompressed[lookBackPointer] == id) {
      fileSize++
      lookBackPointer--
    }

    var forwardPointer = 0
    while (forwardPointer <= lookBackPointer) {
      if (decompressed[forwardPointer] == FREE_SPACE) {
        var freeSpaceSize = 0
        var lookAheadPointer = forwardPointer
        while (lookAheadPointer <= lookBackPointer && decompressed[lookAheadPointer] == FREE_SPACE) {
          freeSpaceSize++
          lookAheadPointer++
        }

        if (freeSpaceSize >= fileSize) {
          // copy and update pointers
          for (j in 0 until fileSize) {
            decompressed[forwardPointer + j] = id
            decompressed[backwardPointer - j] = FREE_SPACE
          }

          break
        } else {
          forwardPointer = lookAheadPointer
        }
      } else {
        forwardPointer++
      }
    }

    backwardPointer = lookBackPointer
  }

  return computeChecksum(decompressed)
}

private fun decompress(diskMap: List<Int>): ArrayList<Int> {
  val decompressed = ArrayList<Int>()
  for (i in diskMap.indices) {
    if (i % 2 == 0) {
      val id = i / 2
      for (j in 0 until diskMap[i]) {
        decompressed.add(id)
      }
    } else {
      for (j in 0 until diskMap[i]) {
        decompressed.add(FREE_SPACE)
      }
    }
  }

  return decompressed
}

private fun computeChecksum(compacted: List<Int>): Long {
  return compacted.mapIndexed { index, c ->
    if (c == FREE_SPACE) 0 else index * "$c".toLong()
  }.sum()
}

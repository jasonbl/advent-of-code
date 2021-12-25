package year2021.day16;

import util.InputLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Solution {

  public static void main(String[] args) {
    Iterator<String> bits = getBitIterator();
    Packet packet = readPacket(bits);
    System.out.println("Part 1: " + sumVersions(packet));
    System.out.println("Part 2: " + computeValue(packet));
  }

  private static long sumVersions(Packet packet) {
    if (packet.getType() == 4) {
      return packet.getVersion();
    }

    long subPacketVersionSum = packet.getSubPackets().stream()
        .mapToLong(Solution::sumVersions)
        .sum();

    return packet.getVersion() + subPacketVersionSum;
  }

  private static long computeValue(Packet packet) {
    if (packet.getType() == 0) {
      return packet.getSubPackets().stream()
          .mapToLong(Solution::computeValue)
          .sum();
    } else if (packet.getType() == 1) {
      return packet.getSubPackets().stream()
          .mapToLong(Solution::computeValue)
          .reduce(1, (l1, l2) -> l1 * l2);
    } else if (packet.getType() == 2) {
      return packet.getSubPackets().stream()
          .mapToLong(Solution::computeValue)
          .min().getAsLong();
    } else if (packet.getType() == 3) {
      return packet.getSubPackets().stream()
          .mapToLong(Solution::computeValue)
          .max().getAsLong();
    } else if (packet.getType() == 4) {
      return packet.getValue();
    } else if (packet.getType() == 5) {
      long valueOne = computeValue(packet.getSubPackets().get(0));
      long valueTwo = computeValue(packet.getSubPackets().get(1));
      return valueOne > valueTwo ? 1 : 0;
    } else if (packet.getType() == 6) {
      long valueOne = computeValue(packet.getSubPackets().get(0));
      long valueTwo = computeValue(packet.getSubPackets().get(1));
      return valueOne < valueTwo ? 1 : 0;
    } else if (packet.getType() == 7) {
      long valueOne = computeValue(packet.getSubPackets().get(0));
      long valueTwo = computeValue(packet.getSubPackets().get(1));
      return valueOne == valueTwo ? 1 : 0;
    } else {
      throw new RuntimeException("Unexpected packet type");
    }
  }

  private static Packet readPacket(Iterator<String> bits) {
    long version = binToLong(read(bits, 3));
    long type = binToLong(read(bits, 3));
    long length = 6;
    if (type == 4) {
      StringBuilder number = new StringBuilder();
      String chunk;
      while ((chunk = read(bits, 5)).charAt(0) == '1') {
        number.append(chunk.substring(1));
        length += 5;
      }

      number.append(chunk.substring(1));
      length += 5;

      return Packet.builder()
          .version(version)
          .type(type)
          .length(length)
          .value(binToLong(number.toString()))
          .build();
    } else {
      long lengthType = binToLong(read(bits, 1));
      length++;

      List<Packet> subPackets = new ArrayList<>();
      if (lengthType == 0) {
        long remainingLength = binToLong(read(bits, 15));
        length += 15;

        while (remainingLength > 0) {
          Packet subPacket = readPacket(bits);
          remainingLength -= subPacket.getLength();
          length += subPacket.getLength();
          subPackets.add(subPacket);
        }
      } else {
        long numSubPackets = binToLong(read(bits, 11));
        length += 11;

        for (int i = 0; i < numSubPackets; i++) {
          Packet subPacket = readPacket(bits);
          length += subPacket.getLength();
          subPackets.add(subPacket);
        }
      }

      return Packet.builder()
          .version(version)
          .type(type)
          .length(length)
          .subPackets(subPackets)
          .build();
    }
  }

  private static String read(Iterator<String> bits, int numBits) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < numBits; i++) {
      if (!bits.hasNext()) {
        throw new RuntimeException("No bits remaining");
      }

      builder.append(bits.next());
    }

    return builder.toString();
  }

  private static Iterator<String> getBitIterator() {
    return Arrays.stream(InputLoader.load("/year2021/day16/input.txt").split(""))
        .map(Solution::hexToBin)
        .flatMap((bits) -> Arrays.stream(bits.split("")))
        .iterator();
  }

  private static String hexToBin(String hex) {
    int i = Integer.parseInt(hex, 16);
    return String.format("%4s", Integer.toBinaryString(i))
        .replaceAll(" ", "0");
  }

  private static long binToLong(String bin) {
    return Long.parseLong(bin, 2);
  }

}

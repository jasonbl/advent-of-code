#!/bin/bash

if [ "$#" -ne 2 ]; then
  echo "Usage: $0 <year> <day>"
  exit 1
fi

package="year$1/day$2"
solution_path="src/main/java/$package"
input_path="src/main/resources/$package"

mkdir -p "$solution_path"
mkdir -p "$input_path"

solution_template="package year$1.day$2

class Solution {

  fun main() {

  }
}"

echo "$solution_template" > "$solution_path/Solution.kt"
touch "$input_path/input.txt"

git add .

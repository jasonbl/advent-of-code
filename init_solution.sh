#!/bin/bash

if [ "$#" -ne 2 ]; then
  echo "Usage: $0 <year> <day>"
  exit 1
fi

package="year$1/day$2"

solution_path="src/main/java/$package"
solution_name="Solution.kt"
input_path="src/main/resources/$package"
input_name="input.txt"

solution_template="package year$1.day$2

import util.InputLoader

fun main() {
  val input = InputLoader.load(\"/$package/$input_name\")
}"

mkdir -p "$solution_path"
mkdir -p "$input_path"

echo "$solution_template" > "$solution_path/$solution_name"
touch "$input_path/$input_name"

git add .

#!/bin/bash

input=$1

while IFS= read -r line
do
  curl -X POST localhost:8080/repositories  -H "Content-Type: application/json" -d "$line"
done < "$input"
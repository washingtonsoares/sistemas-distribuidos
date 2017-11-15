#!/bin/bash

serverAmount=$1
initialPort=$2

for ((number=0; number < $serverAmount; number++)){
  port=$((initialPort+number))
  mvn exec:java -Dexec.mainClass=chavevalor.ChaveValorServer -Dexec.args="$port $number $serverAmount $initialPort" &
}
exit 0

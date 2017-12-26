#!/bin/bash

serverAmount=$1
initialThriftPort=$2
initialCopyCatPort=$3

for ((number1=0; number1 < $serverAmount; number1++)){
  port=$((initialThriftPort+number1))

  for ((number=0; number < $serverAmount; number++)){
    port1=$((initialCopyCatPort))
    port2=$((initialCopyCatPort+1))
    port3=$((initialCopyCatPort+2))
    mvn exec:java -Dexec.mainClass=chavevalor.MapStateMachine -Dexec.args="$number localhost $port1 localhost $port2 localhost $port3" &
  }

  mvn exec:java -Dexec.mainClass=chavevalor.ChaveValorServer -Dexec.args="$port $number1 $serverAmount $initialThriftPort $number localhost $port1 localhost $port2 localhost $port3" &
  initialThriftPort = $(( initialThriftPort +1))
  initialCopyCatPort = $(( initialCopyCatPort +3))
}

exit 0

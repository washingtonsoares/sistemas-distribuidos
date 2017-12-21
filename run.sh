#!/bin/bash

serverAmount=$1
initialThriftPort=$2
initialCopyCatPort=$3

for ((number=0; number < $serverAmount; number++)){
  port=$((initialThriftPort+number))
  mvn exec:java -Dexec.mainClass=chavevalor.ChaveValorServer -Dexec.args="$port $number $serverAmount $initialThriftPort" &
}

for ((number=0; number < $serverAmount*3; number+=3)){
  port1=$((initialCopyCatPort))
  port2=$((initialCopyCatPort+1))
  port3=$((initialCopyCatPort+2))
  mvn exec:java -Dexec.mainClass=chavevalor.MapStateMachine -Dexec.args="$number localhost $port1 localhost $port2 localhost $port3" &
}
exit 0

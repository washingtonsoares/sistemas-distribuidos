#!/bin/bash

serverAmount=$1
initialPort=6060

for ((number=1;number <= $serverAmount; number++)){
  port=$((initialPort+number))
  echo "$port"
  mvn exec:java -Dexec.mainClass=chavevalor.ChaveValorServer -Dexec.args="$port" &
}
exit 0

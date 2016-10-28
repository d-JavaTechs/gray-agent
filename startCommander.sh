#!/bin/bash

pid=`ps -ef | grep java| grep gray-agent | awk '{print $2}'`

kill -9  $pid

echo "$pid killed..."

#
host=`hostname`
ip=`ifconfig| grep  -A 1 'eth0:'| grep inet | awk '{ print $2}'`

#
nohup mvn exec:java -Dexec.mainClass="com.isuwang.operations.server.Main" -Dexec.args="$host $ip $2" & 


#!/bin/bash
#nohup mvn exec:java -Dexec.mainClass="com.isuwang.operations."$1".Main" -Dexec.args="$host $ip $2" &

mkdir -p /data/isuwang/logs/gray-agent

git pull


pid=`ps -ef | grep java| grep gray-agent | awk '{print $2}'`

kill -9  $pid

host=`hostname`
ip=`ifconfig| grep  -A 1 'eth0:'| grep inet | awk '{ print $2}'`

echo "unset httpProxy:$http_proxy"
unset http_proxy
echo "proxy unsetd:$http_proxy"

commanderHost=10.169.142.116
#需传Commander参数
nohup java -jar gray-agent.jar  $commanderHost $host $ip & 

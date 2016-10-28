#!/bin/bash
LOG_FILE=/data/isuwang/logs/gray-agent/agent.log
cd  /kscompose/scompose 

branch() {
    echo "`date "+%Y-%m-%d %H:%M:%S"` branch $@">>$LOG_FILE
    echo "`date "+%Y-%m-%d %H:%M:%S"` git pull ">>$LOG_FILE
    git  pull > /dev/null

    echo "`date "+%Y-%m-%d %H:%M:%S"` git branch -a">>$LOG_FILE
    git  branch -a
}

build(){
  if [ $@ = "master" ]; then
     source /home/publish-product/.bash_profile
     cd /kscompose/scompose-product
  else
     source /home/publish-gray/.bash_profile
  fi

  echo "build $@">>$LOG_FILE
      echo "`date "+%Y-%m-%d %H:%M:%S"` git checkout $@">>$LOG_FILE
            git pull
            git checkout $@
            git pull
      echo "`date "+%Y-%m-%d %H:%M:%S"` ./scompose s-pull">>$LOG_FILE
           if [ $@ = "master" ]; then
             ./scompose s-pull-by-gid
           else
             ./scompose s-pull
           fi 
      echo "`date "+%Y-%m-%d %H:%M:%S"` ./scompose s-rebuild -X -Pproduction">>$LOG_FILE
            ./scompose s-rebuild -Pproduction
      echo "`date "+%Y-%m-%d %H:%M:%S"` ./scompose s-docker-push">>$LOG_FILE
           ./scompose s-docker-push
}

deploy(){
   echo "deploy $@">>$LOG_FILE
     # set http_proxy
     source /home/isuwang/.bash_profile
     while [ $# \> 0 ]
      do
          echo "`date "+%Y-%m-%d %H:%M:%S"` git pull">>$LOG_FILE
             git  pull
          echo "`date "+%Y-%m-%d %H:%M:%S"` git checkout $@">>$LOG_FILE
             git  checkout $@
             git pull
          echo "`date "+%Y-%m-%d %H:%M:%S"` ./scompose up -d">>$LOG_FILE
             ./scompose up -d
       shift
    done
}

rollback(){
  echo "`date "+%Y-%m-%d %H:%M:%S"` rollback $@">>$LOG_FILE
   # set http_proxy
   source /home/isuwang/.bash_profile
   while [ $# \> 0 ]
      do
            echo "`date "+%Y-%m-%d %H:%M:%S"` git pull">>$LOG_FILE
            git  pull
            echo "`date "+%Y-%m-%d %H:%M:%S"` ./scompose  s-rollback $@">>$LOG_FILE
            ./scompose  s-rollback $@
      shift
    done
}

images(){
    echo "`date "+%Y-%m-%d %H:%M:%S"` docker images">>$LOG_FILE
    /usr/bin/docker images
}

services(){
   echo "`date "+%Y-%m-%d %H:%M:%S"` ./scompose  ps">>$LOG_FILE
   ./scompose  ps
}

serviceRestart()
{
   echo "`date "+%Y-%m-%d %H:%M:%S"` docker restart servicesList;">>$LOG_FILE
    while [ $# \> 0 ]
      do
       /usr/bin/docker restart $@
      shift
    done
}

serviceStop(){
  echo "`date "+%Y-%m-%d %H:%M:%S"` docker stop servicesList;">>$LOG_FILE
   while [ $# \> 0 ]
      do
       /usr/bin/docker stop $@;
      shift
    done
}

case $1 in
  "branch" | "build" | "deploy" | "rollback" | "images" | "services" | "serviceRestart" | "serviceStop") eval $@ ;;
   *) echo "invalid command $1" ;;
esac


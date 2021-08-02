#!/bin/sh
JAVA="java -jar -server -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -javaagent:/software/lib/hotfix.jar -XX:SurvivorRatio=8 -XX:MaxGCPauseMillis=100 -XX:ParallelGCThreads=8 -XX:G1NewSizePercent=60 -XX:G1MaxNewSizePercent=90 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/software/bx/logs/battle_dump -Xms7168m -Xmx7168m -Xss512k -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m -Dspring.profiles.active=prod-profile -Dfile.encoding=UTF-8"
#jar包名称  别名
PROGRAM="hinx-1.0.jar hinx"
if [ $1 == "start" ]
  then   
   echo $PROGRAM  "starting !"
  nohup $JAVA $PROGRAM >/dev/null 2>&1 &
  echo "For details, please check the project log!"
elif [ $1 == "stop" ]
  then
  echo $PROGRAM  "stopping !"
  #查看是否存在这个进程，返回结果是数字，
  pcount=`ps -ef | grep "$PROGRAM" | grep -v "grep" | wc -l`
  if [ $pcount -gt 0 ]; then  #返回的数字不小于0 说明存在进程
        #获取进程ID 并 杀掉进程
    pid=`ps -ef | grep "$PROGRAM" | grep -v "grep" | awk '{ print $2; }'`
    kill $pid
  else
    echo "$PROGRAM" not running;
  fi
elif [ $1 == "restart" ]
  then
  echo $PROGRAM  "restart !"
  pcount=`ps -ef | grep "$PROGRAM" | grep -v "grep" | wc -l`
  if [ $pcount -gt 0 ]; then  #返回的数字不小于0 说明存在进程
        #获取进程ID 并 杀掉进程
    pid=`ps -ef | grep "$PROGRAM" | grep -v "grep" | awk '{ print $2; }'`
    kill $pid
    sleep 1
  fi
   nohup $JAVA $PROGRAM >/dev/null 2>&1 & 
  echo "For details, please check the project log!"
else
  echo "Please make sure the positon variable is [start] [stop]."
fi
exit 0

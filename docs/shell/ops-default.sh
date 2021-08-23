#!/bin/sh
tradePortalPID=0

COMMON_CONFIG="\"-Dconfig=config/server.properties\" \"-Dvertx.zookeeper.config=./config/cluster/zookeeper.json\""
ENCODE_CONFIG="\"-Dfile.encoding=UTF8\""
GC_CONFIG="-XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:SurvivorRatio=8 -XX:MaxGCPauseMillis=100 -XX:ParallelGCThreads=8 -XX:G1NewSizePercent=60 -XX:G1MaxNewSizePercent=90 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/software/bx/logs/battle_dump"
MEMORY_CONFIG=" -Xms256m -Xmx256m -Xss512k -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m"
JAVA="java -cp \"hinx-1.0-SNAPSHOT.jar;libs/*\" -server "
# -javaagent:/software/lib/hotfix.jar
APP_MAIN="com.ohayoo.whitebird.Start"

getTradeProtalPID(){
    javaps=`$JAVA_HOME/bin/jps -l | grep $APP_MAIN`
    if [ -n "$javaps" ]; then
        tradePortalPID=`echo $javaps | awk '{print $1}'`
    else
        tradePortalPID=0
    fi
}

start(){
    getTradeProtalPID
    echo "==============================================================================================="
    if [ $tradePortalPID -ne 0 ]; then
        echo "$APP_MAIN already started(PID=$tradePortalPID)"
        echo "==============================================================================================="
    else
        echo -n "Starting $APP_MAIN"
        nohup $JAVA $COMMON_CONFIG $ENCODE_CONFIG $GC_CONFIG $MEMORY_CONFIG $APP_MAIN >/dev/null 2>&1 &
        getTradeProtalPID
        if [ $tradePortalPID -ne 0 ]; then
            echo "(PID=$tradePortalPID)...[Success]"
            echo "==============================================================================================="
        else
            echo "[Failed]"
            echo "==============================================================================================="
        fi
    fi
}

status(){
    getTradeProtalPID
    echo "==============================================================================================="
    if [ $tradePortalPID -ne 0 ]; then
        echo "$APP_MAIN PID=$tradePortalPID started"
        echo "==============================================================================================="
    else
        echo "$APP_MAIN don't start "
    fi
}

stop(){
    getTradeProtalPID
    echo "==============================================================================================="
    if [ $tradePortalPID -ne 0 ]; then
        echo "$APP_MAIN PID=$tradePortalPID"
        kill -9 $tradePortalPID
        echo "==============================================================================================="
    else
        echo "$APP_MAIN don't start "
    fi
}

echo "./xxx.sh start | status | stop"
$1
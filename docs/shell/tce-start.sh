#!/bin/sh
tradePortalPID=0

COMMON_CONFIG="\"-Dconfig=config/server.properties\" \"-Dvertx.zookeeper.config=./config/cluster/zookeeper.json\""
ENCODE_CONFIG="\"-Dfile.encoding=UTF8\""
GC_CONFIG="-XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:SurvivorRatio=8 -XX:MaxGCPauseMillis=100 -XX:ParallelGCThreads=8 -XX:G1NewSizePercent=60 -XX:G1MaxNewSizePercent=90 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/software/bx/logs/battle_dump"
MEMORY_CONFIG=" -Xms256m -Xmx256m -Xss512k -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m"
JAVA="java -cp \"hinx-1.0-SNAPSHOT.jar;libs/*\" -server "
# -javaagent:/software/lib/hotfix.jar
APP_MAIN="com.ohayoo.whitebird.Start"

nohup $JAVA $COMMON_CONFIG $ENCODE_CONFIG $GC_CONFIG $MEMORY_CONFIG $APP_MAIN >/dev/null 2>&1 &
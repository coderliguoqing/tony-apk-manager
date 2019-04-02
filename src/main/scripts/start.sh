#!/bin/sh
export JAVA_HOME=$JAVA_HOME
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
PIDFILE=service.pid
ROOT_DIR="$(cd $(dirname $0) && pwd)"
CLASSPATH=./*:$ROOT_DIR/lib/*:$ROOT_DIR/conf/
JAVA_OPTS="-Xms512M -Xmx512M -Xmn400M -Xss300K -XX:+DisableExplicitGC -XX:SurvivorRatio=4 -XX:MetaspaceSize=128M
		-XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=70 -XX:+CMSParallelRemarkEnabled 
		-XX:LargePageSizeInBytes=128M -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly 
		-XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -XX:+HeapDumpOnOutOfMemoryError"
MAIN_CLASS=cn.com.bluemoon.admin.web.WebAdminApplication
if [ ! -d "logs" ]; then
   mkdir logs
fi
if [ -f "$PIDFILE" ]; then
    echo "Service is already start ..."
else
    echo "Service  start ..."
    nohup java $JAVA_OPTS -cp $CLASSPATH $MAIN_CLASS 1> logs/bm-demo-springboot.out 2>&1  &
    printf '%d' $! > $PIDFILE
    echo "Service  start SUCCESS "
fi

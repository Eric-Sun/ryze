#!/bin/sh

function help()
{
	echo "Usage: server-daemon.sh (start|stop) bar server"
}

if [ "$#" -ne 2 ]; then
	help
	exit 0
fi

SERVER_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
SERVER_HOME=`dirname $SERVER_HOME`
SERVER_LOGS=$SERVER_HOME/logs
mkdir -p $SERVER_LOGS
SERVER_LOGS_OUT_FILE=$SERVER_LOGS/server.out
SERVER_CONF_DIR=$SERVER_HOME/conf

SERVER_PID_DIR=$SERVER_HOME/run
mkdir -p $SERVER_PID_DIR
SERVER_LIB=$SERVER_HOME/lib
SERVER_TMP=$SERVER_HOME/tmp
mkdir -p $SERVER_TMP


action=$1
name=$2

SERVER_PID_FILE=$SERVER_PID_DIR/$name.pid

for jar in `ls $SERVER_LIB/*.jar`
do
      CLASSPATH="$CLASSPATH:""$jar"
done

#if [ -d "$SERVER_HOME/server-webapps" ]; then
#    CLASSPATH=${CLASSPATH}:$SERVER_HOME
#fi
CLASSPATH=$CLASSPATH:$SERVER_HOME/config/deploy:$JAVA_HOME/lib/tools.jar
SERVER_LOGS_OUT_FILE=$SERVER_LOGS/server.out

case $action in
	start)
	if [ -f $SERVER_PID_FILE ]; then
		pid=`cat $SERVER_PID_FILE`
		echo "component '$name' running as process $pid. Stop it first."
		exit -1
	fi
	JAVA_OPT="-server -Xms512m -Xmx512m -XX:NewSize=256m -XX:MaxNewSize=256m -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=70 -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -Xloggc:$ELEVATOR_LOGS/gc.log -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$ELEVATOR_LOGS/gc_dump"
    nohup java $JAVA_OPT -DSERVER_HOME=$SERVER_HOME -Djava.library.path=$JAVA_LIBRARY_PATH -classpath $CLASSPATH:$SERVER_HOME/server.jar com.j13.poppy.starter.ServerStarter $SERVER_TMP 8081 >>$SERVER_LOGS_OUT_FILE 2>&1 &
	echo $! > $SERVER_PID_FILE
	echo "See log file: $SERVER_LOGS_OUT_FILE"
	;;

	stop)
	if [ ! -f $SERVER_PID_FILE ]; then
		echo "no component named '$name' to stop."
		exit -1
	fi
	pid=`cat $SERVER_PID_FILE`
	kill $pid
	echo -n "Stop Component ($name, PID=$pid)..."
        stopped=0
        while [[ $stopped -eq 0 ]]; do
                ps -eo pid | grep "^[[:space:]]*$pid$" > /dev/null
                if [ $? -eq 0 ]; then
                        stopped=0
                else
                        stopped=1
                fi
                sleep 1
                echo -n "."
        done
        echo
	rm $SERVER_PID_FILE
	;;
esac

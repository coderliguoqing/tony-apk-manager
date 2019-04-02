#!/bin/sh
PIDFILE=service.pid
if [ -f "$PIDFILE" ]; then
     kill -9 `cat $PIDFILE`
     rm -rf $PIDFILE
     echo "Service is stop SUCCESS!"
else
    echo "Service is already stop ..."
fi
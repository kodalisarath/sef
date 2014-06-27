#!/bin/sh

# Get standard environment variables
PRGDIR=`dirname "$PRG"`

# Only set APP_HOME if not already set
APP_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`
 
CLASSPATH=${APP_HOME}/classes:`find $APP_HOME/lib | grep jar | tr '\n' ':'`

 
${JAVA_HOME}/bin/java -cp $CLASSPATH com.ericsson.sef.dump.SubscriberDump
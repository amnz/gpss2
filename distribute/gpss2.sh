#!/bin/sh

# ------------- please set enviroments -------------------------------
JAVA_HOME=""
GPSS_LIB_DIRS="./lib"
GPSS_OPT=""

# ------------- check Java Home --------------------------------------
if [ -z "$JAVA_HOME" ] ;  then
  echo "You must set JAVA_HOME to point at your Java Development Kit installation"
  exit 1
fi

# ------------- set GPSS Options -------------------------------------
GPSS_LIB_DIRS="-Djava.ext.dirs=${GPSS_LIB_DIRS}"

if [ -d ${JAVA_HOME}/jre ] ; then
  __SERVER_JVM_PATH=${JAVA_HOME}/jre/lib/i386/server
  GPSS_LIB_DIRS="${GPSS_LIB_DIRS}:${JAVA_HOME}/jre/lib/ext"
else
  __SERVER_JVM_PATH=${JAVA_HOME}/lib/i386/server
  GPSS_LIB_DIRS="${GPSS_LIB_DIRS}:${JAVA_HOME}/lib/ext"
fi

GPSS_OPT="$GPSS_OPT ${GPSS_LIB_DIRS}"

if [ -d ${__SERVER_JVM_PATH} ] ; then
  GPSS_OPT="$GPSS_OPT -server"
  echo "Use Java HotSpot Server VM"
else
  echo "Use Java HotSpot Client VM"
fi

# ------------- set classpath ----------------------------------------
GPSS_CP=$CLASSPATH

GPSS_CP=$GPSS_CP:./classes
GPSS_CP=$GPSS_CP:./bootstrap.jar

${JAVA_HOME}/bin/java ${GPSS_OPT} -cp "${GPSS_CP}" jp.wda.g2.standalone.Bootstrap $@

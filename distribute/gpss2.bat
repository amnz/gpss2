@echo off
if "%OS%" == "Windows_NT" setlocal

rem ------------- please set enviroments -------------------------------
rem ���ϐ� JAVA_HOME ��ݒ肵�Ă��Ȃ��ꍇ�́A
rem 9�s�ڂ̖`�� rem ���폜���AJDK�ւ̃p�X���L�q���Ă��������B
rem ��jset JAVA_HOME=c:\Program Files\java\jdk1.6.0_02

rem set JAVA_HOME=
set GPSS_LIB_DIRS=.\lib
set GPSS_OPT=

rem ------------- check Java Home --------------------------------------
if not "%JAVA_HOME%" == "" goto checkJdk
goto noJdk

:checkJdk
if exist "%JAVA_HOME%\bin\java.exe" goto okHome

:noJdk
echo You must set JAVA_HOME to point at your Java Development Kit installation
goto end

:okHome

rem ------------- set GPSS Options -------------------------------------
set GPSS_LIB_DIRS=-Djava.ext.dirs=%GPSS_LIB_DIRS%
if exist "%JAVA_HOME%\jre" goto setjdk

:setjre
set __SERVER_JVM_PATH=%JAVA_HOME%\bin\server
set GPSS_LIB_DIRS=%GPSS_LIB_DIRS%;%JAVA_HOME%\lib\ext
goto okJdk

:setjdk
set __SERVER_JVM_PATH=%JAVA_HOME%\jre\bin\server
set GPSS_LIB_DIRS=%GPSS_LIB_DIRS%;%JAVA_HOME%\jre\lib\ext

:okJdk

rem ------------- select HotSpotVM -------------------------------------
set GPSS_OPT=%GPSS_OPT% %GPSS_LIB_DIRS%
if exist "%__SERVER_JVM_PATH%" goto useServer

:useClient
echo Use Java HotSpot Client VM
goto okHotspot

:useServer
set GPSS_OPT=%GPSS_OPT% -server
echo Use Java HotSpot Server VM

:okHotspot

rem ------------- set classpath ----------------------------------------
set GPSS_CP=%CLASSPATH%

set GPSS_CP=%GPSS_CP%;./classes
set GPSS_CP=%GPSS_CP%;./bootstrap.jar

%JAVA_HOME%\bin\java %GPSS_OPT% -cp "%GPSS_CP%" jp.wda.g2.standalone.Bootstrap %1 %2 %3 %4 %5

:end

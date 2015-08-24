echo off

REM ******************************************
REM command sctructure: mysql -u <user> -p <database> < script.sql > output.tab
REM ******************************************

if "%1" == "" Goto Exit

SET RUN_FILE=%1
echo running %RUN_FILE% against MySQL

mysql.exe -u comocspo_admin -p comocspo_pool < %RUN_FILE%

:Exit

#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $APSPATH)
source ${ABSDIR}/profile.sh

IDLE_PORT=$(find_idle_port)

echo "> $IDLE_PORT 에서 구동 중인 pid 확인"
IDLE_PORT=$(losf -ti tcp:${IDLE_PORT})

if [ -z ${IDLE_PORT} ]
then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $IDLE_PORT"
  kill -15 $IDLE_PORT
  sleep 5
fi
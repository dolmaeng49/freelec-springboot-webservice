#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ubuntu/app/step3
PROJECT_NAME=freelec-springboot-webservice

echo "> Build 파일 복사"
cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)

echo "> $JAR_NAME 에 실행권한 추가 및 실행"
cd $REPOSITORY
chmod +x $JAR_NAME

IDLE_PROFILE=$(find_idle_profile)

echo "> $JAR_NAME 을 $IDLE_PROFILE 로 실행"
nohup java -jar -Dspring.config.location=/home/ubuntu/app/application-oauth.properties,/home/ubuntu/app/application-real-db.properties,classpath:/application-$IDLE_PROFILE.properties -Dspring.profiles.active=$IDLE_PROFILE $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &

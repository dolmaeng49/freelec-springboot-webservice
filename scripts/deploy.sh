#!/bin/bash

REPOSITORY=/home/ubuntu/app/step2
PROJECT_NAME=freelec-springboot-webservice

echo "> Build 파일 복사"
cp $REPOSITORY/zip/*.jar $REPOSITORY/

CURRENT_PID=$(pgrep -f ${PROJECT_NAME})
echo "> 현재 구동중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
        echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
        echo "> kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 5
fi

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)

echo "> $JAR_NAME 에 실행권한 추가 및 실행"
cd $REPOSITORY
chmod +x $JAR_NAME

nohup java -jar -Dspring.config.location=/home/ubuntu/app/application-oauth.properties,/home/ubuntu/app/application-real-db.properties,classpath:/application-real.properties -Dspring.profiles.active=real $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &

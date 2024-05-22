#!/bin/bash

# 스크립트 실행중 하나라도 오류 발생 시 자동 종료
set -e

# Spring 프로젝트 컨테이너 이름 가져오기
SPRING_CONTAINER_ID=$(docker ps -q -f ancestor=sahyunjin/devrace-image:latest)

# Spring 프로젝트 컨테이너가 이미 mynetwork에 연결되어 있는지 확인
CONNECTED_CONTAINERS=$(docker network inspect mynetwork -f '{{range .Containers}}{{.Name}} {{end}}')

if [[ $CONNECTED_CONTAINERS == *"$SPRING_CONTAINER_ID"* ]]; then
  echo "Already Connected - Spring 컨테이너는 이미 'mynetwork'에 연결되어 있습니다."
else
  # Spring 프로젝트 컨테이너를 네트워크에 연결
  docker network connect mynetwork $SPRING_CONTAINER_ID

  # 네트워크 연결 성공 여부 확인
  if [ $? -eq 0 ]; then
    echo "SUCCESS - Spring 컨테이너가 성공적으로 'mynetwork'에 연결되었습니다."
  else
    echo "ERROR - Spring 컨테이너를 'mynetwork'에 연결하는 데 실패했습니다."
    exit 1
  fi
fi
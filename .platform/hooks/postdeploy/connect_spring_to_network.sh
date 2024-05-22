#!/bin/bash

# 스크립트 실행중 하나라도 오류 발생 시 자동 종료
set -e

# Spring 프로젝트 컨테이너 이름 가져오기
SPRING_CONTAINER_ID=$(docker ps -q -f ancestor=sahyunjin/devrace-image:latest)

# 컨테이너 ID가 올바르게 가져와졌는지 확인
if [ -z "$SPRING_CONTAINER_ID" ]; then
  echo "ERROR - Spring 컨테이너를 찾을 수 없습니다. 이미지 이름을 확인하세요: sahyunjin/devrace-image:latest"
  exit 1
fi

# Spring 프로젝트 컨테이너가 이미 mynetwork에 연결되어 있는지 확인
docker network inspect mynetwork --format '{{json .Containers}}' | grep -q "$SPRING_CONTAINER_ID"
NETWORK_CONNECTED=$?

# 이미 연결되어 있지 않다면 연결 시도
if [ "$NETWORK_CONNECTED" -ne 0 ]; then
  docker network connect mynetwork $SPRING_CONTAINER_ID

  # 네트워크 연결 성공 여부 확인
  if [ $? -eq 0 ]; then
    echo "SUCCESS - Spring 컨테이너가 성공적으로 mynetwork에 연결되었습니다."
  else
    echo "ERROR - Spring 컨테이너를 네트워크에 연결하는 데 실패했습니다."
    exit 1
  fi
else
  echo "Spring 컨테이너가 이미 mynetwork에 연결되어 있습니다."
fi

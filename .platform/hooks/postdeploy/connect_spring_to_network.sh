#!/bin/bash

# Spring 프로젝트 컨테이너 이름 가져오기
SPRING_CONTAINER_ID=$(docker ps -q -f ancestor=sahyunjin/devrace-image:latest)

# 컨테이너 ID가 올바르게 가져와졌는지 확인
if [ -z "$SPRING_CONTAINER_ID" ]; then
  echo "ERROR - Spring 컨테이너를 찾을 수 없습니다. 이미지 이름을 확인하세요: sahyunjin/devrace-image:latest"
  exit 1
fi

# Spring 프로젝트 컨테이너를 네트워크에 연결
docker network connect mynetwork $SPRING_CONTAINER_ID

# 네트워크 연결 성공 여부 확인
if [ $? -eq 0 ]; then
  echo "SUCCESS - Spring 컨테이너가 성공적으로 mynetwork에 연결되었습니다."
else
  echo "ERROR - Spring 컨테이너를 네트워크에 연결하는 데 실패했습니다."
  exit 1
fi
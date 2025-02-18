#!/bin/bash

# 오류 발생 시 스크립트 종료
set -e

# 업데이트 및 필수 패키지 설치
sudo apt-get update
sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common

# Docker 설치
if ! command -v docker &> /dev/null; then
    echo "Docker가 설치되어 있지 않습니다. 설치를 진행합니다."
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
    sudo apt-get update
    sudo apt-get install -y docker-ce
else
    echo "Docker가 이미 설치되어 있습니다."
fi

# Docker 서비스 시작 및 활성화
if ! sudo systemctl is-active --quiet docker; then
    echo "Docker 서비스를 시작합니다."
    sudo systemctl start docker
    sudo systemctl enable docker
else
    echo "Docker 서비스가 이미 실행 중입니다."
fi

# Docker 버전 확인
sudo docker --version

# Redis Docker 이미지 다운로드 및 실행
if [ "$(sudo docker ps -q -f name=redis-server)" ]; then
    echo "Redis 서버가 이미 실행 중입니다."
else
    echo "Redis 서버를 실행합니다."
    sudo docker run --name redis-server -d -p 6379:6379 redis
fi

# Redis가 정상적으로 실행되고 있는지 확인
sudo docker ps
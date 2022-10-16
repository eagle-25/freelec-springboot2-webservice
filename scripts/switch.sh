#!/usr/bin/env bash

# 엔진엑스가 바라보는 스프링 붙트를 최신 버전으로 변경

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
    IDLE_PORT=$(find_idle_port)

    echo "> Rotating Target Port: IDLE_PORT"
    echo "> Rotate the Port"
    echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

    echo "> Reload nginx"
    sudo service nginx reload  
}
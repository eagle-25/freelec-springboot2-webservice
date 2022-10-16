#!/usr/bin/env bash

# 이 파일은 다른 스크립트 파일에서 공용으로 사용할 'profile' 입니다.
# 이 파일은 포트 체크 로직을 포함합니다.

# 쉬고있는 profile 찾기: real1과 real2의 운영은 서로 배타적임.
# real1이 사용중이라면, real2가 쉬고 있고, 반대면 real1이 쉬고있음. 

function find_idle_profile()
{
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

    if [ ${RESPONSE_CODE} -ge 400 ] # 400보다 크면(즉 40x, 50x에러를 모두 포함함.)
    then
        CURRENT_PROFILE=real2
    else
        CURRENT_PROFILE=$(curl -s http://localhost/profile)
    fi

    if [ ${CURRENT_PROFILE} == real1 ]
    then
        IDLE_PROFILE=real2
    else
        IDLE_PROFILE=real1
    fi

    echo "${IDLE_PROFILE}"
}

# 쉬고있는 profile의 port 찾기
function find_idle_port()
{
    IDLE_PROFILE=$(find_idle_profile)

    if [ ${IDLE_PROFILE} == real1 ]
    then
        echo "8081"
    else
        echo "8082"
    fi
}
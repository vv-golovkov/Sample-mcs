@echo off
docker-compose down && ^
docker rmi ^
    sample-mcs-m1service:latest ^
    sample-mcs-m2service:latest ^
    sample-mcs-m3service:latest ^
    sample-mcs-cs-api-gateway:latest ^
    sample-mcs-cs-config-server:latest
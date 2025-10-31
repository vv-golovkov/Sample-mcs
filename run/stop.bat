@echo off
docker-compose down && ^
docker rmi ^
    sample-mcs-m1service:latest ^
    sample-mcs-m2service:latest ^
    sample-mcs-m3service:latest ^
    sample-mcs-cs_api_gateway:latest
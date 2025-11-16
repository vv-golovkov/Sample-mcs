@echo off
docker-compose up --build -d m1service && docker-compose logs -f

@REM так також можливо: SPRING_PROFILES_ACTIVE=prod docker-compose up -d
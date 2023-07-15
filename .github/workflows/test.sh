#!/bin/bash

pwd

echo 'deploy is running'

sudo su

# pull the latest changes
echo 'pulling latest changes from github'
cd "${AWS_BE_APP_PATH}" || exit
git status
git switch main &&
git fetch --all &&
git pull origin main

# run the docker file
echo 'run new docker images'
docker compose -f ../docker-compose-test.yml up -d --build

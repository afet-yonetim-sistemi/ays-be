#!/bin/bash

echo 'deploy is running'

# connect to the server
echo 'connecting to server'
echo "${AWS_PRIVATE_KEY}" > private_key && chmod 600 private_key
ssh -o StrictHostKeyChecking=no -i private_key ${AWS_USERNAME}@${AWS_TEST_ENVIRONMENT_IP}

# pull the latest changes
echo 'pulling latest changes from github'
cd "${AWS_BE_APP_PATH}" || exit
git status
git switch hotfix/build-flow &&
git fetch --all &&
git pull origin main

# stop already existing images
echo 'stopping already existing images'
docker ps
docker stop "$(docker ps -aq)"
docker rm "$(docker ps -aq)"
docker rmi "$(docker images -q)"

# run the docker file
echo 'run new docker images'
docker compose -f ../docker-compose-test.yml up -d --build

#!/bin/bash

echo 'deploy is running'

# connect to the server
echo 'connecting to server'
echo "${AWS_PRIVATE_KEY}" > private_key && chmod 600 private_key
ssh -o StrictHostKeyChecking=no -i private_key ${AWS_USERNAME}@${AWS_TEST_ENVIRONMENT_IP}
sudo su

# pull the latest changes
echo 'pulling latest changes from github'
pwd
ls -al
cd ${AWS_BE_APP_PATH} || exit
git status
git switch main &&
git fetch --all &&
git pull origin main

# run the docker file
echo 'run new docker images'
docker compose -f ../docker-compose-test.yml up -d --build

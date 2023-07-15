#!/bin/bash

echo 'deploy is running'

# connect to the server
echo 'connecting to server and'
echo "${AWS_PRIVATE_KEY}" > private_key && chmod 600 private_key
ssh -o StrictHostKeyChecking=no -i private_key ${AWS_USERNAME}@${AWS_TEST_ENVIRONMENT_IP} ; sudo su ; pwd ; cd ${AWS_BE_APP_PATH} ; pwd ; git status ; git switch main ; git fetch --all ; git pull origin main || exit

# run the docker file
echo 'run new docker images'
ssh -o StrictHostKeyChecking=no -i private_key ${AWS_USERNAME}@${AWS_TEST_ENVIRONMENT_IP} ; sudo su ; cd ${AWS_BE_APP_PATH} ; docker compose -f ../docker-compose-test.yml up -d --build

#!/bin/bash

echo 'deploy is running'

# connect to the server
echo 'connecting to server'
echo "${AWS_PRIVATE_KEY}" > private_key && chmod 600 private_key
ssh -i private_key ${AWS_USERNAME}@${AWS_TEST_ENVIRONMENT_IP} "
  sudo su &&
  cd ${AWS_BE_APP_PATH} &&
  git status &&
  git switch main &&
  git fetch --all &&
  git pull origin main &&
  docker compose -f ../docker-compose-test.yml up -d --build
"
#!/bin/bash

echo 'deploy is running'

# connect to the server
echo 'connecting to server'
echo "${AWS_PRIVATE_KEY}" >private_key && chmod 600 private_key
ssh -o StrictHostKeyChecking=no -i private_key "${AWS_USERNAME}"@"${AWS_TEST_ENVIRONMENT_IP}" "
  cd ${AWS_BE_APP_PATH} ;
  sudo  git status ;
  sudo  git switch hotfix/build-flow ;
  sudo  git fetch --all ;
  sudo  git pull ;
  sudo  docker compose -f docker-compose-test.yml up -d --build
"
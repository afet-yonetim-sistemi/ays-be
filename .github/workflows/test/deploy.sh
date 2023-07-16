#!/bin/bash
# Bash script starts here

# Outputting a message indicating that the deployment is starting
echo 'deployment is starting...'

# Outputting a message indicating that the script is connecting to the server
echo 'connecting to server'

# Saving the value of AWS_PRIVATE_KEY into a file named private_key with permissions set to restrict access to the user
echo "${AWS_PRIVATE_KEY}" >private_key && chmod 600 private_key

# Establishing an SSH connection to the specified server using the private_key file and specified username and ip address
# If the connection fails, exit the script
ssh -o StrictHostKeyChecking=no -i private_key "${AWS_USERNAME}"@"${AWS_TEST_ENVIRONMENT_IP}" || exit

# Outputting a message indicating that the script is pulling the latest changes from GitHub and rebuilding the Docker image
echo 'pulling latest changes from github and rebuild docker image'

# Establishing an SSH connection to the specified server using the private_key file and specified username and ip address
# cd ${AWS_BE_APP_PATH}                                         -> Change directory to the specified from AWS_BE_APP_PATH
# sudo git status                                               -> Display the status of the Git repository
# sudo git switch ${GH_LIVE_DEPLOYMENT_BRANCH}                  -> Switch to the specified Git branch from GH_LIVE_DEPLOYMENT_BRANCH
# sudo git reset --hard                                         -> Reset the repository to the HEAD state, discarding any local changes in the working directory and staging area
# sudo git fetch --all                                          -> Fetch the latest changes from all branches of the Git repository
# sudo git pull                                                 -> Pull the latest changes from the current branch of the Git repository
# sudo docker compose -f docker-compose-live.yml up -d --build  -> Start the containers defined in the docker-compose-live.yml file using Docker Compose
#                                                                  -f docker-compose-live.yml specifies the Docker Compose file to use
#                                                                  up -d starts the containers in detached mode (running in the background)
#                                                                  --build rebuilds the Docker images if necessary
# If any command within the SSH session fails, exit the script
ssh -o StrictHostKeyChecking=no -i private_key "${AWS_USERNAME}"@"${AWS_TEST_ENVIRONMENT_IP}" "
  cd ${AWS_BE_APP_PATH};
  sudo git reset --hard;
  sudo git status;
  sudo git fetch --all;
  sudo git switch ${GH_TEST_DEPLOYMENT_BRANCH};
  sudo git pull;
  sudo docker compose -f docker-compose-test.yml up -d --build
" || exit

# Outputting a message indicating that the deployment is done
echo 'deployment is done'

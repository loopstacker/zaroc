#!/bin/bash

GIT_FOLDER='/opt/gitlab_website_sync/data/zarok-website/'
WEBSERVER_FOLDER='/var/www/html/'
LOG_FILE='/opt/gitlab_website_sync/log/log.txt'

echo "$(date) Running the webserver sync with git">>"${LOG_FILE}"

if [ "$(whoami)" != 'svcteam' ]
then
    echo "You need to run this as svcteam user">>"${LOG_FILE}"
    exit 1
fi

cd "${GIT_FOLDER}"
if [ $? -ne 0 ]
then
    echo "Cannot change into the repo directory">>"${LOG_FILE}"
    exit 2
fi

git fetch origin && git reset --hard origin/prod >/dev/null 2>>"${LOG_FILE}"
if [ $? -ne 0 ]
then
    echo "Cannot pull from repo">>"${LOG_FILE}"
    exit 3
fi
echo "$(date) Repo pulled"

rsync -v --delete -r --no-owner --no-group --exclude='game-data' --exclude='.git' --exclude='.idea' --exclude='.htaccess' "${GIT_FOLDER}" "${WEBSERVER_FOLDER}" &>/dev/null
if [ $? -ne 0 ]
then
    echo "Could not copy files from local repo into the webserver folder">>"${LOG_FILE}"
    exit 4
fi

echo "$(date) The files have been copied to the webserver">>"${LOG_FILE}"

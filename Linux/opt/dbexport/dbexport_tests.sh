#!/bin/bash

EXPORT_FILE="/opt/dbexport/data/game.json"
SCRIPT_FILE="/opt/dbexport/dbexport.sh"
WEBSERVER_MOVES_FILE="/var/www/html/game-data/moves.json"
CORRECT_CRON_ENTRY="17 * * * * $SCRIPT_FILE"

if [ -f "${EXPORT_FILE}" ] && grep -q "\[" "${EXPORT_FILE}" && grep -q "\]" "${EXPORT_FILE}"
then
    echo "✅ OK: File is in JSON format and syntax"
else
    echo "❌ NOK: JSON file does not exist"
fi

if [ -s "${EXPORT_FILE}" ] # -s for size>0
then
    echo "✅ OK: File is not empty"
else
    echo "❌ NOK: File is empty"
fi

if [ "$(stat -c '%G' "${EXPORT_FILE}" 2>/dev/null)" = "project" ]
then
    echo "✅ OK: File is accessible by team members"
else
    echo "❌ NOK: File is not accessible by team members"
fi

if [ -x "${SCRIPT_FILE}" ]
then
    echo "✅ OK: Script is executable by the current user"
else
    echo "❌ NOK: Script is not executable by current user"
fi

if [ "$(ls -l "${SCRIPT_FILE}" 2>/dev/null | cut -c7)" = "x" ]
then
    echo "✅ OK: Script is executable by the team"
else
    echo "❌ NOK: Script is not executable by the team"
fi

if [ -L "${WEBSERVER_MOVES_FILE}" ]
then
    if curl -s --fail http://localhost/game-data/moves.json &>/dev/null
    then
        echo "✅ OK: Moves log is on the online website"
    else
        echo "❌ NOK: Moves log is missing from the online website"
    fi
else
    echo "❌ NOK: Moves log symlink is not in the website folder"
fi

echo " OPTIONAL: testing the correctness of crontab"
if sudo -u svcteam crontab -l 2>/dev/null | grep -Fxq "$CORRECT_CRON_ENTRY"
then
    echo "✅ OK: svcteam crontab is correct"
else
    echo "❌ NOK: svcteam crontab is incorrect"
fi

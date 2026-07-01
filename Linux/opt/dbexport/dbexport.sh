#!/bin/bash

NDJSON_EXPORT_LOCATION="/opt/dbexport/data/game.ndjson"
JSON_EXPORT_LOCATION="/opt/dbexport/data/game.json"
LOG_LOCATION="/opt/dbexport/log/dbexport.log"
LOG_DATE_FORMAT="%-d/%-m/%Y %H:%M %Z"

echo "$(date +"${LOG_DATE_FORMAT}"): --- script started ---" >> "${LOG_LOCATION}"

psql "postgresql://USER:PASS@localhost/game" -c "COPY (SELECT row_to_json(t) FROM (SELECT * FROM v_player_moves) t) TO STDOUT;" > "${NDJSON_EXPORT_LOCATION}"
if [ $? -ne 0 ]; then
    echo "$(date +"${LOG_DATE_FORMAT}"): ERROR - psql export failed" >> "${LOG_LOCATION}"
    exit 2
fi
echo "$(date +"${LOG_DATE_FORMAT}"): moves exported to ndjson" >> "${LOG_LOCATION}"

jq -s '.' "${NDJSON_EXPORT_LOCATION}" > "${JSON_EXPORT_LOCATION}"
if [ $? -ne 0 ]; then
    echo "$(date +"${LOG_DATE_FORMAT}"): ERROR - ndjson -> json convertion failed" >> "${LOG_LOCATION}"
    exit 3
fi
echo "$(date +"${LOG_DATE_FORMAT}"): moves converted to json and updated on the webserver" >> "${LOG_LOCATION}"

echo "The script has been run successfully"

#!/bin/bash

echo "\033[96m
     ___  ____    ____  _______,
    /   \ \   \  /   / /       |
   /  ^  \ \   \/   / |   (----'
  /  /_\  \ \_    _/   \   \\\\
 /  _____  \  |  | .----)   |
/__/     \__\ |__| |_______/
:: AYS | Afet Yönetim Sistemi ::\033[0m
"

echo "\033[96mPlease provide the following values for migration 🚀\033[0m"

while true; do
  echo "\033[97m"
  read -p "➡️ File Type (ddl or dml): " MIGRATION_TYPE
  MIGRATION_TYPE=$(echo "$MIGRATION_TYPE" | tr '[:upper:]' '[:lower:]')

  if [[ "$MIGRATION_TYPE" == "ddl" || "$MIGRATION_TYPE" == "dml" ]]; then
    break
  else
    echo "\033[41m⚠️ Please enter only 'ddl' or 'dml'.\033[0m"
  fi
done

while true; do
  echo "\033[97m"
  read -p "➡️ GitHub Username: " GH_USERNAME
  if [[ -n "$GH_USERNAME" ]]; then
    break
  fi
  echo "\033[41m⚠️ Please enter your GitHub username.\033[0m"
done

echo "\033[0m"

echo "📝 \033[44mCreating migration file...\033[0m"

sleep 1

TIMESTAMP=$(date +"%Y%m%d%H%M")
FILENAME="${TIMESTAMP}-ays-${MIGRATION_TYPE}.xml"

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
TARGET_DIR="${SCRIPT_DIR}/../src/main/resources/db/changelog/changes"

FILE_PATH="${TARGET_DIR}/${FILENAME}"

cat <<EOF > "$FILE_PATH"
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
        http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-latest.xsd">

    <changeSet id="${TIMESTAMP}-ays-${MIGRATION_TYPE}" author="${GH_USERNAME}">

    </changeSet>

</databaseChangeLog>
EOF

echo ""
echo "🎉 \033[102mFile created successfully!\033[0m"
echo ""
echo "🆔 \033[96mID:\033[0m  $FILENAME"
echo "👤 \033[96mAuthor:\033[0m $GH_USERNAME"
echo ""

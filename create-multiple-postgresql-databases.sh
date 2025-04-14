#!/bin/bash
set -e  # Exit immediately on error
set -u  # Treat unset variables as errors

function create_user_and_database() {
    local database=$1
    echo "Creating user and database '$database'"
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
        CREATE USER "$database";
        CREATE DATABASE "$database";
        GRANT ALL PRIVILEGES ON DATABASE "$database" TO "$database";
EOSQL
}

# Hardcoded database list
DATABASES=("karas" "keycloak")

echo "Creating hardcoded databases: ${DATABASES[*]}"
for db in "${DATABASES[@]}"; do
    create_user_and_database "$db"
done
echo "Database creation complete"
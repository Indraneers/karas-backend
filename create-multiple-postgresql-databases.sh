#!/bin/bash
set -e
set -u

function create_user_and_database() {
    local db=$1
    echo "Ensuring user and database '$db' exist..."

    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
      DO \$\$
      BEGIN
        IF NOT EXISTS (SELECT FROM pg_database WHERE datname = '$db') THEN
          CREATE DATABASE "$db";
        END IF;

        IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = '$db') THEN
          CREATE USER "$db" WITH PASSWORD '$db';
        END IF;

        GRANT ALL PRIVILEGES ON DATABASE "$db" TO "$db";
      END
      \$\$;
EOSQL
}

DATABASES=("karas" "keycloak")
for db in "${DATABASES[@]}"; do
    create_user_and_database "$db"
done

echo "Database setup complete!"

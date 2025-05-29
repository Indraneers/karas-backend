-- Table: audit
CREATE TABLE audit
(
    id UUID PRIMARY KEY,
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    name TEXT,
    service TEXT,
    http_method TEXT,
    request_url TEXT,
    old_value TEXT,
    new_value TEXT,
    user_id VARCHAR(255) REFERENCES user_account (id)
);
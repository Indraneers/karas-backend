DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'user_account'
        AND column_name = 'password'
    ) THEN
ALTER TABLE user_account
    ADD COLUMN password TEXT;
END IF;
END$$;
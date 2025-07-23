-- Migration to convert TIMESTAMP WITHOUT TIME ZONE to TIMESTAMPTZ
-- Assuming existing data is in Phnom Penh timezone (Asia/Phnom_Penh, UTC+7)

-- Set timezone for this session to ensure consistent conversion
SET timezone = 'Asia/Phnom_Penh';

-- Vehicle table
ALTER TABLE vehicle
ALTER COLUMN created_at TYPE TIMESTAMPTZ USING created_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN created_at SET DEFAULT NOW(),
    ALTER COLUMN updated_at TYPE TIMESTAMPTZ USING updated_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN updated_at SET DEFAULT NOW();

-- Customer table
ALTER TABLE customer
ALTER COLUMN created_at TYPE TIMESTAMPTZ USING created_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN created_at SET DEFAULT NOW(),
    ALTER COLUMN updated_at TYPE TIMESTAMPTZ USING updated_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN updated_at SET DEFAULT NOW();

-- Category table
ALTER TABLE category
ALTER COLUMN created_at TYPE TIMESTAMPTZ USING created_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN created_at SET DEFAULT NOW(),
    ALTER COLUMN updated_at TYPE TIMESTAMPTZ USING updated_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN updated_at SET DEFAULT NOW();

-- Subcategory table
ALTER TABLE subcategory
ALTER COLUMN created_at TYPE TIMESTAMPTZ USING created_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN created_at SET DEFAULT NOW(),
    ALTER COLUMN updated_at TYPE TIMESTAMPTZ USING updated_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN updated_at SET DEFAULT NOW();

-- Product table
ALTER TABLE product
ALTER COLUMN created_at TYPE TIMESTAMPTZ USING created_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN created_at SET DEFAULT NOW(),
    ALTER COLUMN updated_at TYPE TIMESTAMPTZ USING updated_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN updated_at SET DEFAULT NOW();

-- Unit table
ALTER TABLE unit
ALTER COLUMN created_at TYPE TIMESTAMPTZ USING created_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN created_at SET DEFAULT NOW(),
    ALTER COLUMN updated_at TYPE TIMESTAMPTZ USING updated_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN updated_at SET DEFAULT NOW();

-- Restock table (created_at already exists, just convert type and update default)
ALTER TABLE restock
ALTER COLUMN created_at TYPE TIMESTAMPTZ USING created_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN created_at SET DEFAULT NOW(),
    ALTER COLUMN updated_at TYPE TIMESTAMPTZ USING updated_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN updated_at SET DEFAULT NOW();

-- Restock_item table
ALTER TABLE restock_item
ALTER COLUMN created_at TYPE TIMESTAMPTZ USING created_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN created_at SET DEFAULT NOW(),
    ALTER COLUMN updated_at TYPE TIMESTAMPTZ USING updated_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN updated_at SET DEFAULT NOW();

-- Sale table (created_at already exists, just convert type and update default)
ALTER TABLE sale
ALTER COLUMN created_at TYPE TIMESTAMPTZ USING created_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN created_at SET DEFAULT NOW(),
    ALTER COLUMN updated_at TYPE TIMESTAMPTZ USING updated_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN updated_at SET DEFAULT NOW(),
    ALTER COLUMN due_at TYPE TIMESTAMPTZ USING updated_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN due_at SET DEFAULT NOW();

-- Item table
ALTER TABLE item
ALTER COLUMN created_at TYPE TIMESTAMPTZ USING created_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN created_at SET DEFAULT NOW(),
    ALTER COLUMN updated_at TYPE TIMESTAMPTZ USING updated_at AT TIME ZONE 'Asia/Phnom_Penh',
    ALTER COLUMN updated_at SET DEFAULT NOW();

-- Reset timezone to UTC (recommended for applications)
SET timezone = 'UTC';
-- Replace your enum creation with a simple VARCHAR column
ALTER TABLE vehicle
    ADD COLUMN vehicle_type VARCHAR(20) NOT NULL DEFAULT 'OTHER';

-- If you already created the enum type and need to migrate:
ALTER TABLE vehicle
ALTER COLUMN vehicle_type TYPE VARCHAR(20)
    USING vehicle_type::text;

-- Then you can drop the enum type if you want
DROP TYPE vehicle_type;
-- Step 1: Create the enum type
CREATE TYPE vehicle_type AS ENUM (
  'MOTORBIKE',
  'TUK_TUK',
  'PASSENGER_CAR',
  'COMMERCIAL_VEHICLE',
  'OTHER'
);

-- Step 2: Add the column to the vehicle table
ALTER TABLE vehicle
    ADD COLUMN vehicle_type vehicle_type;

-- Optional: Set a default (if you want future inserts to default to OTHER)
ALTER TABLE vehicle
    ALTER COLUMN vehicle_type SET DEFAULT 'OTHER';

UPDATE vehicle
    SET vehicle_type = 'PASSENGER_CAR'
    WHERE vehicle_type IS NULL;

ALTER TABLE vehicle
    ALTER COLUMN vehicle_type SET NOT NULL;
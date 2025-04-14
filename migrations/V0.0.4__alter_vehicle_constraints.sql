DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'uc_vehicle_engineno'
    ) THEN
ALTER TABLE vehicle DROP CONSTRAINT uc_vehicle_engineno;
END IF;

    IF EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'uc_vehicle_makeandmodel'
    ) THEN
ALTER TABLE vehicle DROP CONSTRAINT uc_vehicle_makeandmodel;
END IF;

    IF EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'uc_vehicle_vinno'
    ) THEN
ALTER TABLE vehicle DROP CONSTRAINT uc_vehicle_vinno;
END IF;
END
$$;

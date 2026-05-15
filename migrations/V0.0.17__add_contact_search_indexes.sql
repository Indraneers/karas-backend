-- Trigram indexes for unified customer + vehicle search.
-- Plate-first lookup is the auto-shop's mental model: someone walks in,
-- you type the plate, you get the customer, the car, and their history.

CREATE INDEX IF NOT EXISTS idx_customer_name_trgm
  ON customer USING gin (name gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_customer_contact_trgm
  ON customer USING gin (contact gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_vehicle_plate_trgm
  ON vehicle USING gin (plate_number gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_vehicle_vin_trgm
  ON vehicle USING gin (vin_no gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_vehicle_engine_trgm
  ON vehicle USING gin (engine_no gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_vehicle_make_model_trgm
  ON vehicle USING gin (make_and_model gin_trgm_ops);

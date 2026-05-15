-- Enable trigram extension for fuzzy / prefix search across the catalogue.
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Trigram GIN indexes on the fields the POS search hits most often.
-- These let `name ILIKE %q%` and `similarity(name, :q)` use an index instead
-- of a full scan, and they support typo-tolerant matching ("twiste" → "Twister").

CREATE INDEX IF NOT EXISTS idx_product_name_trgm
  ON product USING gin (name gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_product_identifier_trgm
  ON product USING gin (identifier gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_unit_name_trgm
  ON unit USING gin (name gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_subcategory_name_trgm
  ON subcategory USING gin (name gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_category_name_trgm
  ON category USING gin (name gin_trgm_ops);

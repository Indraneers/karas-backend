-- Vehicle table
UPDATE vehicle SET created_at = NOW() WHERE created_at IS NULL;
UPDATE vehicle SET updated_at = NOW() WHERE updated_at IS NULL;

-- Customer table
UPDATE customer SET created_at = NOW() WHERE created_at IS NULL;
UPDATE customer SET updated_at = NOW() WHERE updated_at IS NULL;

-- Category table
UPDATE category SET created_at = NOW() WHERE created_at IS NULL;
UPDATE category SET updated_at = NOW() WHERE updated_at IS NULL;

-- Subcategory table
UPDATE subcategory SET created_at = NOW() WHERE created_at IS NULL;
UPDATE subcategory SET updated_at = NOW() WHERE updated_at IS NULL;

-- Product table
UPDATE product SET created_at = NOW() WHERE created_at IS NULL;
UPDATE product SET updated_at = NOW() WHERE updated_at IS NULL;

-- Unit table
UPDATE unit SET created_at = NOW() WHERE created_at IS NULL;
UPDATE unit SET updated_at = NOW() WHERE updated_at IS NULL;

-- Restock table
UPDATE restock SET created_at = NOW() WHERE created_at IS NULL;
UPDATE restock SET updated_at = NOW() WHERE updated_at IS NULL;

-- Restock_item table
UPDATE restock_item SET created_at = NOW() WHERE created_at IS NULL;
UPDATE restock_item SET updated_at = NOW() WHERE updated_at IS NULL;

-- Sale table
UPDATE sale SET created_at = NOW() WHERE created_at IS NULL;
UPDATE sale SET updated_at = NOW() WHERE updated_at IS NULL;
UPDATE sale SET due_at = NOW() WHERE due_at IS NULL;

-- Item table
UPDATE item SET created_at = NOW() WHERE created_at IS NULL;
UPDATE item SET updated_at = NOW() WHERE updated_at IS NULL;
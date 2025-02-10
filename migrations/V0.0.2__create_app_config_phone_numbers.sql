CREATE TABLE app_config_phone_numbers (
  app_config_id BIGINT NOT NULL,
  phone_numbers VARCHAR(50) NOT NULL,
  FOREIGN KEY (app_config_id) REFERENCES app_config(id) ON DELETE CASCADE
);

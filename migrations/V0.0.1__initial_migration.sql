-- Table: user_account
CREATE TABLE user_account
(
    id       VARCHAR(255) NOT NULL,
    email    VARCHAR(255),
    username VARCHAR(255) NOT NULL,
    role     SMALLINT,
    CONSTRAINT pk_user_account PRIMARY KEY (id),
    CONSTRAINT uc_user_account_username UNIQUE (username)
);

-- Table: auto_service
CREATE TABLE auto_service
(
    id        VARCHAR(255) NOT NULL,
    name      VARCHAR(255) NOT NULL,
    is_active BOOLEAN      NOT NULL,
    price     INTEGER,
    CONSTRAINT pk_autoservice PRIMARY KEY (id),
    CONSTRAINT uc_autoservice_name UNIQUE (name)
);

-- Table: customer
CREATE TABLE customer
(
    id      VARCHAR(255) NOT NULL,
    name    VARCHAR(255),
    note    VARCHAR(255),
    address VARCHAR(255),
    contact VARCHAR(255),
    CONSTRAINT pk_customer PRIMARY KEY (id),
    CONSTRAINT uc_customer_name UNIQUE (name)
);

-- Table: category
CREATE TABLE category
(
    id                VARCHAR(255) NOT NULL,
    name              VARCHAR(255),
    img               VARCHAR(255),
    color             VARCHAR(255),
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT uc_category_name UNIQUE (name)
);

-- Table: subcategory
CREATE TABLE subcategory
(
    id            VARCHAR(255) NOT NULL,
    name          VARCHAR(255),
    category_id   VARCHAR(255) NOT NULL,
    img           VARCHAR(255),
    color         VARCHAR(255),
    CONSTRAINT pk_subcategory PRIMARY KEY (id),
    CONSTRAINT uc_subcategory_name UNIQUE (name),
    CONSTRAINT FK_SUBCATEGORY_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id)
);

-- Table: product
CREATE TABLE product
(
    id             VARCHAR(255) NOT NULL,
    name           VARCHAR(255),
    identifier     VARCHAR(255),
    subcategory_id VARCHAR(255) NOT NULL,
    base_unit      VARCHAR(255),
    variable       BOOLEAN      NOT NULL,
    img            VARCHAR(255),
    CONSTRAINT pk_product PRIMARY KEY (id),
    CONSTRAINT FK_PRODUCT_ON_SUBCATEGORY FOREIGN KEY (subcategory_id) REFERENCES subcategory (id)
);

-- Table: unit
CREATE TABLE unit
(
    id           VARCHAR(255) NOT NULL,
    name         VARCHAR(255),
    quantity     BIGINT,
    product_id   VARCHAR(255) NOT NULL,
    price        INTEGER,
    to_base_unit BIGINT,
    CONSTRAINT pk_unit PRIMARY KEY (id),
    CONSTRAINT FK_UNIT_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id)
);

-- Table: restock
CREATE TABLE restock
(
    id         VARCHAR(255)                NOT NULL,
    user_id    VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_restock PRIMARY KEY (id),
    CONSTRAINT FK_RESTOCK_ON_USER FOREIGN KEY (user_id) REFERENCES user_account (id)
);

-- Table: restock_item
CREATE TABLE restock_item
(
    id         VARCHAR(255) NOT NULL,
    restock_id VARCHAR(255) NOT NULL,
    unit_id    VARCHAR(255) NOT NULL,
    quantity   INTEGER      NOT NULL,
    status     SMALLINT     NOT NULL,
    CONSTRAINT pk_restockitem PRIMARY KEY (id),
    CONSTRAINT FK_RESTOCKITEM_ON_RESTOCK FOREIGN KEY (restock_id) REFERENCES restock (id),
    CONSTRAINT FK_RESTOCKITEM_ON_UNIT FOREIGN KEY (unit_id) REFERENCES unit (id)
);

-- Table: app_config
CREATE TABLE app_config
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    branch_name_en VARCHAR(255)                            NOT NULL,
    branch_name_kh VARCHAR(255)                            NOT NULL,
    logo           VARCHAR(255)                            NOT NULL,
    address_en     VARCHAR(255)                            NOT NULL,
    address_kh     VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_appconfig PRIMARY KEY (id)
);

-- Table: maintenance
CREATE TABLE maintenance
(
    id         VARCHAR(255)                NOT NULL,
    sale_id    BIGINT,
    vehicle_id VARCHAR(255)                NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    mileage    INTEGER                     NOT NULL,
    note       VARCHAR(255),
    CONSTRAINT pk_maintenance PRIMARY KEY (id)
);

-- Table: maintenance_auto_service
CREATE TABLE maintenance_auto_service
(
    id              VARCHAR(255) NOT NULL,
    maintenance_id  VARCHAR(255),
    auto_service_id VARCHAR(255),
    price           INTEGER      NOT NULL,
    discount        INTEGER,
    CONSTRAINT pk_maintenanceautoservice PRIMARY KEY (id),
    CONSTRAINT FK_MAINTENANCEAUTOSERVICE_ON_AUTOSERVICE FOREIGN KEY (auto_service_id) REFERENCES auto_service (id),
    CONSTRAINT FK_MAINTENANCEAUTOSERVICE_ON_MAINTENANCE FOREIGN KEY (maintenance_id) REFERENCES maintenance (id)
);

-- Table: vehicle
CREATE TABLE vehicle
(
    id                    VARCHAR(255) NOT NULL,
    customer_id           VARCHAR(255) NOT NULL,
    vin_no                VARCHAR(255),
    engine_no             VARCHAR(255),
    mileage               INTEGER,
    note                  VARCHAR(255),
    plate_number          VARCHAR(255),
    make_and_model        VARCHAR(255),
    future_maintenance_id VARCHAR(255),
    CONSTRAINT pk_vehicle PRIMARY KEY (id),
    CONSTRAINT uc_vehicle_engineno UNIQUE (engine_no),
    CONSTRAINT uc_vehicle_makeandmodel UNIQUE (make_and_model),
    CONSTRAINT uc_vehicle_platenumber UNIQUE (plate_number),
    CONSTRAINT uc_vehicle_vinno UNIQUE (vin_no),
    CONSTRAINT FK_VEHICLE_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id),
    CONSTRAINT FK_VEHICLE_ON_FUTUREMAINTENANCE FOREIGN KEY (future_maintenance_id) REFERENCES maintenance (id)
);

-- Table: vehicle_maintenances
CREATE TABLE vehicle_maintenances
(
    vehicle_id      VARCHAR(255) NOT NULL,
    maintenances_id VARCHAR(255) NOT NULL,
    CONSTRAINT pk_vehicle_maintenances PRIMARY KEY (vehicle_id, maintenances_id),
    CONSTRAINT fk_vehmai_on_maintenance FOREIGN KEY (maintenances_id) REFERENCES maintenance (id),
    CONSTRAINT fk_vehmai_on_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle (id)
);

-- Table: vehicle_maintenance
CREATE TABLE vehicle_maintenance
(
    id           VARCHAR(255)                NOT NULL,
    next_mileage INTEGER                     NOT NULL,
    next_date    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    vehicle_id   VARCHAR(255)                NOT NULL,
    note         VARCHAR(255),
    CONSTRAINT pk_vehiclemaintenance PRIMARY KEY (id),
    CONSTRAINT FK_VEHICLEMAINTENANCE_ON_VEHICLE FOREIGN KEY (vehicle_id) REFERENCES vehicle (id)
);

-- Table: vehicle_maintenance_services
CREATE TABLE vehicle_maintenance_services
(
    vehicle_maintenance_id VARCHAR(255) NOT NULL,
    services_id            VARCHAR(255) NOT NULL,
    CONSTRAINT pk_vehiclemaintenance_services PRIMARY KEY (vehicle_maintenance_id, services_id),
    CONSTRAINT uc_vehicle_maintenance_services_services UNIQUE (services_id),
    CONSTRAINT fk_vehmaiser_on_maintenance_auto_service FOREIGN KEY (services_id) REFERENCES maintenance_auto_service (id),
    CONSTRAINT fk_vehmaiser_on_vehicle_maintenance FOREIGN KEY (vehicle_maintenance_id) REFERENCES vehicle_maintenance (id)
);

-- Table: sale
CREATE TABLE sale
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    due_at      TIMESTAMP WITHOUT TIME ZONE,
    discount    INTEGER,
    user_id     VARCHAR(255),
    customer_id VARCHAR(255),
    vehicle_id  VARCHAR(255),
    status      SMALLINT                                NOT NULL,
    CONSTRAINT pk_sale PRIMARY KEY (id),
    CONSTRAINT FK_SALE_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id),
    CONSTRAINT FK_SALE_ON_USER FOREIGN KEY (user_id) REFERENCES user_account (id),
    CONSTRAINT FK_SALE_ON_VEHICLE FOREIGN KEY (vehicle_id) REFERENCES vehicle (id)
);

-- Table: item
CREATE TABLE item
(
    id       VARCHAR(255) NOT NULL,
    price    INTEGER,
    quantity INTEGER,
    discount INTEGER,
    sale_id  BIGINT,
    unit_id  VARCHAR(255) NOT NULL,
    CONSTRAINT pk_item PRIMARY KEY (id),
    CONSTRAINT FK_ITEM_ON_SALE FOREIGN KEY (sale_id) REFERENCES sale (id),
    CONSTRAINT FK_ITEM_ON_UNIT FOREIGN KEY (unit_id) REFERENCES unit (id)
);

ALTER TABLE maintenance
    ADD CONSTRAINT FK_MAINTENANCE_ON_SALE FOREIGN KEY (sale_id) REFERENCES sale (id);

ALTER TABLE maintenance
    ADD CONSTRAINT FK_MAINTENANCE_ON_VEHICLE FOREIGN KEY (vehicle_id) REFERENCES vehicle (id);


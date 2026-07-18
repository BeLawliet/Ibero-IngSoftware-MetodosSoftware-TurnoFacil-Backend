CREATE TABLE clients (
    id UUID PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(254) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    notes VARCHAR(2000),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT uk_clients_email UNIQUE (email),
    CONSTRAINT ck_clients_email_lowercase CHECK (email = LOWER(email)),
    CONSTRAINT ck_clients_first_name_not_blank CHECK (BTRIM(first_name) <> ''),
    CONSTRAINT ck_clients_last_name_not_blank CHECK (BTRIM(last_name) <> ''),
    CONSTRAINT ck_clients_phone_not_blank CHECK (BTRIM(phone) <> '')
);

CREATE INDEX idx_clients_active ON clients (active);
CREATE INDEX idx_clients_name ON clients (last_name, first_name);

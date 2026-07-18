CREATE TABLE employees (
    id UUID PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(254) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    specialty VARCHAR(120) NOT NULL,
    color VARCHAR(7) NOT NULL,
    availability VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    notes VARCHAR(2000),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT uk_employees_email UNIQUE (email),
    CONSTRAINT ck_employees_email_lowercase CHECK (email = LOWER(email)),
    CONSTRAINT ck_employees_first_name_not_blank CHECK (BTRIM(first_name) <> ''),
    CONSTRAINT ck_employees_last_name_not_blank CHECK (BTRIM(last_name) <> ''),
    CONSTRAINT ck_employees_phone_not_blank CHECK (BTRIM(phone) <> ''),
    CONSTRAINT ck_employees_specialty_not_blank CHECK (BTRIM(specialty) <> ''),
    CONSTRAINT ck_employees_color CHECK (color ~ '^#[0-9A-F]{6}$'),
    CONSTRAINT ck_employees_availability CHECK (availability IN ('AVAILABLE', 'BUSY', 'OFF_SHIFT')),
    CONSTRAINT ck_employees_inactive_off_shift CHECK (active OR availability = 'OFF_SHIFT')
);

CREATE INDEX idx_employees_name ON employees (last_name, first_name);
CREATE INDEX idx_employees_active ON employees (active);
CREATE INDEX idx_employees_specialty ON employees (specialty);
CREATE INDEX idx_employees_availability ON employees (availability);

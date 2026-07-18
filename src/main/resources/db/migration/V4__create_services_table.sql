CREATE TABLE services (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(2000),
    category VARCHAR(30) NOT NULL,
    duration_minutes INTEGER NOT NULL,
    price NUMERIC(12, 2) NOT NULL,
    color VARCHAR(7) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT ck_services_name_not_blank CHECK (BTRIM(name) <> ''),
    CONSTRAINT ck_services_category CHECK (
        category IN ('BARBERSHOP', 'BEAUTY', 'CONSULTATION', 'ADVISORY', 'OTHER')
    ),
    CONSTRAINT ck_services_duration_positive CHECK (duration_minutes > 0),
    CONSTRAINT ck_services_price_non_negative CHECK (price >= 0),
    CONSTRAINT ck_services_color CHECK (color ~ '^#[0-9A-F]{6}$')
);

CREATE UNIQUE INDEX uk_services_name_lower ON services (LOWER(name));
CREATE INDEX idx_services_name ON services (name);
CREATE INDEX idx_services_category ON services (category);
CREATE INDEX idx_services_active ON services (active);

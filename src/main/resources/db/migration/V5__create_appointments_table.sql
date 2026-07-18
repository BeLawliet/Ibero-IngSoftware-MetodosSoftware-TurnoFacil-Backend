CREATE TABLE appointments (
    id UUID PRIMARY KEY,
    client_id UUID NOT NULL,
    employee_id UUID NOT NULL,
    service_id UUID NOT NULL,
    appointment_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    duration_minutes INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    notes VARCHAR(2000),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_appointments_client FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT fk_appointments_employee FOREIGN KEY (employee_id) REFERENCES employees (id),
    CONSTRAINT fk_appointments_service FOREIGN KEY (service_id) REFERENCES services (id),
    CONSTRAINT ck_appointments_status CHECK (
        status IN ('SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')
    ),
    CONSTRAINT ck_appointments_duration_positive CHECK (duration_minutes > 0),
    CONSTRAINT ck_appointments_time_order CHECK (end_time > start_time),
    CONSTRAINT ck_appointments_duration_matches CHECK (
        end_time = start_time + duration_minutes * INTERVAL '1 minute'
    )
);

CREATE INDEX idx_appointments_date ON appointments (appointment_date);
CREATE INDEX idx_appointments_status ON appointments (status);
CREATE INDEX idx_appointments_employee_date ON appointments (employee_id, appointment_date);
CREATE INDEX idx_appointments_client ON appointments (client_id);
CREATE INDEX idx_appointments_service ON appointments (service_id);
CREATE INDEX idx_appointments_date_start_time ON appointments (appointment_date, start_time);

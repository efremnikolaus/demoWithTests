CREATE TABLE IF NOT EXISTS public.users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    country VARCHAR(255),
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS public.addresses (
    id SERIAL PRIMARY KEY,
    address_has_active BOOLEAN DEFAULT FALSE,
    city VARCHAR(255),
    country VARCHAR(255),
    street VARCHAR(255),
    employee_id INTEGER
    CONSTRAINT fk_address_employee REFERENCES public.users
);
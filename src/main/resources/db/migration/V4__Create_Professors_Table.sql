CREATE TYPE gender AS ENUM ('Male', 'Female');

CREATE TABLE professors
(
    id                    SERIAL PRIMARY KEY,
    name                  VARCHAR(50) NOT NULL,
    surname               VARCHAR(50) NOT NULL,
    patronymic            VARCHAR(50),
    birth_date            DATE,
    gender                gender,
    contact_number        VARCHAR(15),
    email                 VARCHAR(100),
    address               VARCHAR(255),
    employment_start_date DATE,
    academic_degree       VARCHAR(50),
    academic_rank         VARCHAR(50),
    achievements          JSONB,
    other_information     TEXT,
    created_at            timestamp with time zone DEFAULT now(),
    updated_at            timestamp with time zone DEFAULT now()
);

CREATE TRIGGER professors_updated_at
    BEFORE UPDATE
    ON professors
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at();
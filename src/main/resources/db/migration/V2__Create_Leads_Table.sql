CREATE TABLE leads
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    number     VARCHAR(20)  NOT NULL,
    email      VARCHAR(255) NOT NULL,
    reason     VARCHAR(20)  NOT NULL,
    status     bool                     DEFAULT false,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now()
);

CREATE TRIGGER leeds_updated_at
    BEFORE UPDATE
    ON leads
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at();
CREATE TABLE news
(
    id         SERIAL PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    text       TEXT         NOT NULL,
    images     JSONB,
    status     bool                     DEFAULT true,
    created_at timestamp with time zone DEFAULT now(),
    updated_at timestamp with time zone DEFAULT now()
);

CREATE OR REPLACE FUNCTION update_updated_at()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER news_updated_at
    BEFORE UPDATE
    ON news
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at();
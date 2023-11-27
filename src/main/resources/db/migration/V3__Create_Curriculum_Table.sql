CREATE TABLE curriculum
(
    direction_code VARCHAR(20),
    department    VARCHAR(20),
    plan          JSONB,
    created_at    timestamp with time zone DEFAULT now(),
    updated_at    timestamp with time zone DEFAULT now(),
    PRIMARY KEY (direction_code, department)
);

CREATE TRIGGER curriculum_updated_at
    BEFORE UPDATE
    ON curriculum
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at();
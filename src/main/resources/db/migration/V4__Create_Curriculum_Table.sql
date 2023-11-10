CREATE TABLE Curriculum
(
    direction_code VARCHAR(20),
    department     VARCHAR(20),
    plan           JSONB,
    updated_at     BIGINT DEFAULT EXTRACT(EPOCH FROM CURRENT_TIMESTAMP) * 1000,
    creation_time  BIGINT DEFAULT EXTRACT(EPOCH FROM CURRENT_TIMESTAMP) * 1000,
    PRIMARY KEY (direction_code, department)
);

CREATE TRIGGER curriculum_updated_at
    BEFORE UPDATE
    ON Curriculum
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at();
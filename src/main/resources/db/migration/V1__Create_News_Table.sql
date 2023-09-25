CREATE TABLE news
(
    id            SERIAL PRIMARY KEY,
    title         VARCHAR(255) NOT NULL,
    text          TEXT         NOT NULL,
    images        JSONB,
    creation_time BIGINT DEFAULT EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)
);
CREATE TABLE leads
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    number        VARCHAR(20) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    reason        VARCHAR(20) NOT NULL,
    status        bool   DEFAULT false,
    creation_time BIGINT DEFAULT EXTRACT(EPOCH FROM CURRENT_TIMESTAMP) * 1000
);
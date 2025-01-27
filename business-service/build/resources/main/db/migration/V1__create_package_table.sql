CREATE SCHEMA IF NOT EXISTS edu;

CREATE TABLE edu.package
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(255) UNIQUE NOT NULL,
    shape            TEXT[]              NOT NULL,
    symbol           CHAR(1)             NOT NULL,
    start_position_x INT                 NOT NULL,
    start_position_y INT                 NOT NULL
);

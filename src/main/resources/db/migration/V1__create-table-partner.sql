CREATE EXTENSION postgis;

CREATE TABLE partner (
    id BIGSERIAL PRIMARY KEY,
    trading_name TEXT NOT NULL,
    owner_name TEXT NOT NULL,
    document TEXT NOT NULL UNIQUE,
    coverage_area GEOMETRY(MULTIPOLYGON, 4326) NOT NULL,
    address GEOMETRY(POINT, 4326) NOT NULL
);
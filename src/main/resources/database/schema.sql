-- Clean and create tables with FKs & indexes
DROP TABLE IF EXISTS time_record;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS project;

CREATE TABLE project (
    id BIGINT PRIMARY KEY,
    name VARCHAR(200) NOT NULL
);

CREATE TABLE employee (
    id BIGINT PRIMARY KEY,
    name VARCHAR(60) NOT NULL
);

CREATE TABLE time_record (
    id BIGINT PRIMARY KEY,
    employee_id BIGINT NOT NULL REFERENCES employee(id),
    project_id BIGINT NOT NULL REFERENCES project(id),
    time_from TIMESTAMP NOT NULL,
    time_to TIMESTAMP NOT NULL
);
-- For optimization of report query - check file: part1-explanation.md

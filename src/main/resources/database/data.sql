INSERT INTO employee (id, name) VALUES
    (101, 'tom'),
    (102, 'jerry');

INSERT INTO project (id, name) VALUES
    (1, 'Sample Project A'),
    (2, 'Sample Project B');

INSERT INTO time_record (id, employee_id, project_id, time_from, time_to) VALUES
    (1, 101, 1, '2025-08-08 08:00:00', '2025-08-08 17:00:00'),
    (2, 102, 2, '2025-08-08 09:00:00', '2025-08-08 18:30:00'),
    (3, 101, 1, '2025-08-21 08:15:00', '2025-08-21 17:10:00');
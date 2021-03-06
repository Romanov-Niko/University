INSERT INTO groups (name)
VALUES ('AA-11'),
       ('BB-22'),
       ('CC-33');

INSERT INTO students (group_id, specialty, course, admission, graduation, name, surname, date_of_birth, gender, email,
                      phone_number)
VALUES (1, 'math', 1, '2015-06-01', '2020-06-01', 'first', 'student', '2000-04-01', 'female', 'fourth@gmail.com',
        '44444'),
       (2, 'biology', 2, '2014-06-01', '2019-06-01', 'second', 'student', '2000-05-01', 'female', 'fifth@gmail.com',
        '55555'),
       (3, 'history', 3, '2013-06-01', '2018-06-01', 'third', 'student', '2000-06-01', 'female', 'sixth@gmail.com',
        '66666');

INSERT INTO teachers (name, surname, date_of_birth, gender, email, phone_number)
VALUES ('first', 'teacher', '1990-01-01', 'male', 'first@gmail.com', '11111'),
       ('second', 'teacher', '1990-02-01', 'male', 'second@gmail.com', '22222'),
       ('third', 'teacher', '1990-03-01', 'male', 'third@gmail.com', '33333');

INSERT INTO lessons_times (begin_time, end_time)
VALUES ('08:00:00', '09:00:00'),
       ('09:00:00', '10:00:00'),
       ('10:00:00', '11:00:00');

INSERT INTO audiences (room_number, capacity)
VALUES (101, 100),
       (102, 100),
       (103, 100);

INSERT INTO subjects (name, credit_hours, course, specialty)
VALUES ('Calculus', 120, 1, 'math'),
       ('Anatomy', 120, 2, 'biology'),
       ('The world history', 120, 3, 'history');

INSERT INTO lessons (subject_id, teacher_id, audience_id, lesson_time_id, date)
VALUES (1, 1, 1, 1, '2017-06-01'),
       (2, 2, 2, 2, '2017-06-02'),
       (3, 3, 3, 3, '2017-06-03');

INSERT INTO lessons_groups (lesson_id, group_id)
VALUES (1, 1),
       (2, 2),
       (3, 3);

INSERT INTO teachers_subjects (teacher_id, subject_id)
VALUES (1, 1),
       (2, 2),
       (3, 3);
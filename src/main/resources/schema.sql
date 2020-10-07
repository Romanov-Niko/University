DROP TABLE IF EXISTS lessons_groups;
DROP TABLE IF EXISTS teachers_subjects;
DROP TABLE IF EXISTS lessons;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS teachers;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS audiences;
DROP TABLE IF EXISTS lessons_times;
DROP TABLE IF EXISTS subjects;

CREATE TABLE groups
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE students
(
    id            SERIAL PRIMARY KEY,
    group_id      INTEGER,
    specialty     VARCHAR(255),
    course        INTEGER,
    admission     DATE,
    graduation    DATE,
    name          VARCHAR(255),
    surname       VARCHAR(255),
    date_of_birth DATE,
    gender        VARCHAR(255),
    email         VARCHAR(255),
    phone_number  VARCHAR(255),
    CONSTRAINT FK_students_to_groups FOREIGN KEY (group_id) REFERENCES groups (id) ON UPDATE CASCADE
);

CREATE TABLE teachers
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(255),
    surname       VARCHAR(255),
    date_of_birth DATE,
    gender        VARCHAR(255),
    email         VARCHAR(255),
    phone_number  VARCHAR(255)
);

CREATE TABLE lessons_times
(
    id         SERIAL PRIMARY KEY,
    begin_time TIME,
    end_time   TIME
);

CREATE TABLE audiences
(
    id          SERIAL PRIMARY KEY,
    room_number INT,
    capacity    INT
);

CREATE TABLE subjects
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(255),
    credit_hours INT,
    course       INT,
    specialty    VARCHAR(255)
);

CREATE TABLE lessons
(
    id             SERIAL PRIMARY KEY,
    subject_id     INT,
    teacher_id     INT,
    audience_id    INT,
    lesson_time_id INT,
    date           DATE,
    CONSTRAINT FK_lessons_to_subjects FOREIGN KEY (subject_id) REFERENCES subjects (id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_lessons_to_teachers FOREIGN KEY (teacher_id) REFERENCES teachers (id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_lessons_to_audiences FOREIGN KEY (audience_id) REFERENCES audiences (id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_lessons_to_lessons_times FOREIGN KEY (lesson_time_id) REFERENCES lessons_times (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE lessons_groups
(
    lesson_id INT REFERENCES lessons (id) ON UPDATE CASCADE ON DELETE CASCADE,
    group_id  INT REFERENCES groups (id) ON UPDATE CASCADE ON DELETE CASCADE,
    UNIQUE (lesson_id, group_id)
);

CREATE TABLE teachers_subjects
(
    teacher_id INT REFERENCES teachers (id) ON UPDATE CASCADE ON DELETE CASCADE,
    subject_id INT REFERENCES subjects (id) ON UPDATE CASCADE ON DELETE CASCADE,
    UNIQUE (teacher_id, subject_id)
);
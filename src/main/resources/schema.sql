DROP TABLE IF EXISTS lessons_groups;
DROP TABLE IF EXISTS days_lessons;
DROP TABLE IF EXISTS teachers_subjects;
DROP TABLE IF EXISTS lessons;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS teachers;
DROP TABLE IF EXISTS persons;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS audiences;
DROP TABLE IF EXISTS lessons_times;
DROP TABLE IF EXISTS days;
DROP TABLE IF EXISTS subjects;

CREATE TABLE persons
(
    person_id     SERIAL PRIMARY KEY,
    name          VARCHAR(255),
    surname       VARCHAR(255),
    date_of_birth DATE,
    gender        VARCHAR(255),
    email         VARCHAR(255),
    phone_number  VARCHAR(255)
);

CREATE TABLE groups
(
    group_id SERIAL PRIMARY KEY,
    name     VARCHAR(255)
);

CREATE TABLE students
(
    student_id SERIAL PRIMARY KEY,
    person_id  INT,
    group_id   INTEGER,
    specialty  VARCHAR(255),
    course     INTEGER,
    admission  DATE,
    graduation DATE,
    CONSTRAINT FK_students_to_persons FOREIGN KEY (person_id) REFERENCES persons (person_id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_students_to_groups FOREIGN KEY (group_id) REFERENCES groups (group_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE teachers
(
    teacher_id SERIAL PRIMARY KEY,
    person_id  INT,
    CONSTRAINT FK_teachers_to_persons FOREIGN KEY (person_id) REFERENCES persons (person_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE days
(
    day_id SERIAL PRIMARY KEY,
    day    DATE
);

CREATE TABLE lessons_times
(
    lesson_time_id SERIAL PRIMARY KEY,
    begin_time     TIME,
    end_time       TIME
);

CREATE TABLE audiences
(
    audience_id SERIAL PRIMARY KEY,
    room_number INT,
    capacity    INT
);

CREATE TABLE subjects
(
    subject_id   SERIAL PRIMARY KEY,
    name         VARCHAR(255),
    credit_hours INT,
    course       INT,
    specialty    VARCHAR(255)
);

CREATE TABLE lessons
(
    lesson_id      SERIAL PRIMARY KEY,
    subject_id     INT,
    teacher_id     INT,
    audience_id    INT,
    lesson_time_id INT,
    CONSTRAINT FK_lessons_to_subjects FOREIGN KEY (subject_id) REFERENCES subjects (subject_id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_lessons_to_teachers FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_lessons_to_audiences FOREIGN KEY (audience_id) REFERENCES audiences (audience_id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_lessons_to_lessons_times FOREIGN KEY (lesson_time_id) REFERENCES lessons_times (lesson_time_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE lessons_groups
(
    lesson_id INT REFERENCES lessons (lesson_id) ON UPDATE CASCADE ON DELETE CASCADE,
    group_id  INT REFERENCES groups (group_id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (lesson_id, group_id)
);

CREATE TABLE days_lessons
(
    day_id INT REFERENCES days (day_id) ON UPDATE CASCADE ON DELETE CASCADE,
    lesson_id  INT REFERENCES lessons (lesson_id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (day_id, lesson_id)
);

CREATE TABLE teachers_subjects
(
    teacher_id INT REFERENCES teachers (teacher_id) ON UPDATE CASCADE ON DELETE CASCADE,
    subject_id  INT REFERENCES subjects (subject_id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (teacher_id, subject_id)
);
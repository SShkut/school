DROP TABLE IF EXISTS students_courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS courses;

CREATE TABLE groups (
	id serial PRIMARY KEY,
	group_name VARCHAR(255) NOT NULL
);

CREATE TABLE students (
	id serial PRIMARY KEY,
	group_id INTEGER,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE SET NULL
);

CREATE TABLE courses (
	id serial PRIMARY KEY,
	course_name VARCHAR(255) NOT NULL,
	course_description VARCHAR(255)
);

CREATE TABLE students_courses (
	student_id INTEGER,
	course_id INTEGER,
	FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
	FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE,
	UNIQUE (student_id, course_id)
);
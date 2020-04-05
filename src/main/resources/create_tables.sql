DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS students_courses;

CREATE TABLE groups (
	group_id serial PRIMARY KEY,
	group_name VARCHAR(255) NOT NULL
);

CREATE TABLE students (
	student_id serial PRIMARY KEY,
	group_id INTEGER,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	FOREIGN KEY (group_id) REFERENCES groups (group_id)
);

CREATE TABLE courses (
	course_id serial PRIMARY KEY,
	course_name VARCHAR(255) NOT NULL,
	course_description VARCHAR(255)
);

CREATE TABLE students_courses (
	student_id INTEGER,
	course_id INTEGER,
	PRIMARY KEY (student_id, course_id)
);
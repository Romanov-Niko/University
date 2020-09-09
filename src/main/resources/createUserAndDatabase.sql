SELECT pg_terminate_backend(pid)
FROM pg_stat_activity
WHERE pid <> pg_backend_pid()
  AND datname = 'database_university';
DROP DATABASE IF EXISTS database_university;
CREATE DATABASE database_university;
DROP USER IF EXISTS rector;
CREATE USER rector WITH ENCRYPTED PASSWORD 'university';
GRANT ALL PRIVILEGES ON DATABASE database_university TO rector;
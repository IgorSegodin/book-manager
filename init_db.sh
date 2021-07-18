export PGPASSWORD=postgres

psql -h localhost -p 6432 -U postgres << END_OF_SCRIPT

CREATE DATABASE "book-manager" WITH OWNER = postgres ENCODING = 'UTF8' TABLESPACE = pg_default LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8' CONNECTION LIMIT = -1;

END_OF_SCRIPT
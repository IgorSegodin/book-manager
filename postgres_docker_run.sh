#!/usr/bin/env bash

docker run --rm -it -p 6432:5432 \
--name="postgres" \
-e 'POSTGRES_PASSWORD=postgres' \
postgres:13.4

#-v "${WORK_FOLDER}/data":/var/lib/postgresql/data \

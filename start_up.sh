#!/bin/bash

export PSQL_HOST=$1
export PSQL_USER=$2
export PSQL_PASSWORD=$3
export IEX_PUB_TOKEN=$4
export PGPASSWORD=$PSQL_PASSWORD
export PSQL_URL="jdbc:postgresql://${PSQL_HOST}:5432/jrvstrading"

#start docker
systemctl status docker || systemctl start docker || sleep 5

#create docker volume to persist db data
docker volume ls | grep "pgdata" || docker volume create pgdata || sleep 1

#stop existing jrvs-psql container
docker ps | grep jrvs-psql && docker stop $(docker ps | grep jrvs-psql | awk '{print $1}')

#start docker
docker run --rm --name jrvs-psql -e POSTGRES_PASSWORD=$PSQL_PASSWORD -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 $PSQL_USER
sleep 5

psql -h $PSQL_HOST -U $PSQL_USER -f ./trading_ddl/init_db.sql
psql -h $PSQL_HOST -U $PSQL_USER -d jrvstrading -f ./trading_ddl/schema.sql

#run springboot app
/usr/bin/java -jar ./lib/trading-1.0-SNAPSHOT.jar
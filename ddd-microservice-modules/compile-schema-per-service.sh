#! /bin/bash -e
for schema in h_hotel h_room h_payment h_order h_user;
do
  user=${schema}_user
  password=${schema}_password
  cat >> /docker-entrypoint-initdb.d/init.sql <<END
  CREATE USER '${user}'@'%' IDENTIFIED BY '$password';
  create database $schema;
  GRANT ALL PRIVILEGES ON $schema.* TO '${user}'@'%' WITH GRANT OPTION;
  USE $schema;
END
done

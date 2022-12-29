FROM gitpod/workspace-mysql
COPY settings.xml /home/gitpod/.m2

RUN /bin/bash -c "/usr/bin/mysqld_safe --skip-grant-tables &" && \
  sleep 5 && \
  mysql -u root -e "CREATE DATABASE mydb" && \
  mysql -u root < init/orsdb.sql
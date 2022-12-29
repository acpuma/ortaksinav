FROM gitpod/workspace-mysql
COPY settings.xml /home/gitpod/.m2

ADD init/orsdb.sql /etc/mysql/data.sql
RUN cp /etc/mysql/data.sql /docker-entrypoint-initdb.d
FROM gitpod/workspace-mysql
COPY settings.xml /home/gitpod/.m2

RUN /bin/bash -c "mysql -u root < init/orsdb.sql"
FROM gitpod/workspace-mysql
COPY settings.xml /home/gitpod/.m2

RUN mvn install
RUN brew install tomcat@9
RUN mysql -u root < init/orsdb.sql


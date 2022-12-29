FROM gitpod/workspace-mysql
COPY settings.xml /home/gitpod/.m2

FROM tomcat:9

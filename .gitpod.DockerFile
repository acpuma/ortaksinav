FROM tomcat:9

FROM gitpod/workspace-mysql
COPY settings.xml /home/gitpod/.m2

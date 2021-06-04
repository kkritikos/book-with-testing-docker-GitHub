FROM tomcat:8.5.43-jdk8
ADD ./book-rest/target/book.war /usr/local/tomcat/webapps
ADD ./tomcat-users.xml /usr/local/tomcat/conf
RUN sed -i 's/port="8080"/port="8090"/' ${CATALINA_HOME}/conf/server.xml
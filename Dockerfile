FROM tomcat:11.0-jdk21

# Remove default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR into Tomcat
COPY target/DigitalTransactionLimit-0.0.1-SNAPSHOT.war \
     /usr/local/tomcat/webapps/ROOT.war

# Expose Tomcat port
EXPOSE 8080

CMD ["catalina.sh", "run"]

FROM centos:6.6

COPY build/distributions/identity-server-0.1-1.noarch.rpm /tmp/identity-server.rpm
RUN yum install -y java /tmp/identity-server.rpm && rm -f /tmp/identity-server.rpm && yum clean all
COPY env/docker_identity-server /etc/default/identity-server
ENTRYPOINT . /etc/default/identity-server && java -Dserver.port=$IDENTITY_SERVER_PORT -jar /opt/identity-server/lib/IdentityService.war

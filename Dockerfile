FROM azul/zulu-openjdk-centos:8-latest

COPY build/distributions/identity-server-0.1-1.noarch.rpm /tmp/identity-server.rpm
RUN yum install -y /tmp/identity-server.rpm && rm -f /tmp/identity-server.rpm && yum clean all
COPY env/docker_identity-server /etc/default/identity-server
ENTRYPOINT . /etc/default/identity-server && java -Dserver.port=$IDENTITY_SERVER_PORT -jar /opt/identity-server/lib/IdentityService.war

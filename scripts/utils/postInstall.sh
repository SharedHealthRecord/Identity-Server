#!/bin/sh

ln -s /opt/identity-server/bin/identity-server /etc/init.d/identity-server
ln -s /opt/identity-server/etc/identity-server /etc/default/identity-server
ln -s /opt/identity-server/var /var/run/identity-server

if [ ! -e /var/log/identity-server ]; then
    mkdir /var/log/identity-server
fi

# Add identity-server service to chkconfig
chkconfig --add identity-server
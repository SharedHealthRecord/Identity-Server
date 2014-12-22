#!/bin/sh

rm -f /etc/init.d/identity-server
rm -f /etc/default/identity-server
rm -f /var/run/identity-server

#Remove identity-server from chkconfig
chkconfig --del identity-server || true

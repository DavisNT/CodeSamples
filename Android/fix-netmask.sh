#!/system/bin/sh

# Copyright 2017 Davis Mosenkovs
# Use at your own risk (see LICENSE for details).

# Sets netmask to 255.255.255.255 for mobile data (4G, LTE etc.) interface.
# This can prevent overlapping of mobile provider's network IP address space with some private network (e.g. VPN).
# Root (and probably toybox/busybox) is required.

# Set this to proper interface name.
MOBILE_INTERFACE=rmnet0


echo -n "Before: "
ip address show dev ${MOBILE_INTERFACE} | grep inet

MOBILE_INTERFACE_IP=`ip address show dev ${MOBILE_INTERFACE} | grep inet | head -n 1 | cut -f 6 -d " "`
MOBILE_INTERFACE_IP_WO_MASK=`echo ${MOBILE_INTERFACE_IP} | cut -f 1 -d "/"`
ip address add ${MOBILE_INTERFACE_IP_WO_MASK}/32 dev ${MOBILE_INTERFACE}
ip address del ${MOBILE_INTERFACE_IP} dev ${MOBILE_INTERFACE}

echo -n "After: "
ip address show dev ${MOBILE_INTERFACE} | grep inet

echo "Full output:"
ip address show dev ${MOBILE_INTERFACE}

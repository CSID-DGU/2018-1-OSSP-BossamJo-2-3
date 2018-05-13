#!/bin/sh

read floorNumber

#echo $floorNumber

apInfo=`cat /sys/class/net/wlp1s0/address`
echo $apInfo

if [`grep "$apInfo" test.txt`] 
then
	echo "참" 
else
	echo "거짓"
fi

#`cat /sys/class/net/wlp1s0/address >> test.txt`



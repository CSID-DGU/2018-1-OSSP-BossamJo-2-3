#!/bin/bash

#Create       : 18.5.11
#Modify       : 18.5.16
#Student Name : Lim JunSu
#Student Num  : 2010111661
#Description  : 현재 연결된 AP의 mac address를 파싱하는 스크립트 문.

#wireless lan chipset mac address
#apInfo=`cat /sys/class/net/wlp1s0/address`

# -o 옵션은 매칭되는 전체 라인이 아니라 매칭되는 문자열만 추출
# -E 정규표현식 사용
DoThisOnCtrlC(){
	echo "At end of loop : count=$count"
	make --makefile=/home/gokaist/바탕화면/2018-1-OSSP-BossamJo-3/makefile
	exit 0
}
	
trap 'DoThisOnCtrlC' SIGINT


while :
do
	apInfo=`iwconfig | grep -o -E '([[:xdigit:]]{1,2}:){5}[[:xdigit:]]{1,2}'`
	echo $apInfo
	isInFile=$(cat APInfo.txt | grep -c "$apInfo")

	# *.txt에 mac address가 저장되어 있는지 확인
	if [ $isInFile -eq 0 ] 
	then
		echo "문자열이 존재하지 않습니다 mac주소 저장" 
		`iwconfig | grep -o -E '([[:xdigit:]]{1,2}:){5}[[:xdigit:]]{1,2}' >> ./APInfo.txt`
	else
		echo "mac 주소가 이미 파일에 있습니다"
	fi

	((count++))
	sleep 2
done

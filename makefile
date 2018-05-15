CC = gcc
CFLAGS = -I/usr/include/mysql -L/usr/lib/mysql -lmysqlclient
TARGET = mysql
SOURCE = /home/gokaist/바탕화면/2018-1-OSSP-BossamJo-3/DBHandling/mysql.c

$(TARGET) : $(SOURCE)
	$(CC) -o $(TARGET) $(SOURCE) $(CFLAGS)
	./mysql

clean :
	rm -rf ./GetAPInfo/mysql
	rm -rf ./GetAPInfo/APInfo.txt

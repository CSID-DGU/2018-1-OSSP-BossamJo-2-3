CC = gcc
CFLAGS = -I/usr/include/mysql -L/usr/lib/mysql -lmysqlclient
TARGET = mysql
SOURCE = ./DBHandling/mysql.c

$(TARGET) : $(SOURCE)
	$(CC) -o $(TARGET) $(SOURCE) $(CFLAGS)
	./mysql

clean :
	rm -rf ./mysql
	rm -rf ./GetAPInfo/APInfo.txt

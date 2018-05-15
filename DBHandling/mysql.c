//Create        : 18.5.13
//Modify        : 18.5.15
//Student Name  : Lim JunSu
//Student Num   : 2010111661
//Description   : connection sql db and save ap mac address

#include <mysql.h>
#include <string.h>
#include <stdio.h>

//#define DB_HOST " "
//#define DB_USER " "
//#define DB_PASS " "
#define DB_NAME "APInfo"
#define CHOP(x) x[strlen(x)-1] = ' '

int main(void)
{
	MYSQL* connection=NULL, conn;
	MYSQL_RES* sql_result;
	MYSQL_ROW sql_row;
	FILE* pFile=NULL;
	int query_stat;

	char apMacAddress[20];
	char floorInfo[30];
	char DBUserId[5];
	char DBUserPwd[5];
	char IPAddress[15];	
	char query[255];

	fprintf(stdout, "Input Building Information : ");
	fgets(floorInfo, 30, stdin);
	CHOP(floorInfo);
//	fprintf(stdout, "%s\n", floorInfo);

	fprintf(stdout, "Input Database IP, ID, PWD : ");
	fscanf(stdin, "%s %s %s", IPAddress, DBUserId, DBUserPwd);
	fgetc(stdin);
//	fprintf(stdout, "%s %s %s\n", IPAddress, DBUserId, DBUserPwd);

	pFile=fopen("/home/gokaist/바탕화면/2018-1-OSSP-BossamJo-3/GetAPInfo/APInfo.txt", "rt");
	if(pFile==NULL)
	{
		fprintf(stderr, "File open error");
		return -1;
	}
/*
	while(fscanf(pFile, "%s", apMacAddress)!=-1)
	{
		fprintf(stdout, "%s\n", apMacAddress);
	} 
*/
	mysql_init(&conn);

	connection=mysql_real_connect(&conn, IPAddress, DBUserId, DBUserPwd, DB_NAME, 3306, (char*)NULL, 0);

	if(connection == NULL)
	{
		fprintf(stderr, "Mysql connection error : %s", mysql_error(&conn));
		return -1;
	}

	
	while(fscanf(pFile, "%s", apMacAddress)!=-1)
	{
		sprintf(query, "insert into APInfo_db values "
				"('%s', '%s')",
				floorInfo, apMacAddress);
	
		query_stat = mysql_query(connection, query);
		if(query_stat != 0)
		{
			fprintf(stderr, "Mysql query error : %s", mysql_error(&conn));
			return -1;
		}
	}
	
	fclose(pFile);
	mysql_close(connection);
}

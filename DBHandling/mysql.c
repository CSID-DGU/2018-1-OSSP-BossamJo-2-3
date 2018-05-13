#include <mysql.h>
#include <string.h>
#include <stdio.h>

#define DB_HOST " "
#define DB_USER " "
#define DB_PASS " "
#define DB_NAME "APInfo"
#define CHOP(x) x[strlen(x)-1] = ' '

int main(void)
{
	MYSQL* connection=NULL, conn;
	MYSQL_RES* sql_result;
	MYSQL_ROW sql_row;
	int query_stat;

	char apMacAddress[20];
	char floorInfo[20];
	char query[255];

	mysql_init(&conn);

	connection=mysql_real_connect(&conn, DB_HOST, DB_USER, DB_PASS, DB_NAME, 3306, (char*)NULL, 0);

	if(connection == NULL)
	{
		fprintf(stderr, "Mysql connection error : %s", mysql_error(&conn));
		return -1;
	}

	fgets(floorInfo, 20, stdin);
	CHOP(floorInfo);

	fgets(apMacAddress, 20, stdin);
	CHOP(apMacAddress);


	sprintf(query, "insert into APInfo_db values "
			"('%s', '%s')",
			floorInfo, apMacAddress);
	
	query_stat = mysql_query(connection, query);
	if(query_stat != 0)
	{
		fprintf(stderr, "Mysql query error : %s", mysql_error(&conn));
		return -1;
	}
	
	mysql_close(connection);
}

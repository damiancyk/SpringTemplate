package com.damiancyk.dialect;

import java.sql.Types;

import org.hibernate.dialect.SQLServer2012Dialect;
import org.hibernate.type.StringType;

public class MySQLServerDialect extends SQLServer2012Dialect {

	long MAX_LENGTH = 255;

	public MySQLServerDialect() {

		registerColumnType(Types.CLOB, "varchar(MAX)");
		registerColumnType(Types.LONGNVARCHAR, "varchar(MAX)");
		registerColumnType(Types.VARCHAR, "varchar(MAX)");
		registerColumnType(Types.VARCHAR, MAX_LENGTH, "varchar($l)");
		registerColumnType(Types.CLOB, "nvarchar(MAX)");
		registerColumnType(Types.LONGNVARCHAR, "nvarchar(MAX)");
		registerColumnType(Types.NVARCHAR, "nvarchar(MAX)");
		registerColumnType(Types.NVARCHAR, MAX_LENGTH, "nvarchar($l)");
		registerHibernateType(Types.NVARCHAR, StringType.INSTANCE.getName());
		registerHibernateType(Types.LONGVARCHAR, StringType.INSTANCE.getName());
	}
}
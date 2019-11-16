package com.eying.pcss.workflow.config;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.PostgreSQL95Dialect;
import org.hibernate.type.StringType;

import java.sql.Types;

/**
 * 定义自己的方言。
 */
public class JsonbPostgresDialect extends MySQL8Dialect {

    public JsonbPostgresDialect() {
        super ();
        registerColumnType (Types.JAVA_OBJECT, "json");
        registerHibernateType (Types.ARRAY, StringType.class.getName ());
    }
}
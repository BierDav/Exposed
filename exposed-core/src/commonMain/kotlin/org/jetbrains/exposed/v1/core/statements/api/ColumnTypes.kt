package org.jetbrains.exposed.v1.core.statements.api


/**
 * Copy of [java.sql.Types]
 */
object ColumnTypes {
    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * `BIT`.
    </P> */
    val BIT: Int = -7

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * `TINYINT`.
    </P> */
    val TINYINT: Int = -6

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * `SMALLINT`.
    </P> */
    const val SMALLINT: Int = 5

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * `INTEGER`.
    </P> */
    const val INTEGER: Int = 4

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * `BIGINT`.
    </P> */
    val BIGINT: Int = -5

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * `FLOAT`.
    </P> */
    const val FLOAT: Int = 6

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * `REAL`.
    </P> */
    const val REAL: Int = 7


    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * `DOUBLE`.
    </P> */
    const val DOUBLE: Int = 8

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * `NUMERIC`.
    </P> */
    const val NUMERIC: Int = 2

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * `DECIMAL`.
    </P> */
    const val DECIMAL: Int = 3

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * `CHAR`.
    </P> */
    const val CHAR: Int = 1

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * `VARCHAR`.
    </P> */
    const val VARCHAR: Int = 12

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * `LONGVARCHAR`.
    </P> */
    val LONGVARCHAR: Int = -1


    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * `DATE`.
    </P> */
    const val DATE: Int = 91

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * `TIME`.
    </P> */
    const val TIME: Int = 92

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * `TIMESTAMP`.
    </P> */
    const val TIMESTAMP: Int = 93


    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * `BINARY`.
    </P> */
    val BINARY: Int = -2

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * `VARBINARY`.
    </P> */
    val VARBINARY: Int = -3

    /**
     * <P>The constant in the Java programming language, sometimes referred
     * to as a type code, that identifies the generic SQL type
     * `LONGVARBINARY`.
    </P> */
    val LONGVARBINARY: Int = -4

    /**
     * <P>The constant in the Java programming language
     * that identifies the generic SQL value
     * `NULL`.
    </P> */
    const val NULL: Int = 0

    /**
     * The constant in the Java programming language that indicates
     * that the SQL type is database-specific and
     * gets mapped to a Java object that can be accessed via
     * the methods `getObject` and `setObject`.
     */
    const val OTHER: Int = 1111


    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * `JAVA_OBJECT`.
     * @since 1.2
     */
    const val JAVA_OBJECT: Int = 2000

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * `DISTINCT`.
     * @since 1.2
     */
    const val DISTINCT: Int = 2001

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * `STRUCT`.
     * @since 1.2
     */
    const val STRUCT: Int = 2002

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * `ARRAY`.
     * @since 1.2
     */
    const val ARRAY: Int = 2003

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * `BLOB`.
     * @since 1.2
     */
    const val BLOB: Int = 2004

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * `CLOB`.
     * @since 1.2
     */
    const val CLOB: Int = 2005

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * `REF`.
     * @since 1.2
     */
    const val REF: Int = 2006

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type `DATALINK`.
     *
     * @since 1.4
     */
    const val DATALINK: Int = 70

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type `BOOLEAN`.
     *
     * @since 1.4
     */
    const val BOOLEAN: Int = 16

    //------------------------- JDBC 4.0 -----------------------------------
    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type `ROWID`
     *
     * @since 1.6
     */
    val ROWID: Int = -8

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type `NCHAR`
     *
     * @since 1.6
     */
    val NCHAR: Int = -15

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type `NVARCHAR`.
     *
     * @since 1.6
     */
    val NVARCHAR: Int = -9

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type `LONGNVARCHAR`.
     *
     * @since 1.6
     */
    val LONGNVARCHAR: Int = -16

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type `NCLOB`.
     *
     * @since 1.6
     */
    const val NCLOB: Int = 2011

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type `XML`.
     *
     * @since 1.6
     */
    const val SQLXML: Int = 2009

    //--------------------------JDBC 4.2 -----------------------------
    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type `REF CURSOR`.
     *
     * @since 1.8
     */
    const val REF_CURSOR: Int = 2012

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * `TIME WITH TIMEZONE`.
     *
     * @since 1.8
     */
    const val TIME_WITH_TIMEZONE: Int = 2013

    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * `TIMESTAMP WITH TIMEZONE`.
     *
     * @since 1.8
     */
    const val TIMESTAMP_WITH_TIMEZONE: Int = 2014
}

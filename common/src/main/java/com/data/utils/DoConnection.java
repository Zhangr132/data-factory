package com.data.utils;

import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Author zhangr132
 * @Date 2024/4/10 9:38
 * @注释
 */



public class DoConnection {
    private static Logger logger = LogManager.getLogger();

    public static Connection connection(String dbtype,String linktype,String tns,String ip,String port,String dbname,String username,String password){
        Connection conn = null;
        try {
            String jdbcurl = "";
            if ("ORACLE".equals(dbtype.toUpperCase())) {
                if("单节点".equals(linktype)){
                    jdbcurl = "jdbc:oracle:thin:@" + ip + ":"
                            + port + ":" + dbname;
                }else{
                    jdbcurl = "jdbc:oracle:thin:@ "+ tns;
                }
                conn =DBHelper.initOracle(jdbcurl,username,password, false);

            } else if ("MYSQL".equals(dbtype.toUpperCase())) {
                jdbcurl = "jdbc:mysql://" + ip + ":"
                        + port + "/" + dbname
                        + "?useUnicode=true&characterEncoding=utf-8";
                conn =DBHelper.initMysql(jdbcurl, username,password, false);

            } else if ("DB2".equals(dbtype.toUpperCase())) {
                jdbcurl = "jdbc:db2://" + ip + ":"
                        + port + "/" + dbname;
                conn =DBHelper.initDB2(jdbcurl, username,password, false);

            } else if ("SQLSERVER".equals(dbtype.toUpperCase())) {
                jdbcurl = "jdbc:sqlserver://" + ip + ":"
                        + port + ";DatabaseName=" + dbname;
                conn =DBHelper.initSQLServer(jdbcurl, username,password, false);

            }else if("GREENPLUM".equals(dbtype.toUpperCase())){
                /*jdbcurl = "jdbc:pivotal:greenplum://" + ip + ":"
                        + port + ";DatabaseName=" + dbname;*/
                jdbcurl = tns;
                conn = DBHelper.initGreenPlum(jdbcurl, username, password, false);
            }else if("HIVE".equals(dbtype.toUpperCase())){
                /*jdbcurl = "jdbc:hive2://" + ip + ":"+port + "/default";*/
                jdbcurl = tns;
                conn = DBHelper.initHIVE(jdbcurl, username, password, false);
            }else {
                return null;
            }
        } catch (Exception e) {
            logger.error("数据源连接失败",e);
            return null;
        }

        return conn;
    }

    public static void close(Connection conn){
        if (conn != null) {
            DBHelper.closeDB(conn, null, null);
        } else {
            DBHelper.closeDB(conn, null, null);
        }
    }
}


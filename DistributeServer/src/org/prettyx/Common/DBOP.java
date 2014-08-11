package org.prettyx.Common;
// +----------------------------------------------------------------------
// | Multipurpose Integrated Modeling System
// +----------------------------------------------------------------------
// | Copyright (c) 2014 http://prettyx.org All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.gnu.org/licenses/gpl.html )
// +----------------------------------------------------------------------
// | Author: XieFan <xiefan1228@gmail.com>
// +----------------------------------------------------------------------

import org.prettyx.DistributeServer.DistributeServer;

import java.beans.*;
import java.sql.*;
import java.sql.Statement;

/**
 * Encapsulate all of connecting operation
 *
 */

public class DBOP {

    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public DBOP(){

        try {
            DriverManager.registerDriver(new org.sqlite.JDBC());
            connection = DriverManager.getConnection("jdbc:sqlite:" + DistributeServer.absolutePathOfDB);
            statement = connection.createStatement();
//            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            statement.setQueryTimeout(5);
        } catch (Exception e){
            LogUtility.logUtility().log2err(e.getMessage());
        }
    }

    /**
     * Return execute result
     *
     * @return
     *      ResultSet
     */
    public ResultSet Operation(String sql) {
        try {
            resultSet = statement.executeQuery(sql);
            LogUtility.logUtility().log2out("First Table: " + resultSet.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }
}

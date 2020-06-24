/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.utils;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Poca
 */
public class DBUtils {
    public static Connection getConnection() throws NamingException, SQLException {
        Context context = new InitialContext();
        Context webServerContext = (Context) context.lookup("java:comp/env");
        DataSource ds = (DataSource) webServerContext.lookup("PRXProjDB");
        Connection con = ds.getConnection();
        return con;
    }
}

package org.rest_api.db_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/ecommerceDB";
        String user = System.getenv("dbUsername");
        String password = System.getenv("dbPassword");
        return DriverManager.getConnection(url, user, password);
    }
}

package Connection;


import java.sql.*;

public interface ConnectionPool {

    public static Connection getConnection() throws SQLException {

                String url = "jdbc:mysql://localhost:3306/accessToProduct";
                String user =  "root";
                String psw =  "MyFirstRoot";
         return DriverManager.getConnection(url, user, psw);
    }
}



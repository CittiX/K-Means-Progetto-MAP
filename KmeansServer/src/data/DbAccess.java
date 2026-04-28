package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbAccess {
    String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    final String DBMS = "jdbc:mysql";
    final String SERVER = "localhost";
    final String DATABASE = "MapDB";
    final String PORT = "3306";
    final String USER_ID = "MapUser";
    final String PASSWORD = "map";
    final String URL = DBMS + "://" + SERVER + ":" + PORT + "/";
    Connection conn;

    void initConnection() throws DatabaseConnectionException {
        try {
            Class.forName(DRIVER_CLASS_NAME);
            this.conn = DriverManager.getConnection(URL + DATABASE , USER_ID, PASSWORD);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            Class.forName(DRIVER_CLASS_NAME);
            return DriverManager.getConnection(URL + DATABASE, USER_ID, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void closeConnection() throws SQLException {
        try {
            Class.forName(DRIVER_CLASS_NAME);
            getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}

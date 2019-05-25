package entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionManager {
    private static final String url = "jdbc:postgresql://localhost:5432/dauphine";
    private static final String user = "mydauphine";
    private static final  String password = "mydauphine";

    public Connection connectToDb() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}

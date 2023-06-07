package nl.saxion.ptbc.shared;
import java.sql.*;

public class Shared {
    // now start query-ing
    public static final String COMMUNICATION_SERVER = "localhost";
    public static final int COMMUNICATION_PORT = 50000;

    public static Connection setupDatabaseConnection() {

        String dbURL = "jdbc:sqlite:sql/testDatabase";
        Connection conn;
        {
            try {
                conn = DriverManager.getConnection(dbURL);
                System.out.println("Connection works");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return conn;
        }
    }
}

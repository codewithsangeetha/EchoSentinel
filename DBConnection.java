import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/echo_sentinel";
    
    private static final String USER = System.getenv("DB_USER");

    private static final String PASSWORD = System.getenv("DB_PASSWORD");
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            System.out.println("EchoSentinel connected to MySQL successfully!");
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            System.out.println(e.getMessage());
        }
    }
}

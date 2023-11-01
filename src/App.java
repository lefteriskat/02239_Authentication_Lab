import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
    public static void main(String[] args) {
        String url = "jdbc:mariadb://localhost:3306/data_security";
        String username = "lefteris";
        String password = "12344321";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            // Create the "data_security" database
            statement.execute("CREATE DATABASE IF NOT EXISTS data_security");

            // Select the "data_security" database
            statement.execute("USE data_security");

            // Create the "users" table
            statement.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "username VARCHAR(255) NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "salt VARCHAR(255) NOT NULL," +
                    "PRIMARY KEY (username)" +
                    ")");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
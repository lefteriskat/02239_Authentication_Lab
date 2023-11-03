package com.dtu.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import com.dtu.myinterface.PrintServerInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthenticationService {
    private Path userDatabasePath;
    public Connection conn;
    private Map<String, User> userDatabase = new HashMap<>();
    private Map<String, String> tokens = new HashMap<>();

    public static final long TOKEN_VALIDITY_PERIOD = 3600000; // 1 hour in milliseconds

    public static final String USER_DETAIL_DELIMITER = ":::";

    private static final String AUTHENTICATION_ERROR_PASSWORD = "Authentication failed due to wrong password.Access to printer denied.";
    private static final String AUTHENTICATION_ERROR_USERNAME= "Authentication failed because username doesn't exist.Access to printer denied.";


    public static class DBMS{
        private String url ;
        private String DBusername;
        private String DBpassword ;
        
        public DBMS(){
            Properties properties = new Properties();
            try (FileInputStream input = new FileInputStream("config.properties")) {
                properties.load(input);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // Read the database configuration
            String dbUrl = properties.getProperty("db.url");
            String dbUsername = properties.getProperty("db.username");
            String dbPassword = properties.getProperty("db.password");

            // Use the configuration
            System.out.println("Database URL: " + dbUrl);
            System.out.println("Database Username: " + dbUsername);
            url = dbUrl;
            DBusername = dbUsername;
            DBpassword = dbPassword;
        }

        public String getUrl(){
            return url;
        }
        public String getUsername(){
            return DBusername;
        }
        public String getPassword(){
            return DBpassword;
        }
    }


    public static class User{
        private String username;
        private byte[] salt;
        private byte[] passwordHash;
        private String authToken;

        public User(String username, byte[] salt, byte[] passwordHash) {
            this.username = username;
            this.salt = salt;
            this.passwordHash = passwordHash;
        }
        public User(String username, String passwordHash, String salt) {
            this.username = username;
            this.salt = Base64.getDecoder().decode(salt);
            this.passwordHash = Base64.getDecoder().decode(passwordHash);
        }

        public User(String userString) {
            String[] userStringArray = userString.split(USER_DETAIL_DELIMITER);
            this.username = userStringArray[0];
            this.salt = Base64.getDecoder().decode(userStringArray[1]);
            this.passwordHash = Base64.getDecoder().decode(userStringArray[2]);
        }

        public String getUsername() {
            return username;
        }

        public byte[] getSalt() {
            return salt;
        }

        public byte[] getPasswordHash() {
            return passwordHash;
        }

        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }

        @Override
        public String toString() {
            return username + USER_DETAIL_DELIMITER + Base64.getEncoder().encodeToString(salt) + USER_DETAIL_DELIMITER + Base64.getEncoder().encodeToString(passwordHash);
        }
    }
    /*Constructor to use system file */
    public AuthenticationService(String _userDatabasePath) {
        userDatabasePath = Path.of(_userDatabasePath);
        try {
            loadUsersFromFile();
        } catch (IOException e) {
            System.out.println("Error loading user database from file: " + e.getMessage());
        }
    }
    /*Constructor to use MariaDB database connection*/
    public AuthenticationService() throws SQLException {
        
        DBMS database = new DBMS();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(database.getUrl(), database.getUsername(), database.getPassword());
            Statement statement = connection.createStatement();

            System.out.println("Connection successfully established");
            
            // Create the "data_security" database
            statement.execute("CREATE DATABASE IF NOT EXISTS data_security");

            // Select the "data_security" database
            statement.execute("USE data_security");

            // Create the "users" table
            statement.execute("CREATE TABLE IF NOT EXISTS auth_users (" +
                    "username VARCHAR(255) NOT NULL," +
                    "hashed_password VARCHAR(255) NOT NULL," +
                    "salt VARCHAR(255) NOT NULL," +
                    "PRIMARY KEY (username)" +
                    ")");
        }catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        conn=connection;
        try {
            loadUsersFromDatabase();
        } catch (SQLException e) {
            System.out.println("Error loading users data from database: " + e.getMessage());
        }
    }

    private void loadUsersFromFile() throws IOException {
        if (!Files.exists(userDatabasePath)) {
            throw new FileNotFoundException("Database file not found: " + userDatabasePath);
        }
        try (BufferedReader reader = Files.newBufferedReader(userDatabasePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = new User(line);
                userDatabase.put(user.getUsername(), user);
            }
            System.out.println("User database loaded from file: " + userDatabasePath);
        } catch (IOException e) {
            System.out.println("Error loading user database from file: " + e.getMessage());
        }
    }
    private void loadUsersFromDatabase() throws SQLException {

        try (PreparedStatement selectStatement = conn.prepareStatement("select * from auth_users")) {

            ResultSet rs = selectStatement.executeQuery();
                

            while (rs.next()) { // will traverse through all rows
                String username = rs.getString("username");
                String pw = rs.getString("hashed_password");
                String salt = rs.getString("salt");

                User user = new User(username,pw,salt);
                userDatabase.put(user.getUsername(), user);
                /* 
                System.out.println(user);
                System.out.println(pw);
                System.out.println(salt);
                System.out.println("--------------------------");*/
            }
            System.out.println("User data loaded from database successfully.");
        } catch (SQLException e) {
            System.out.println("Error loading users data from database: " + e.getMessage());
        }
    }

    public void saveUsersToFile() throws IOException {
        if (!Files.exists(userDatabasePath)) {
            System.out.println("Creating new user database file: " + userDatabasePath);
            Files.createFile(userDatabasePath);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(userDatabasePath)) {
            for (User user : userDatabase.values()) {
                writer.write(user.toString());
                writer.newLine();
            }
            System.out.println("User database saved to file: " + userDatabasePath);
        } catch (IOException e) {
            System.out.println("Error saving user database to file: " + e.getMessage());
        }
    }

    public void saveUsersToDatabase() throws SQLException {
        
        if(conn.isClosed()){
            DBMS database = new DBMS();
            conn=DriverManager.getConnection(database.getUrl(), database.getUsername(), database.getPassword());
        }
        String insertSql = "INSERT INTO auth_users (username, hashed_password, salt) VALUES (?, ?, ?)";
        
        try (PreparedStatement insertStatement = conn.prepareStatement(insertSql)) {

            for (User user : userDatabase.values()) {

                insertStatement.setString(1, user.username);
                insertStatement.setString(2, Base64.getEncoder().encodeToString(user.passwordHash)); // passwordHash is Byte[]
                insertStatement.setString(3, Base64.getEncoder().encodeToString(user.salt)); // salt is Byte[]

                int rowsAffected = insertStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("User inserted successfully.");
                } else {
                    System.out.println("Failed to insert user.");
                }
            }
        } catch (SQLException e) {
                System.out.println("Error saving user database to file: " + e.getMessage());
        }
    }
    

    public User registerUser(String username, String password) throws Exception {
        byte[] salt = generateSalt();
        byte[] passwordHash = hashPassword(password, salt);
        User newUser = new User(username, salt, passwordHash);
        userDatabase.put(username, newUser);
        
        //reopen connection
        if(conn.isClosed()){
            DBMS database = new DBMS();
            conn=DriverManager.getConnection(database.getUrl(), database.getUsername(), database.getPassword());
        }
        

        String insertSql = "INSERT INTO auth_users (username, hashed_password, salt) VALUES (?, ?, ?)";

        /*add to the database */
        try( PreparedStatement insertStatement = conn.prepareStatement(insertSql)){
            insertStatement.setString(1, username);
            insertStatement.setString(2, Base64.getEncoder().encodeToString(passwordHash)); // passwordHash is Byte[]
            insertStatement.setString(3, Base64.getEncoder().encodeToString(salt)); // salt is Byte[]

            int rowsAffected = insertStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User inserted successfully.");
            } else {
                System.out.println("Failed to insert user.");
            }
        }catch(SQLException e){
            System.out.println("Failed to insert user at database.");
            e.printStackTrace();
        }

        return newUser;
    }
    public String loginUser(String username, String password) throws Exception {
        if (userDatabase.containsKey(username)) {
            User user = userDatabase.get(username);
            byte[] providedHash = hashPassword(password, user.getSalt());
            if (MessageDigest.isEqual(providedHash, user.getPasswordHash())) {
                String authToken = generateAuthToken();
                user.setAuthToken(authToken);
                tokens.put(authToken, username);
                return authToken;
            }
        }
        else return AUTHENTICATION_ERROR_USERNAME; // Authentication failed
        
        return AUTHENTICATION_ERROR_PASSWORD; // Authentication failed
    }

    public boolean authenticateWithToken(String username, String authToken) {
        if (tokens.containsKey(authToken) && tokens.get(authToken).equals(username)) {
            System.out.println(authToken);
            
            long tokenTimestamp = Long.parseLong(authToken.split("-")[1]);
            if (System.currentTimeMillis() - tokenTimestamp <= TOKEN_VALIDITY_PERIOD) {
                return true; // Token is valid
            }
            System.out.println("egggg");
            tokens.remove(authToken); // Token has expired
        }
        System.out.println("Token not found or expired");
        return false; // Token not found or expired
    }

    private String generateAuthToken() {
        // Generate a unique token by combining a UUID with a timestamp.
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomUUID = String.valueOf(UUID.randomUUID().hashCode() & Integer.MAX_VALUE);
        return randomUUID + "-" + timestamp;
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private byte[] hashPassword(String password, byte[] salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] passwordBytes = password.getBytes();
        md.update(passwordBytes);
        return md.digest();
    } 
}


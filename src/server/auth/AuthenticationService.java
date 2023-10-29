package server.auth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthenticationService {
    private Path userDatabasePath;
    private Map<String, User> userDatabase = new HashMap<>();
    private Map<String, String> tokens = new HashMap<>();

    public static final long TOKEN_VALIDITY_PERIOD = 3600000; // 1 hour in milliseconds

    public static final String USER_DETAIL_DELIMITER = ":::";

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

    public AuthenticationService(String _userDatabasePath) {
        userDatabasePath = Path.of(_userDatabasePath);
        try {
            loadUsersFromFile();
        } catch (IOException e) {
            System.out.println("Error loading user database from file: " + e.getMessage());
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
    


    public User registerUser(String username, String password) throws Exception {
        byte[] salt = generateSalt();
        byte[] passwordHash = hashPassword(password, salt);
        User newUser = new User(username, salt, passwordHash);
        userDatabase.put(username, newUser);
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
        return null; // Authentication failed
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

    public static void main(String[] args) throws Exception {
        String userDatabasePath = "C:\\Users\\Maciek\\Desktop\\DTU\\semester_3\\data_security\\assignement_2\\02239_Authentication_Lab\\users.db";

        AuthenticationService authService = new AuthenticationService(userDatabasePath);

        // Register a new user
        // User newUser1 = authService.registerUser("lefteris", "password123");
        // User newUser2 = authService.registerUser("admin", "admin");

        // for(int i = 0; i < 10; i++) {
        //     var token = authService.loginUser("lefteris", "password123");
        //     if (token != null) {
        //         System.out.println("Authentication successful for user: lefteris");
        //     } else {
        //         System.out.println("Authentication failed for user: lefteris");
        //     }        
        // }
    }
}


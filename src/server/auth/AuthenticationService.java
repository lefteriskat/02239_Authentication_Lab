package server.auth;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthenticationService {
    private Map<String, User> userDatabase = new HashMap<>();
    private Map<String, String> tokens = new HashMap<>();

    public static final long TOKEN_VALIDITY_PERIOD = 3600000; // 1 hour in milliseconds

    public static class User {
        private String username;
        private byte[] salt;
        private byte[] passwordHash;
        private String authToken;

        public User(String username, byte[] salt, byte[] passwordHash) {
            this.username = username;
            this.salt = salt;
            this.passwordHash = passwordHash;
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
            tokens.remove(authToken); // Token has expired
        }
        return false; // Token not found or expired
    }

    private String generateAuthToken() {
        // Generate a unique token by combining a UUID with a timestamp.
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomUUID = String.valueOf(UUID.randomUUID().hashCode());
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
        AuthenticationService authService = new AuthenticationService();

        // Register a new user
        User newUser = authService.registerUser("lefteris", "password123");

        // Authenticate the user
        String username = "lefteris";
        String password = "password123";
        String token = authService.loginUser(username, password);
        if ( authService.authenticateWithToken(username, token)) {
            System.out.println("Authentication successful for user: " + username);
        } else {
            System.out.println("Authentication failed for user: " + username);
        }
    }
}


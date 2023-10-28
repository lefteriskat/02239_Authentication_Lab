package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import server.auth.AuthenticationService;

public class PrintServerImpl extends UnicastRemoteObject implements PrintServerInterface {
    private Map<String, Printer> printers;
    private boolean isPrintServerRunning;
    private Map<String, String> configParams;
    private AuthenticationService authenticationService;

    private static final String AUTHENTICATION_ERROR = "Authentication required! Please sign in or register if you don't have an account";
    private static final String SERVER_ERROR = "Server error";

    public PrintServerImpl(String userDatabasePath) throws RemoteException {
        isPrintServerRunning = false;
        configParams = new HashMap<>();
        printers = new HashMap<>();
        authenticationService = new AuthenticationService(userDatabasePath);
    }

    public PrintServerImpl(String userDatabasePath, Map<String, Printer> _printers) throws RemoteException {
        isPrintServerRunning = false;
        configParams = new HashMap<>();
        printers = _printers;
        authenticationService = new AuthenticationService(userDatabasePath);
    }

    @Override
    public String register(String username, String password) throws RemoteException {
        String output;
        try {
            AuthenticationService.User newUser = authenticationService.registerUser(username, password);
            if (newUser != null) {
                output  = "User " + username + " registered successfully.";
            } else {
                output =  "User registration failed. The username " + username + " may already be taken.";
            }
            System.out.println("Server: "+ output);
            return output;

        } catch (Exception e) {
            System.out.println( "User registration failed: " + e.getMessage());
            return SERVER_ERROR;
        }
    }

    @Override
    public String signIn(String username, String password) throws RemoteException {
        try {
            String authToken = authenticationService.loginUser(username, password);
            if (authToken != null) {
                return authToken; // Sign-in successful; return the authentication token.
            } else {
                System.out.println("Server: Authentication failed. Please check your username and password.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Sign-in failed: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String print(String filename, String printer, String username, String token) throws RemoteException {
        if (authenticationService.authenticateWithToken(username, token)) {
            Printer target = printers.get(printer);
            String output;
            if ( target != null) {
                output = "File: " + filename + " printed at Printer: " + printer + "!";
            }
            else {
                output = "No printer with name: " + printer + " !";
            }
            System.out.println("Server: " + output);
            return output;
        }
        return AUTHENTICATION_ERROR;
    }

    @Override
    public String queue(String printer, String username, String token) throws RemoteException {
        if (authenticationService.authenticateWithToken(username, token)) {
            Printer target = printers.get(printer);
            String output;
            if ( target != null) {
                output = printer+ "'s queue is: " + target.getPrintQueue().toString();
            }
            else {
                output = "No printer with name: " + printer + " !";
            }
            System.out.println("Server: " + output);
            return output;
        }
        return AUTHENTICATION_ERROR;
    }

    @Override
    public String topQueue(String printer, int job, String username, String token) throws RemoteException {
        if (authenticationService.authenticateWithToken(username, token)) {
            Printer target = printers.get(printer);
            String output;
            if ( target != null) {
                output = target.topQueue(job);
            }
            else {
                output = "No printer with name: " + printer + " !";
            }
            System.out.println("Server: " + output);
            return output;
        }
        return AUTHENTICATION_ERROR;
    }

    @Override
    public String start(String username, String token) throws RemoteException {
        if (authenticationService.authenticateWithToken(username, token)) {
            isPrintServerRunning = true;
            String output = "Print Server is running!";
            System.out.println("Server: " + output);
            return output;
        }
        return AUTHENTICATION_ERROR;
    }

    @Override
    public String stop(String username, String token) throws RemoteException {
        if (authenticationService.authenticateWithToken(username, token)) {
            isPrintServerRunning = false;
            String output = "Print Server is stopped!";
            System.out.println("Server: " + output);
            return output;
        }
        return AUTHENTICATION_ERROR;
    }

    @Override
    public String restart(String username, String token) throws RemoteException {
        if (authenticationService.authenticateWithToken(username, token)) {
            stop(username, token);
            for (Printer printer : printers.values()) {
                printer.clearQueue();
            }
            start(username, token);
            String output = "Print Server is restarted!";
            System.out.println("Server: " + output);
            return output;
        }
        return AUTHENTICATION_ERROR;
    }

    @Override
    public String status(String printer, String username, String token) throws RemoteException {
        if (authenticationService.authenticateWithToken(username, token)) {
            Printer target = printers.get(printer);
            String output;
            if ( target != null) {
                output = "Printer " + printer + "Status:" + target.getStatus();
            }
            else {
                output = "No printer with name: " + printer + " !";
            }
            System.out.println("Server: " + output);
            return output;
        }
        return AUTHENTICATION_ERROR;
    }

    @Override
    public String readConfig(String parameter, String username, String token) throws RemoteException {
        if (authenticationService.authenticateWithToken(username, token)) {
            String output = "Parameter <" + parameter + "> not found.";
            if (configParams.containsKey(parameter)) {
                output = "Parameter's <" + parameter + "> value: " + configParams.get(parameter);
            }
            System.out.println("Server: " + output);
            return output;
        }
        return AUTHENTICATION_ERROR;
    }

    @Override
    public String setConfig(String parameter, String value, String username, String token) throws RemoteException {
        if (authenticationService.authenticateWithToken(username, token)) {
            configParams.put(parameter, value);
            String output = "Parameter <" + parameter + "> set with value: <"+ value +">.";
            System.out.println("Server: " + output);
            return output;
        }
        return AUTHENTICATION_ERROR;
    }
}
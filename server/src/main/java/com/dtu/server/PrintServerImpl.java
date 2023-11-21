package com.dtu.server;

import com.dtu.server.policies.AccessPolicy;
import com.dtu.server.policies.AccessPolicy.AccessPolicyOptions;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.SQLException;
import com.dtu.myinterface.PrintServerInterface;

public class PrintServerImpl extends UnicastRemoteObject implements PrintServerInterface {
    private Map<String, Printer> printers;
    private boolean isPrintServerRunning;
    private Map<String, String> configParams;
    private AuthenticationService authenticationService;

    private AccessPolicy accessPolicy;

    private static final String AUTHENTICATION_ERROR = "Authentication required! Please sign in or register if you don't have an account";
    private static final String SERVER_ERROR = "Server error";

    /* 
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

     
    }*/
    public PrintServerImpl(Map<String, Printer> _printers, AccessPolicyOptions p) throws RemoteException, SQLException {
        isPrintServerRunning = false;
        configParams = new HashMap<>();
        printers = _printers;
        authenticationService = new AuthenticationService();
        accessPolicy = new AccessPolicy(p);

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
            

            try{
                accessPolicy.checkPermission(username, Operations.print);
                //it will throw an exception if it is not allowed

                Printer target = printers.get(printer);
                String output;
                if ( target != null) {
                    target.setStatus("ON");
                    target.addToQueue(filename);
                    output = "File: " + filename + " added to printer: " + printer + " queue!";
                }
                else {
                    output = "No printer with name: " + printer + " !";
                }
                System.out.println("Server: " + output);
                return output;
            }catch(Exception e){
                return e.getMessage();
            }


        }
        return AUTHENTICATION_ERROR;
    }

    @Override
    public String queue(String printer, String username, String token) throws RemoteException {
        if (authenticationService.authenticateWithToken(username, token)) {
            
            try{
                accessPolicy.checkPermission(username, Operations.queue);
                //it will throw an exception if it is not allowed

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

            }catch(Exception e){
                return e.getMessage();
            }
        }
        return AUTHENTICATION_ERROR;
    }

    @Override
    public String topQueue(String printer, int job, String username, String token) throws RemoteException {
        if (authenticationService.authenticateWithToken(username, token)) {

            //function to check user permissions for this action
            try{
                accessPolicy.checkPermission(username, Operations.topQueue);
                //it will throw an exception if it is not allowed

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
            }catch(Exception e){
                return e.getMessage();
            }
        }
        return AUTHENTICATION_ERROR;
    }

    @Override
    public String start(String username, String token) throws RemoteException {
        if (authenticationService.authenticateWithToken(username, token)) {

            try{
                accessPolicy.checkPermission(username, Operations.start);
                //it will throw an exception if it is not allowed

                isPrintServerRunning = true;
                String output = "Print Server is running!";
                System.out.println("Server: " + output);
                return output;
            }catch(Exception e){
                return e.getMessage();
            }
        }
        return AUTHENTICATION_ERROR;
    }

    @Override
    public String stop(String username, String token) throws RemoteException {
        if (authenticationService.authenticateWithToken(username, token)) {

            try{
                accessPolicy.checkPermission(username, Operations.stop);
                //it will throw an exception if it is not allowed

                isPrintServerRunning = false;
                String output = "Print Server is stopped!";
                System.out.println("Server: " + output);
                return output;
            }catch(Exception e){
                return e.getMessage();
            }
        }
        return AUTHENTICATION_ERROR;
    }

    @Override
    public String restart(String username, String token) throws RemoteException {
        if (authenticationService.authenticateWithToken(username, token)) {

            try{
                accessPolicy.checkPermission(username, Operations.restart);
                //it will throw an exception if it is not allowed

                stop(username, token);
                for (Printer printer : printers.values()) {
                    printer.clearQueue();
                }
                start(username, token);
                String output = "Print Server is restarted!";
                System.out.println("Server: " + output);
                return output;
            }catch(Exception e){
                return e.getMessage();
            }
        }
        return AUTHENTICATION_ERROR;
    }

    @Override
    public String status(String printer, String username, String token) throws RemoteException {
        if (authenticationService.authenticateWithToken(username, token)) {

            try{
                accessPolicy.checkPermission(username, Operations.status);
                //it will throw an exception if it is not allowed

                Printer target = printers.get(printer);
                String output;
                if ( target != null) {
                    output = "Printer " + printer + " Status: " + target.getStatus();
                }
                else {
                    output = "No printer with name: " + printer + " !";
                }
                System.out.println("Server: " + output);
                return output;

                }catch(Exception e){
                    return e.getMessage();
                }
        }
        return AUTHENTICATION_ERROR;
    }

    @Override
    public String readConfig(String parameter, String username, String token) throws RemoteException {
        if (authenticationService.authenticateWithToken(username, token)) {

            try{
                accessPolicy.checkPermission(username, Operations.readConfig);
                //it will throw an exception if it is not allowed


                String output = "Parameter <" + parameter + "> not found.";
                if (configParams.containsKey(parameter)) {
                    output = "Parameter's <" + parameter + "> value: " + configParams.get(parameter);
                }
                System.out.println("Server: " + output);
                return output;
            }catch(Exception e){
                return e.getMessage();
            }
        }
        return AUTHENTICATION_ERROR;
    }

    @Override
    public String setConfig(String parameter, String value, String username, String token) throws RemoteException {
        if (authenticationService.authenticateWithToken(username, token)) {

            try{
                accessPolicy.checkPermission(username, Operations.setConfig);
                //it will throw an exception if it is not allowed

                configParams.put(parameter, value);
                String output = "Parameter <" + parameter + "> set with value: <"+ value +">.";
                System.out.println("Server: " + output);
                return output;

            }catch(Exception e){
                return e.getMessage();
            }
        }
        return AUTHENTICATION_ERROR;
    }
}
package com.dtu.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.dtu.myinterface.PrintServerInterface;

public class PrintClient {
    public static void main(String[] args) {
        try {
            // Locate the RMI registry
            Registry registry = LocateRegistry.getRegistry("localhost", 1077);

            // Look up the server object by name
            PrintServerInterface server = (PrintServerInterface) registry.lookup("PrintServer");
            
            testBasedAccessControl(server);
            //register(server,"tiago","1234567");

            /* 
            System.out.println("------------REGISTER------------------");

            System.out.println("Register new user ...");
            //register new user
            register(server,"georgios","geo123rgios");
            register(server,"tiago","1234567");
            
            System.out.println("------------SIGN IN------------------");
            System.out.println("Trying to sign in user with wrong password ...");
            //try log in user with wrong password
            String token = signIn(server, "tiago", "tiago123");
            
            System.out.println("Sign in user with correct password ...");
            //log in user with correct password
            token = signIn(server, "tiago", "1234567");
            
            System.out.println("------------PRINTER SERVER------------------");
            System.out.println("Start printer server ...");
            start(server, "tiago", token);
            System.out.println("Stop printer server ...");
            stop(server, "tiago", token);
            System.out.println("Start printer server again ...");
            start(server, "tiago", token);

            //printer status
            System.out.println("------------STATUS----------------");
            status(server, "MyPrinter1", "tiago", token);

            System.out.println("------------PRINT----------------");
            System.out.println("Add files to the queue with logged in user account and respective token ...");
            //add files to printer queue
            print(server, "example1.pdf", "MyPrinter1", "tiago", token);
            print(server, "example2.pdf", "MyPrinter1", "tiago", token);
            print(server, "example3.pdf", "MyPrinter1", "tiago", token);

            
            System.out.println("Trying to add file to the queue with not logged in username (georgios) ...");
            //add files with a user who is not logged in
            print(server, "example4.pdf", "MyPrinter1", "georgios", token);
            
            System.out.println("------------QUEUE----------------");
            //check printer queue list
            queue(server, "MyPrinter1", "tiago", token);
            
            //move job 2 to the top of the queue
            topQueue(server, "MyPrinter1", 2, "tiago", token); // Move the 2nd job to the top.

            //confirm the top of the queue is job 2            
            queue(server, "MyPrinter1", "tiago", token);
            
            System.out.println("------------CONFIG------------------");
            //set config
            setConfig(server, "Draft Mode", "YES", "tiago", token);

            //read config
            readConfig(server, "Draft Mode", "tiago", token);

            //restart printer server
            restart(server, "tiago", token);

            System.out.println("------------QUEUE AFTER RESTART----------------");
            queue(server, "MyPrinter1", "tiago", token);
            */
            /* 

            print(server, "example.pdf", "MyPrinter1", "tiago", token);
            queue(server, "MyPrinter1", "tiago", token);
            topQueue(server, "MyPrinter1", 2, "tiago", token); // Move the 2nd job to the top.
            start(server, "tiago", token);
            stop(server, "tiago", token);
            restart(server, "tiago", token);
            status(server, "MyPrinter1", "tiago", token);
            setConfig(server, "someParameter", "NewValue", "tiago", token);
            readConfig(server, "someParameter", "tiago", token);*/
            /*
            // Call the server's methods and print the results
            String token = signIn(server, "lefteris", "password123");
            print(server, "example.pdf", "MyPrinter1", "lefteris", token);
            queue(server, "MyPrinter1", "lefteris", token);
            topQueue(server, "MyPrinter1", 2, "lefteris", token); // Move the 2nd job to the top.
            start(server, "lefteris", token);
            stop(server, "lefteris", token);
            restart(server, "lefteris", token);
            status(server, "MyPrinter1", "lefteris", token);
            setConfig(server, "someParameter", "NewValue", "lefteris", token);
            readConfig(server, "someParameter", "lefteris", token);
            */
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void register(PrintServerInterface server, String username, String password) throws RemoteException {
        try {
            String output = server.register(username, password);
            System.out.println("Client: " + output);
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static String signIn(PrintServerInterface server, String username, String password) throws RemoteException {
        String token = null;
        String AUTHENTICATION_ERROR_PASSWORD = "Authentication failed due to wrong password.Access to printer denied.";
        String AUTHENTICATION_ERROR_USERNAME= "Authentication failed because username doesn't exist.Access to printer denied.";
    
        try {
            token = server.signIn(username, password);
            if( !token.equals(AUTHENTICATION_ERROR_PASSWORD) && !token.equals(AUTHENTICATION_ERROR_USERNAME)) {
                System.out.println("Client: User " + username + "signed in successfully. Access to printer authorized.");
                return token;
            }
            else {
                System.err.println("Client: " + token);
                return null;
            }
            
            
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
            return token;
        }
    }

    private static void print(PrintServerInterface server, String filename, String printer, String username, String token) throws RemoteException {
        try {
            String output = server.print(filename, printer, username, token);
            System.out.println("Client: " + output);
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void queue(PrintServerInterface server, String printer, String username, String token) throws RemoteException {
        try {
            String output = server.queue(printer, username, token);
            System.out.println("Client: " + output);
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void topQueue(PrintServerInterface server, String printer, int job, String username, String token) throws RemoteException {
        try {
            String output = server.topQueue(printer, job, username, token);
            System.out.println("Client: " + output);
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void start(PrintServerInterface server, String username, String token) throws RemoteException {
        try {
            String output = server.start(username, token);
            System.out.println("Client: " + output);
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void stop(PrintServerInterface server, String username, String token) throws RemoteException {
        try {
            String output = server.stop(username, token);
            System.out.println("Client: " + output);
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void restart(PrintServerInterface server, String username, String token) throws RemoteException {
        try {
            String output = server.restart(username, token);
            System.out.println("Client: " + output);
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void status(PrintServerInterface server, String printer, String username, String token) throws RemoteException {
        try {
            String output = server.status(printer, username, token);
            System.out.println("Client: " + output);
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void readConfig(PrintServerInterface server, String parameter, String username, String token) throws RemoteException {
        try {
            String output = server.readConfig(parameter, username, token);
            System.out.println("Client: " + output);
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void setConfig(PrintServerInterface server, String parameter, String value, String username, String token) throws RemoteException {
        try {
            String output = server.setConfig(parameter, value, username, token);
            System.out.println("Client: " + output);
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    private static void testUser(String username, String password, PrintServerInterface server) throws RemoteException{
        
        String printer="MyPrinter1";
        String token;
        token = signIn(server, username, password);

        System.out.println("Evaluate "+ username +" permissions: ");
        start(server, username, token);
        
        print(server, "example1.pdf", printer, username, token);

        queue(server, "MyPrinter1", username, token);
        topQueue(server, "MyPrinter1", 1, username, token);

        restart(server, username, token);

        status(server,printer, username, token);
        setConfig(server, "Draft Mode", "YES", username, token);
        readConfig(server, "Draft Mode", username, token);
        
        stop(server, username, token);
    }
    private static void testBasedAccessControl(PrintServerInterface s) throws RemoteException{

        testUser("Alice", "pass1", s);
        testUser("Bob", "pass2", s);
        testUser("Cecilia", "pass3", s);
        testUser("David", "pass4", s);
        testUser("Erica", "pass5", s);
        testUser("Fred", "pass6", s);
        testUser("George", "pass7", s);
        // testUser("Henry", s);
        // testUser("Ida", s);

        /*the tests depend if the server is configured to manage in RBAC or UBAC approach to evaluate both*/
    }
}
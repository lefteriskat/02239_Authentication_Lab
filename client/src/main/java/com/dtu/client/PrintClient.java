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
            
            //register(server,"tiago","1234567");
            String token = signIn(server, "tiago", "1234567");

            print(server, "example.pdf", "MyPrinter1", "tiago", token);
            queue(server, "MyPrinter1", "tiago", token);
            topQueue(server, "MyPrinter1", 2, "tiago", token); // Move the 2nd job to the top.
            start(server, "tiago", token);
            stop(server, "tiago", token);
            restart(server, "tiago", token);
            status(server, "MyPrinter1", "tiago", token);
            setConfig(server, "someParameter", "NewValue", "tiago", token);
            readConfig(server, "someParameter", "tiago", token);
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
        try {
            token = server.signIn(username, password);
            if( token != null) {
                System.out.println("Client: User " + username + "signed in successfully!");
            }
            return token;
            
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
}


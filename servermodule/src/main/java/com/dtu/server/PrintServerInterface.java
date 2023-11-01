package com.dtu.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrintServerInterface extends Remote {
    String register(String username, String password) throws RemoteException;
    String signIn(String username, String password) throws RemoteException;
    String print(String filename, String printer, String username, String token) throws RemoteException; // prints file filename on the specified printer
    String queue(String printer, String username, String token) throws RemoteException; // lists the print queue for a given printer on
    String topQueue(String printer, int job, String username, String token) throws RemoteException; // moves job to the top of the queue
    String start(String username, String token) throws RemoteException; // starts the print server
    String stop(String username, String token) throws RemoteException; // stops the print server
    String restart(String username, String token) throws RemoteException; // stops the print server, clears the print queue and starts the print server again
    String status(String printer, String username, String token) throws RemoteException; // prints status of printer on the user's display
    String readConfig(String parameter, String username, String token) throws RemoteException;   // prints the value of the parameter on the print server to the user's display
    String setConfig(String parameter, String value, String username, String token) throws RemoteException;   // sets the parameter on the print server to value
} 

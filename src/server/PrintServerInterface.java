package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrintServerInterface extends Remote {
    String print(String filename, String printer) throws RemoteException; // prints file filename on the specified printer
    String queue(String printer) throws RemoteException; // lists the print queue for a given printer on
    String topQueue(String printer, int job) throws RemoteException; // moves job to the top of the queue
    String start() throws RemoteException; // starts the print server
    String stop() throws RemoteException; // stops the print server
    String restart() throws RemoteException; // stops the print server, clears the print queue and starts the print server again
    String status(String printer) throws RemoteException; // prints status of printer on the user's display
    String readConfig(String parameter) throws RemoteException;   // prints the value of the parameter on the print server to the user's display
    String setConfig(String parameter, String value) throws RemoteException;   // sets the parameter on the print server to value
} 

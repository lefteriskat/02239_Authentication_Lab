package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import server.PrintServerInterface;

public class PrintClient {
    public static void main(String[] args) {
        try {
            // Locate the RMI registry
            Registry registry = LocateRegistry.getRegistry("localhost", 1077);

            // Look up the server object by name
            PrintServerInterface server = (PrintServerInterface) registry.lookup("PrintServer");

            // Call the server's methods and print the results
            printResult(server, "example.pdf", "MyPrinter");
            queueResult(server, "MyPrinter");
            topQueueResult(server, "MyPrinter", 2); // Move the 2nd job to the top.
            startResult(server);
            stopResult(server);
            restartResult(server);
            statusResult(server, "MyPrinter");
            readConfigResult(server, "someParameter");
            setConfigResult(server, "someParameter", "NewValue");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void printResult(PrintServerInterface server, String filename, String printer) throws RemoteException {
        try {
            String output = server.print(filename, printer);
            System.out.println("Client: " + output);
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void queueResult(PrintServerInterface server, String printer) throws RemoteException {
        try {
            String output = server.queue(printer);
            System.out.println("Client: " + output);
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void topQueueResult(PrintServerInterface server, String printer, int job) throws RemoteException {
        try {
            String output = server.topQueue(printer, job);
            System.out.println("Client: " + output);
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void startResult(PrintServerInterface server) throws RemoteException {
        try {
            String output = server.start();
            System.out.println("Client: " + output);
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void stopResult(PrintServerInterface server) throws RemoteException {
        try {
            String output = server.stop();
            System.out.println("Client: " + output);
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void restartResult(PrintServerInterface server) throws RemoteException {
        try {
            String output = server.restart();
            System.out.println("Client: " + output);
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void statusResult(PrintServerInterface server, String printer) throws RemoteException {
        try {
            String output = server.status(printer);
            System.out.println("Client: " + output);
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void readConfigResult(PrintServerInterface server, String parameter) throws RemoteException {
        try {
            String output = server.readConfig(parameter);
            System.out.println("Client: " + output);
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void setConfigResult(PrintServerInterface server, String parameter, String value) throws RemoteException {
        try {
            String output = server.setConfig(parameter, value);
            System.out.println("Client: " + output);
        } catch (RemoteException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}


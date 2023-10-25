package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PrintServer {
    public static void main(String[] args) {
        try {
            // Create the server object
            PrintServerInterface server = new PrintServerImpl();

            // Create and start the RMI registry on port 1099
            Registry registry = LocateRegistry.createRegistry(1077);

            // Bind the server object to a name
            registry.rebind("PrintServer", server);

            System.out.println("Server is running...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

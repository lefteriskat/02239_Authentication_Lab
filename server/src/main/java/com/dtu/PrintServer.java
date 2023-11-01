package server.src.main.java.com.dtu;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class PrintServer {
    public static void main(String[] args) {
        try {
            String userDatabasePath = "C:\\Users\\Maciek\\Desktop\\DTU\\semester_3\\data_security\\assignement_2\\02239_Authentication_Lab\\users.db";

            // Create printers to be used for the server
            Printer printer1 = new Printer("MyPrinter1");
            Printer printer2 = new Printer("MyPrinter2");
            Map<String, Printer> printers = Map.of(printer1.getName(), printer1, printer2.getName(), printer2);
            
            // Create the server object with the printers
            PrintServerInterface server = new PrintServerImpl(userDatabasePath, printers);
            
            // Create and start the RMI registry on port 1077
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

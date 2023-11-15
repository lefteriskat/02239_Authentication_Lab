package com.dtu.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.dtu.myinterface.PrintServerInterface;
import com.dtu.server.loaders.UsersLoader;
import com.dtu.server.policies.AccessPolicy;
import com.dtu.server.policies.AccessPolicy.AccessPolicyOptions;

public class PrintServer {
    public static void main(String[] args) {
        
        try {
            // Create printers to be used for the server
            Printer printer1 = new Printer("MyPrinter1");
            Printer printer2 = new Printer("MyPrinter2");
            Map<String, Printer> printers = Map.of(printer1.getName(), printer1, printer2.getName(), printer2);
            
            UsersLoader.load();

            AccessPolicyOptions accessPolicy= AccessPolicy.AccessPolicyOptions.userBased;
            // Create the server object with the printers
            PrintServerInterface server = new PrintServerImpl(printers,accessPolicy);

           
            
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

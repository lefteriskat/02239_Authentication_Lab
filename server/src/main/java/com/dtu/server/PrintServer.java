package com.dtu.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.dtu.myinterface.PrintServerInterface;

public class PrintServer {
    public static void main(String[] args) {
        try {
            String url = "jdbc:mariadb://localhost:3306/data_security";
            String username = "root";
            String password = "...";

            try (Connection connection = DriverManager.getConnection(url, username, password);
                    Statement statement = connection.createStatement()) {

                System.out.println("Connection successfully established");
                
                // Create the "data_security" database
                statement.execute("CREATE DATABASE IF NOT EXISTS data_security");

                // Select the "data_security" database
                statement.execute("USE data_security");

                // Create the "users" table
                statement.execute("CREATE TABLE IF NOT EXISTS users (" +
                        "username VARCHAR(255) NOT NULL," +
                        "password VARCHAR(255) NOT NULL," +
                        "salt VARCHAR(255) NOT NULL," +
                        "PRIMARY KEY (username)" +
                        ")");

                // Create printers to be used for the server
                Printer printer1 = new Printer("MyPrinter1");
                Printer printer2 = new Printer("MyPrinter2");
                Map<String, Printer> printers = Map.of(printer1.getName(), printer1, printer2.getName(), printer2);
                
                // Create the server object with the printers
                PrintServerInterface server = new PrintServerImpl(connection, printers);
                
                // Create and start the RMI registry on port 1077
                Registry registry = LocateRegistry.createRegistry(1077);

                // Bind the server object to a name
                registry.rebind("PrintServer", server);

                System.out.println("Server is running...");
                
                

            }catch (SQLException e) {
                e.printStackTrace();
            }

            
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

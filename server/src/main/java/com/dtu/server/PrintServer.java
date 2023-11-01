package com.dtu.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
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
            String username = "lefteris";
            String password = "";

            try (Connection connection = DriverManager.getConnection(url, username, password);
                    Statement statement = connection.createStatement()) {

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

                System.out.println("OOOOOOOOOOk");

            } catch (SQLException e) {
                e.printStackTrace();
            }
            String userDatabasePath = "/home/lefteris/Documents/DTU_Courses/Autumn_2023/Data_Security/Assignment_2/02239_Authentication_Lab/users.db";

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

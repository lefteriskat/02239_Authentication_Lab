package com.dtu.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

import com.dtu.myinterface.PrintServerInterface;
import com.dtu.server.loaders.RolesLoader;
import com.dtu.server.loaders.UsersLoader;
import com.dtu.server.loaders.UsersToRolesLoader;
import com.dtu.server.policies.AccessPolicy;
import com.dtu.server.policies.RoleBasedPolicies;
import com.dtu.server.policies.UserBasedPolicies;
import com.dtu.server.policies.AccessPolicy.AccessPolicyOptions;

public class PrintServer {
    public static void main(String[] args) {
        
        try {
            // Create printers to be used for the server
            Printer printer1 = new Printer("MyPrinter1");
            Printer printer2 = new Printer("MyPrinter2");
            Map<String, Printer> printers = Map.of(printer1.getName(), printer1, printer2.getName(), printer2);
            
            /* 
            //load users operations permissions
            UsersLoader.load();
            
            //load roles and respective permissions
            RolesLoader.load();

            //load users and respective roles
            UsersToRolesLoader.load();*/

            //AccessPolicyOptions accessPolicy= AccessPolicy.AccessPolicyOptions.userBased;
            AccessPolicyOptions accessPolicy= AccessPolicy.AccessPolicyOptions.roleBased;

            PrintServerInterface server = new PrintServerImpl(printers,accessPolicy);
            // server.register("Alice", "pass1");
            // server.register("Bob", "pass2");
            // server.register("Cecilia", "pass3");
            // server.register("David", "pass4");
            // server.register("Erica", "pass5");
            // server.register("Fred", "pass6");
            // server.register("George", "pass7");

            UserBasedPolicies.startUserBasedPoliciesConnection();
            
            RoleBasedPolicies.startRoleBasedPoliciesConnection();
            
            // Create the server object with the printers
            
            
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

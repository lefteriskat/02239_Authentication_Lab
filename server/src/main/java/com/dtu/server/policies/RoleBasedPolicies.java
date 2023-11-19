package com.dtu.server.policies;

import java.util.Set;

import com.dtu.server.Operations;
import com.dtu.server.loaders.UsersLoader;

public class RoleBasedPolicies {
    /*Method CheckRolePermission: 
     * username: user's name 
     * op: operation 
     
        *Responsible to check if  role is the user is allowed to perform the operation taking into account his role
     */
    public static void CheckRolePermission(String username,Operations op)  {
        /*
         * code to get user's role
        */

        Set<Operations> roleOperations = UsersLoader.usersOperations.get(username);
        if(roleOperations == null || roleOperations.isEmpty()) {
            throw new IllegalArgumentException("User "+ username+ " does not have any operations assigned.");
        }
        if (!roleOperations.contains(op)) {
            throw new IllegalArgumentException("User "+ username+ " is not allowed to perform "+op+".");

        }
    }
    
}

package com.dtu.server.policies;

import com.dtu.server.Operations;
import com.dtu.server.loaders.UsersLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserBasedPolicies {
    /*Method CheckUserPermission: 
     * username: user's name 
     * op: operation 
     
        *Responsible to check if the user is allowed to perform the operation
     */
    public static void CheckUserPermission(String username,Operations op)  {
        Set<Operations> userOperations = UsersLoader.usersOperations.get(username);
        if(userOperations == null || userOperations.isEmpty()) {
            throw new IllegalArgumentException("User "+ username+ " does not have any operations assigned.");
        }
        if (!userOperations.contains(op)) {
            throw new IllegalArgumentException("User "+ username+ " is not allowed to perform "+op+".");

        }
    }


}

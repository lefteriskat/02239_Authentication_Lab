package com.dtu.server.loaders;

import com.dtu.server.Operations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UsersLoader {
    
    public static final Map<String, Set<Operations>> usersOperations = new HashMap<>();
    /*Method load:      
        *Responsible to load users and respective operations they are allowed to perform
    */
    public static void load(){
        /*SQL queries */
    }
    public static Set<Operations> getUserOperations(String username) {
        return usersOperations.get(username);
    }
}

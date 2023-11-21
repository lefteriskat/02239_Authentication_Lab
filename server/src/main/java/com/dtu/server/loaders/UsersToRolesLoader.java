package com.dtu.server.loaders;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.dtu.server.Role;

public class UsersToRolesLoader {
            public static final Map<String, Set<Role>> usersRoles = new HashMap<>();
    /*Method load:      
        *Responsible to load users' respective roles
    */
    public static void load(){
        /*SQL queries */
    }
    public static Set<Role> getUserRoles(String username) {
        return usersRoles.get(username);
    }
}

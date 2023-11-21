package com.dtu.server.loaders;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.dtu.server.Operations;
import com.dtu.server.Role;

public class RolesLoader {
        public static final Map<Role, Set<Operations>> rolesOperations = new HashMap<>();
    /*Method load:      
        *Responsible to load roles and respective operations they are allowed to perform
    */
    public static void load(){
        /*SQL Queries */
    }

    public static Set<Operations> getRoleOperations(Role r) {
        return rolesOperations.get(r);
    }
}

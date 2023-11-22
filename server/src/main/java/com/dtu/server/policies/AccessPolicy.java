package com.dtu.server.policies;

import java.sql.SQLException;

import com.dtu.server.Operations;

public class AccessPolicy{
    public enum AccessPolicyOptions {
        roleBased,
        userBased
    }

    private AccessPolicyOptions policy ;

    public AccessPolicy(AccessPolicyOptions p){
        policy=p;
    }
    public void checkPermission(String username, Operations op) throws IllegalArgumentException, SQLException{
        switch (policy) {
            case userBased:
                UserBasedPolicies.CheckUserPermission(username, op);
                break;
        
            case roleBased:
                // code to check Role Permission
                RoleBasedPolicies.CheckRolePermission(username, op);
                break;

            default:
                throw new IllegalArgumentException("Invalid access control policy.");
        }
    }
}


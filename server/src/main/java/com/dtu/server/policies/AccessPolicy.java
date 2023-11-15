package com.dtu.server.policies;

import com.dtu.server.Operations;
import com.dtu.server.policies.UserBasedPolicies;

public class AccessPolicy{
    public enum AccessPolicyOptions {
        roleBased,
        userBased
    }

    private AccessPolicyOptions policy ;

    public AccessPolicy(AccessPolicyOptions p){
        policy=p;
    }
    public void checkPermission(String username, Operations op){
        switch (policy) {
            case roleBased:
                UserBasedPolicies.CheckUserPermission(username, op);
                break;
        
            case userBased:
                // code to check Role Permission
                break;

            default:
                throw new IllegalArgumentException("Invalid access controll policy.");
        }
    }
}


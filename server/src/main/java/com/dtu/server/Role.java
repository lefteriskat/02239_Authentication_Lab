package com.dtu.server;
import java.util.Arrays;
import java.util.Optional;

//still need to assign the number to each role. The goal is to build an hierarquichal RBAC 
public enum Role {
    
    manager(1), //Alice is managing the print server, so she has the rights to perform all operations.

    /*Bob is the janitor who doubles as service technician,
    he has the rights to start, stop and restart the print
    server as well as inspect and modify the service parameters,
    i.e., invoke the status, readConfig and setConfig operations
    */
    server_technician(2), //status, readConfig and serConfig
    janitor(3), // start, stop and restart the print server

    /*Cecilia is a power user, who is allowed to print files and manage the print queue
    , i.e., use queue and topQueue as well as restart the print server when everything seems to be stuck.
    */
    power_user(4), // print files and manage the print queue ( i.e queue, topQueue and restart)

    /* Finally, David, Erica, Fred and George are ordinary users who are only allowed to print files 
    and display the print queue.
    */
    ordinary_users(5);

    Role(int value) {
        this.value = value;
    }

    private final int value;

    public int getValue() {
        return value;
    }

    public static Operations getFromValue(int value) {
        Optional<Operations> res = Arrays.stream(Operations.values()).filter(o -> o.getValue() == value).findFirst();
        return res.orElse(null);
    }
}

package com.dtu.server.policies;

import com.dtu.server.AuthenticationService.DBMS;
import com.dtu.server.Operations;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserBasedPolicies {

    
    public static Connection conn;
    /*Constructor to use MariaDB database connection*/
    public static void startUserBasedPoliciesConnection() throws SQLException {
        
        DBMS database = new DBMS();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(database.getUrl(), database.getUsername(), database.getPassword());

            System.out.println("Connection successfully established");
            
            /*Statements to create tables needed for UserBased Policies*/

        }catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        conn=connection;
    }

     //Responsible to check if the user is allowed to perform the operation
    public static void CheckUserPermission(String username,Operations op) throws SQLException  {
        /* 
        Set<Operations> userOperations = UsersLoader.usersOperations.get(username);
        if(userOperations == null || userOperations.isEmpty()) {
            throw new IllegalArgumentException("User "+ username+ " does not have any operations assigned.");
        }
        if (!userOperations.contains(op)) {
            throw new IllegalArgumentException("User "+ username+ " is not allowed to perform "+op+".");
        }
        */
        /*SQL queries to get Role permission */
        /*
         * Arguments: $USERNAME, $OPERATIONID

		SELECT COUNT(*)
		FROM UsersOperations UO
		WHERE UO.username = $USERNAME
		AND UO.operation_id = $OPERATIONID
		
		Return: 1 - if user has permission for this operation
			0 - if doesnt
         */

        // to be sure connection is open
        if(conn.isClosed()){
            DBMS database = new DBMS();
            conn=DriverManager.getConnection(database.getUrl(), database.getUsername(), database.getPassword());
        }

        String selectSql = "SELECT COUNT(*) as mycount FROM UsersOperations UO WHERE UO.username = ? and UO.operation_id = ?";
        
        try (PreparedStatement selectStatement = conn.prepareStatement(selectSql)) {
            int auth=1;
            selectStatement.setString(1, username);
            selectStatement.setInt(2, op.getValue());
            ResultSet rs = selectStatement.executeQuery();
            if (rs.next()) {
                    auth=rs.getInt("mycount");
            } 
            if(auth==0) throw new IllegalArgumentException("User "+ username+ " is not allowed to perform "+op+".");
            
        } catch (SQLException e) {
                throw new SQLException("Error when querying the db: " + e.getMessage());
        }

    }


}

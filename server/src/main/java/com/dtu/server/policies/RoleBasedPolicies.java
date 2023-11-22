package com.dtu.server.policies;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.dtu.server.AuthenticationService.DBMS;
import com.dtu.server.AuthenticationService.User;
import com.dtu.server.Operations;
import com.dtu.server.loaders.RolesLoader;
import com.dtu.server.loaders.UsersLoader;

public class RoleBasedPolicies {
    
    public static Connection conn;
    public static void startRoleBasedPoliciesConnection() throws SQLException {
        
        DBMS database = new DBMS();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(database.getUrl(), database.getUsername(), database.getPassword());

            System.out.println("Connection successfully established");
            
            /*Statements to create tables needed for RoleBased Policies*/

        }catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        conn=connection;
    }
    /*Method CheckRolePermission: 
     * username: user's name 
     * op: operation 
     
        *Responsible to check if  role is the user is allowed to perform the operation taking into account his role
     */
    public static void CheckRolePermission(String username,Operations op) throws SQLException  {
        /*
         * code to get user's role
        */
        /* 
        Set<Operations> roleOperations = RolesLoader.getRoleOperations();
        if(roleOperations == null || roleOperations.isEmpty()) {
            throw new IllegalArgumentException("User "+ username+ " does not have any operations assigned.");
        }
        if (!roleOperations.contains(op)) {
            throw new IllegalArgumentException("User "+ username+ " is not allowed to perform "+op+".");
        }*/
        /*SQL queries to get Role permission */

        // to be sure connection is open
        if(conn.isClosed()){
            DBMS database = new DBMS();
            conn=DriverManager.getConnection(database.getUrl(), database.getUsername(), database.getPassword());
        }

        String selectSqlRoles = "SELECT UR.role_id FROM UsersRoles UR WHERE UR.username = ?";
        

        try (PreparedStatement selectStatement = conn.prepareStatement(selectSqlRoles)) {

            selectStatement.setString(1, username);
            ResultSet rs = selectStatement.executeQuery();

            
            while (rs.next()) { // will traverse through all rows
                /*code to get all the Roles assigned to the username */
            }
            
        } catch (SQLException e) {
                System.out.println("Error saving user database to file: " + e.getMessage());
        }
        /*A: get roles of the user

			Arguments: $USERNAME

			SELECT UR.role_id
			FROM UsersRoles UR
			WHERE UR.username = $USERNAME

			Return: $ROLEIDS (list of all roles assigned to user)
	

	
		B: get all parent roles of all roles
		# It has to be done in a loop for inheritance of higher level than one

			Arguments: $ROLEIDS

			SELECT RP.parent_role_id
			FROM RolesParents RP
			WHERE RP.parent_role_id in $ROLEIDS 
	
			Return: $PARENTROLEIDS



		C: get all operations for all roles and all parent roles
		
			Arguments: $ROLEIDS, $PARENTROLEIDS, $OPERATIONID
	
			SELECT COUNT(*)
			FROM RolesOperations RO
			WHERE (
				RO.role_id in $PARENTROLEIDS
				OR RO.role_id in $ROLEIDS
			) AND RO.operation_id = $OPERATIONID
	
			Return: 0 - if user does not have permission
				>0 - if user has permission */
    }
    
}

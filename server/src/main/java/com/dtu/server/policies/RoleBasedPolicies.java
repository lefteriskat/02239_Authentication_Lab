package com.dtu.server.policies;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
        // to be sure connection is open
        if(conn.isClosed()){
            DBMS database = new DBMS();
            conn=DriverManager.getConnection(database.getUrl(), database.getUsername(), database.getPassword());
        }

        String selectSqlRoles = "SELECT UR.role_id FROM UsersRoles UR WHERE UR.username = ?";
        
        /*A: get roles of the user

			Arguments: $USERNAME

			SELECT UR.role_id
			FROM UsersRoles UR
			WHERE UR.username = $USERNAME

			Return: $ROLEIDS (list of all roles assigned to user) */
        List<Integer> roleIds = new ArrayList<>();
        try (PreparedStatement selectStatement = conn.prepareStatement(selectSqlRoles)) {

            selectStatement.setString(1, username);
            ResultSet rs = selectStatement.executeQuery();
            
            
            while (rs.next()) { // will traverse through all rows
                /*code to get all the Roles assigned to the username */
                int role = rs.getInt("role_id");
                roleIds.add(role);
            }
        } catch (SQLException e) {
                System.out.println("1: Error retrieving data from database " + e.getMessage());
        }
        
        /*
		B: get all parent roles of all roles
		# It has to be done in a loop for inheritance of higher level than one

			Arguments: $ROLEIDS

			SELECT RP.parent_role_id
			FROM RolesParents RP
			WHERE RP.parent_role_id in $ROLEIDS 
	
			Return: $PARENTROLEIDS
        */
        // to be sure connection is open
        if(conn.isClosed()){
            DBMS database = new DBMS();
            conn=DriverManager.getConnection(database.getUrl(), database.getUsername(), database.getPassword());
        }
        
        String selectSqlParents = "SELECT RP.role_id FROM RolesParents RP WHERE RP.parent_role_id = ?" ;

        for (int i = 0; i < roleIds.size(); i++) {
            try (PreparedStatement selectStatement = conn.prepareStatement(selectSqlParents)) {

                selectStatement.setInt(1, roleIds.get(i)); //role index i 
                ResultSet rs = selectStatement.executeQuery();
                
                while (rs.next()) { // will traverse through all rows
                    /*code to get all the Roles assigned to the username */
                    int role = rs.getInt("role_id"); //not sure about this 

                    //check if this role is already in the list
                    if (!roleIds.contains(role)){
                        roleIds.add(role);
                    }
                }
            } catch (SQLException e) {
                    System.out.println("2: Error retrieving data from database " + e.getMessage());
            }
        }

        /*
		C: get all operations for all roles and all parent roles
		
			Arguments: $ROLEIDS, $PARENTROLEIDS, $OPERATIONID
	
			SELECT COUNT(*)
			FROM RolesOperations RO
			WHERE (
				RO.role_id in $PARENTROLEIDS
				OR RO.role_id in $ROLEIDS
			) AND RO.operation_id = $OPERATIONID
	
			Return: 0 - if user does not have permission
				>0 - if user has permission 
        */

        

        for (int i = 0; i < roleIds.size(); i++) {
            String selectSqlOperation = "SELECT COUNT(*) FROM RolesOperations RO WHERE RO.role_id = ? AND RO.operation_id = ?" ;
            try (PreparedStatement selectStatement = conn.prepareStatement(selectSqlOperation)) {

                selectStatement.setInt(1, roleIds.get(i)); //role index i
                selectStatement.setInt(2, op.getValue()); //operation
                ResultSet rs = selectStatement.executeQuery();
                
                while (rs.next()) { // will traverse through all rows
                    /*code to get all the Roles assigned to the username */
                    int permission = rs.getInt("Count(*)"); //not sure about this 
                    if(permission>0){
                        return;
                    }
                }
            } catch (SQLException e) {
                    System.out.println("3: Error retrieving data from database " + e.getMessage());
            }
        }

        //if it arrives here is because the permission is not allowed, so we should return an error
        throw new IllegalArgumentException("User "+ username+ " is not allowed to perform "+op+".");

    }
    
}

-- Create the database
DROP DATABASE
IF EXISTS RBAC;

-- Create the database
CREATE DATABASE RBAC;

-- Use the newly created database
USE RBAC;

-- Create Users table
CREATE TABLE Users (
   username VARCHAR(50) PRIMARY KEY,
   hashed_password VARCHAR(255) NOT NULL,
   salt VARCHAR(255) NOT NULL
);

-- Create Roles table
CREATE TABLE Roles (
    role_id INT PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL
);

-- Create Operations table
CREATE TABLE Operations (
    operation_id INT PRIMARY KEY,
    operation_name VARCHAR(255) NOT NULL
);

CREATE TABLE UsersOperations (
    user_operation_id INT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    operation_id INT NOT NULL,
    FOREIGN KEY (username) REFERENCES Users(username),
    FOREIGN KEY (operation_id) REFERENCES Operations(operation_id)
);

-- Create Users-Roles relationship table
CREATE TABLE UsersRoles (
    userrole_id INT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY (username) REFERENCES Users(username),
    FOREIGN KEY (role_id) REFERENCES Roles(role_id)
);

-- Create Roles-Operations relationship table
CREATE TABLE RolesOperations (
    role_operation_id INT PRIMARY KEY,
    role_id INT NOT NULL,
    operation_id INT NOT NULL,
    FOREIGN KEY (role_id ) REFERENCES Roles(role_id),
    FOREIGN KEY (operation_id) REFERENCES Operations(operation_id)
);

-- Create Roles-Parents relationship table
CREATE TABLE RolesParents (
    roles_parent_id INT PRIMARY KEY,
    role_id INT NOT NULL,
    parent_role_id INT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES Roles(role_id),
    FOREIGN KEY (parent_role_id) REFERENCES Roles(role_id)
);

-- Use the RBAC database
USE RBAC;

-- Insert dummy data into Users table
INSERT INTO Users (username, hashed_password, salt) VALUES
('Alice', 'Z0jqZ2Wl42sMhf4fFieXKFpyeXsFHqJLBUyfTyt95Lo', 'IfZ1Y65X0pwOuCB/DDQqeQ=='),
('Bob', 'eSEXPWSE4kccEO7cW0r/FfQnpPNioT2p/POW0XVY9nA=', 'voZ4VvAXZOdteDWJG0vW7A=='),
('Cecilia', 'Z/e38s6kHvn601z716ZSfwEXSTH7vGlymQuTjVBaKZc=', 'I62zNaZMBIno9obCTrOEOg=='),
('David', 'oXs67LylC+yrwWNBYNvP48jYFHq6H0P9YFdQH+vnFa8=', 'FOrloIn6X6rOn99VhVMcXA=='),
('Erica', '/qkAB4ykH2qwX8Q9j1XcU10kSR0ZMRRAe3rgh8OPh1g=', 'LkjFlWlgTuhQIB6Z7M7oWg=='),
('Fred', 'A3UHRX5tHwtkrRzQcgN6MLaU8r74x7jNQ1w/4VQwsjM=', 'qV3dlvt8+/KmZHT0FIuNGw=='),
('George', 'HiV577gXqJn1waGYG8juixA6IiiGpcGtbdVNTEegT/8=', '1tfugvskq86+tDyr5iNhXA==');

-- Insert dummy data into Roles table
INSERT INTO Roles (role_id, role_name) VALUES
(1, 'manager'),
(2, 'service_technician'),
(3, 'janitor'),
(4, 'power_user'),
(5, 'ordinary_user');

-- Insert dummy data into Operations table
INSERT INTO Operations (operation_id, operation_name) VALUES
(1, 'print'),
(2, 'queue'),
(3, 'topQueue'),
(4, 'start'),
(5, 'stop'),
(6, 'restart'),
(7, 'status'),
(8, 'readConfig'),
(9, 'setConfig');

-- Insert dummy data into UsersRoles table
INSERT INTO UsersRoles (userrole_id, username, role_id) VALUES
(1, 'Alice', 1),
(2, 'Bob', 2),
(3, 'Bob', 3),
(4, 'Cecilia', 4),
(5, 'David', 5),
(6, 'Erica', 5),
(7, 'Fred', 5),
(8, 'George', 5);

INSERT INTO UsersOperations (user_operation_id, username, operation_id) VALUES
(1, 'Alice', 1),
(2, 'Alice', 2),
(3, 'Alice', 3),
(4, 'Alice', 4),
(5, 'Alice', 5),
(6, 'Alice', 6),
(7, 'Alice', 7),
(8, 'Alice', 8),
(9, 'Alice', 9),
(10, 'Bob', 1),
(11, 'Bob', 2),
(12, 'Bob', 3),
(13, 'Bob', 4),
(14, 'Bob', 5),
(15, 'Bob', 6),
(16, 'Cecilia', 1),
(17, 'Cecilia', 2),
(18, 'Cecilia', 3),
(19, 'Fred', 1),
(20, 'Fred', 2),
(21, 'George', 1),
(22, 'George', 2),
(23, 'David', 1),
(24, 'David', 2),
(25, 'Erica', 1),
(26, 'Erica', 2)
;

-- Insert dummy data into RolesOperations table
INSERT INTO RolesOperations (role_operation_id, role_id, operation_id) VALUES
( 1, 5, 1),
( 2, 5, 2),
( 3, 4, 3),
( 4, 4, 6),
( 5, 3, 4),
( 6, 3, 5),
( 7, 3, 6),
( 8, 2, 7),
( 9, 2, 8),
(10, 2, 9);

-- Insert dummy data into RolesParents table
INSERT INTO RolesParents (roles_parent_id, role_id, parent_role_id) VALUES
(1, 2, 1),
(2, 3, 1),
(3, 4, 1),
(4, 5, 2),
(5, 5, 3),
(6, 5, 4);

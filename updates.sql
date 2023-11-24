USE RBAC;


-- For ACL

INSERT INTO Users (username, hashed_password, salt) VALUES
('Henry', 'Z0jqZ2Wl42sMhf4fFieXKFpyeXsFHqJLBUyfTyt95Lo', 'IfZ1Y65X0pwOuCB/DDQqeQ=='),
('Ida', 'eSEXPWSE4kccEO7cW0r/FfQnpPNioT2p/POW0XVY9nA=', 'voZ4VvAXZOdteDWJG0vW7A==');

DELETE FROM Users WHERE username = 'Bob';

DELETE FROM UsersOperations WHERE username = 'George';

INSERT INTO UsersOperations (user_operation_id, username, operation_id) VALUES
(27, 'George', 7),
(28, 'George', 8),
(29, 'George', 9),
(30, 'Henry', 1),
(31, 'Henry', 2),
(32, 'Ida', 1),
(33, 'Ida', 2),
(34, 'Ida',3)
;

INSERT INTO UsersRoles (userrole_id, username, role_id) VALUES
(9, 'George', 2),
(10, 'Henry', 5),
(11, 'Ida', 4);
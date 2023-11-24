# 02239 Authentication and Access Control Lab

## Command line Usage Instructions
Before running the application you have to install MariaDB and run  the `RBAC.sql` file to create the database used by the application.
Then if you want to perform the updates described in the description of the Access Control Lab you have to run the file `updates.sql`.

In order to compile the java files run the following from the root folder of the repo:
* `mvn clean install`

This will create a bin folder with all the compiled .class files.

Then in order to run the server and client apps you have to open two terminals and run the following:
* Server: `java -jar server/target/server-1.0-SNAPSHOT-jar-with-dependencies.jar`
* Client: `java -jar client/target/client-1.0-SNAPSHOT-jar-with-dependencies.jar`

## Dummy data
For the purpose of testing `users.db` file containing three registered users has been provided. Remember about setting the right path to the file before running the server.
| username | password |
|----------|----------|
| lefteris | password123 |
| maciek | passw0rd |
| admin | admin |

In order to generate a new `users.db` file consider function `saveUsersToFile()` in `AuthenticationService`.

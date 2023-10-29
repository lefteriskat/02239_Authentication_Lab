# 02239_Authentication_Lab

## Command line Usage Instructions
In order to compile the java files run the following from the root folder of the repo:
* `javac -d bin src/server/*.java src/server/auth/*.java src/client/*.java`

This will create a bin folder with all the compiled .class files.

Then in order to run the server and client apps you have to open two terminals and run the following:
* Server: `java -cp bin server.PrintServer`
* Client: `java -cp bin client.PrintClient`

## Dummy data
For the purpose of testing `users.db` file containing three registered users has been provided. Remember about setting the right path to the file before running the server.
| username | password |
|----------|----------|
| lefteris | password123 |
| maciek | passw0rd |
| admin | admin |

In order to generate a new `users.db` file consider function `saveUsersToFile()` in `AuthenticationService`.

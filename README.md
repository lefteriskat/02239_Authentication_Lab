# 02239_Authentication_Lab

## Command line Usage Instructions
In order to compile the java files run the following from the root folder of the repo:
* `javac -d bin src/server/*.java src/server/auth/*.java src/client/*.java`

This will create a bin folder with all the compiled .class files.

Then in order to run the server and client apps you have to open two terminals and run the following:
* Server: `java -cp bin server.PrintServer`
* Client: `java -cp bin client.PrintClient`
package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class PrintServerImpl extends UnicastRemoteObject implements PrintServerInterface {
    private Map<String, Printer> printers;
    private boolean isPrintServerRunning;
    private Map<String, String> configParams;

    public PrintServerImpl() throws RemoteException {
        isPrintServerRunning = false;
        configParams = new HashMap<>();
        printers = new HashMap<>();
    }

    @Override
    public String print(String filename, String printer) throws RemoteException {
        //here we have to add authentication things
        String output = "File: " + filename + "printed at Printer: " + printer + "!";
        System.out.println("Server: "+output);
        return output;
    }

    @Override
    public String queue(String printer) throws RemoteException {
        Printer target = printers.get(printer);
        String output;
        if ( target != null) {
            output = printer+ "'s queue is: " + target.getPrintQueue().toString();
        }
        else {
            output = "No printer with name: " + printer + " !";
        }
        System.out.println("Server: "+output);
        return output;  
    }

    @Override
    public String topQueue(String printer, int job) throws RemoteException {
        Printer target = printers.get(printer);
        String output;
        if ( target != null) {
            output = target.topQueue(job);
        }
        else {
            output = "No printer with name: " + printer + " !";
        }
        System.out.println("Server: " + output);
        return output;  
    }

    @Override
    public String start() throws RemoteException {
        isPrintServerRunning = true;
        String output = "Print Server is running!";
        System.out.println("Server: " + output);
        return output;
    }

    @Override
    public String stop() throws RemoteException {
        isPrintServerRunning = false;
        String output = "Print Server is stopped!";
        System.out.println("Server: " + output);
        return output;
    }

    @Override
    public String restart() throws RemoteException {
        stop();
        for (Printer printer : printers.values()) {
            printer.clearQueue();
        }
        start();
        String output = "Print Server is restarted!";
        System.out.println("Server: " + output);
        return output;
    }

    @Override
    public String status(String printer) throws RemoteException {
        Printer target = printers.get(printer);
        String output;
        if ( target != null) {
            output = "Printer " + printer + "Status:" + target.getStatus();
        }
        else {
            output = "No printer with name: " + printer + " !";
        }
        System.out.println("Server: " + output);
        return output;
    }

    @Override
    public String readConfig(String parameter) throws RemoteException {
        // Add code to retrieve and return the value of the specified parameter.
        String output = "Parameter <" + parameter + "> not found.";
        if (configParams.containsKey(parameter)) {
            output = "Parameter's <" + parameter + "> value: " + configParams.get(parameter);
        }
        System.out.println("Server: " + output);
        return output;
    }

    @Override
    public String setConfig(String parameter, String value) throws RemoteException {
        // Add code to set the parameter on the print server to the specified value.
        configParams.put(parameter, value);
        String output = "Parameter <" + parameter + "> set with value: <"+ value +">.";
        System.out.println("Server: " + output);
        return output;
    }
}
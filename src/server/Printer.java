package server;

import java.util.LinkedList;

public class Printer {
    private String name;
    private String status;
    private LinkedList<String> printQueue;

    public Printer(String name) {
        this.name = name;
        this.status = "OFF";
        this.printQueue = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addToQueue(String filename) {
        printQueue.add(filename);
    }

    public LinkedList<String> getPrintQueue() {
        return printQueue;
    }

    public void clearQueue() {
        printQueue.clear();
    }

    public String topQueue(int job) {
        if (job >= 1 && job <= printQueue.size()) {
            // Ensure that the job number is within a valid range.

            String selectedJobFileName = printQueue.get(job - 1); // Adjust for 0-based index.
            printQueue.remove(job - 1);
            printQueue.addFirst(selectedJobFileName);

            return "Job number " + job + "with filename:" + selectedJobFileName + "moved to the top of the queue.";
        } else {
            return "Invalid job number.";
        }
    }

}

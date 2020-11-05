package Assignment2;

import java.util.ArrayList;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

public class Assignment2 {

    public static void main(String[] args) throws InterruptedException {
        if(args[0] == null || args[1] == null){
            System.out.println("Failed to parse command line arguments. usage: Assignment2 <customers> <capacity>");
        }

        int capacity = Integer.parseInt(args[0]);
        //default value;
        int customers = Integer.parseInt(args[1]);

        System.out.println("Main Thread");
        ArrayList<Person> personArrayList = new ArrayList<>(customers);

        //let customers get in: their array index is their ticket no.
        for(int i = 0; i < customers; i++){
            Person p = new Person(i);
            personArrayList.add(p);
        }

        Server server = new Server(capacity);

        int count = 0;
        while(count < customers) {
            try{
                System.out.println("Adding customer " + count + " to small room");
                server.executeTask(personArrayList.get(count));
            }catch (RejectedExecutionException e){
                System.out.println("Failed to add customer " + count);
                //waiting for the customer queue to be free'd
                //the program will try to push a new customer until no exception is thrown
                //could be implemented with explicit locks
                Thread.sleep(100);
                continue;
            }
            count++;
        }
        //server terminates when there are no customers left
        server.endServer();
    }
}

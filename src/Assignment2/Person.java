package Assignment2;//the task "person"


import java.util.concurrent.TimeUnit;

public class Person implements Runnable {
    private int ticket;
    public Person(int i){
        this.ticket = i;
    }
    @Override
    //launched by server.
    public void run() {
        System.out.println("I'm n. " + ticket + "in queue");
        try {
            //every person waits for a random time to end and leave the small room.
            double time = Math.random();
            Thread.sleep((long)(1000L*time));
        } catch (InterruptedException e) {
            System.out.println("I've been interrupted.");
        }
    }
}

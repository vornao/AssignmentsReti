package Assignment2;

import java.util.ArrayList;
import java.util.concurrent.RejectedExecutionException;


/**
 * Simulare il flusso di clienti in un ufficio postale che ha 4 sportelli. Nell'ufficio esiste:
 * un'ampia sala d'attesa in cui ogni persona può entrare liberamente. Quando entra,
 * ogni persona prende il numero dalla numeratrice e aspetta il proprio turno in questa sala.
 * una seconda sala, meno ampia, posta davanti agli sportelli, in cui possono essere presenti al massimo k persone
 * Una persona si mette quindi prima in coda nella prima sala, poi passa nella seconda sala.
 * Ogni persona impiega un tempo differente per la propria operazione allo sportello.
 * Una volta terminata l'operazione, la persona esce dall'ufficio
 *
 * Scrivere un programma in cui:
 * - l'ufficio viene modellato come una classe JAVA, in cui viene attivato un ThreadPool di dimensione uguale al numero degli sportelli
 * - la coda delle persone presenti nella sala d'attesa è gestita esplicitamente dal programma
 * - la seconda coda (davanti agli sportelli) è quella gestita implicitamente dal ThreadPool
 * - ogni persona viene modellata come un task, un task che deve essere assegnato ad uno dei thread associati agli sportelli
 * - si preveda di far entrare tutte le persone nell'ufficio postale, all'inizio del programma
 */

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

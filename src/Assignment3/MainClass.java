package Assignment3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Il laboratorio di Informatica del Polo Marzotto è utilizzato da tre tipi di utenti, studenti, tesisti e professori
 * ed ogni utente deve fare una richiesta al tutor per accedere al laboratorio.
 * I computers del laboratorio sono numerati da 1 a 20. Le richieste di accesso sono diverse
 * a seconda del tipo dell'utente:
 *
 * i professori accedono in modo esclusivo a tutto il laboratorio, poichè hanno necessità di utilizzare
 * tutti i computers per effettuare prove in rete.
 * i tesisti richiedono l'uso esclusivo di un solo computer, identificato dall'indice i,
 * poichè su quel computer è istallato un particolare software necessario per lo sviluppo della tesi.
 * gli studenti richiedono l'uso esclusivo di un qualsiasi computer.
 * I professori hanno priorità su tutti nell'accesso al laboratorio, i tesisti hanno priorità sugli studenti.
 *
 * Nessuno può essere interrotto mentre sta usando un computer.
 * Scrivere un programma JAVA che simuli il comportamento degli utenti e del tutor.
 * Il programma riceve in ingresso il numero di studenti, tesisti e professori che utilizzano il laboratorio ed attiva
 * un thread per ogni utente.
 * Ogni utente accede k volte al laboratorio, con k generato casualmente.
 * Simulare l'intervallo di tempo che intercorre tra un accesso ed il successivo e l'intervallo di
 * permanenza in laboratorio mediante il metodo sleep.
 * Il tutor deve coordinare gli accessi al laboratorio. Il programma deve terminare quando tutti gli utenti hanno
 * completato i loro accessi al laboratorio.
 */

public class MainClass {
    final static int labCapacity = 20;
    final static int labUsers = 50;
    final static int maxAccessesPerUser = 10;
    private static Random rand = new Random();

    public static void main(String[] args) throws InterruptedException {

        ComputerLab lab = new ComputerLab(labCapacity);
        ArrayList<User> userArray = new ArrayList<>(labUsers);
        Tutor tutor = new Tutor(labCapacity, lab);

        //number of accesses per user;
        int k;
        //user type (default = student)
        int level = User.STUDENT;
        //pc requested by PhDs
        int pc = 0;

        for(int i = 0; i < 50; i++){
            //getting random level for user i
            level = 1 + rand.nextInt(User.PROFESSOR);

            //getting random accesses for user i [1; maxAccessesPerUser]
            k = 1 + rand.nextInt(maxAccessesPerUser);

            //if user == PHD then request for a specific pc
            if(level == User.PHD){
                pc = rand.nextInt(labCapacity);
            }

            //create user with new params
            User u = new User(i, level, pc, lab);

            //dd user to array k times
            for(int j = 0; j < k; j++){
                userArray.add(u);
            }
        }

        //shuffle request array to grant a random order.
        Collections.shuffle(userArray);

        //submitting request to tutor;
        for(User u : userArray){
            tutor.requestAccess(u);
        }

        //tutor starts
        tutor.start();
    }
}

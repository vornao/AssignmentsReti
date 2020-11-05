package Assignment4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Risolvere il problema della simulazione del Laboratorio di informatica,
 * assegnato nella lezione precedente, utilizzando il costrutto di Monitor
 */

public class ComputerLab {
    final static int labCapacity = 20;
    final static int labUsers = 50;
    final static int maxAccessesPerUser = 3;
    private static Random rand = new Random();
    private static Boolean[] computerArray = new Boolean[labCapacity];


    public static void main(String[] args) throws InterruptedException {

        MonitorComputerLab lab = new MonitorComputerLab(labCapacity);
        ArrayList<User> userArray = new ArrayList<>(labUsers);
        Tutor tutor = new Tutor(labCapacity, lab);


        //number of accesses per user;
        int k;
        //user type (default = student)
        int level = User.STUDENT;
        //pc requested by PhDs
        int pc = 0;

        for(int i = 0; i < 25; i++){
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

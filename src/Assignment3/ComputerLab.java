package Assignment3;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ComputerLab {

    //array of booleans: true if computer i is being used, false o/w
    private Boolean[] computerArray;

    //lock and conditions to grant mutual exclusion to computerArray
    private Lock lock;
    private Condition cond_set;
    private Condition cond_reset;

    //no. of computer currently in use
    private int count;

    public ComputerLab(int c){
        computerArray = new Boolean[c];
        Arrays.fill(computerArray, false);

        lock = new ReentrantLock();
        cond_set = lock.newCondition();
        cond_reset = lock.newCondition();
    }

    //set a specified computer in use -> used by PhD students in the simulation
    public void setIndex(int i) throws InterruptedException {
        lock.lock();
        while(count == computerArray.length && computerArray[i]){
            cond_reset.await();
        }
        computerArray[i] = true;
        count++;
        cond_set.signal();
        lock.unlock();
        //System.out.println("Set Index pc " + i);
    }

    //set all PCs in use -> used by Teachers
    public void setAll() throws InterruptedException {
        lock.lock();
        while(count > 0){
            cond_reset.await();
        }
        for(int i = 0; i < computerArray.length; i++){
            count++;
            computerArray[i] = true;
        }
        cond_set.signal();
        lock.unlock();
        //System.out.println("Set All");
    }

    //set the first free pc found in use -> used by generic user
    public int set() throws InterruptedException {
        lock.lock();
        int assigned = 0;
        while(count == computerArray.length){
            cond_reset.await();
        }

        //for loop checking for the first free pc;
        for(int i = 0; i < computerArray.length; i++){
            if(!computerArray[i]) {
                computerArray[i] = true;
                assigned = i;
                break;
            }
        }
        count++;
        cond_set.signal();
        lock.unlock();
        //System.out.println("Set pc " + assigned);

        //returns assigned pc
        return assigned;
    }

    //reset a specified pc to free
    public void reset(int i) throws InterruptedException {
        lock.lock();
        while(count == 0 && !computerArray[i]){
            cond_set.await();
        }
        computerArray[i] = false;
        count--;
        cond_reset.signal();
        lock.unlock();
        //System.out.println("Reset pc " + i);
    }

    //reset all PCs
    public void resetAll() throws InterruptedException {
        lock.lock();
        while(count < computerArray.length){
            cond_set.await();
        }
        for(int i = 0; i < computerArray.length; i++){
            count--;
            computerArray[i] = true;
        }
        cond_reset.signal();
        lock.unlock();
        //System.out.println("Reset All");
    }
}

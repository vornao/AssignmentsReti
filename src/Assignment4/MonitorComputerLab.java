package Assignment4;
import java.util.Arrays;

public class MonitorComputerLab {

    //array of booleans: true if computer i is being used, false o/w

    //lock and conditions to grant mutual exclusion to computerArray

    //no. of computer currently in use
    private int count;
    private final Boolean[] array;
    public MonitorComputerLab(int c){
        array = new Boolean[c];
        Arrays.fill(array, false);
    }

    //set a specified computer in use -> used by PhD students in the simulation
    public synchronized void setIndex(int i) throws InterruptedException {
            while (array[i]) {
                this.wait();
            }
            array[i] = true;
            count++;
            this.notify();
            //System.out.println("Set Index pc " + i);
        }

    //set all PCs in use -> used by Teachers
    public synchronized void setAll() throws InterruptedException {
            while (count > 0) {
                this.wait();
            }
            for (int i = 0; i < array.length; i++) {
                count++;
                array[i] = true;
            }
            this.notify();
            //System.out.println("Set All");
        }

    //set the first free pc found in use -> used by generic user
    public synchronized int set() throws InterruptedException {
        int assigned = 0;
            while (count == array.length) {
                this.wait();
            }
            //for loop checking for the first free pc;
            for (int i = 0; i < array.length; i++) {
                if (!array[i]) {
                    array[i] = true;
                    assigned = i;
                    break;
                }
            }
            count++;
            this.notify();
            //System.out.println("Set pc " + assigned);

            //returns assigned pc
            return assigned;
        }

    //reset a specified pc to free
    public synchronized void reset(int i) throws InterruptedException {
            while (count == 0 && !array[i]) {
                this.wait();
            }
            array[i] = false;
            count--;
            this.notify();
            //System.out.println("Reset pc " + i);
        }

    //reset all PCs
    public synchronized void resetAll() throws InterruptedException {
            while (count < array.length) {
                this.wait();
            }
            for (int i = 0; i < array.length; i++) {
                count--;
                array[i] = false;
            }
            this.notify();
            //System.out.println("Reset All");
        }
    }


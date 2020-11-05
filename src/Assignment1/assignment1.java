package Assignment1;
/*
    Luca Miglior - Matricola 580671
    Reti di calcolatori e laboratorio: Assignment 1 -- A.A 2020/2021
    Pi Calculator using Java Threads and Gregory Leibniz Series.
    input format: accuracy (e.g. 0.0001) timeout (in seconds)
 */

public class assignment1 {
    private static final int millis_to_sec = 1000;
    static double k;
    static PiCalc piCalc;

    public static void main(String[] args) throws InterruptedException {
        if(args.length == 0){
            System.out.println("Invalid arguments - Usage: java assignment1 [accuracy] [timeout]");
            return;
        }
        System.out.println("Reti di calcolatori e Laboratorio - Assignment 1\nGregory - Leibniz Series Pi Calculator\n");

        k = Double.parseDouble(args[0]);
        piCalc = new PiCalc(k);
        Thread piCalcThread = new Thread(piCalc);
        piCalcThread.start();
        //wait for the piCalcThread to terminate within the proper time
        piCalcThread.join(millis_to_sec*Integer.parseInt(args[1]));
        //o/w the thread will be interrupted and will print the result.
        piCalcThread.interrupt();
    }
}

//using runnable interface to implement PiCalc private class
class PiCalc implements Runnable{
    private double k;
    private double pi = 0;
    private int count = 0;

    //constructor m
    //method: sets accuracy
    public PiCalc(Double acc){
        this.k = acc;
    }
    public void run(){
        System.out.println("Calculating pi to given accuracy... "+"("+k+")");

        //every iteration of the while loop checks if the thread has been interrupted
        while(Math.abs(Math.PI - 4 * pi) > k && !Thread.currentThread().isInterrupted()){
            pi += Math.pow(-1, count)/(2*count+1);
            count++;
        }
        //checking while-loop exit condition.
        if(Thread.currentThread()
                .isInterrupted()){
            System.out.println("Time elapsed - Thread Interrupted");
        }else{
            System.out.println("Accuracy reached!");
        }
        /*print output -> run is a void method, could not return a value to main thread.
        *a shared data structure could be used, or class PiCalc could have been implemented using "Callable" interface
        *in order to return a Future<Double> value to the main thread.
        */
        System.out.println("Computed pi value is: " + 4*pi);
    }
}


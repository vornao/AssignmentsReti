package Assignment3;

import java.util.Random;

public class User implements Runnable, Comparable<User>{
    static final int STUDENT = 1;
    static final int PHD = 2;
    static final int PROFESSOR = 3;
    private int maxWaitingTime = 1000;
    private int level;
    private int id;
    private int pc;
    private ComputerLab lab;
    private Random rand = new Random();

    public User(int id, int level, int pc, ComputerLab lab){
        this.id = id;
        this.level = level;
        this.lab = lab;
        this.pc = pc;
    }

    public void run(){
        //long time1 = System.currentTimeMillis();
        System.out.println("User level: " + this.getLevel() + "\tID: " + this.id);
        try {
            Thread.sleep(rand.nextInt(maxWaitingTime));
        } catch (InterruptedException ignored) {
            ;
            //cannot be interrupted
        }
        try {
            if(this.level == 3){
                lab.resetAll();
            }else{
                lab.reset(this.pc);
            }
        } catch (InterruptedException ignored) {
            ;
        }
        //long time2=System.currentTimeMillis();
    }

    public int getLevel(){
        return this.level;
    }

    public void assign(int i){
        this.pc = i;
    }

    //return requested or assigned pc
    public int getIndex(){
        return this.pc;
    }

    @Override
    public int compareTo(User u) {
        return  u.getLevel() - this.level;
    }
}

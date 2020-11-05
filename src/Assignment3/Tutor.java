package Assignment3;

import java.util.PriorityQueue;


public class Tutor {
    private PriorityQueue<User> accessQueue;
    private ComputerLab computerLab;

    public Tutor(int capacity, ComputerLab lab){
        //using Java Priority Queue to order users
        accessQueue = new PriorityQueue<>(capacity);
        //setting lab tutor has to manage.
        computerLab = lab;
    }

    public void requestAccess(User u){
        accessQueue.add(u);
    }

    public void start() throws InterruptedException {
        User u;

        //while there are users to be serverd:
        while(accessQueue.size() > 0){
            u = accessQueue.remove();
            switch(u.getLevel()) {
                    //user is a Professor
                case User.PROFESSOR:
                    //he needs all pc to finish his work, access is strictly sequential
                    computerLab.setAll();
                    startUserThread(u);
                    break;
                    //User is PhD student, request for a specific pc
                case User.PHD:
                    computerLab.setIndex(u.getIndex());
                    startUserThread(u);
                    break;
                    //none of these, normal user (student), assigning first free machine
                default:
                    u.assign(computerLab.set());
                    startUserThread(u);
                    break;
            }
        }
    }

    private void startUserThread(User u){
        Thread userThread = new Thread(u);
        userThread.start();
    }
}

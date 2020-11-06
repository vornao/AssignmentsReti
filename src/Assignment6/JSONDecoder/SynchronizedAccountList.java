package Assignment6.JSONDecoder;

import Assignment6.Account;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

public class SynchronizedAccountList {
    private LinkedList<Account> directories;
    private AtomicBoolean readCompleted;
    private int size = 0;

    public SynchronizedAccountList(){
        readCompleted = new AtomicBoolean(false);
        directories = new LinkedList<>();
    }

    public synchronized void push(Account e){
        this.directories.push(e);
        size++;
        this.notifyAll();
    }

    public synchronized Account pop(){
        while(size == 0){
            //if isComplete equals true and queue size is zero there is no push to wait
            //method can return null, consumers can stop.
            if(this.isComplete()){
                return null;
            }else {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        size--;
        this.notifyAll();
        return this.directories.pop();
    }

    public boolean isComplete(){
        return this.readCompleted.get();
    }

    public synchronized void setCompleted(){
        this.readCompleted.set(true);
        this.notifyAll();
    }

}

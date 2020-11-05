package Assignment5;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

public class SynchronizedLinkedList {
    private LinkedList<File> directories;
    private AtomicBoolean readCompleted;
    private int size = 0;

    public SynchronizedLinkedList(){
        readCompleted = new AtomicBoolean(false);
        directories = new LinkedList<>();
    }

    public synchronized void push(File f){
        this.directories.push(f);
        size++;
        this.notifyAll();
    }

    public synchronized File pop(){
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

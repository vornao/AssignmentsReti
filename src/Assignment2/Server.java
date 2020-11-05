package Assignment2;

import java.util.concurrent.*;

public class Server {
    private ThreadPoolExecutor executor;
    int k;

    public Server(int roomCapacity){
        this.k = roomCapacity;
        ArrayBlockingQueue<Runnable> personArrayBlockingQueue = new ArrayBlockingQueue<>(k);
        executor = new ThreadPoolExecutor(1, k, 10, TimeUnit.SECONDS, personArrayBlockingQueue);
    }

    public void executeTask(Person p){
        try {
            executor.execute(p);
        } catch (RejectedExecutionException e){
            throw new RejectedExecutionException("thread pool rejected execution");
        }
    }
    public void endServer(){
        executor.shutdown();
    }
}


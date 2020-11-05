package Assignment5;

import java.io.*;


//create monitor for the output stream to avoid concurrent access issues
public class SynchronizedDataStream {
    DataOutputStream outputStream;
    boolean busy = false;

    public SynchronizedDataStream(String path) {
        try {
            outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(path))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized void write(String s) {
        while(busy){
            try {
                this.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        busy = true;
        try {
            outputStream.writeBytes(s);
        }catch (IOException e){
            e.printStackTrace();
        }
        busy = false;
        this.notify();
    }
}

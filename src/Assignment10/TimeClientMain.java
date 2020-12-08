package Assignment10;

import java.io.IOException;
import java.net.UnknownHostException;

public class TimeClientMain {
    public static void main(String[] args) throws IOException, InterruptedException {

        MulticastTimeClient mtc = new MulticastTimeClient("239.255.1.3", 6789);
        mtc.start();
        System.out.println("Client started: press Enter key to quit.");
        try{
            System.in.read();
            System.out.println("Bye.");
            mtc.interrupt();
        }catch(Exception ignored){
            ;
        }
        mtc.join(3000);
    }
}

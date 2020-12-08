package Assignment10;

import java.net.SocketException;
import java.net.UnknownHostException;

public class TimeServerMain {
    private static int port;
    private static String address;
    public static void main(String[] args) throws InterruptedException {
        address = (args[0]);
        try{
            port = Integer.parseInt(args[1]);
        }catch (NumberFormatException e){
            System.out.println(e.getMessage());
            System.out.println("Usage: java TimeServerMain <MULTICAST ADDRESS> <PORT>");
            System.exit(-1);
        }

        MulticastTimeServer mts = null;
        try{
            mts = new MulticastTimeServer(address, port);
        }catch (UnknownHostException | SocketException e){
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        Thread mtsThread = new Thread(mts);
        mtsThread.start();
        System.out.println("Press Enter key to quit server");
        try{
            System.in.read();
            mts.interrupt();
            mtsThread.join(3000);
        }catch (Exception ignored){
            ;
        }
        System.out.println("Bye.");

    }
}

package Assignment10;

import java.io.IOException;

public class TimeClientMain {
    private static int port;
    private static String addr;

    public static void main(String[] args){
        MulticastTimeClient mtc = null;

        //parsing command line options
        if(args.length < 2) System.out.println("Usage: java TimeClientMain <ADDRESS> <PORT>");
        try{
            port = Integer.parseInt(args[1]);
            addr = args[0];
        }catch (NumberFormatException e){
            System.out.println("Usage: java TimeClientMain <ADDRESS> <PORT>");
        }

        try{
             mtc = new MulticastTimeClient(addr, port);
        }catch (IOException e){
            e.getMessage();
            System.exit(-1);
        }
        //start multicast listener thread
        mtc.start();
        System.out.printf("Client started (Multicast %s:%d)\nPress Enter key to quit.\n", addr, port);
        try{
            System.in.read();
            System.out.println("Bye.");
            mtc.interrupt();
            mtc.join(3000);
        }catch(Exception ignored){
            ;
        }
    }
}

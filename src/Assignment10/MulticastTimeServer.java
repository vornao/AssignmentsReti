package Assignment10;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class MulticastTimeServer extends Thread{

    private InetAddress multicastGroup;
    private final int port; //multicast group port
    private final int sleep = 1000; //0.7 seconds sleep
    private DatagramSocket ds = null;

    public MulticastTimeServer(String addr, int port) throws UnknownHostException, SocketException {
        this.port = port;
        ds = new DatagramSocket();
        multicastGroup = InetAddress.getByName(addr);
        if(!multicastGroup.isMulticastAddress()) throw new IllegalArgumentException("Invalid Multicast Address");
    }

    public void interrupt(){
        Thread.currentThread().interrupt();
        ds.close();
    }

    public void run(){
        System.out.printf("SERVER STARTED: %s:%d\n", multicastGroup.toString(), port);
        try{
            while(!Thread.currentThread().isInterrupted()){
                String currentTime = String.valueOf(System.currentTimeMillis());
                DatagramPacket dp = new DatagramPacket(
                        currentTime.getBytes(),
                        currentTime.length(),
                        this.multicastGroup,
                        this.port);
                ds.send(dp);
                //System.out.println("SERVER SENT: " + currentTime);
                Thread.sleep(sleep);
            }
        }catch (IOException | InterruptedException e){
            e.getMessage();
            return;
        }
    }



}

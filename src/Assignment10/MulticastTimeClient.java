package Assignment10;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.channels.ClosedByInterruptException;
import java.util.Calendar;

public class MulticastTimeClient extends Thread {

    private int port;
    private InetAddress timeGroup;
    private final int length = 64;
    private MulticastSocket multicastWelcome;

    public MulticastTimeClient(String addr, int port) throws UnknownHostException, IllegalArgumentException{
        this.timeGroup = InetAddress.getByName(addr);
        if (!this.timeGroup.isMulticastAddress()) throw new IllegalArgumentException();
        this.port = port;
    }

    @Override
    public void interrupt(){
        Thread.currentThread().interrupt();
        multicastWelcome.close();
    }

    public void run(){
        multicastWelcome = null;
        try {
            multicastWelcome = new MulticastSocket(this.port);
            multicastWelcome.joinGroup(this.timeGroup);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(!Thread.currentThread().isInterrupted()) {
            try{
                DatagramPacket dat = new DatagramPacket(new byte[this.length], length);
                assert multicastWelcome != null;
                multicastWelcome.receive(dat);
                String timestamp = new String(dat.getData(), dat.getOffset(), dat.getLength());
                printDate(timestamp);
            } catch (IOException e) {
                e.getMessage();
                return;
            }
        }
    }

    private void printDate(String timestamp){
        Calendar c = Calendar.getInstance();
        try{
            long millis = Long.parseLong(timestamp);
            c.setTimeInMillis(millis);
            System.out.printf("\r%d/%d/%d %d:%d:%2d",
                    c.get(Calendar.DAY_OF_MONTH),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.YEAR),
                    c.get(Calendar.HOUR_OF_DAY),
                    c.get(Calendar.MINUTE),
                    c.get(Calendar.SECOND));
        }
        catch (NumberFormatException e){
            System.out.println("Wrong timestamp format!");
            return;
        }

    }
}

package Assignment9;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

//sender
public class PingClient {
    private  String ADDRESS;
    private  int PORT;
    private  int PACKETS = 10;
    private  int TIMEOUT = 1000;

    public PingClient(String Address, int Port){
        this.PORT = Port;
        this.ADDRESS = Address;
    }

    public void run() throws IOException {
        //System.out.println("Connecting to: " + ADDRESS + ":" + PORT);
        DatagramSocket datagramSocket = new DatagramSocket();
        datagramSocket.setSoTimeout(TIMEOUT);
        byte[] buf = new byte[64];
        DatagramPacket receivedPacket = new DatagramPacket(buf, buf.length);

        ArrayList<Integer> rtts = new ArrayList<>(10);

        //send 10 pings
        for(int i = 0; i < PACKETS; i++) {
            //send message: "PING seq_no timestamp"
            StringBuilder pingMessage = new StringBuilder("PING ")
                    .append(i)
                    .append(" ")
                    .append(System.currentTimeMillis());

            //send new datagramPacket with string
            try {
                datagramSocket.send(new DatagramPacket(
                        pingMessage.toString().getBytes(),
                        pingMessage.toString().getBytes().length,
                        InetAddress.getByName(ADDRESS),
                        PORT));
            } catch (IOException e){
                System.out.println("ERROR: " + e.getMessage());
            }
            System.out.print(pingMessage + " RTT: ");

            //wait for echo response
            try{
                datagramSocket.receive(receivedPacket);
                //take timestamp as soon as ECHO PING has been received
                long current_timestamp = System.currentTimeMillis();
                //build string with response msg
                String resp = new String(receivedPacket.getData(),
                        0,
                        receivedPacket.getLength(),
                        StandardCharsets.US_ASCII);
                //retrieve timestamp splitting response string
                long recv_timestamp = Long.parseLong(resp.split(" ")[2]);
                int rtt = (int)(current_timestamp - recv_timestamp);
                System.out.println(rtt + " ms");
                //add RTT time to RTTs array.
                rtts.add(rtt);
            }
            catch (SocketTimeoutException e){
                //response timed out, RTT shall be -1
                rtts.add(-1);
                System.out.println("*");
            }
            catch (IOException e){
                System.out.println("ERROR: " + e.getMessage());
            }
        }
        printStats(rtts);
    }

    private void printStats(ArrayList<Integer> rtts){
        System.out.println("\n\t\t\t\t- - - - PING STATISTICS - - - -");

        //format will be: ping sent, ping received, percent loss packets
        int pingNo = rtts.size();
        int pingReceived = pingNo - Collections.frequency(rtts, -1);
        int lossRate = Math.round((1 - ((float)pingReceived / (float)pingNo)) * 100F);
        String line1 = String.format("%d packets transmitted, %d packet received, %d%\u0025 packet loss",
                pingNo,
                pingReceived,
                lossRate);
        System.out.println(line1);

        if(pingReceived != 0) {
            float sum = 0;
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;

            for (Integer i : rtts) {
                if (i != -1) {
                    sum += i;
                    min = i < min ? i : min;
                    max = i > max ? i : max;
                }
            }
            float avg = sum / (float) pingReceived;
            String line2 = String.format("round-trip (ms) min/avg/max = %d/%.2f/%d", min, avg, max);
            System.out.println(line2);
        }
    }
}

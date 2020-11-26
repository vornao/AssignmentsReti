package Assignment9;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

//sender
public class PingClient {
    private static String ADDRESS;
    private static int PORT;
    private static int TIMEOUT = 1000;

    public static void main(String[] args) throws IOException, InterruptedException {
        Options options = new Options();
        options.addOption("s", "set-server", true, "Server Address");
        options.addOption("p", "set-port", true, "Server Port");
        HelpFormatter helpFormatter = new HelpFormatter();

        try {
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine commandLine = commandLineParser.parse(options, args);
            if(args.length < 2) throw new ParseException("Missing arguments");

            if (commandLine.hasOption("n") || commandLine.hasOption("--set-server")) {
                ADDRESS = (commandLine.getOptionValues("n")[0]);
            }
            if (commandLine.hasOption("p")){
                PORT = Integer.parseInt(commandLine.getOptionValues("p")[0]);
            }
        }catch(ParseException p){
            System.out.println(p.getMessage());
            helpFormatter.printHelp("java PingClient", options);
            System.exit(-1);
        }

        //System.out.println("Connecting to: " + ADDRESS + ":" + PORT);
        DatagramSocket datagramSocket = new DatagramSocket();
        datagramSocket.setSoTimeout(TIMEOUT);
        byte[] buf = new byte[64];
        DatagramPacket receivedPacket = new DatagramPacket(buf, buf.length);

        ArrayList<Integer> rtts = new ArrayList<>(10);
        //send 10 pings
        for(int i = 0; i < 10; i++) {
            //send message: "PING seqno timestamp"
            StringBuilder pingMessage = new StringBuilder("PING ")
                    .append(i)
                    .append(" ")
                    .append(System.currentTimeMillis());

            //send new datagramPacket with string
            datagramSocket.send(new DatagramPacket(
                    pingMessage.toString().getBytes(),
                    pingMessage.toString().getBytes().length,
                    InetAddress.getByName(ADDRESS),
                    PORT));

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
                //retreive timestamp splitting response string
                long recv_timestamp = Long.parseLong(resp.split(" ")[2]);
                int diff = (int)(current_timestamp - recv_timestamp);

                //add RTT time to RTTs array.
                rtts.add(diff);
            }
            catch (SocketTimeoutException e){
                //response timed out, RTT shall be -1
                rtts.add(-1);
            }
        }
        printStats(rtts);
    }

    private static void printStats(ArrayList<Integer> rtts){
        System.out.println("\n\t\t\t- - - - PING STATISTICS - - - -");

        //format will be: ping sent, ping received, percent loss packets
        int pingNo = rtts.size();
        int pingReceived = pingNo - Collections.frequency(rtts, -1);
        int lossRate = (int)((1 - ((float)pingReceived / (float)pingNo)) * 100F);
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

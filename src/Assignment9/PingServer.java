package Assignment9;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class PingServer {
    private static int PORT;
    private static int MAX_PING_DELAY = 300;

    public static void main(String[] args) throws SocketException {
        Options options = new Options();
        options.addOption("p", "set-port", true, "Server Port");

        try {
            CommandLineParser commandLineParser = new DefaultParser();
            CommandLine commandLine = commandLineParser.parse(options, args);
            if (commandLine.hasOption("p")) {
                PORT = Integer.parseInt(commandLine.getOptionValues("p")[0]);
            }
        } catch (
                ParseException p) {
            System.out.println(p.getMessage());
        }

        System.out.println("Server ready: port " + PORT);
        Random random = new Random();
        try(DatagramSocket serverSocket = new DatagramSocket(PORT)){
            serverSocket.setSoTimeout(100000);
            byte[] buf = new byte[1024];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);
            DatagramPacket send;

            while(true){
                StringBuilder consoleOutput = new StringBuilder();
                serverSocket.receive(recv);
                String msg = new String(recv.getData(), 0, recv.getLength(), StandardCharsets.US_ASCII);
                consoleOutput.append(recv.getAddress())
                        .append(":> ")
                        .append(recv.getPort())
                        .append(" ")
                        .append(msg)
                        .append(" ACTION: ");

                //simulate loss probability 1/4
                if(random.nextInt(4) != 2) {
                    int sleepTime = random.nextInt(MAX_PING_DELAY);
                    Thread.sleep(sleepTime);
                    send = new DatagramPacket(msg.getBytes(), msg.getBytes().length, recv.getAddress(), recv.getPort());
                    serverSocket.send(send);
                    consoleOutput.append("delayed ").append(sleepTime).append(" ms");
                }else{
                    consoleOutput.append("not sent");
                }
                System.out.println(consoleOutput.toString());
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

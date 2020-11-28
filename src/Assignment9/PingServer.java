package Assignment9;

import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.StringTokenizer;

public class PingServer implements Runnable{
    private int PORT;
    private String ADDRESS;
    private Random random;
    private int MAX_PING_DELAY;

    public PingServer(int PORT, String ADDRESS, int DELAY){
        this.PORT = PORT;
        this.ADDRESS = ADDRESS;
        this.MAX_PING_DELAY = DELAY;
        this.random = new Random();
    }

    public void run(){
        try(DatagramSocket serverSocket = new DatagramSocket(PORT)){
            byte[] buf = new byte[1024];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);
            DatagramPacket send;

            while(true){
                Arrays.fill(buf, (byte)0);
                StringBuilder consoleOutput = new StringBuilder();
                serverSocket.receive(recv);

                String msg = new String(recv.getData(), 0, recv.getLength(), StandardCharsets.US_ASCII);

                if(!checkMessage(msg)){
                    System.out.println("String not valid");
                    continue;
                }

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


    private boolean checkMessage(String msg){
        int count = 0;
        StringTokenizer st = new StringTokenizer(msg, " ");
        if(st.countTokens() > 3) return false;
        while(st.hasMoreTokens()){
            switch(count){
                case 0:
                    if(!st.nextToken().equals("PING")) return false;
                    else count++;
                    break;
                case 1:
                    try{
                        Integer.parseInt(st.nextToken());
                        count++;
                    }catch (NumberFormatException e){
                        return false;
                    }
                    break;
                case 2:
                    try{
                        Long.parseLong(st.nextToken());
                        count++;
                    }catch (NumberFormatException e) {
                        return false;
                    }
                    break;
                }
            }
        return true;
        }
    }

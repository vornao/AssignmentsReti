package Assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private Socket clientSocket;
    private StringBuilder requestBuilder;

    public RequestHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        System.out.println("client connected " + Thread.currentThread().toString());
        BufferedReader inFromClient;
        OutputStream outForClient;
        try {
            inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }catch(IOException e){
            e.printStackTrace();
            return;
        }

        /**
         * [HTTP REQUEST EXAMPLE TO PARSE]
         *
         * GET / HTTP/1.1
         * Host: localhost:8080
         * Upgrade-Insecure-Requests: 1
         * Accept: text/html,application/xhtml+xml,application/xml;q=0.9;
         * User - Agent:Mozilla / 5.0 (Macintosh; Intel Mac OS X 10_15_6)AppleWebKit
         * Accept - Language:en - gb
         * Accept - Encoding:gzip, deflate
         * Connection:keep - alive
         */

        requestBuilder = new StringBuilder();
        String requestLine = null;
        while(true){
            try {

                if (!(requestLine = inFromClient.readLine()).isEmpty()) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            requestBuilder.append(requestLine).append("\r\n");
        }
        System.out.println(requestLine);
    }
}

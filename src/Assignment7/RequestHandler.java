package Assignment7;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class RequestHandler implements Runnable {
    private Socket clientSocket;

    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        System.out.println("client connected " + Thread.currentThread().toString());
        BufferedReader inFromClient;
        String requestLine;

        try {
            inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            try {
                if (!(requestLine = inFromClient.readLine()).isEmpty()){
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            sendResponse(requestLine, clientSocket);
            inFromClient.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendResponse(String requestLine, Socket clientSocket) throws IOException {

        OutputStream outToClient = clientSocket.getOutputStream();
        StringTokenizer tokenizer = new StringTokenizer(requestLine, "/ ");
        StringBuilder responseHeaders;
        String extension;
        final String basePath = "./www/";

        ArrayList<String> requestLineToArray = new ArrayList<>();

        while(tokenizer.hasMoreTokens()){
            requestLineToArray.add(tokenizer.nextToken());
        }

        if(requestLineToArray.get(1).equals("HTTP")) {
            requestLineToArray.add(1, "index.html");
        }

        requestLine = basePath + requestLineToArray.get(1);

        int index = requestLineToArray.get(1).indexOf(".");
        extension = requestLineToArray.get(1).substring(index);

        File requestedFile = new File(requestLine);
        FileInputStream fileInputStream;

        try{
            fileInputStream = new FileInputStream(requestedFile);
        } catch (FileNotFoundException e){
            e.printStackTrace();
            outToClient.write(fileNotFoundMessageBuilder());
            return;
        }

        System.out.println("Providing resource: " + requestedFile);
        responseHeaders =  new StringBuilder( "HTTP/1.1 200 OK\r\n" + "Server: SampleJavaServer\r\n");

        switch (extension){
            case ".txt":
                responseHeaders.append("Content-Type: text/plain\r\n");
                break;
            case ".jpg":
                responseHeaders.append("Content-Type: image/jpeg\r\n");
                break;
            case ".html":
                responseHeaders.append("Content-Type: text/html\r\n");
                break;
            case ".gif":
                responseHeaders.append("Content-Type: image/gif\r\n");
                break;
            default:
                break;
        }

        try {
            responseHeaders.append("\r\n");
            byte[] responseContent = new byte[(int)requestedFile.length()];

            assert outToClient != null;

            if(fileInputStream.read(responseContent) == -1) throw new IOException("Failed to read file.");

            outToClient.write(responseHeaders.toString().getBytes());
            outToClient.write(responseContent);
            outToClient.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] fileNotFoundMessageBuilder(){
        String message = "HTTP/1.1 404 Not Found\r\n" + "Server: SampleJavaServer\r\n" +
                "Content-Type: text/html\r\n\r\n" +
                "<h1>ERROR 404&nbsp;</h1>\n" +
                "<p>Not Found</p>";
        return message.getBytes();
    }

}

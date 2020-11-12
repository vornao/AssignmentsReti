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
        BufferedReader inFromClient = null;
        OutputStream outToClient = null;
        try {
            inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String requestLine;
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
        String extension;
        ArrayList<String> requestLineToArray = new ArrayList<>();

        while(tokenizer.hasMoreTokens()){
            requestLineToArray.add(tokenizer.nextToken());
        }
        System.out.println(requestLineToArray.get(1));
        String basePath = "./www/";
        if(requestLineToArray.get(1).equals("HTTP")) {
            requestLine = basePath + "index.html";
        }else{
            requestLine = basePath + requestLineToArray.get(1);
        }

        int index = requestLineToArray.get(1).indexOf(".");
        extension = requestLineToArray.get(1).substring(index);

        File requestedFile = new File(requestLine);
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = new FileInputStream(requestedFile);
        } catch (FileNotFoundException e){
            e.printStackTrace();
            outToClient.write(fileNotFoundMessageBuilder());
            return;
        }
        StringBuilder responseHeaders =  new StringBuilder( "HTTP/1.1 200 OK\r\n" + "Server: SampleJavaServer\r\n");

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

            fileInputStream.read(responseContent);
            outToClient.write(responseHeaders.toString().getBytes());
            outToClient.write(responseContent);
            System.out.println("Response sent to client");
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

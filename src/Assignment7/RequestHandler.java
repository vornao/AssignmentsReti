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
        System.out.println("Client connected. (" + Thread.currentThread().toString() + ")");

        //used to read request
        BufferedReader inFromClient;
        String requestLine;

        while (true) {
            try {
                inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
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
        System.out.println("Client disconnectet.");
    }

    private void sendResponse(String requestLine, Socket clientSocket) throws IOException {

        //used to send data to client
        OutputStream outToClient = clientSocket.getOutputStream();

        //needed to tokenize requestLine and get request needed from client, as well as file extension
        StringTokenizer tokenizer = new StringTokenizer(requestLine, "/ ");
        StringBuilder responseHeaders;
        String extension;

        //base path to look for contents
        final String basePath = HTTPServer.BASEPATH;

        ArrayList<String> requestLineToArray = new ArrayList<>();

        while(tokenizer.hasMoreTokens()){
            requestLineToArray.add(tokenizer.nextToken());
        }

        //if request file is null, redirect to ./www/index.html
        if(requestLineToArray.get(1).equals("HTTP")) {
            requestLineToArray.add(1, "index.html");
        }

        requestLine = basePath + requestLineToArray.get(1);

        //read requested content file
        File requestedFile = new File(requestLine);
        FileInputStream fileInputStream;
        System.out.println(requestedFile);
        try{
            fileInputStream = new FileInputStream(requestedFile);
        } catch (FileNotFoundException e){

            //if not found send 404 page to client, and return
            System.out.println("404 File not found!");
            outToClient.write(fileNotFoundMessageBuilder());
            return;
        }

        //gathering file extension to specify content type
        int index = requestLineToArray.get(1).indexOf(".");
        extension = requestLineToArray.get(1).substring(index);


        //building headers start
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
        responseHeaders.append("\r\n");
        //end of headers building


        try {

            byte[] responseContent = new byte[(int)requestedFile.length()];
            assert outToClient != null;

            if(fileInputStream.read(responseContent) == -1) {
                System.out.println("Failed to read file.");
                outToClient.close();
                return;
            }

            //sending everything to client
            outToClient.write(responseHeaders.toString().getBytes());
            outToClient.write(responseContent);
            outToClient.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //html string builder for 404 error
    private byte[] fileNotFoundMessageBuilder(){
        String message = "HTTP/1.1 404 Not Found\r\n" + "Server: SampleJavaServer\r\n" +
                "Content-Type: text/html\r\n\r\n" +
                "<h1>ERROR 404&nbsp;</h1>\n" +
                "<p>Not Found</p>";
        return message.getBytes();
    }

}

package Assignment7;

import Assignment2.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HTTPServer {

    private static int PORT = 6789;
    private static final int COREPOOLSIZE = 4;
    private static final int MAXIMUMPOOLSIZE = 26;
    private static final int KEEPALIVETIME = 20;
    static String BASEPATH;

    public static void main(String[] args) throws IOException {
        if(args.length < 2){
            System.out.println("Usage: -$java ./HTTPServer <PORT> <FILES PATH> <REQUESTS_BEFORE_SHUTDOWN> (optional, leave blank for unlimited request)");
            System.exit(0);
        }
        PORT = Integer.parseInt(args[0]);
        BASEPATH = args[1] + "/";
        int requestsBeforeShutdown = -1;
        if(args.length > 2) {
            requestsBeforeShutdown = args[2] != null ? (Integer.parseInt(args[2])) : -1;
        }
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("failed to start http server");
            System.exit(-1);
        }
        System.out.println("HTTP SERVER STARTED PORT: " + PORT);
        System.out.println("FILES DIRECTORY: " + BASEPATH);
        System.out.println("connect to http://localhost:" + PORT);

        LinkedBlockingQueue<Runnable> requestQueue = new LinkedBlockingQueue<>(8);
        ThreadPoolExecutor clientExecutor = new ThreadPoolExecutor(
                COREPOOLSIZE,
                MAXIMUMPOOLSIZE,
                KEEPALIVETIME,
                TimeUnit.SECONDS,
                requestQueue);

        int requestCount = 0;

        //every client is handled by a thread in the threadpool
        //waits for requestBeforeShutdown requests, o/w handles infinite requests.
        if(requestsBeforeShutdown != -1) while(requestCount < requestsBeforeShutdown){
            acceptAndHandle(serverSocket, clientExecutor);
            requestCount++;
        }
        else
            while(true) acceptAndHandle(serverSocket, clientExecutor);

        clientExecutor.shutdown();
        serverSocket.close();
    }

    private static void acceptAndHandle(ServerSocket serverSocket, ThreadPoolExecutor clientExecutor){
        try{
            Socket client = serverSocket.accept();
            clientExecutor.submit(new RequestHandler(client));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

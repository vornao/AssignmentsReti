package Assignment7;

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
        if(args.length != 2){
            System.out.println("Usage: -$java ./HTTPServer <PORT> <FILES PATH>");
        }
        BASEPATH = args[1] + "/";
        PORT = Integer.parseInt(args[0]);

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
        ThreadPoolExecutor clientExecutor = new ThreadPoolExecutor(COREPOOLSIZE, MAXIMUMPOOLSIZE, KEEPALIVETIME, TimeUnit.SECONDS, requestQueue);

        int count = 0;
        //every client is handled by a thread in the threadpool
        //waits for 20 clients before shutting down
        //change while statement to true in order to accept unlimited requests.
        while(count < 20){
            try{
                Socket client = serverSocket.accept();
                clientExecutor.submit(new RequestHandler(client));
            }catch (IOException e){
                e.printStackTrace();
            }
            count++;
        }
        clientExecutor.shutdown();
        serverSocket.close();
    }
}

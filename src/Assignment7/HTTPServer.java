package Assignment7;

import jdk.nashorn.internal.runtime.RewriteException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HTTPServer {

    private static final int PORT = 6789;
    private static final int COREPOOLSIZE = 4;
    private static final int MAXIMUMPOOLSIZE = 26;
    private static final int KEEPALIVETIME = 20;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("failed to start http server");
            System.exit(-1);
        }
        LinkedBlockingQueue<Runnable> requestQueue = new LinkedBlockingQueue<>(8);
        ThreadPoolExecutor clientExecutor = new ThreadPoolExecutor(COREPOOLSIZE, MAXIMUMPOOLSIZE, KEEPALIVETIME, TimeUnit.SECONDS, requestQueue);

        while(true){
            try{
                Socket client = serverSocket.accept();
                clientExecutor.submit(new RequestHandler(client));
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}
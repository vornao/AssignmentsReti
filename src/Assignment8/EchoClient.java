package Assignment8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class EchoClient {
    private static int PORT = 6789;
    private static String ADDRESS = "localhost";
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws IOException {
        if(args.length > 1){
            try{
                PORT = Integer.parseInt(args[1]);
                ADDRESS = args[0];
            }catch(NumberFormatException e){
                System.out.println("Usage: ./EchoClient <ADDRESS> <PORT>");
                System.exit(-1);
            }
        }
        else System.out.println("[Using default params: " + ADDRESS + " " + PORT + "]");

        BufferedReader input = null;
        ByteBuffer buffer = null;
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(ADDRESS, PORT));
            socketChannel.configureBlocking(true);
            input = new BufferedReader(new InputStreamReader(System.in));
            buffer = ByteBuffer.wrap(new byte[BUFFER_SIZE]);

        }catch(IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        String message = null;
        System.out.println("CLIENT STARTED " + ADDRESS + ":" + PORT);

        while(true){
            //System.out.print("OUT: ");
            StringBuilder response = new StringBuilder();
            while((message = input.readLine()).equals(""));
            if(message.contains("!quit")){
                buffer.put("!quit".getBytes());
                buffer.flip();
                socketChannel.write(buffer);
                socketChannel.close();
                break;
            }
            buffer.put(message.getBytes());
            buffer.flip();
            while(buffer.hasRemaining()) socketChannel.write(buffer);
            buffer.clear();
            socketChannel.read(buffer);

            buffer.flip();
            while (buffer.hasRemaining()) response.append((char)buffer.get());
            System.out.println("[SERVER RESPONSE] " + response.toString());
            buffer.clear();

        }
    }
}

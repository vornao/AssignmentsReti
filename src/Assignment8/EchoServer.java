package Assignment8;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * SIMPLE ECHO WEB SERVER WITH NIO AND CHANNELS
 * Assignment 08 - NIO Echo Server
 * scrivere un programma echo server usando la libreria java NIO e, in particolare, il Selector e canali in modalità
 * non bloccante, e un programma echo client, usando NIO (va bene anche con modalità bloccante).
 *
 * Il server accetta richieste di connessioni dai client, riceve messaggi inviati dai client
 * e li rispedisce (eventualmente aggiungendo "echoed by server" al messaggio ricevuto).
 * Il client legge il messaggio da inviare da console, lo invia al server e visualizza quanto ricevuto dal server.
 *
 */


public class EchoServer {
    private static int PORT = 6789;
    private static Selector selector = null;
    private static final String ESCAPE_SEQUENCE = "!quit";


    public static void main(String[] args){
        if(args.length < 1) System.out.println("using default port [" + PORT + "]");
        else PORT = Integer.parseInt(args[0]);

        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            System.out.println("Fatal error - " + e.getMessage());
            System.exit(-1);
        }

        System.out.println("ECHO SERVER STARTED - PORT: " + PORT);

        while(true){
            try {
                if (selector.select() == 0) continue;
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey key = selectionKeyIterator.next();
                    selectionKeyIterator.remove();
                    //register read operation first to read input value.
                    if      (key.isAcceptable()) registerRead(selector, key);
                    else if (key.isReadable())   readSocketChannel(selector, key);
                    else if (key.isWritable())   writeSocketChannel(selector, key);
                }
            }catch (IOException ex){
                System.out.println("Exception - " + ex.getMessage());
            }
        }
    }

    private static void registerRead(Selector sel, SelectionKey selectionKey) throws IOException {
        ServerSocketChannel ss = (ServerSocketChannel) selectionKey.channel();
        SocketChannel sc = ss.accept();
        sc.configureBlocking(false);
        //set new buffer for channel
        ByteBuffer scByteBuffer = ByteBuffer.wrap(new byte[1024]);
        System.out.println("Client accepted - " + sc.getRemoteAddress());
        sc.register(sel, SelectionKey.OP_READ, scByteBuffer);
    }

    private static void readSocketChannel(Selector sel, SelectionKey selectionKey) throws IOException {
        StringBuilder message = new StringBuilder();
        SocketChannel sc = (SocketChannel) selectionKey.channel();
        sc.configureBlocking(false);
        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();

        //writing buffer with sc bytes
        int readBytes = sc.read(buffer);
        if(readBytes < 0){
            sc.close();
            throw new IOException("channel read failed.");
        }

        //ready for reading
        buffer.flip();
        while (buffer.hasRemaining()) message.append((char) buffer.get());
        //pos = message.length

        System.out.println("Message received from client: " + sc.getRemoteAddress() + ": " + message);
        if (message.toString().contains("!quit")) {
            sc.close();
            System.out.println("Escape sequence received: Connection closed.");
            return;
        }

        //ready to use buffer again in read mode
        buffer.flip();
        sc.register(sel, SelectionKey.OP_WRITE, buffer);
    }

    private  static void writeSocketChannel(Selector sel, SelectionKey selectionKey) throws IOException {
        SocketChannel sc;
        sc = (SocketChannel) selectionKey.channel();
        sc.configureBlocking(false);
        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
        sc.write(buffer);
        buffer.clear();
        //ready for new writes [pos = 0]
        sc.register(sel, SelectionKey.OP_READ, buffer);
    }
}

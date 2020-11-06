package Assignment5;

import java.io.File;

/**
* Si scriva un programma JAVA che riceve in input un filepath che individua una directory D
* stampa le informazioni del contenuto di quella directory e, ricorsivamente, di tutti i file contenuti nelle sottodirectory di D
* il programma deve essere strutturato come segue:
* - attiva un thread produttore ed un insieme di k thread consumatori
* - il produttore comunica con i consumatori mediante una coda
* - il produttore visita ricorsivamente la directory data ed, eventualmente tutte le sottodirectory e mette nella coda il nome di ogni directory individuata
* - i consumatori prelevano dalla coda i nomi delle directories e stampano il loro contenuto  (nomi dei file)
* - la coda deve essere realizzata con una LinkedList.
* - Ricordiamo che una Linked List non è una struttura thread-safe.
*/



public class FileExplorer {
    static SynchronizedDataStream synchronizedDataStream;
    public static void main(String[] args){
        String path = null;
        int k = 1;
        String fileRead = "contents.txt";
        synchronizedDataStream = new SynchronizedDataStream(fileRead);

        SynchronizedLinkedList directoryList = new SynchronizedLinkedList();

        try{
            k = Integer.parseInt(args[1]);
            path = args[0];
        }catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Usage: ./FileExplorer <path-to-explore> <threads-number>");
            System.exit(-1);
        }
        assert path != null;
        File startDirectory = new File(path);
        if(!startDirectory.exists()){
            System.out.println("no such file or directory");
            System.exit(-1);
        }

        if(!startDirectory.isDirectory()){
            System.out.println("provided path is not directory");
            System.exit(-1);
        }

        //starts k file readers thread - default 1;
        for(int i = 0; i < k; i++){
            Consumer consumer = new Consumer(directoryList);
            Thread threadConsumer = new Thread(consumer);
            threadConsumer.start();
        }
        //producer push directory in the list
        Producer producer = new Producer(directoryList, path);
        Thread threadProducer = new Thread(producer);
        threadProducer.start();

    }
}

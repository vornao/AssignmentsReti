package Assignment5;

import java.io.File;

public class Producer implements Runnable{
    private String basePath;
    private SynchronizedLinkedList directoryList;
    private File startDirectory;

    public Producer(SynchronizedLinkedList directoryList, String path){
        this.directoryList = directoryList;
        this.basePath = path;
        startDirectory = new File(basePath);
    }

    @Override
    public void run() {
        if(!startDirectory.exists()) {
            System.out.println("no such file or directory");
            System.exit(-1);
        }

        if(!startDirectory.isDirectory()) {
            System.out.println("file is not a directory");
            System.exit(-1);
        }
        directoryList.push(startDirectory);
        try {
            ExploreDirectory(directoryList, startDirectory);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //set completed bit
        directoryList.setCompleted();
    }

    private void ExploreDirectory(SynchronizedLinkedList list, File path) throws InterruptedException {
        File[] fileList = path.listFiles();
        if (fileList == null){
            return;
        }
        for(File cf : fileList){
            if(cf.isDirectory()){
                list.push(cf);
                ExploreDirectory(list, cf);
            }
        }
    }
}

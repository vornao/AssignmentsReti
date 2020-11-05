package Assignment5;

import java.io.File;

public class Consumer implements Runnable{
    private SynchronizedLinkedList directoryList;

    public Consumer(SynchronizedLinkedList directoryList){
        this.directoryList = directoryList;
    }

    @Override
    public void run() {
        File file = directoryList.pop();
        //create a string with directory name, appending lines for each file found.
        while (file != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(file.getPath()).append("\n");
            File[] fileList = file.listFiles();
            if (fileList == null) {
                continue;
            }
            for (File cf : fileList) {
                if (!cf.isDirectory()) {
                    sb.append("\t|").append(cf.getName()).append("\n");
                }

            }
            sb.append("\n");
            //safe write to file output
            FileExplorer.synchronizedDataStream.write(sb.toString());
            file = directoryList.pop();
        }
    }
}

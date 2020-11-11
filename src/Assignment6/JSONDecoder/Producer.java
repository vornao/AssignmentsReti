package Assignment6.JSONDecoder;
import Assignment6.Account;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Producer {
    private static AtomicIntegerArray arrayCausali = new AtomicIntegerArray(5);

    public static void main(String[] args) throws IOException {

        FileChannel fileChannel = FileChannel.open(
                Paths.get("Accounts.json"),
                StandardOpenOption.READ);
        StringBuilder stringBuilder = new StringBuilder();
        ByteBuffer buffer = ByteBuffer.allocateDirect((int)fileChannel.size());
        fileChannel.read(buffer);

        buffer.flip();
        while(buffer.hasRemaining()) {
            stringBuilder.append((char)(buffer.get()));
        }

        //parsing json file array from string
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Account> accountList = objectMapper.readValue(stringBuilder.toString(), new TypeReference<ArrayList<Account>>() {});
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        for(Account a : accountList){
            executor.execute(new Consumer(a, arrayCausali));
        }

        StringBuilder s = new StringBuilder();
        s.append("Bonifici: "    ).append(arrayCausali.get(0));
        s.append("\nAccrediti: " ).append(arrayCausali.get(1));
        s.append("\nBollettino: ").append(arrayCausali.get(2));
        s.append("\nF24: "       ).append(arrayCausali.get(3));
        s.append("\nBancomat: "  ).append(arrayCausali.get(4));
        System.out.println(s.toString());

        executor.shutdown();
    }

}

package Assignment6.JSONCreator;
import Assignment6.Account;
import Assignment6.Payment;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Random;

public class JsonFileCreator {
    private static final int ACCOUNTS = 100;
    private static final int PAYMENTS = 100;
    private static final String[] firstNames = { "Jacob", "Helena", "Marco", "Giulia", "Henry", "Sheldon", "Amy", "Howard", "Martha",
                                    "Giovanni", "Lisa", "Abigale", "Mohamed", "Salvatore", "Ilaria", "Gianluigi", "Paolo",
                                    "Davide","Susanna", "Pedro", "Rosa", "Sara"};

    private static final String[] lastNames = {  "Johnson", "Jackson", "Ali", "Rossi", "Bianchi", "Pardini", "Salenti",
                                    "Portobello","Perez","Sustaita", "Treviso","Emiliano", "Verdi", "De Angeli",
                                    "Solenni", "Pertini", "Frattani", "Colageri", "Cossu", "Marianelli", "Arga", "Filante" };


    public static void main(String[] args) throws IOException {
        WritableByteChannel fileChannel = FileChannel.open(
                Paths.get("Accounts.json"),
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE);

        ByteBuffer buffer = ByteBuffer.allocateDirect(500000);
        ArrayList<Account> accounts = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        Random random = new Random();

        int accountNumber = random.nextInt(ACCOUNTS);
        for(int i = 0; i < accountNumber; i++) {
            accounts.add(createRandomAccount());
        }

        buffer.flip();
        buffer = ByteBuffer.wrap(objectMapper.writeValueAsBytes(accounts));
        fileChannel.write(buffer);
        buffer.clear();

    }

    private static Account createRandomAccount(){
        Random random = new Random();
        String name = firstNames[random.nextInt(firstNames.length)]
                      + " " +
                      lastNames[random.nextInt(lastNames.length)];

        Account a = new Account(name, new ArrayList<>());
        int payments = random.nextInt(PAYMENTS);

        for(int i = 0; i < payments; i++){
            String date = (1 + random.nextInt(31)) + "/" + (1 + random.nextInt(12)) + "/200" + random.nextInt(4);
            a.addPayment(new Payment(date, random.nextInt(5)));
        }
        return a;
    }
}

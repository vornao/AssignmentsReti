package Assignment6.JSONDecoder;

import Assignment6.Account;
import Assignment6.Payment;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Consumer implements Runnable{
    private Account account;
    private AtomicIntegerArray array;

    public Consumer(Account a, AtomicIntegerArray array){
        this.array = array;
        this.account = a;
    }

    @Override
    public void run() {
        //atomically increments Producer.arrayCausali
        for(Payment p : account.getPayments()){
            array.addAndGet(p.getCausale(), 1);
        }
    }
}

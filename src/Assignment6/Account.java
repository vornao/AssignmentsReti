package Assignment6;

import java.util.ArrayList;

public class Account {
    private String name;
    private ArrayList<Payment> payments;

    public Account(String name){
        this.name = name;
        payments = new ArrayList<>();
    }

    public String getName(){
        return this.name;
    }

    public ArrayList<Payment> getPayments(){
        return this.payments;
    }

    public void addPayment(Payment e){
        this.payments.add(e);
    }

}

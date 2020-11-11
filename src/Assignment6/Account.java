package Assignment6;

import java.util.List;

public class Account {
    private String name;
    private List<Payment> payments;

    public Account(){

    }

    public Account(final String name, final List<Payment> payments){
        this.name = name;
        this.payments = payments;
    }

    public void setName(final String name){
        this.name = name;
    }
    //@JsonProperty("name")
    public String getName(){
        return name;
    }
    //@JsonProperty("payments")
    public List<Payment> getPayments(){
        return payments;
    }

    public void setPayments(final List<Payment> payments) {
        this.payments = payments;
    }

    public void addPayment(Payment e){
        this.payments.add(e);
    }

}

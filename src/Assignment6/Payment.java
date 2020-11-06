package Assignment6;

public class Payment {
    private Integer causale;
    private String Date;
    public Payment(String date, Integer causale){
        this.Date = date;
        this.causale = causale;
    }

    public String getDate(){
        return this.Date;
    }

    public Integer getCausale(){
        return this.causale;
    }
}

package Assignment6;

public class Payment {
    private Integer causale;
    private String Date;
    public Payment(){

    }

    public Payment(final String date, final Integer causale){
        this.Date = date;
        this.causale = causale;
    }
    public void setDate(final String date){
        this.Date = date;
    }
    public void setCausale(final Integer causale){
        this.causale = causale;
    }
    public String getDate(){
        return this.Date;
    }

    public Integer getCausale(){
        return this.causale;
    }
}

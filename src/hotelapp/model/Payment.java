package hotelapp.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Payment implements Serializable {
    private String method;
    private double total;
    private double bayar;
    private double kembalian;
    private LocalDateTime paidAt;

    public Payment(String method, double total, double bayar){
        this.method = method;
        this.total = total;
        this.bayar = bayar;
        this.kembalian = bayar - total;
        this.paidAt = LocalDateTime.now();
    }

    public String getMethod(){ return method; }
    public double getTotal(){ return total; }
    public double getBayar(){ return bayar; }
    public double getKembalian(){ return kembalian; }
    public LocalDateTime getPaidAt(){ return paidAt; }

    @Override
    public String toString(){
        return method + " | Rp " + total + " | Bayar: Rp " + bayar + " | Kembali: Rp " + kembalian;
    }
}

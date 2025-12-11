package hotelapp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Booking implements Serializable {
    private String id;
    private Tamu tamu;
    private Kamar kamar;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Payment payment;
    private boolean checkedIn = false;
    private boolean checkedOut = false;

    public Booking(String id, Tamu tamu, Kamar kamar, LocalDate checkIn, LocalDate checkOut){
        this.id = id;
        this.tamu = tamu;
        this.kamar = kamar;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public String getId(){ return id; }
    public Tamu getTamu(){ return tamu; }
    public Kamar getKamar(){ return kamar; }
    public LocalDate getCheckIn(){ return checkIn; }
    public LocalDate getCheckOut(){ return checkOut; }

    public void doCheckIn(){ this.checkedIn = true; this.kamar.setTersedia(false); }
    public void doCheckOut(){ this.checkedOut = true; this.kamar.setTersedia(true); }

    public boolean isCheckedIn(){ return checkedIn; }
    public boolean isCheckedOut(){ return checkedOut; }

    public void setPayment(Payment p){ this.payment = p; }
    public Payment getPayment(){ return payment; }
    public boolean isPaid(){ return payment != null; }

    public double getTotalHarga(){
        long hari = ChronoUnit.DAYS.between(checkIn, checkOut);
        if(hari <= 0) hari = 1;
        return hari * kamar.getHarga();
    }

    public String getStatus(){
        if(checkedOut) return "SELESAI";
        if(checkedIn) return "SEDANG MENGINAP";
        return "BELUM CHECK-IN";
    }

    @Override
    public String toString(){
        return id + " | " + tamu.getName() +
               " | No:" + kamar.getNomor() + " (" + kamar.getTipe() + ")" +
               " | " + checkIn + " -> " + checkOut +
               " | Rp " + String.format("%.0f", getTotalHarga()) +
               " | " + getStatus() +
               (isPaid() ? " | SUDAH BAYAR" : " | BELUM BAYAR");
    }
}

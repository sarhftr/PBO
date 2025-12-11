package hotelapp.model;

import java.io.Serializable;

public class Kamar implements Serializable {
    private String nomor;   
    private String tipe;
    private double harga;
    private boolean tersedia;

    public Kamar(String nomor, String tipe, double harga){
        this.nomor = nomor;
        this.tipe = tipe;
        this.harga = harga;
        this.tersedia = true;
    }

    public String getNomor(){ return nomor; }
    public String getTipe(){ return tipe; }
    public double getHarga(){ return harga; }
    public boolean isTersedia(){ return tersedia; }
    public void setTersedia(boolean v){ this.tersedia = v; }

    @Override
    public String toString(){
        return nomor + " | " + tipe + " | Rp " + String.format("%.0f", harga) + (tersedia ? " | AVAILABLE" : " | NOT AVAILABLE");
    }
}

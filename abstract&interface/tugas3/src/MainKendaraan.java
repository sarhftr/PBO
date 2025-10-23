interface Kendaraan {
    void jalan();
    void berhenti();
}

class Mobil implements Kendaraan {
    public void jalan() {
        System.out.println("Mobil sedang berjalan...");
    }
    public void berhenti() {
        System.out.println("Mobil berhenti di lampu merah.");
    }
}

public class MainKendaraan {
      public static void main(String[] args) {
        Mobil m = new Mobil();
        m.jalan();
        m.berhenti();
    }  
}

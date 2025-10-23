interface Gerak {
    void berjalan();
}

interface Suara {
    void bersuara();
}

abstract class Makhluk implements Gerak, Suara {
    abstract void makan();
}

class Manusia extends Makhluk {
    public void berjalan() {
        System.out.println("Manusia berjalan dengan dua kaki.");
    }
    public void bersuara() {
        System.out.println("Manusia berbicara dengan bahasa.");
    }
    void makan() {
        System.out.println("Manusia makan menggunakan tangan.");
    }
}

public class MainManusia {
        public static void main(String[] args) {
        Manusia manusia = new Manusia();
        manusia.berjalan();
        manusia.bersuara();
        manusia.makan();
    }
}

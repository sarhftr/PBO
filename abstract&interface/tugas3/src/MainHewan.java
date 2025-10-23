abstract class Hewan {
    abstract void suara();
    void tidur() {
        System.out.println("Hewan sedang tidur...");
    }
}

class Kucing extends Hewan {
    @Override
    void suara() {
        System.out.println("Kucing mengeong: Meong!");
    }
}

public class MainHewan {
        public static void main(String[] args) {
        Kucing kucing = new Kucing();
        kucing.suara();
        kucing.tidur();
    }
}

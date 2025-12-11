package hotelapp;

import javax.swing.SwingUtilities;
import hotelapp.service.DataManager;
import hotelapp.gui.LoginForm;

public class Main {
    public static void main(String[] args) {
        // load data & init kamar (hanya buat saat kosong)
        DataManager.loadAll();
        DataManager.initDefaultKamar();
        SwingUtilities.invokeLater(() -> new LoginForm());
    }
}
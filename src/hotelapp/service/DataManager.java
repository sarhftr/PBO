package hotelapp.service;

import hotelapp.model.*;
import java.util.*;
import java.io.*;

public class DataManager {
    public static List<Tamu> tamuList = new ArrayList<>();
    public static List<Admin> adminList = new ArrayList<>();
    public static List<Kamar> kamarList = new ArrayList<>();
    public static List<Booking> bookingList = new ArrayList<>();

    private static final String FILE_NAME = "hotel_data.ser";

    private static int tamuCounter = 1;
    private static int bookingCounter = 1;

    static {
    }

    public static void initDefaultKamar(){
        if(!kamarList.isEmpty()) return;
        // STANDARD 101..105
        for(int i=1;i<=5;i++) kamarList.add(new Kamar(String.valueOf(100 + i), "STANDARD", 200000));
        // DELUXE 201..205
        for(int i=1;i<=5;i++) kamarList.add(new Kamar(String.valueOf(200 + i), "DELUXE", 350000));
        // SUITE 301..305
        for(int i=1;i<=5;i++) kamarList.add(new Kamar(String.valueOf(300 + i), "SUITE", 600000));
    }

    @SuppressWarnings("unchecked")
    public static void loadAll(){
        File f = new File(FILE_NAME);
        if(!f.exists()){
            // seed minimal data
            adminList.add(new Admin("A001","admin","admin","Administrator"));
            tamuList.add(new Tamu(nextTamuId(),"guest","guest","Guest Sample"));
            initDefaultKamar();
            return;
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))){
            tamuList = (List<Tamu>) ois.readObject();
            adminList = (List<Admin>) ois.readObject();
            kamarList = (List<Kamar>) ois.readObject();
            bookingList = (List<Booking>) ois.readObject();
            // recover counters to avoid id duplicate
            tamuCounter = tamuList.size() + 1;
            bookingCounter = bookingList.size() + 1;
        }catch(Exception e){
            System.out.println("Load data failed, using defaults.");
            adminList.clear();
            tamuList.clear();
            kamarList.clear();
            bookingList.clear();
            adminList.add(new Admin("A001","admin","admin","Administrator"));
            tamuList.add(new Tamu(nextTamuId(),"guest","guest","Guest Sample"));
            initDefaultKamar();
        }
    }

    public static void saveAll(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))){
            oos.writeObject(tamuList);
            oos.writeObject(adminList);
            oos.writeObject(kamarList);
            oos.writeObject(bookingList);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String nextTamuId(){ return "T" + String.format("%03d", tamuCounter++); }
    public static String nextBookingId(){ return "B" + String.format("%03d", bookingCounter++); }

    // helper find
    public static Admin findAdminByUsername(String u){
        for(Admin a : adminList) if(a.getUsername().equals(u)) return a;
        return null;
    }
    public static Tamu findTamuByUsername(String u){
        for(Tamu t : tamuList) if(t.getUsername().equals(u)) return t;
        return null;
    }
}

package hotelapp.service;

import hotelapp.model.*;

public class AuthService {
    public static User login(String username, String password){
        Admin a = DataManager.findAdminByUsername(username);
        if(a != null && a.getPassword().equals(password)) return a;
        Tamu t = DataManager.findTamuByUsername(username);
        if(t != null && t.getPassword().equals(password)) return t;
        return null;
    }

    public static Tamu registerTamu(String username, String password, String name){
        // simple uniqueness check
        if(DataManager.findTamuByUsername(username) != null) return null;
        String id = DataManager.nextTamuId();
        Tamu t = new Tamu(id, username, password, name);
        DataManager.tamuList.add(t);
        DataManager.saveAll();
        return t;
    }
}

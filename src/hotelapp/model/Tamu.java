package hotelapp.model;

public class Tamu extends User {
    public Tamu(String id, String username, String password, String name){
        super(id, username, password, name);
    }

    @Override
    public String getRole(){ return "TAMU"; }

    
}
